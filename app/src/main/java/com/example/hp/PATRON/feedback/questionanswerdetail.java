package com.example.hp.PATRON.feedback;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.PATRON.R;
import com.example.hp.PATRON.dbconnect.DBHelper;
import com.example.hp.PATRON.networkstatus;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by hp on 11-09-2018.
 */

public class questionanswerdetail extends Activity {

   String api;
   int id;
    Context c;
    Activity a;
    LinearLayout m_ll;
    TextView guestname;
    ProgressDialog progress;
    com.example.hp.PATRON.dbconnect.DBHelper db;
    public questionanswerdetail(Context ctx) {
        c = ctx;
        db = new DBHelper(ctx);
        Dialog dialog = new Dialog(ctx);
        dialog.setTitle("feedback response");
        dialog.requestWindowFeature(Window.DECOR_CAPTION_SHADE_AUTO);
        dialog.requestWindowFeature(Window.FEATURE_SWIPE_TO_DISMISS);
        dialog.setContentView(R.layout.questionresponse);

        Window w = dialog.getWindow();
        w.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        a=this;
        m_ll = (LinearLayout) dialog.findViewById(R.id.anshere);
        guestname=dialog.findViewById(R.id.guestname);
        guestname.setText(getguestdetails.guestname);


        db=new DBHelper(c);
        api = db.getkeys().get(0);
        //Bundle bundle = getIntent().getExtras();
        //id = Integer.parseInt(bundle.getString("id"));
        System.out.println("this is "+api+"       "+id);
        progress=new ProgressDialog(ctx);
        progress.setMessage("please wait..");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.setCanceledOnTouchOutside(false);
        if(networkstatus.check(ctx))
        {

                Any a=new Any();
                a.execute();
            dialog.show();
            progress.show();
            }
        else{
            Toast.makeText(this, "No Active Data connection", Toast.LENGTH_SHORT).show();
        }
    }
    class Any extends AsyncTask<String, String, String> {
        String result="";

        @Override
        protected String doInBackground(String... params) {
              System.out.println(getguestdetails.guestid+getguestdetails.strdate+getguestdetails.endate);
            String SOAP_ACTION = "http://tempuri.org/GetFeedbackDetails2";

            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "GetFeedbackDetails2";

            String URL = "http://erp.hellopatna.com/WebService/BilltronFeedback.asmx";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("_APIKey",api.toString());
            request.addProperty("_GuestID",getguestdetails.guestid);
            request.addProperty("_FromDate",getguestdetails.strdate);
            request.addProperty("_ToDate",getguestdetails.endate);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transport = new HttpTransportSE(URL);
            try {
                transport.call(SOAP_ACTION, envelope);
                SoapObject res=(SoapObject)envelope.bodyIn;
                result=res.getProperty(0).toString();
                System.out.println("*******************************************");
                if(result.equalsIgnoreCase("true"))
                {

                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println(result);
            parsexml(result);
        }
    }
    public void parsexml(String s) {

        JSONParser parse = new JSONParser();
        try {
            System.out.println(s);
            JSONObject jobj = (JSONObject)parse.parse(s);
            JSONArray jsonArray = (JSONArray) jobj.get("Table");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonobj_1 = (JSONObject)jsonArray.get(i);
                TextView feeddate = new TextView(c);
                TextView txq = new TextView(c);
                TextView txa = new TextView(c);
                TextView txspace = new TextView(c);
                TextView txspacestart = new TextView(c);
                m_ll.addView(txspacestart);
                feeddate.setLayoutParams(new LinearLayout.LayoutParams(-1, 60));
                feeddate.setText("    On - "+feedback1.convert(jsonobj_1.get("FeedbackDate").toString()));
                m_ll.addView(feeddate);
                txq.setLayoutParams(new LinearLayout.LayoutParams(-1, 60));
                txq.setText("     Q.     "+jsonobj_1.get("Question").toString());
                m_ll.addView(txq);
                txa.setLayoutParams(new LinearLayout.LayoutParams(-1, 60));
                txa.setText("     Ans.  "+jsonobj_1.get("Answer").toString());
                m_ll.addView(txa);
                txspace.setLayoutParams(new LinearLayout.LayoutParams(-1, 20));
                m_ll.addView(txspace);
            }
            progress.dismiss();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}