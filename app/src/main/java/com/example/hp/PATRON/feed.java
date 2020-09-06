package com.example.hp.PATRON;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.PATRON.dbconnect.DBHelper;
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
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hp on 11-09-2018.
 */
public class feed extends Fragment implements View.OnClickListener {
String api,guestid;
int id;
String question,answer;
    String[][] quesansArray;
    LinearLayout m_ll;
    com.example.hp.PATRON.dbconnect.DBHelper db;
   ProgressDialog progress;
   Button submit;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.feedbacktous,
                container, false);
        m_ll = (LinearLayout) view.findViewById(R.id.addhere);
        submit=view.findViewById(R.id.submit);
        submit.setOnClickListener(this);
        progress=new ProgressDialog(getActivity());
        progress.setMessage("please wait..");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        db=new DBHelper(getActivity());
        api = db.getkeys().get(0);
        id=Integer.parseInt(getOnlyDigits(db.getbranchid().get(0)));
        guestid =getArguments().getString("guestid");
        System.out.println("this is "+api+"       "+id+"     "+guestid);
        Any a=new Any();
        a.execute();









        return view;

    }

    @Override
    public void onClick(View view) {
        if(networkstatus.check(getActivity())) {
            if (quesansArray[0][1]==null) {
                Toast.makeText(getActivity().getApplicationContext(), "please give feedback.", Toast.LENGTH_SHORT).show();
            } else {
                Any1 a = new Any1();
                progress.show();
                a.execute();
            }
        }
        else
            {
                Toast.makeText(getActivity().getApplicationContext(), "No Active Data connection", Toast.LENGTH_SHORT).show();
            }
        }




    class Any extends AsyncTask<String, String, String> {
        String result="";
        @Override
        protected String doInBackground(String... params) {

            String SOAP_ACTION = "http://tempuri.org/GetFeedbackQuestionSettings2";

            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "GetFeedbackQuestionSettings2";

            String URL = "http://erp.hellopatna.com/WebService/BilltronFeedback.asmx";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);



            request.addProperty("_APIKey",api.toString());
            request.addProperty("_BranchID",id);

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
            System.out.println(result);
            parsexml(result);
        }
}
    public static String getOnlyDigits(String s) {
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(s);
        String number = matcher.replaceAll("");
        return number;
    }

    public void parsexml(String s) {
        final ArrayList<String> ques = new ArrayList<String>();
        ArrayList<String>qid=new ArrayList<>();
        JSONParser parse = new JSONParser();
        try {
            System.out.println(s);
            JSONObject jobj = (JSONObject)parse.parse(s);
            JSONArray jsonArray = (JSONArray) jobj.get("Table");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonobj_1 = (JSONObject)jsonArray.get(i);
                qid.add(jsonobj_1.get("ID").toString());
                ques.add(jsonobj_1.get("Question").toString());

                for(int j=0;j<qid.size();j++)
                {
                    final SmileRating smileRating=new SmileRating(getActivity());
                    smileRating.setId(Integer.parseInt(qid.get(j)));
                    TextView tx=new TextView(getActivity());

                    tx.setTextColor(Color.WHITE);
                    tx.setLayoutParams(new LinearLayout.LayoutParams(-1,120));

                    tx.setId(i);
                    tx.setText("Q."+qid.get(j).toString()+ques.get(j).toString());
                    tx.setTypeface(null, Typeface.BOLD);
                    m_ll.addView(tx);
                    smileRating.setLayoutParams(new LinearLayout.LayoutParams(-1,270));
                    smileRating.setId(j);

                    smileRating.setShowLine(false);
                    m_ll.addView(smileRating);

                    smileRating.setOnSmileySelectionListener((new SmileRating.OnSmileySelectionListener() {

                        @Override
                        public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
                            Any1 a=new Any1();
                            // reselected is false when user selects different smiley that previously selected one
                            // true when the same smiley is selected.
                            // Except if it first time, then the value will be false.
                            switch (smiley) {
                                case SmileRating.BAD:
                                    quesansArray[smileRating.getId()][0]=ques.get(smileRating.getId());
                                    quesansArray[smileRating.getId()][1]="BAD";


                                    break;
                                case SmileRating.GOOD:
                                    System.out.println("good");
                                    quesansArray[smileRating.getId()][0]=ques.get(smileRating.getId());
                                    quesansArray[smileRating.getId()][1]="good";
                                    break;
                                case SmileRating.GREAT:
                                    quesansArray[smileRating.getId()][0]=ques.get(smileRating.getId());
                                    quesansArray[smileRating.getId()][1]="great";
                                    break;
                                case SmileRating.OKAY:
                                    System.out.println("ohkey");
                                    quesansArray[smileRating.getId()][0]=ques.get(smileRating.getId());
                                    quesansArray[smileRating.getId()][1]="ohkey";
                                    break;
                                case SmileRating.TERRIBLE:
                                    System.out.println("terrible");
                                    quesansArray[smileRating.getId()][0]=ques.get(smileRating.getId());
                                    quesansArray[smileRating.getId()][1]="terrible";
                                    break;
                            }   }

                    }));}
                quesansArray = new String[ques.size()][2];
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    class Any1 extends AsyncTask<String, String, String> {
        String result="";
        @Override
        protected String doInBackground(String... params) {

            String SOAP_ACTION = "http://tempuri.org/InsertFeedbackAnswer";

            String NAMESPACE = "http://tempuri.org/";
            String METHOD_NAME = "InsertFeedbackAnswer";
            for (int i = 0; i < quesansArray.length; i++) {
                question = quesansArray[i][0];
                answer = quesansArray[i][1];
            String URL = "http://erp.hellopatna.com/WebService/BilltronFeedback.asmx";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);



            request.addProperty("_APIKey",api.toString());
            request.addProperty("_BranchID",id);
            request.addProperty("_GuestID",guestid);
            request.addProperty("_FeedbackDate","09/14/2018");
            request.addProperty("_Question",question);
            request.addProperty("_Answer",answer);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transport = new HttpTransportSE(URL);

            try {
                transport.call(SOAP_ACTION, envelope);
                SoapObject res=(SoapObject)envelope.bodyIn;
                result=res.getProperty(0).toString();
                System.out.println("*******************************************");

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }}
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println(result);
            progress.dismiss();

        }
    }
}

