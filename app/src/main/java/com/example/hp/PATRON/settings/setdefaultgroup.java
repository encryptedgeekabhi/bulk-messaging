package com.example.hp.PATRON.settings;

/**
 * Created by hp on 04-09-2018.
 */
import org.json.simple.parser.JSONParser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import org.json.simple.JSONObject;
import com.example.hp.PATRON.R;
import com.example.hp.PATRON.dbconnect.DBHelper;

import org.json.simple.parser.ParseException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import android.widget.ArrayAdapter;

import org.json.simple.JSONArray;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
public class setdefaultgroup extends AppCompatActivity implements View.OnClickListener {
    Button setgroup_btn;

    Spinner spin;
int id;
    DBHelper db;
String api="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectgroup);
        setgroup_btn = findViewById(R.id.setgroup_btn);

        spin = findViewById(R.id.group_spinner);
        setgroup_btn.setOnClickListener(this);

            db = new DBHelper(this);
            try {
                api = db.getkeys().get(0);
            }
            catch (Exception a)
            {
                Toast.makeText(this,"Please Login First.",Toast.LENGTH_SHORT).show();
            }
        if(api.isEmpty())
{
    Toast.makeText(this,"Please Login First.",Toast.LENGTH_SHORT).show();
}
else{
        Any any=new Any();
        any.execute();

    }}
    public static String getOnlyDigits(String s) {
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(s);
        String number = matcher.replaceAll("");
        return number;
    }
    @Override
    public void onClick(View view) {
        String text = spin.getSelectedItem().toString();
       id=Integer.parseInt(getOnlyDigits(text));
       System.out.println(id);
      Integer i=  db.updategroup(api.toString(),text.toString(),id);
        Toast.makeText(this,"ID saved : "+Integer.toString(id),Toast.LENGTH_SHORT).show();
       db.close();
        this.finish();
    }
    class Any extends AsyncTask<String, String, String> {
        String result;

        @Override
        protected String doInBackground(String... params) {

            String SOAP_ACTION = "http://tempuri.org/GetGroupName";

            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "GetGroupName";

            String URL = "http://vas.hellopatna.com/smsmediaService.asmx";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);


            request.addProperty("_ApiKey",api.toString());


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transport = new HttpTransportSE(URL);

            try {
                transport.call(SOAP_ACTION, envelope);

                SoapObject res = (SoapObject) envelope.bodyIn;
                result =res.getProperty(0).toString();
                System.out.println("*******************************************");
//                Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_SHORT).show();


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
                          parsexml(result);

    }
}
    public void parsexml(String s)
    {
        ArrayList<String> grouplist = new ArrayList<String>();
        JSONParser parse = new JSONParser();
        try {
            System.out.println(s);
            JSONObject jobj = (JSONObject)parse.parse(s);
            JSONArray jsonArray = (JSONArray) jobj.get("Table");
            if(jsonArray==null)
            {
                Toast.makeText(this,"No Group Found.. : ",Toast.LENGTH_SHORT).show();
            }
            else{
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonobj_1 = (JSONObject)jsonArray.get(i);
                System.out.println("\nPlace id:" +jsonobj_1.get("GroupName"));
               grouplist.add(jsonobj_1.get("GroupName").toString()+"--"+jsonobj_1.get("ID"));

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, grouplist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(adapter);
                spin.setSelection(0);


            }}
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
}