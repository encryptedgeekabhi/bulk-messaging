package com.example.hp.PATRON.feedback;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hp.PATRON.R;
import com.example.hp.PATRON.dbconnect.DBHelper;
import com.example.hp.PATRON.feed;
import com.example.hp.PATRON.networkstatus;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hp on 10-09-2018.
 */

public class feedback extends Fragment implements View.OnClickListener,validate,DatePickerDialog.OnDateSetListener {
EditText name,phone,email,dob,marige,remarks,address;
int datefor;
    String api;
    int id;
    Date birth,mrg;
    com.example.hp.PATRON.dbconnect.DBHelper db;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.feed_userdetails,
                container, false);
        db=new DBHelper(getActivity());
        api = db.getkeys().get(0);
        id=Integer.parseInt(getOnlyDigits(db.getbranchid().get(0)));
name=view.findViewById(R.id.name);
phone=view.findViewById(R.id.mobile);
email=view.findViewById(R.id.email);
address=view.findViewById(R.id.address);
dob=view.findViewById(R.id.dob);
remarks=view.findViewById(R.id.remarks);
marige=view.findViewById(R.id.marriage);
dob.setOnClickListener(this);
marige.setOnClickListener(this);
        Button submit=(Button)view.findViewById(R.id.submit);
        submit.setOnClickListener(this);



        return view;

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.submit)
        {
            if(networkstatus.check(getActivity()))
            {
     boolean res=  checkblank();
       if(res==true)
       {

           Any a=new Any();
a.execute();

    }}
    else{
                Toast.makeText(getActivity().getApplicationContext(), "No Active Data connection", Toast.LENGTH_SHORT).show();
            }
            }

        if(view.getId()==R.id.dob)
        {
            DatePickerDialog dialog = new DatePickerDialog(getActivity(),this, 1996, 9, 03);
            dialog.show();
           datefor=1;
        }
        if(view.getId()==R.id.marriage)
        {
            DatePickerDialog dialog = new DatePickerDialog(getActivity(),this, 1996, 9, 03);
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
            Toast.makeText(getActivity(),"invalid name..",Toast.LENGTH_SHORT).show();
        res=false;
        }
       else if(phone.getText().toString().isEmpty()||phone.getText().toString().length()!=10)
        {
            phone.setHint("invalid phone");
            phone.setText("");
            Toast.makeText(getActivity(),"invalid name..",Toast.LENGTH_SHORT).show();
            res=false;
        }
        else if(email.getText().toString().isEmpty())
        {
            phone.setHint("invalid phone");
            res=false;
            Toast.makeText(getActivity(),"invalid name..",Toast.LENGTH_SHORT).show();
        }
        return res;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        if(datefor==1)
dob.setText(Integer.toString(i1+1)+"/"+Integer.toString(i2)+"/"+Integer.toString(i));

        if(datefor==2)
            marige.setText(Integer.toString(i1+1)+"/"+Integer.toString(i2)+"/"+Integer.toString(i));
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

            String SOAP_ACTION = "http://tempuri.org/InsertGuestDetails";

            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "InsertGuestDetails";

            String URL = "http://erp.hellopatna.com/WebService/BilltronFeedback.asmx";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);



            request.addProperty("_APIKey",api.toString());
            request.addProperty("_BranchID",id);
            request.addProperty("_GuestName",name.getText().toString());
            request.addProperty("_Address",address.getText().toString());
            request.addProperty("_Mobile",phone.getText().toString());
            request.addProperty("_Email",email.getText().toString());
            request.addProperty("_DOB",dob.getText().toString());
            request.addProperty("_MarriageAnniversary",marige.getText().toString());
            request.addProperty("_Remarks",remarks.getText().toString());
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
                Bundle b = new Bundle();
                b.putString("guestid", result);
                Fragment f = new feed();
                f.setArguments(b);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.removable, f, f.getClass().getSimpleName()).addToBackStack(null).commit();

            }
            else{
                Toast.makeText(getActivity().getApplicationContext(),"please retry",Toast.LENGTH_SHORT).show();
            }
    }}
}
