package com.example.hp.PATRON;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.PATRON.dbconnect.DBHelper;

/**
 * Created by hp on 12-09-2018.
 */

public class pinlogin extends Activity implements View.OnClickListener {
    Button b;
    TextView forget;
    EditText pin;
    com.example.hp.PATRON.dbconnect.DBHelper db=new DBHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pinlogin);
        b=findViewById(R.id.login);
        pin=findViewById(R.id.pinvalue);
        forget = findViewById(R.id.forget);
        forget.setOnClickListener(this);
        b.setOnClickListener(this);

    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.forget)
        {
            this.finish();
            Intent i=new Intent(this,nowlogin.class);
            startActivity(i);
        }
        if(view.getId()==R.id.login)
        {
       if(db.getpinlogin(pin.getText().toString()).size()>0)
        {
           this.finish();
        }
        else
       {
           Toast.makeText(getApplicationContext(),"invalid input..",Toast.LENGTH_SHORT).show();
       }
    }}
}
