package com.example.hp.PATRON.contacts.massage.sendtocustom;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.hp.PATRON.R;

import java.util.ArrayList;

public class customcontacts extends Fragment {

    public customcontacts()
    {}

    private ListView lv;
    private ArrayList<Model> modelArrayList;
    private CustomAdapter customAdapter;
    private Button btnselect, btndeselect, btnnext;
    ArrayList<String> aa= new ArrayList<String>();
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.customsend,
                container, false);
        getNumber(getActivity().getContentResolver());
        lv=v.findViewById(R.id.lv);


        btndeselect= v.findViewById(R.id.deselect);
        btnselect=v.findViewById(R.id.select);
        btnnext=v.findViewById(R.id.next);
        modelArrayList = getModel(false);
        customAdapter = new CustomAdapter(getActivity(),modelArrayList);
        lv.setAdapter(customAdapter);

        btnselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelArrayList = getModel(true);
                customAdapter = new CustomAdapter(getActivity(),modelArrayList);
                lv.setAdapter(customAdapter);
            }
        });
        btndeselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelArrayList = getModel(false);
                customAdapter = new CustomAdapter(getActivity(),modelArrayList);
                lv.setAdapter(customAdapter);
            }
        });
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NextActivity next=new NextActivity();

                String a=next.numtosend.get(5);
                btnnext.setText(a.toString());


            }
        });




        return v;
    }





   private ArrayList<Model> getModel(boolean isSelect){
        ArrayList<Model> list = new ArrayList<>();
        for(int i = 1; i < aa.size(); i++){

            Model model = new Model();
            model.setSelected(isSelect);
            String s=aa.get(i).toString();
            model.setContacts(s);
            list.add(model);
        }
        return list;
    }
    public void getNumber(ContentResolver cr) {
        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            aa.add(name + "-" + number);

        }

        phones.close();


    }

}
