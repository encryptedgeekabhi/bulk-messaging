package com.example.hp.PATRON.feedback;

/**
 * Created by hp on 15-09-2018.
 */

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
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

public class getguestdetails extends Fragment implements View.OnClickListener {
    String api;
boolean allshow=true;
boolean bdyshow=false;
boolean mrgshow=false;
    public static int guestid=0;
    public static String guestname;
    int datefor;
    ProgressDialog progress;
    int id;
    EditText startdate,enddate;
    LinearLayout add;
    Button submit;
    RadioButton all,bdy,mrgdy;
    public static String strdate,endate;
    com.example.hp.PATRON.dbconnect.DBHelper db;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.guestdetail,
                container, false);
        progress=new ProgressDialog(getActivity());
        progress.setMessage("please wait..");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        db=new DBHelper(getActivity());
        add=view.findViewById(R.id.add);
        all=view.findViewById(R.id.all);
        bdy=view.findViewById(R.id.bdy);
        mrgdy=view.findViewById(R.id.mrgdy);
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
strdate=startdate.getText().toString();
endate=enddate.getText().toString();
                if(all.isChecked())
                {
                    allshow=true;
                    bdyshow=false;
                    mrgshow=false;
                }
                if(mrgdy.isChecked())
                {
                    allshow=false;
                    bdyshow=false;
                    mrgshow=true;

                }
            if(bdy.isChecked())
            {
                allshow=false;
                bdyshow=true;
                mrgshow=false;

            }
            add.removeAllViews();
            System.out.println("now printing");
                System.out.println(allshow);
            System.out.println(bdyshow);
            System.out.println(mrgshow);
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

            String SOAP_ACTION = "http://tempuri.org/GetGuestDetails2";

            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "GetGuestDetails2";

            String URL = "http://erp.hellopatna.com/WebService/BilltronFeedback.asmx";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            System.out.println(api+"    "+id+"    "+startdate.getText()+"    "+enddate.getText());

            request.addProperty("_APIKey",api.toString());
            request.addProperty("_BranchID",id);
            request.addProperty("_FromDate",startdate.getText().toString());
            request.addProperty("_ToDate",enddate.getText().toString());
            request.addProperty("_DOB",bdyshow);
            request.addProperty("_Anniversary",mrgshow);
            request.addProperty("_All",allshow);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transport = new HttpTransportSE(URL);

            try {
                transport.call(SOAP_ACTION, envelope);
                SoapObject res=(SoapObject)envelope.bodyIn;
                //SoapFault res=(SoapFault) envelope.bodyIn;
                result=res.getProperty(0).toString();
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
                final LinearLayout ln1=new LinearLayout(getActivity());
                ln1.setOrientation(LinearLayout.HORIZONTAL);
                final LinearLayout ln2=new LinearLayout(getActivity());
                ln2.setOrientation(LinearLayout.HORIZONTAL);
                final LinearLayout ln3=new LinearLayout(getActivity());
                ln3.setOrientation(LinearLayout.HORIZONTAL);
                final LinearLayout ln4=new LinearLayout(getActivity());
                ln4.setOrientation(LinearLayout.HORIZONTAL);
                final LinearLayout ln5=new LinearLayout(getActivity());
                ln5.setOrientation(LinearLayout.HORIZONTAL);
                final LinearLayout ln6=new LinearLayout(getActivity());
                ln6.setOrientation(LinearLayout.HORIZONTAL);
                final ImageView click=new ImageView(getActivity());
                click.setBackgroundResource(R.drawable.detailicon);
                final TextView tx=new TextView(getActivity());
                TextView tx1=new TextView(getActivity());
                TextView tx2=new TextView(getActivity());
                TextView tx3=new TextView(getActivity());
                TextView tx4=new TextView(getActivity());
                TextView tx5=new TextView(getActivity());
                TextView txspace=new TextView(getActivity());
                TextView txspaceup=new TextView(getActivity());
                TextView txspacedown=new TextView(getActivity());

                click.setLayoutParams(new LinearLayout.LayoutParams(60,60));
                txspaceup.setLayoutParams(new LinearLayout.LayoutParams(-1,30));
                txspacedown.setLayoutParams(new LinearLayout.LayoutParams(-1,30));
                tx.setLayoutParams(new LinearLayout.LayoutParams(800,60));
                tx1.setLayoutParams(new LinearLayout.LayoutParams(800,60));
                tx2.setLayoutParams(new LinearLayout.LayoutParams(800,60));
                tx3.setLayoutParams(new LinearLayout.LayoutParams(800,60));
                tx4.setLayoutParams(new LinearLayout.LayoutParams(800,60));
                tx5.setLayoutParams(new LinearLayout.LayoutParams(800,60));
                txspace.setLayoutParams(new LinearLayout.LayoutParams(-1,30));
                txspace.setBackgroundResource(R.drawable.top);
                click.setId(Integer.parseInt(jsonobj_1.get("ID").toString()));
                tx.setText("   Name       :   "+jsonobj_1.get("GuestName").toString());
                tx1.setText("   Address   :  "+jsonobj_1.get("Address").toString());
                tx2.setText("   Moble       :  "+jsonobj_1.get("Mobile").toString());
                tx3.setText("   Email        :  "+jsonobj_1.get("Email").toString());
                tx4.setText("   DOB          :  "+jsonobj_1.get("DOB").toString());
                tx5.setText("   Remarks  :  "+jsonobj_1.get("Remarks").toString());
                click.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println(click.getId());
                        guestname=jsonobj_1.get("GuestName").toString();
                        guestid=click.getId();
                        questionanswerdetail p=new questionanswerdetail(getActivity());

                    }

                });


                   // colorchange =false;
                //add.addView(click);
                tx.setTypeface(null, Typeface.BOLD);
                ln1.addView(tx);
                add.addView(txspaceup);
                add.addView(ln1);
                tx1.setTypeface(null, Typeface.BOLD);
                ln2.addView(tx1);
                add.addView(ln2);
                tx2.setTypeface(null, Typeface.BOLD);
                ln3.addView(tx2);
                ln3.addView(click);
                add.addView(ln3);
                tx3.setTypeface(null, Typeface.BOLD);
               ln4.addView(tx3);
                add.addView(ln4);
                tx4.setTypeface(null, Typeface.BOLD);
                ln5.addView(tx4);
                add.addView(ln5);
                tx5.setTypeface(null, Typeface.BOLD);
                ln6.addView(tx5);
                add.addView(ln6);
                add.addView(txspacedown);
                add.addView(txspace);


            }

            progress.dismiss();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
}
