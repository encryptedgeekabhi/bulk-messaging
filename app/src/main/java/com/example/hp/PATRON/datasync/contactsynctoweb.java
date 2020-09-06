package com.example.hp.PATRON.datasync;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.PATRON.R;
import com.example.hp.PATRON.dbconnect.DBHelper;

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


    public class contactsynctoweb extends android.support.v4.app.Fragment {

    SQLiteDatabase db;
    String api,uid;
    int gid;

    ListView list;
        TextView tx;
        ArrayList<String> aa= new ArrayList<String>();
        com.example.hp.PATRON.dbconnect.DBHelper dbhelper;
        public contactsynctoweb()
        {}
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.dbcontactshow,
                    container, false);
            dbhelper = new DBHelper(getActivity());
            db=dbhelper.getReadableDatabase();
            String selectloginQuery = "SELECT * FROM login";
            Cursor cursor1 = db.rawQuery(selectloginQuery, null);
            if (cursor1.getCount() > 0) {
                while (cursor1.moveToNext()) {
                    // Read columns data
                    api = cursor1.getString(cursor1.getColumnIndex("api"));
                    gid = cursor1.getInt(cursor1.getColumnIndex("groupid"));
                    uid = cursor1.getString(cursor1.getColumnIndex("id"));
                }


                System.out.println("value is: " + api + Integer.toString(gid) + uid);
                any a = new any();
                a.execute();
            }
            else
            {
                Toast.makeText(getActivity().getApplicationContext(),"Please login First..",Toast.LENGTH_SHORT).show();
            }
        return v;
        }


class any extends AsyncTask<String,String,String> {
String returnval;
    @Override
    protected String doInBackground(String... strings) {
try{
        System.out.println("sync result:  ");

        String SOAP_ACTION = "http://tempuri.org/InsertUpdatePhoneBook";

        String NAMESPACE = "http://tempuri.org/";
        String METHOD_NAME = "InsertUpdatePhoneBook";

        String URL = "http://vas.hellopatna.com/smsmediaService.asmx";

           String selectQuery = "SELECT * FROM contacts";
            Cursor cursor = db.rawQuery(selectQuery, null);
    if(cursor.getCount() >0)
    {

        while (cursor.moveToNext()) {
            // Read columns data
             String name = cursor.getString(cursor.getColumnIndex("name"));
               String phone = cursor.getString(cursor.getColumnIndex("phone"));
              String remark = cursor.getString(cursor.getColumnIndex("remarks"));
             String email = cursor.getString(cursor.getColumnIndex("email"));

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("_ApiKey", api.toString());
            request.addProperty("_ID", 0);
            request.addProperty("_GroupID", gid);

            request.addProperty("_Mobile", phone.toString());
            request.addProperty("_CName", name.toString());
            request.addProperty("_Address", "patna");
            request.addProperty("_Place", "patna");
            request.addProperty("_DOB", "01/01/1947");
            request.addProperty("_Marriage", "01/01/1947");
            request.addProperty("_Status", "Active");
            request.addProperty("_Remarks", remark.toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(SOAP_ACTION, envelope);
            SoapObject res = (SoapObject) envelope.bodyIn;

            System.out.println("sync result:  " + res.getProperty(0).toString());
        }
        returnval="true";

    }
    else
        returnval="false";

} catch (IOException e1) {
    e1.printStackTrace();
} catch (XmlPullParserException e) {
    e.printStackTrace();
}
        db.close();


        return returnval;

    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
if(result.equalsIgnoreCase("false"))
{
    Toast.makeText(getActivity().getApplication(),"No data for sync",Toast.LENGTH_SHORT).show();
}
else
    Toast.makeText(getActivity().getApplicationContext(),"data synced..",Toast.LENGTH_SHORT).show();
    }
        }}
