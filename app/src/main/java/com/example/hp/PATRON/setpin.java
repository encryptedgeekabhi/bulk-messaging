package com.example.hp.PATRON;
/**
 * Created by hp on 03-09-2018.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hp.PATRON.dbconnect.DBHelper;

public class setpin extends AppCompatActivity implements View.OnClickListener {
    Button b;
    EditText pin;
    String id,pass;
    com.example.hp.PATRON.dbconnect.DBHelper db=new DBHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setpin);
        b=findViewById(R.id.pin_btn);

        pin=findViewById(R.id.pin_data);
        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        boolean res;
        Bundle bundle = getIntent().getExtras();
         id = bundle.getString("uid");
         pass=bundle.getString("pass");
        if(pin.getText().toString().isEmpty()||pin.getText().length()<4)
        {
            Toast.makeText(this,"enter Four digit pin..", Toast.LENGTH_SHORT).show();
        }
        else
        {
        /*   res=db.insertlogin(id.toString(),pass.toString(),pin.getText().toString());
            if(res)
            {
                Toast.makeText(this,"pin saved..", Toast.LENGTH_SHORT).show();

                this.finish();
            }
            else
            {
             boolean check=   db.getlogin(id.toString());
             if(check)
                Toast.makeText(this,"already registered", Toast.LENGTH_SHORT).show();
            }*/
            Intent i=new Intent(this,com.example.hp.PATRON.login.getapi.class);
            i.putExtra("uid",id.toString());
            i.putExtra("pass",pass.toString());
            i.putExtra("pin",pin.getText().toString());
            startActivity(i);
            this.finish();

    }


    }
}

