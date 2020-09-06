package com.example.hp.PATRON.feedback;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.PATRON.R;
import com.example.hp.PATRON.dbconnect.DBHelper;
import com.example.hp.PATRON.networkstatus;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hp on 15-09-2018.
 */

public class feedback1 extends Activity implements View.OnClickListener,validate, View.OnFocusChangeListener {

    com.example.hp.PATRON.dbconnect.DBHelper db = new DBHelper(this);
    ProgressDialog progress;
    Activity ctx;
    EditText name,phone,email,dob,marige,address;
    int datefor;
    int guestid=0;
    String api;
    Activity a;
    int id;
    Date birth,mrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_userdetails);
        LinearLayout ln= findViewById(R.id.addhere);
        ln.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                hideKeyboard(view);
                return false;
            }});
        db=new DBHelper(this);
        a=this;
        progress=new ProgressDialog(this);
        progress.setMessage("please wait..");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.setCanceledOnTouchOutside(false);
        api = db.getkeys().get(0);
        id=Integer.parseInt(getOnlyDigits(db.getbranchid().get(0)));
        name=findViewById(R.id.name);
        phone=findViewById(R.id.mobile);
        email=findViewById(R.id.email);
        address=findViewById(R.id.address);
        dob=findViewById(R.id.dob);
        phone.setOnFocusChangeListener(this);
        marige=findViewById(R.id.marriage);
        dob.setOnClickListener(this);
        marige.setOnClickListener(this);
        Button submit=findViewById(R.id.submit);
        submit.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.submit)
        {
            if(networkstatus.check(this))
            {
                boolean res=  checkblank();
                if(res==true)
                {
                   progress.show();
                    Any a=new Any();
                    a.execute();

                }}
            else{
                Toast.makeText(this, "No Active Data connection", Toast.LENGTH_SHORT).show();
            }
        }

        if(view.getId()==R.id.dob)
        {
            DatePickerDialog dialog = new DatePickerDialog(this,datePickerListener, 1996, 8, 03);
            dialog.show();
            datefor=1;
        }
        if(view.getId()==R.id.marriage)
        {
            DatePickerDialog dialog = new DatePickerDialog(this,datePickerListener, 1996, 8, 03);
            dialog.show();
            datefor=2;
        }
    }
    @Override
    public boolean checkblank() {
        boolean res=true;
        if(name.getText().toString().isEmpty())
        {
            name.setHint("invalid name");
            Toast.makeText(this,"invalid name..",Toast.LENGTH_SHORT).show();
            res=false;
        }
        else if(phone.getText().toString().isEmpty()||phone.getText().toString().length()!=10)
        {
            phone.setHint("invalid phone");
            phone.setText("");
            Toast.makeText(this,"invalid Phone..",Toast.LENGTH_SHORT).show();
            res=false;
        }
        else if(email.getText().toString().isEmpty())
        {
            phone.setHint("invalid phone");
            res=false;
            Toast.makeText(this,"invalid email..",Toast.LENGTH_SHORT).show();
        }
        return res;
    }
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            if (datefor == 1)
            {
                if (i2 <= 9) {
                    if(i1<=8)
                        dob.setText("0" + Integer.toString(i2) + "/" +"0"+ Integer.toString(i1 + 1) + "/" + Integer.toString(i));
                    else

                        dob.setText("0" + Integer.toString(i2) + "/" + Integer.toString(i1 + 1) + "/" + Integer.toString(i));
                }
                else if(i1<=8)
                {
                    dob.setText(Integer.toString(i2) + "/" +"0"+Integer.toString(i1 + 1) + "/" + Integer.toString(i));
                }
                else
                {
                    dob.setText(Integer.toString(i2) + "/" + Integer.toString(i1 + 1) + "/" + Integer.toString(i));
                }
            }
            else if (datefor == 2) {
                if (i2 <= 9)
                {
                    if(i1<=8)
                        marige.setText("0" + Integer.toString(i2) + "/" +"0"+ Integer.toString(i1 + 1) + "/" + Integer.toString(i));
                    else
                        marige.setText("0" + Integer.toString(i2) + "/" + Integer.toString(i1 + 1) + "/" + Integer.toString(i));
                }
                else if(i1<=8)
                {
                    marige.setText(Integer.toString(i2) + "/" +"0"+Integer.toString(i1 + 1) + "/" + Integer.toString(i));
                }
                else
                {
                    marige.setText(Integer.toString(i2) + "/" + Integer.toString(i1 + 1) + "/" + Integer.toString(i));
                }
            }}
    };
    public static String getOnlyDigits(String s) {
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(s);
        String number = matcher.replaceAll("");
        return number;
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if(phone.isFocused())
        {}
        else
        {
progress.show();
Any1 a=new Any1();
a.execute();
    }}

    class Any extends AsyncTask<String, String, String> {
        String result="";
        @Override
        protected String doInBackground(String... params) {

            String SOAP_ACTION = "http://tempuri.org/InsertUpdateGuestDetails";

            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "InsertUpdateGuestDetails";

            String URL = "http://erp.hellopatna.com/WebService/BilltronFeedback.asmx";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);



            request.addProperty("_APIKey",api.toString());
            request.addProperty("_BranchID",id);
            request.addProperty("_GuestID",guestid);
            request.addProperty("_GuestName",name.getText().toString());
            request.addProperty("_Address",address.getText().toString());
            request.addProperty("_Mobile",phone.getText().toString());
            request.addProperty("_Email",email.getText().toString());
            request.addProperty("_DOB",dob.getText().toString());
            request.addProperty("_MarriageAnniversary",marige.getText().toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transport = new HttpTransportSE(URL);

            try {
                transport.call(SOAP_ACTION, envelope);
                SoapObject res=(SoapObject)envelope.bodyIn;
                // SoapFault res=(SoapFault) envelope.bodyIn;
                result=res.getProperty(0).toString();

                //System.out.println(res);
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
            if(Integer.parseInt(result)>0) {
                progress.hide();
                Intent i=new Intent(a,feed1.class);
                i.putExtra("guestid",result.toString());
                startActivity(i);
                a.finish();



            }
            else{
                Toast.makeText(a,"please retry",Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        }}
    protected void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    class Any1 extends AsyncTask<String, String, String> {
        String result="";
        @Override
        protected String doInBackground(String... params) {

            String SOAP_ACTION = "http://tempuri.org/GetGuestDetailsByMobile2";

            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "GetGuestDetailsByMobile2";

            String URL = "http://erp.hellopatna.com/WebService/BilltronFeedback.asmx";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);



            request.addProperty("_APIKey",api.toString());
            request.addProperty("_BranchID",id);
            request.addProperty("_Mobile",phone.getText().toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transport = new HttpTransportSE(URL);

            try {
                transport.call(SOAP_ACTION, envelope);
                SoapObject res=(SoapObject)envelope.bodyIn;
                // SoapFault res=(SoapFault) envelope.bodyIn;
                result=res.getProperty(0).toString();

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
            if(result !=null) {
                progress.dismiss();
            parsexml(result);

            }
            else{
                progress.dismiss();
            }
        }}

    public void parsexml(String s) {
        JSONParser parse = new JSONParser();
        try {
            System.out.println(s);
            JSONObject jobj = (JSONObject)parse.parse(s);
            JSONArray jsonArray = (JSONArray) jobj.get("Table");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonobj_1 = (JSONObject) jsonArray.get(i);
                name.setText(jsonobj_1.get("GuestName").toString());
                address.setText(jsonobj_1.get("Address").toString());
                email.setText(jsonobj_1.get("Email").toString());
                guestid=Integer.parseInt(jsonobj_1.get("ID").toString());
               dob.setText(convert(jsonobj_1.get("DOB").toString()));
                marige.setText(convert(jsonobj_1.get("MarriageAnniversary").toString()));

            }} catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public static String convert(String s){
        SimpleDateFormat newformat = new SimpleDateFormat("dd/MM/yyyy");
        try{
            if(s.contains("T")){
                String datestring = s.split("T")[0];
                SimpleDateFormat oldformat = new SimpleDateFormat("yyyy-MM-dd");
                String reformattedStr = newformat.format(oldformat.parse(datestring));
                return reformattedStr;
            }
            else{
                if(Integer.parseInt(s.split("-")[0])>13){
                    SimpleDateFormat oldformat = new SimpleDateFormat("yyyy-MM-dd");
                    String reformattedStr = newformat.format(oldformat.parse(s));
                    return reformattedStr;
                }
                else{
                    SimpleDateFormat oldformat = new SimpleDateFormat("MM-dd-yyyy");
                    String reformattedStr = newformat.format(oldformat.parse(s));
                    return reformattedStr;
                }

            }
        }
        catch (Exception e){
            return null;
        }
    }
}


