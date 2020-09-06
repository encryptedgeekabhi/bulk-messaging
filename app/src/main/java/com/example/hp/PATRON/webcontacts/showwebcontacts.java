package com.example.hp.PATRON.webcontacts;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.util.ArrayList;

/**
 * Created by hp on 07-09-2018.
 */

public class showwebcontacts extends android.support.v4.app.Fragment {
    ArrayList<String> grouplist = new ArrayList<String>();
    ArrayList<String> allwebnumber = new ArrayList<String>();
    ListView list;
    TextView tx;
    ArrayList<String> aa = new ArrayList<String>();
    com.example.hp.PATRON.dbconnect.DBHelper dbhelper;
    int id;
    String api;
    SQLiteDatabase db;

    public showwebcontacts() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dbhelper = new DBHelper(getActivity());
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.showcontacts,
                container, false);
        list = v.findViewById(R.id.list);
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
            any a= new any();
        a.execute();

        return v;
    }
  class any extends AsyncTask<String, String, String>{
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
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,grouplist);
            list.setAdapter(adapter);
        } catch (ParseException e) {
            e.printStackTrace();
        }
}}}