package com.example.hp.PATRON.contacts.massage;
/**
 * Created by hp on 23-08-2018.
 */

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class compose extends Fragment implements View.OnClickListener{


    RadioGroup radioGroup;
    ImageView set;
    ImageView finalsend;
    RadioButton toall,custom;

   static EditText msg;
    String contactnumber="";
    public compose() {
        // Required empty public constructor
    }


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
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);


        return view;
    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.Set)
        {
          if(toall.isChecked())
          {
              getNumber(getActivity().getContentResolver());
              msg.setText(contactnumber.toString());
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
    Thread thread = new Thread(new Runnable() {

        @Override
        public void run() {
            try  {

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });

    public void getNumber(ContentResolver cr) {
        StringBuilder sbPostData= new StringBuilder(contactnumber);
        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {
            String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

         sbPostData.append(getOnlyDigits(number));
if(phones.isLast())
{}
else
{
    sbPostData.append(",");
}
        }
        contactnumber=sbPostData.toString();

        phones.close();


    }
    public static String getOnlyDigits(String s) {
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(s);
        String number = matcher.replaceAll("");
        return number;
    }
}