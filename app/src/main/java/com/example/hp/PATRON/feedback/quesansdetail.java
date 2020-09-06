package com.example.hp.PATRON.feedback;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hp.PATRON.R;
import com.example.hp.PATRON.dbconnect.DBHelper;

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
 * Created by hp on 17-09-2018.
 */

public class quesansdetail extends Activity{
    String api,guestid;
    Dialog dialog;
    int id;
    Activity a;
    LinearLayout m_ll;
    com.example.hp.PATRON.dbconnect.DBHelper db;
    ProgressDialog progress;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        dialog = new Dialog(this);
        dialog.setTitle("detail");
        dialog.setContentView(R.layout.pinvalidation);
        Window w = dialog.getWindow();

        w.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        a=this;
        m_ll = dialog.findViewById(R.id.ans);

        progress=new ProgressDialog(this);
        progress.setMessage("please wait..");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        db=new DBHelper(this);
        api = db.getkeys().get(0);
        Bundle bundle = getIntent().getExtras();
        id = Integer.parseInt(bundle.getString("id"));
        System.out.println("this is "+api+"       "+id+"     "+guestid);
        progress.show();
        Any a=new Any();
        a.execute();

    }
    class Any extends AsyncTask<String, String, String> {
        String result="";
        @Override
        protected String doInBackground(String... params) {

            String SOAP_ACTION = "http://tempuri.org/GetFeedbackDetails2";

            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "GetFeedbackDetails2";

            String URL = "http://erp.hellopatna.com/WebService/BilltronFeedback.asmx";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);



            request.addProperty("_APIKey",api.toString());
            request.addProperty("_GuestID",id);
            request.addProperty("_FromDate","01/01/1947");
            request.addProperty("_ToDate","01/01/1947");
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
                    TextView txq = new TextView(this);
                TextView txa = new TextView(this);
                TextView txspace = new TextView(this);
                    txq.setLayoutParams(new LinearLayout.LayoutParams(-1, 60));
                txq.setText("  Q.  "+jsonobj_1.get("Question").toString());
                m_ll.addView(txq);
                txa.setLayoutParams(new LinearLayout.LayoutParams(-1, 60));
                txa.setText("  Ans.  "+jsonobj_1.get("Answer").toString());
                  m_ll.addView(txa);
                txspace.setLayoutParams(new LinearLayout.LayoutParams(-1, 30));
                m_ll.addView(txspace);



                }
            progress.dismiss();
            dialog.show();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
}
