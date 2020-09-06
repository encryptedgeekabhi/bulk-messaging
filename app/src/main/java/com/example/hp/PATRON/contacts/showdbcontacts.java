package com.example.hp.PATRON.contacts;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.hp.PATRON.R;
import com.example.hp.PATRON.dbconnect.DBHelper;

import java.util.ArrayList;

/**
 * Created by hp on 25-08-2018.
 */

public class showdbcontacts extends android.support.v4.app.Fragment {
    ListView list;
    TextView tx;
    ArrayList<String> aa= new ArrayList<String>();
    SQLiteDatabase db;
    com.example.hp.PATRON.dbconnect.DBHelper dbhelper;
    public showdbcontacts()
    {}
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dbhelper=new DBHelper(getActivity());
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.dbcontactshow,
                container, false);
        TableLayout tableLayout=(TableLayout)v.findViewById(R.id.table);

        TableRow rowHeader = new TableRow(getActivity());
        //rowHeader.setBackgroundColor(Color.parseColor("#c0c0c0"));
        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
        String[] headerText={"Name","Phone","email","Remarks"};
        for(String c:headerText) {
            TextView tv = new TextView(getActivity().getApplicationContext());
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(18);
            tv.setPadding(5, 5, 5, 5);
            tv.setText(c);
            rowHeader.addView(tv);
        }
        tableLayout.addView(rowHeader);

db=dbhelper.getReadableDatabase();

            // Start the transaction.
            db.beginTransaction();

            try
            {
                String selectQuery = "SELECT * FROM contacts";
                Cursor cursor = db.rawQuery(selectQuery,null);
                if(cursor.getCount() >0)
                {
                    while (cursor.moveToNext()) {
                        // Read columns data
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String phone = cursor.getString(cursor.getColumnIndex("phone"));
                        String remark = cursor.getString(cursor.getColumnIndex("remarks"));
                        String email = cursor.getString(cursor.getColumnIndex("email"));
                        // dara rows
                        TableRow row = new TableRow(getActivity().getApplicationContext());
                        row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT));
                        String[] colText = {name + "", phone, email, remark};
                        for (String text : colText) {
                            TextView tv = new TextView(getActivity().getApplicationContext());
                            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                    TableRow.LayoutParams.WRAP_CONTENT));
                            tv.setGravity(Gravity.CENTER);
                            tv.setTextSize(18);
                            tv.setPadding(5, 5, 5, 5);
                            tv.setText(text);
                            row.addView(tv);
                        }
                        tableLayout.addView(row);

                    }

                }






            }
      catch (SQLiteException e)
              {
              e.printStackTrace();

              }

        db.endTransaction();
        db.close();
        return v;
    }
    public void getNumber(ContentResolver cr)
    {
     //    aa = db.getAllCotacts();
      //  ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,aa);
      //  list.setAdapter(adapter);
      //  registerForContextMenu(list);

    }

}
