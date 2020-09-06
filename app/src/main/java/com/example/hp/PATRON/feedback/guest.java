package com.example.hp.PATRON.feedback;

/**
 * Created by hp on 15-09-2018.
 */

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.PATRON.R;
import com.example.hp.PATRON.dbconnect.DBHelper;
import com.example.hp.PATRON.networkstatus;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by hp on 15-09-2018.
 */

public class guest extends Fragment implements View.OnClickListener {
    String api;

public static int guestid=0;
public static String guestname;
    int datefor;
    ProgressDialog progress;
    int id;
    EditText startdate,enddate;
LinearLayout add;
    Button submit;
    com.example.hp.PATRON.dbconnect.DBHelper db;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.getcompleteguestdetail,
                container, false);
        progress=new ProgressDialog(getActivity());
        progress.setMessage("please wait..");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        db=new DBHelper(getActivity());
        add=view.findViewById(R.id.add);
        api = db.getkeys().get(0);
        id=Integer.parseInt(getOnlyDigits(db.getbranchid().get(0)));
        startdate=view.findViewById(R.id.startdate);
        enddate=view.findViewById(R.id.enddate);

        submit=(Button)view.findViewById(R.id.get);
        submit.setOnClickListener(this);
        startdate.setOnClickListener(this);
        enddate.setOnClickListener(this);
        return view;

    }

    public static String getOnlyDigits(String s) {
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(s);
        String number = matcher.replaceAll("");
        return number;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.startdate) {
            DatePickerDialog dialog = new DatePickerDialog(getActivity(),datePickerListener, 2016, 8, 03);
            dialog.show();
            datefor = 1;
        }
        if (view.getId() == R.id.enddate) {
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), datePickerListener, 2016, 8, 03);
            dialog.show();
            datefor = 2;
        }
        if (view.getId() == R.id.get) {
            add.removeAllViews();
            if(networkstatus.check(getActivity()))
            {
            progress.show();
            Any a = new Any();
            a.execute();
        }
        else
            {
                Toast.makeText(getActivity().getApplicationContext(),"No Data connection",Toast.LENGTH_SHORT).show();
            }
        }

    }
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            if (datefor == 1)
            {
                if (i2 <= 9) {
                    if(i1<=8)
                    startdate.setText("0" + Integer.toString(i2) + "/" +"0"+ Integer.toString(i1 + 1) + "/" + Integer.toString(i));
                    else

                        startdate.setText("0" + Integer.toString(i2) + "/" + Integer.toString(i1 + 1) + "/" + Integer.toString(i));
                }
                else if(i1<=8)
                {
                    startdate.setText(Integer.toString(i2) + "/" +"0"+Integer.toString(i1 + 1) + "/" + Integer.toString(i));
                }
                else
                {
                    startdate.setText(Integer.toString(i2) + "/" + Integer.toString(i1 + 1) + "/" + Integer.toString(i));
                }
            }
            else if (datefor == 2) {
                if (i2 <= 9)
                {
                    if(i1<=8)
                        enddate.setText("0" + Integer.toString(i2) + "/" +"0"+ Integer.toString(i1 + 1) + "/" + Integer.toString(i));
                    else
                        enddate.setText("0" + Integer.toString(i2) + "/" + Integer.toString(i1 + 1) + "/" + Integer.toString(i));
                }
                else if(i1<=8)
                {
                    enddate.setText(Integer.toString(i2) + "/" +"0"+Integer.toString(i1 + 1) + "/" + Integer.toString(i));
                }
                else
                {
                    enddate.setText(Integer.toString(i2) + "/" + Integer.toString(i1 + 1) + "/" + Integer.toString(i));
                }
            }}
    };
    class Any extends AsyncTask<String, String, String> {
        String result="";
        @Override
        protected String doInBackground(String... params) {

            String SOAP_ACTION = "http://tempuri.org/GetCompleteFeedbackDetails2";

            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "GetCompleteFeedbackDetails2";

            String URL = "http://erp.hellopatna.com/WebService/BilltronFeedback.asmx";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            System.out.println(api+"    "+id+"    "+startdate.getText()+"    "+enddate.getText());

            request.addProperty("_APIKey",api.toString());
            request.addProperty("_BranchID",id);
            request.addProperty("_FromDate",startdate.getText().toString());
            request.addProperty("_ToDate",enddate.getText().toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transport = new HttpTransportSE(URL);

            try {
                transport.call(SOAP_ACTION, envelope);
                SoapObject res=(SoapObject)envelope.bodyIn;
                // SoapFault res=(SoapFault) envelope.bodyIn;
                result=res.getProperty(0).toString();
                System.out.println(res);

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
        }}
    public void parsexml(String s) {


        JSONParser parse = new JSONParser();
        try {
            System.out.println(s);
            JSONObject jobj = (JSONObject)parse.parse(s);
            JSONArray jsonArray = (JSONArray) jobj.get("Table");
            Boolean colorchange=true;
            for (int i = 0; i < jsonArray.size(); i++) {
                final JSONObject jsonobj_1 = (JSONObject) jsonArray.get(i);
                final TextView tx=new TextView(getActivity());
                TextView tx1=new TextView(getActivity());
                TextView tx2=new TextView(getActivity());
                TextView tx3=new TextView(getActivity());
                TextView tx4=new TextView(getActivity());
                TextView tx5=new TextView(getActivity());
                TextView feeddate=new TextView(getActivity());
                TextView ques=new TextView(getActivity());
                TextView ans=new TextView(getActivity());
                TextView txspace=new TextView(getActivity());
                tx.setLayoutParams(new LinearLayout.LayoutParams(-1,60));
                tx1.setLayoutParams(new LinearLayout.LayoutParams(-1,60));
                tx2.setLayoutParams(new LinearLayout.LayoutParams(-1,60));
               feeddate.setLayoutParams(new LinearLayout.LayoutParams(-1,60));
                ques.setLayoutParams(new LinearLayout.LayoutParams(-1,60));
                ans.setLayoutParams(new LinearLayout.LayoutParams(-1,60));
                txspace.setLayoutParams(new LinearLayout.LayoutParams(-1,80));
                tx.setText("   Name        :   "+jsonobj_1.get("GuestName").toString());
                tx1.setText("   Address   :  "+jsonobj_1.get("Address").toString());
                tx2.setText("   Moble       :  "+jsonobj_1.get("Mobile").toString());
                tx3.setText("   Email        :  "+jsonobj_1.get("Email").toString());
                tx4.setText("   DOB          :  "+feedback1.convert(jsonobj_1.get("DOB").toString()));
                tx5.setText("   Marriage   :  "+feedback1.convert(jsonobj_1.get("MarriageAnniversary").toString()));
                if(jsonobj_1.get("FeedbackDate") !=null)
                feeddate.setText("   Feedback on - "+feedback1.convert(jsonobj_1.get("FeedbackDate").toString()));
                if(jsonobj_1.get("Question").toString()!=null)
                ques.setText("   Ques.     -  "+jsonobj_1.get("Question").toString());
                ans.setText("   Ans.     -  "+jsonobj_1.get("Answer").toString());


                   tx.setBackgroundResource(R.drawable.rectback);
                    tx1.setBackgroundResource(R.drawable.rectback);
                    tx2.setBackgroundResource(R.drawable.rectback);
                    tx3.setBackgroundResource(R.drawable.rectback);
                    tx4.setBackgroundResource(R.drawable.rectback);
                    tx5.setBackgroundResource(R.drawable.rectback);
                  feeddate.setBackgroundResource(R.drawable.rectback);
                    ques.setBackgroundResource(R.drawable.rectback);
                    ans.setBackgroundResource(R.drawable.rectback);
                    colorchange =false;
                tx.setTypeface(null, Typeface.BOLD);

                add.addView(tx);
                tx1.setTypeface(null, Typeface.BOLD);
                add.addView(tx1);
                tx2.setTypeface(null, Typeface.BOLD);
                add.addView(tx2);
                tx3.setTypeface(null, Typeface.BOLD);
                add.addView(tx3);
                tx4.setTypeface(null, Typeface.BOLD);
                add.addView(tx4);
                tx5.setTypeface(null, Typeface.BOLD);
                add.addView(tx5);
                feeddate.setTypeface(null, Typeface.BOLD);
                add.addView(feeddate);
                ques.setTypeface(null, Typeface.BOLD);
                add.addView(ques);
                ans.setTypeface(null, Typeface.BOLD);
                add.addView(ans);
                add.addView(txspace);


            }

progress.dismiss();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
}
