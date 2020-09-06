package com.example.hp.PATRON.dbconnect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "smsmedia.db";
    public static final String CONTACTS_TABLE_NAME = "contacts";
    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_EMAIL = "email";
    public static final String CONTACTS_COLUMN_PHONE = "phone";
    ArrayList<String> numberlist_list = new ArrayList<String>();

    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table contacts " +
                        "(name text,phone text,email text,remarks text)"
        );
        db.execSQL(
                "create table login " +
                        "(id text unique,password text,pin text,api text,defaultgroup text,groupid integer)"
        );
        db.execSQL(
                "create table keys " +
                        "(ID text,serialno text,serialkey text,apikey text)"
        );
        db.execSQL(
                "create table log " +
                        "(ID text,userid text,branchid text,pin text,name text,mobile text,email text,accstatus text)"
        );
        db.execSQL(
                "create table guestdetails " +
                        "(ID INTEGER PRIMARY KEY AUTOINCREMENT,name text,phone text,address text,email text,dob text,mrg text,remarks text)"
        );
        db.execSQL(
                "create table feedback " +
                        "(ID INTEGER PRIMARY KEY AUTOINCREMENT,guestid text,question text,answer text)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean insertContact (String name, String phone, String email,String remarks) {
       SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("remarks", remarks);
        db.insert("contacts", null, contentValues);
        return true;
    }

    public boolean insertkeys (String serno, String serkey, String apikey) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("serialno", serno);
        contentValues.put("serialkey", serkey);
        contentValues.put("apikey", apikey);
        db.insert("keys", null, contentValues);
        return true;
    }
    public boolean insertlog (String uid, String brid, String pin,String name,String mobile,String email,String status) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userid", uid);
        contentValues.put("branchid", brid);
        contentValues.put("pin", pin);
        contentValues.put("name",name);
        contentValues.put("mobile",mobile);
        contentValues.put("email", email);
        contentValues.put("accstatus", status);
        db.insert("log", null, contentValues);
        return true;
    }
    public boolean insertguestdetails (String name, String phone,String address, String email,String dob,String mrg,String remarks) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("address", address);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("address",address);
        contentValues.put("dob", dob);
        contentValues.put("mrg", mrg);
        contentValues.put("remarks", remarks);

        db.insert("guestdetails", null, contentValues);
        return true;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }
    public ArrayList<String> getAllCotacts() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            String contacts=res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME))+" "+res.getString(res.getColumnIndex(CONTACTS_COLUMN_PHONE));
            array_list.add(contacts);

            res.moveToNext();
        }
        return array_list;
    }
    public boolean getloginavilable() {
        //hp = new HashMap();
        boolean result;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from log", null );
        res.moveToFirst();

        if(res.isFirst()){
          result=true;
        }
        else
            result=false;
        return result;
    }
    public boolean getkeysavilable() {
        //hp = new HashMap();
        boolean result;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from keys", null );
        res.moveToFirst();

        if(res.isFirst()){
            result=true;
        }
        else
            result=false;
        return result;
    }
    public boolean getlogin(String id) {


        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from login where id="+id+"", null);
        res.moveToFirst();

        if(res.isNull(0))

        {
            return false;
        }
        return true;
    }
    public Cursor getloginbypin(String pin) {
        SQLiteDatabase db = this.getReadableDatabase();
        String q="select * from login where pin='" + pin+"'";
        Cursor res = db.rawQuery(q, null);
        db.close();
        return res;
    }
    public boolean insertlogin (String name, String password, String pin,String api) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", name);
        contentValues.put("password", password);
        contentValues.put("pin", pin);
        contentValues.put("api", api);
        db.insert("login", null, contentValues);
        db.close();
        return true;

    }
    public boolean deleteall() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("log", null, null);
return true;
    }
    public boolean deletekey() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("keys", null, null);
        return true;
    }
    public ArrayList<String> getapi() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select api from login", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex("api")));
            res.moveToNext();
        }
        db.close();
        return array_list;
    }
    public ArrayList<String> getpinlogin(String pin) {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        String q="select * from log where pin='" + pin+"'";
        Cursor res =  db.rawQuery( q, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
           array_list.add("yes");
            res.moveToNext();
        }
        db.close();
        return array_list;
    }
    public ArrayList<String> getkeys() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from keys", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            String allkey=res.getString(res.getColumnIndex("serialno"))+" "+res.getString(res.getColumnIndex("serialkey"))+" "+res.getString(res.getColumnIndex("apikey"));
            array_list.add(res.getString(res.getColumnIndex("apikey")));

            res.moveToNext();
        }
        return array_list;
    }
    public ArrayList<String> getbranchid() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from log", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex("branchid")));
            res.moveToNext();
        }
        return array_list;
    }
    public ArrayList<String> getAllguest() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from guestdetails", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            String contacts=res.getString(res.getColumnIndex("name"));
            array_list.add(contacts);

            res.moveToNext();
        }
        return array_list;
    }
    public ArrayList<String> getuserdetails() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from log", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex("name")));
            array_list.add(res.getString(res.getColumnIndex("mobile")));
            array_list.add(res.getString(res.getColumnIndex("email")));
            array_list.add(res.getString(res.getColumnIndex("accstatus")));
            res.moveToNext();
        }
        return array_list;
    }

    public int updategroup(String api,String group,int groupid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("defaultgroup",group);
        cv.put("groupid",groupid);
     Integer i=   db.update("login", cv, "api='"+api+"'", null);
     db.close();
    return i;
    }
}
