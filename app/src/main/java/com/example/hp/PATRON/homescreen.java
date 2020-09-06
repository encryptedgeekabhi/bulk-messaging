package com.example.hp.PATRON;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hp.PATRON.dbconnect.DBHelper;

/**
 * Created by hp on 18-09-2018.
 */

public class homescreen extends Fragment {
    SharedPreferences.Editor editor;
static TextView usr,mobile,email,status;
 static   com.example.hp.PATRON.dbconnect.DBHelper db;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.homescreen,
                container, false);
        db=new DBHelper(getActivity());
 usr=view.findViewById(R.id.username);
mobile=view.findViewById(R.id.mobile);
email=view.findViewById(R.id.email);
status=view.findViewById(R.id.status);
if(db.getuserdetails()!=null && db.getuserdetails().size()>=2) {

    usr.setText(db.getuserdetails().get(0));
    mobile.setText(db.getuserdetails().get(1));
    email.setText(db.getuserdetails().get(2));
    status.setText(db.getuserdetails().get(3));
}
    return view;

}

    public static void setdata()
    {
        System.out.println("this is resume");
        usr.setText(db.getuserdetails().get(0));
        mobile.setText(db.getuserdetails().get(1));
        email.setText(db.getuserdetails().get(2));
        status.append(db.getuserdetails().get(3));
    }

}
