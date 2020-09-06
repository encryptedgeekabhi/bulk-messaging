package com.example.hp.PATRON.login;

/**
 * Created by hp on 24-08-2018.
 */
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.hp.PATRON.R;
import com.example.hp.PATRON.dbconnect.DBHelper;

import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;
import com.example.hp.PATRON.setpin;
import java.io.IOException;
import java.util.ArrayList;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

public  class Login extends Fragment implements validation,View.OnClickListener{

    Button log_btn,pin_btn;
    EditText id;
    EditText pass;
    EditText pinvalue;
    TextView tx;
    com.example.hp.PATRON.dbconnect.DBHelper db;
String uid,upass;
ProgressDialog progress;
    NavigationView nav;
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
progress=new ProgressDialog(getActivity());
        progress.setMessage("please wait..");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);


      db=new DBHelper(getActivity());
        View view = inflater.inflate(R.layout.login,
                container, false);
        View v=inflater.inflate(R.layout.nav_header_main,container,false);
        tx=v.findViewById(R.id.loggeduser);
       log_btn= view.findViewById(R.id.btn_login);
       id=view.findViewById(R.id.uid);
        pass=view.findViewById(R.id.upass);
        pinvalue=view.findViewById(R.id.pinvalue);
        pin_btn=view.findViewById(R.id.pin_btn);
       pin_btn.setOnClickListener(this);

        log_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
db.deleteall();

                progress.show();
                checklogin();
            }
        });
        return view;
    }


    @Override
    public void validate() {



        String id_data=(id.getText()).toString();


        if(id_data.equals("steve".toString()))
        {

            Toast.makeText(getActivity(), "login sucess", Toast.LENGTH_LONG).show();


            log_btn.setText("logout");
        }
        else
            Toast.makeText(getActivity(), "login unsucess", Toast.LENGTH_LONG).show();

    }


    @Override
    public void checklogin() {
        uid=id.getText().toString();
        upass=pass.getText().toString();
Any any=new Any();

        any.execute();

    }

    @Override
    public void save() {

    }

    @Override
    public void onClick(View view) {
        if(pinvalue.getText().toString().isEmpty()||pinvalue.getText().length()<4)
            Toast.makeText(getActivity().getApplicationContext(), "please enter 4 digit pin..", Toast.LENGTH_SHORT).show();
        else{
            progress.show();

        ArrayList<String> array_list = new ArrayList<String>();
      array_list=  db.getpinlogin(pinvalue.getText().toString());
      if(array_list.isEmpty()) {

          Toast.makeText(getActivity().getApplicationContext(), "please login with id and pass..", Toast.LENGTH_SHORT).show();
      }
      else
          {
      uid=array_list.get(0).toString();
      upass=array_list.get(1).toString();
      pinlog p=new pinlog();
      p.execute();
    }}
    }


    class Any extends AsyncTask<String, String, String> {
         String result="";
        @Override
    protected String doInBackground(String... params) {

        String SOAP_ACTION = "http://tempuri.org/ValidateUser";

        String NAMESPACE = "http://tempuri.org/";
        String METHOD_NAME = "ValidateUser";

        String URL = "http://vas.hellopatna.com/smsmediaService.asmx";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);



        request.addProperty("_UserName",uid.toString());
        request.addProperty("_Password",upass.toString());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transport = new HttpTransportSE(URL);

        try {
            transport.call(SOAP_ACTION, envelope);
            SoapObject res=(SoapObject)envelope.bodyIn;
            result=res.getProperty(0).toString();

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

if(result.equalsIgnoreCase("true"))
{
    progress.dismiss();
    Bundle bundle = new Bundle();
    bundle.putString("edttext", "Currently Logged on : "+id.getText().toString());
    Fragment f= new loggedfragmant();
    f.setArguments(bundle);
    getActivity().getSupportFragmentManager().beginTransaction()
            .replace(R.id.removable, f, f.getClass().getSimpleName()).addToBackStack(null).commit();



    System.out.println(result);
    getActivity().getFragmentManager().popBackStack();
             Intent i=new Intent(getActivity(),setpin.class);
             i.putExtra("uid",id.getText().toString());
    i.putExtra("pass",pass.getText().toString());
             startActivity(i);
         }
         if(result.equalsIgnoreCase("false"))
         {
             Toast.makeText(getActivity().getApplicationContext(),"invalid input..",Toast.LENGTH_SHORT).show();

         }
        }
     }

class pinlog extends AsyncTask<String, String, String> {
    String result="";
    @Override
    protected String doInBackground(String... params) {

        String SOAP_ACTION = "http://tempuri.org/ValidateUser";

        String NAMESPACE = "http://tempuri.org/";
        String METHOD_NAME = "ValidateUser";

        String URL = "http://vas.hellopatna.com/smsmediaService.asmx";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);



        request.addProperty("_UserName",uid.toString());
        request.addProperty("_Password",upass.toString());

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

        if(result.equalsIgnoreCase("true"))
        {
            progress.dismiss();
            Bundle bundle = new Bundle();
            bundle.putString("edttext", "Currently Logged on : "+uid.toString());
            Fragment f= new loggedfragmant();
            f.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.removable, f, f.getClass().getSimpleName()).addToBackStack(null).commit();




        }
        if(result.equalsIgnoreCase("false"))
        {
            Toast.makeText(getActivity().getApplicationContext(),"invalid input..",Toast.LENGTH_SHORT).show();

        }
        db.close();
    }}
}