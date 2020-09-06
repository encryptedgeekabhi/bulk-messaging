package com.example.hp.PATRON.login;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hp.PATRON.R;

/**
 * Created by hp on 04-09-2018.
 */

public class loggedfragmant extends Fragment {
    TextView tx;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.logedwindow,
                container, false);

        tx=view.findViewById(R.id.loggedtext);
        String strtext = getArguments().getString("edttext");
        tx.setText(strtext.toString());
        tx.setTextColor(Color.GREEN);
        return view;

}
    }
