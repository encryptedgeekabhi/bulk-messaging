package com.example.hp.PATRON.feedback;

/**
 * Created by hp on 15-09-2018.
 */

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by hp on 15-09-2018.
 */

public class marriegenotification extends Fragment {
    String api;

    public static int guestid = 0;
    public static String guestname;
    int datefor;
    ProgressDialog progress;
    int id;
    EditText startdate, enddate;
    LinearLayout add;
    Button submit;
    com.example.hp.PATRON.dbconnect.DBHelper db;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.marriege,
                container, false);
        progress = new ProgressDialog(getActivity());
        progress.setMessage("please wait..");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        db = new DBHelper(getActivity());
        add = view.findViewById(R.id.add);
        api = db.getkeys().get(0);
        id = Integer.parseInt(getOnlyDigits(db.getbranchid().get(0)));
        Any a = new Any();
        a.execute();
        return view;


    }

    public static String getOnlyDigits(String s) {
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(s);
        String number = matcher.replaceAll("");
        return number;
    }

    class Any extends AsyncTask<String, String, String> {
        String result="";
        @Override
        protected String doInBackground(String... params) {

            String SOAP_ACTION = "http://tempuri.org/GetGuestDetails2";

            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "GetGuestDetails2";

            String URL = "http://erp.hellopatna.com/WebService/BilltronFeedback.asmx";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            //System.out.println(api+"    "+id+"    "+startdate.getText()+"    "+enddate.getText());

            request.addProperty("_APIKey",api.toString());
            request.addProperty("_BranchID",id);
            request.addProperty("_FromDate",startdate.getText().toString());
            request.addProperty("_ToDate",enddate.getText().toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transport = new HttpTransportSE(URL);

            try {
                transport.call(SOAP_ACTION, envelope);
                SoapObject res=(SoapObject)envelope.bodyIn;
                // SoapFault res=(SoapFault) envelope.bodyIn;
                result=res.getProperty(0).toString();

                System.out.println(result);

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
        }}
    public void  parsexml(String s) {


        JSONParser parse = new JSONParser();
        try {
            System.out.println(s);
            JSONObject jobj = (JSONObject)parse.parse(s);
            JSONArray jsonArray = (JSONArray) jobj.get("Table");
            Boolean colorchange=true;
            for (int i = 0; i < jsonArray.size(); i++) {
                final JSONObject jsonobj_1 = (JSONObject) jsonArray.get(i);
                final Button click=new Button(getActivity());
                final TextView tx=new TextView(getActivity());
                TextView tx1=new TextView(getActivity());
                TextView tx2=new TextView(getActivity());
                TextView tx3=new TextView(getActivity());
                TextView tx4=new TextView(getActivity());
                TextView tx5=new TextView(getActivity());
                TextView txspace=new TextView(getActivity());
                click.setText("detail");
                click.setLayoutParams(new LinearLayout.LayoutParams(100,50));


                tx.setLayoutParams(new LinearLayout.LayoutParams(-1,60));
                tx1.setLayoutParams(new LinearLayout.LayoutParams(-1,60));
                tx2.setLayoutParams(new LinearLayout.LayoutParams(-1,60));
                txspace.setLayoutParams(new LinearLayout.LayoutParams(-1,20));

                click.setId(Integer.parseInt(jsonobj_1.get("ID").toString()));
                tx.setText("   Name       :   "+jsonobj_1.get("GuestName").toString());
                tx1.setText("   Address   :  "+jsonobj_1.get("Address").toString());
                tx2.setText("   Moble       :  "+jsonobj_1.get("Mobile").toString());
                tx3.setText("   Email        :  "+jsonobj_1.get("Email").toString());
                tx4.setText("   DOB          :  "+jsonobj_1.get("DOB").toString());
                tx5.setText("   Remarks  :  "+jsonobj_1.get("Remarks").toString());
                click.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println(click.getId());
                        guestname=jsonobj_1.get("GuestName").toString();
                        guestid=click.getId();
                        questionanswerdetail p=new questionanswerdetail(getActivity());

                    }

                });


                if(colorchange==true)
                {
                    tx.setBackgroundColor(Color.YELLOW);
                    tx1.setBackgroundColor(Color.YELLOW);
                    tx2.setBackgroundColor(Color.YELLOW);
                    tx3.setBackgroundColor(Color.YELLOW);
                    tx4.setBackgroundColor(Color.YELLOW);
                    tx5.setBackgroundColor(Color.YELLOW);
                    colorchange =false;
                }
                else{
                    tx.setBackgroundColor(Color.CYAN);
                    tx1.setBackgroundColor(Color.CYAN);
                    tx2.setBackgroundColor(Color.CYAN);
                    tx3.setBackgroundColor(Color.CYAN);
                    tx4.setBackgroundColor(Color.CYAN);
                    tx5.setBackgroundColor(Color.CYAN);
                    colorchange=true;
                }
                add.addView(click);
                tx.setTypeface(null, Typeface.BOLD);

                add.addView(tx);
                tx1.setTypeface(null, Typeface.BOLD);
                add.addView(tx1);
                tx2.setTypeface(null, Typeface.BOLD);
                add.addView(tx2);
                tx3.setTypeface(null, Typeface.BOLD);
                add.addView(tx3);
                tx4.setTypeface(null, Typeface.BOLD);
                add.addView(tx4);
                tx5.setTypeface(null, Typeface.BOLD);
                add.addView(tx5);
                add.addView(txspace);


            }

            progress.dismiss();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
}
