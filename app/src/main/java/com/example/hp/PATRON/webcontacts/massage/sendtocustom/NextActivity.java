package com.example.hp.PATRON.webcontacts.massage.sendtocustom;


import java.util.ArrayList;

public class NextActivity {
    int data;
  public  ArrayList<String> numtosend= new ArrayList<String>();
    public NextActivity()
    {

        for (int i = 0; i < CustomAdapter.modelArrayList.size(); i++){
            if(CustomAdapter.modelArrayList.get(i).getSelected()) {
                numtosend.add(CustomAdapter.modelArrayList.get(i).getcontacts());
            }

        }

    }
}





