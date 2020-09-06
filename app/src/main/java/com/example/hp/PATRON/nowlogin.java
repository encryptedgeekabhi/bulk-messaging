package com.example.hp.PATRON;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.PATRON.dbconnect.DBHelper;
import com.example.hp.PATRON.feedback.feedback1;

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
import java.util.zip.Inflater;

/**
 * Created by hp on 12-09-2018.
 */

public class nowlogin extends FragmentActivity implements View.OnClickListener {
EditText uid,upass,pin;
Button btn,forget;
String api;
ProgressDialog progress;
    SharedPreferences.Editor editor;
com.example.hp.PATRON.dbconnect.DBHelper db;
Activity ctx;
    Boolean b;
    View v;
    LayoutInflater l;
 TextView name,mobile,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nowlogin);
removeShortcut();
        ctx=this;
        LinearLayout ln= findViewById(R.id.add);
        ln.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                hideKeyboard(view);
                return false;
            }});
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);
        editor = sharedPref.edit();
        progress=new ProgressDialog(this);
        progress.setMessage("please wait..");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.setCanceledOnTouchOutside(false);
        btn = findViewById(R.id.submit);
        uid= findViewById(R.id.uid);
        upass= findViewById(R.id.upass);
        pin= findViewById(R.id.pin);
        db=new DBHelper(this);
        db.deleteall();
        b=networkstatus.check(this);
        btn.setOnClickListener(this);
        api = db.getkeys().get(0);
        System.out.println(api);
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
    @Override
    public void onClick(View view) {

        if(b==false)
        {
            Toast.makeText(getApplicationContext(),"NO Data Connection Found",Toast.LENGTH_SHORT).show();
        }
        else{

        if(pin.getText().toString().isEmpty()||pin.getText().length()<4)
        {
            Toast.makeText(this,"enter Four digit pin..", Toast.LENGTH_SHORT).show();
        }
        else{
            progress.show();
//        id=Integer.parseInt(getOnlyDigits(spin.getSelectedItem().toString()));
Any1 a=new Any1();
a.execute();
    }}}

    class Any extends AsyncTask<String, String, String> {
        String result;

        @Override
        protected String doInBackground(String... params) {

            String SOAP_ACTION = "http://tempuri.org/GetUserInformation";

            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "GetUserInformation";

            String URL = "http://erp.hellopatna.com/WebService/user.asmx";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);


            request.addProperty("_APIKey",api.toString());
            request.addProperty("_UserName",uid.getText().toString());
            request.addProperty("_Pwd",upass.getText().toString());


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transport = new HttpTransportSE(URL);

            try {
                transport.call(SOAP_ACTION, envelope);

                SoapObject res = (SoapObject) envelope.bodyIn;
                result =res.getProperty(0).toString();


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
    public void parsexml(String s)
    {
        String id;
        JSONParser parse = new JSONParser();
        try {
            System.out.println(s);
            JSONObject jobj = (JSONObject)parse.parse(s);
            JSONArray jsonArray = (JSONArray) jobj.get("Table");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonobj_1 = (JSONObject)jsonArray.get(i);
               id=jsonobj_1.get("BranchID").toString();
                editor.putString("name",jsonobj_1.get("UserName").toString());
                editor.commit();
  boolean b = db.insertlog(uid.getText().toString(), id.toString(), pin.getText().toString(),jsonobj_1.get("UserName").toString(),jsonobj_1.get("Mobile").toString(),jsonobj_1.get("Email").toString(),jsonobj_1.get("status").toString());
                 System.out.println(b);
                Toast.makeText(getApplicationContext(),"Login success",Toast.LENGTH_SHORT).show();
                progress.dismiss();

                db.close();
                ((Activity)ctx).finish();
                homescreen.setdata();
                progress.dismiss();

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    class Any1 extends AsyncTask<String, String, String> {
        String result;

        @Override
        protected String doInBackground(String... params) {

            String SOAP_ACTION = "http://tempuri.org/ValidateLogin";

            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "ValidateLogin";

            String URL = "http://erp.hellopatna.com/WebService/User.asmx";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);


            request.addProperty("_APIKey",api.toString());
            request.addProperty("_UserName",uid.getText().toString());
            request.addProperty("_Pwd",upass.getText().toString());


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transport = new HttpTransportSE(URL);

            try {
                transport.call(SOAP_ACTION, envelope);

                SoapObject res = (SoapObject) envelope.bodyIn;
                result =res.getProperty(0).toString();

System.out.println(result);
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
            if(result==null)
            {
                Toast.makeText(getApplicationContext(),"Slow connection..retry",Toast.LENGTH_SHORT).show();
            }
            else{
            if(result.equalsIgnoreCase("true")) {
              Any a=new Any();
              a.execute();
            }
            else{
                progress.dismiss();
                Toast.makeText(getApplicationContext(),"invalid..",Toast.LENGTH_SHORT).show();

            }

        }}
    }
    public static String getOnlyDigits(String s) {
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(s);
        String number = matcher.replaceAll("");
        return number;
    }
    private void createShortcut(){
        Intent shortcutIntent = new Intent(getApplicationContext(),feedback1.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "feedback");
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.feedback));
        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(intent);
        System.out.println("this is shortcut");
    }
    private void removeShortcut() {

        //Deleting shortcut for MainActivity
        //on Home screen
        Intent shortcutIntent = new Intent(getApplicationContext(),
                feedback1.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent
                .putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "feedback");

        addIntent
                .setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);
    }
    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
