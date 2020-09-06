package com.example.hp.PATRON;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hp.PATRON.dbconnect.DBHelper;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by hp on 12-09-2018.
 */

public class serial extends Activity implements View.OnClickListener {
 EditText serialno,serialkey;
 Button b;
 Activity ctx;
    ProgressDialog progress;
 com.example.hp.PATRON.dbconnect.DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
ctx=this;
        progress=new ProgressDialog(this);
        progress.setMessage("please wait..");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.setCanceledOnTouchOutside(false);
        serialno = findViewById(R.id.serialno);
        serialkey = findViewById(R.id.serialkey);
        b = findViewById(R.id.getapi);
        db=new DBHelper(this);
        db.deletekey();
        db.deleteall();
        b.setOnClickListener(this);
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
    @Override
    public void onClick(View view) {
        Boolean b=networkstatus.check(this);
        if(b==false)
        {
            Toast.makeText(getApplicationContext(),"NO Data Connection Found",Toast.LENGTH_SHORT).show();
        }
        else{
            progress.show();
     System.out.println("into click");
        Any a=new Any();
        a.execute();
    }}

    class Any extends AsyncTask<String, String, String> {
        String result="";
        Boolean error=false;
        @Override
        protected String doInBackground(String... params) {

            String SOAP_ACTION = "http://tempuri.org/ValidateLicense";

            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "ValidateLicense";

            String URL = "http://erp.hellopatna.com/WebService/BilltronLicense.asmx";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("_SerialNo",serialno.getText().toString());
            request.addProperty("_SerialKey",serialkey.getText().toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transport = new HttpTransportSE(URL);

            try {
                transport.call(SOAP_ACTION, envelope);
                System.out.println("into click");
                SoapObject res=(SoapObject)envelope.bodyIn;
                try {
                    result = res.getProperty(0).toString();
                }
                catch(ArrayIndexOutOfBoundsException e){
                    error=true;
                }
                System.out.println(error);
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

            if(result.isEmpty())
            {
                progress.dismiss();
               Toast.makeText(getApplicationContext(),"invalid input..",Toast.LENGTH_SHORT).show();
            }
            else
            {

              Boolean res=  db.insertkeys(serialno.getText().toString(),serialkey.getText().toString(),result.toString());

              if(res==true)
              {
                  ((Activity)ctx).finish();
                 db.close();

                 progress.dismiss();

                  Intent i=new Intent(getApplicationContext(),nowlogin.class);
                  startActivity(i);

              }
               else
              {

                  db.close();
                  Toast.makeText(getApplicationContext(),"data has not saved..",Toast.LENGTH_SHORT).show();
              }

            }
        }
    }

}
