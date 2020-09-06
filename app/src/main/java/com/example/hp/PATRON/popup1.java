package com.example.hp.PATRON;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.hp.PATRON.dbconnect.DBHelper;
import com.example.hp.PATRON.feedback.guest;
import com.example.hp.PATRON.feedback.questionanswerdetail;

/**
 * Created by hp on 22-09-2018.
 */

public class popup1 extends Activity{
    Context c;
    Activity a;
    com.example.hp.PATRON.dbconnect.DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        db = new DBHelper(this);
       setContentView(R.layout.popup1);
        WindowManager.LayoutParams params = getWindow().getAttributes();
            params.x = -50;
            params.y = -20;

            this.getWindow().setAttributes(params);

        }

}
