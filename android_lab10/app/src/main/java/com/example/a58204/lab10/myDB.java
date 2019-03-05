package com.example.a58204.lab10;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by 58204 on 2017/12/6.
 */

public class myDB extends SQLiteOpenHelper{

    private static final String DB_NAME = "Contacts.db";
    private static final String TABLE_NAME = "Contacts";
    private static final int DB_VERSION = 1;

    public myDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "create table " + TABLE_NAME
                + "(name text primary key ,"
                + "birth text ,"
                + "gift text);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String name, String birth, String gift)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",name);
        values.put("birth",birth);
        values.put("gift",gift);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public void update(String name, String birth, String gift)
    {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "name = ?";
        String[] whereArgs = { name };
        ContentValues values = new ContentValues();
        values.put("name",name);
        values.put("birth",birth);
        values.put("gift",gift);
        db.update(TABLE_NAME,values,whereClause,whereArgs);
        db.close();
    }
    public void delete(String name)
    {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "name = ?";
        String[] whereArgs = { name };
        db.delete(TABLE_NAME,whereClause,whereArgs);
        db.close();
    }

    public Member query(String name, String birth, String gift)
    {
        Member m = null;
        SQLiteDatabase db = getReadableDatabase();
        String selection = "name = ?";
        String [] selectionArgs = {name};
        Cursor cursor = db.query(TABLE_NAME,null,selection,selectionArgs,null,null,null);
        if(cursor.moveToNext())
        {
            m = new Member(cursor.getString(0),cursor.getString(1),cursor.getString(2));

        }
        cursor.close();;
        db.close();
        return  m;
    }

    public List<Map<String,Object>> queryAll()
    {
        List<Map<String,Object>> data = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String query_str = "select * from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query_str,null);
        while(cursor.moveToNext())
        {
            Map<String,Object> temp = new LinkedHashMap<>();
            temp.put("name", cursor.getString(0));
            temp.put("birth",cursor.getString(1));
            temp.put("gift",cursor.getString(2));
            data.add(temp);
        }
        return data;
    }
}
