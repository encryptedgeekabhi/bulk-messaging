package com.example.hp.PATRON.webcontacts.massage;
/**
 * Created by hp on 23-08-2018.
 */

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class compose extends Fragment implements View.OnClickListener{

    ArrayList<String> grouplist = new ArrayList<String>();
    ArrayList<String> allwebnumber = new ArrayList<String>();
    RadioGroup radioGroup;
    ImageView set;
    ImageView finalsend;
    RadioButton toall,custom;
    int id;
    String api;
    SQLiteDatabase db;

   static EditText msg;
    String contactnumber="";
    public compose() {
        // Required empty public constructor
    }

    com.example.hp.PATRON.dbconnect.DBHelper dbhelper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.compose, container, false);
        set=view.findViewById(R.id.Set);
        radioGroup=view.findViewById(R.id.sendto);

        custom=view.findViewById(R.id.custom);
        toall=view.findViewById(R.id.toall);

        finalsend=view.findViewById(R.id.finalsend);
        msg=view.findViewById(R.id.msg);
        finalsend.setOnClickListener(this);
        set.setOnClickListener(this);
        dbhelper = new DBHelper(getActivity());
        db=dbhelper.getReadableDatabase();

        // Start the transaction.
        db.beginTransaction();
        String selectQuery = "SELECT * FROM login";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.getCount() >0)
        {
            while (cursor.moveToNext()) {
                // Read columns data
                api = cursor.getString(cursor.getColumnIndex("api"));
                id = cursor.getInt(cursor.getColumnIndex("groupid"));
            }}
        db.endTransaction();
        db.close();
       // StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

       // StrictMode.setThreadPolicy(policy);


        return view;
    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.Set)
        {
          if(toall.isChecked())
          {
              any a=new any();
              a.execute();


          }
          if(custom.isChecked())
          {


          }
        }

if(view.getId()==R.id.finalsend)
{

        URLConnection myURLConnection=null;
        URL myURL=null;
        BufferedReader reader=null;
String message=msg.getText().toString().replaceAll(" ","%20");
    String mainUrl="http://www.hellopatna.com/smsapi/sendsms?username=demo&password=demo&senderid=PATRON&to=9471018608&message="+message.toString()+"&route=1&flash=0&nonenglish=0";


    try
        {
            //prepare connection
            myURL = new URL(mainUrl);
            myURLConnection = myURL.openConnection();
            myURLConnection.connect();
            reader= new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));

            //reading response
            String response;
            while ((response = reader.readLine()) != null)
                //print response
                Log.d("RESPONSE", ""+response);

            //finally close connection
            reader.close();


        }

        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
    }
    class any extends AsyncTask<String, String, String> {
        String result;
        @Override
        protected String doInBackground(String... params) {
            String SOAP_ACTION = "http://tempuri.org/GetPhoneBook";

            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "GetPhoneBook";

            String URL = "http://vas.hellopatna.com/smsmediaService.asmx";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            request.addProperty("_APIKey", api.toString());
            System.out.println(api.toString());
            System.out.println(id);
            request.addProperty("_GroupID",id);

            envelope.setOutputSoapObject(request);
            HttpTransportSE transport = new HttpTransportSE(URL);
            try {
                transport.call(SOAP_ACTION, envelope);
                SoapObject res = (SoapObject)envelope.bodyIn;
                System.out.println("********************");
                System.out.println(res.getProperty(0).toString());
                result=res.getProperty(0).toString();

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            parsexml(result);

        }

        public void parsexml(String s)
        {

            JSONParser parse = new JSONParser();
            try {
                System.out.println(s);
                JSONObject jobj = (JSONObject)parse.parse(s);
                JSONArray jsonArray = (JSONArray) jobj.get("Table");
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonobj_1 = (JSONObject)jsonArray.get(i);
                    allwebnumber.add(jsonobj_1.get("Mobile").toString());
                    grouplist.add(jsonobj_1.get("CName")+" - " +jsonobj_1.get("Mobile"));
                    System.out.println(grouplist.toString());




                }
                msg.setText(allwebnumber.toString());

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }}

}