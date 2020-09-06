package com.example.hp.PATRON;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;


public class PhoneStateReceiver extends BroadcastReceiver {
    static String setnum;
    SharedPreferences.Editor editor;
    @Override

    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        if(sharedPref.getString("popup", "").equalsIgnoreCase("true")) {
            try {
                System.out.println("Receiver start");
                String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

                if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    Toast.makeText(context, "Incoming Call State" + incomingNumber.toString(), Toast.LENGTH_SHORT).show();
                    setnum = incomingNumber.toString();

                    Toast.makeText(context, "Ringing State Number is -" + incomingNumber, Toast.LENGTH_SHORT).show();


                }

                if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    Toast.makeText(context, "Call Idle State", Toast.LENGTH_SHORT).show();
                    // MainActivity.txt.setText("idle state");
                    setnum = incomingNumber.toString();
                    Intent i = new Intent(context, PopUp.class);
                    i.putExtras(intent);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    Thread.sleep(2000);
                    context.startActivity(i);
                    //popup1 p=new popup1(context);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


