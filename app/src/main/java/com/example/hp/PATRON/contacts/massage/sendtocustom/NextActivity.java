package com.example.hp.PATRON.contacts.massage.sendtocustom;


import java.util.ArrayList;

public class NextActivity {
    int data;
  public  ArrayList<String> numtosend= new ArrayList<String>();
    public NextActivity()
    {

        for (int i = 0; i < com.example.hp.PATRON.contacts.massage.sendtocustom.CustomAdapter.modelArrayList.size(); i++){
            if(com.example.hp.PATRON.contacts.massage.sendtocustom.CustomAdapter.modelArrayList.get(i).getSelected()) {
                numtosend.add(CustomAdapter.modelArrayList.get(i).getcontacts());
            }

        }

    }
}





