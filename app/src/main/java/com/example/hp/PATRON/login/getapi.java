package com.example.hp.PATRON.login;

/**
 * Created by hp on 04-09-2018.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hp.PATRON.R;
import com.example.hp.PATRON.dbconnect.DBHelper;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class getapi extends Activity implements View.OnClickListener {
    TextView tx;
    Button api_btn;
    String id, pass, pin, api = " ";
    com.example.hp.PATRON.dbconnect.DBHelper db = new DBHelper(this);
ProgressDialog progress;
Activity ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.getapi);
        api_btn = findViewById(R.id.api_btn);
        progress=new ProgressDialog(this);
        progress.setMessage("please wait..");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        ctx=this;
        tx = findViewById(R.id.api_data);
        api_btn.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("uid");
        pass = bundle.getString("pass");
        pin = bundle.getString("pin");
    }


    @Override
    public void onClick(View view) {
        progress.show();
        Any any = new Any();
        any.execute();
    }

    class Any extends AsyncTask<String, String, String> {
        String result = "";

        @Override
        protected String doInBackground(String... params) {

            String SOAP_ACTION = "http://tempuri.org/GetAPIKey";

            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "GetAPIKey";

            String URL = "http://vas.hellopatna.com/smsmediaService.asmx";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);


            request.addProperty("_UserName", id.toString());
            request.addProperty("_Password", pass.toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transport = new HttpTransportSE(URL);

            try {
                transport.call(SOAP_ACTION, envelope);
                SoapObject res = (SoapObject) envelope.bodyIn;
                result = res.getProperty(0).toString();
                System.out.println("*******************************************");
//                Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_SHORT).show();
                System.out.println(result.toString());

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
            progress.dismiss();
            tx.setText("Your key : "+result.toString());
            api=result.toString();
            Boolean res = db.insertlogin(id.toString(), pass.toString(), pin.toString(), api.toString());
            
if(res==true) {
    System.out.println("saved");
    ((Activity)ctx).finish();

}else
    System.out.println("may be already exist");
db.close();
        }

    }
}