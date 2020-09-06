package com.example.hp.PATRON;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hp.PATRON.dbconnect.DBHelper;

public class PopUp extends Activity implements View.OnClickListener {
EditText name,phone,email,remarks;
Button save;
Button cancel;
com.example.hp.PATRON.dbconnect.DBHelper db=new DBHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            Log.d("PopUp: onCreate: ", "flag2");

            

            // TODO Auto-generated method stub
            super.onCreate(savedInstanceState);

           // getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);


            Log.d("PopUp: onCreate: ", "flagy");

            setContentView(R.layout.popup);

            Log.d("PopUp: onCreate: ", "flagz");

            String number = getIntent().getStringExtra(
                    TelephonyManager.EXTRA_INCOMING_NUMBER);

            phone= findViewById(R.id.phonenumber);
            name=findViewById(R.id.name);
            email=findViewById(R.id.email);
            remarks=findViewById(R.id.remarks);
            save=findViewById(R.id.save);
            cancel=findViewById(R.id.exit);
            phone.setText(PhoneStateReceiver.setnum.toString().substring(3));


            save.setOnClickListener(this);
           // cancel.setOnClickListener(this);
        } 
        catch (Exception e) {
            Log.d("Exception", e.toString());
            // TODO Auto-generated catch block
            e.printStackTrace();
           // save.setText("error");
        }
    }

    @Override
    public void onClick(View view) {
        save.setText("clicked");
        String n,m,e,r;
        n=name.getText().toString();
        m=phone.getText().toString();
        e=email.getText().toString();
        r=remarks.getText().toString();
db.insertContact(n,m,e,r);
        Toast.makeText(this,db.getAllCotacts().toString(), Toast.LENGTH_SHORT).show();
        db.close();
        this.finish();
    }
}