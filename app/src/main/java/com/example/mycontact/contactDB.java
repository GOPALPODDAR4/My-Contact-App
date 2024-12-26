package com.example.mycontact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class contactDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="my database";
    public static final String KEY_ID="id";
    public static final String NAME="name";
    public static final String PHONE_NO="phoneno";

    public contactDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String qry="create table tbl_data (" + KEY_ID +
                " integer primary key autoincrement, " + NAME + " text, " + PHONE_NO + " text )";
        db.execSQL(qry);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String qry="DROP TABLE IF EXISTS tbl_data";
        db.execSQL(qry);
        onCreate(db);
    }
    public String addrecord(String name , String number){

        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();

        ContentValues cv =new ContentValues();
        cv.put(NAME,name);
        cv.put(PHONE_NO,number);

        Long result=sqLiteDatabase.insert("tbl_data",null,cv);

        if (result==-1){
            return  "Failed";
        }
        else {
            return "Successfully Inserted";
        }
    }

    public ArrayList<contactModel> readAllData(){

        ArrayList<contactModel> data =new ArrayList<>();
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        String qry="select * from tbl_data order by " + KEY_ID +" desc";
        Cursor cursor=sqLiteDatabase.rawQuery(qry,null);

        if (cursor.moveToFirst()){
            do {
                contactModel model=new contactModel();
                model.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                model.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                model.setNumber(cursor.getString(cursor.getColumnIndex(PHONE_NO)));
                data.add(model);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }
    public boolean deleteItem(int id){
        SQLiteDatabase db =this.getWritableDatabase();
        int result=db.delete("tbl_data","id=?",new String[]{String.valueOf(id)});
        return  result>0;
    }

    public boolean updateContact(int id, String name, String number){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NAME,name);
        cv.put(PHONE_NO,number);
        long result = db.update("tbl_data",cv,"id=?",new String[]{String.valueOf(id)});

        return result >0;
    }
}
