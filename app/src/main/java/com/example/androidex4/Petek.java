package com.example.androidex4;

import android.database.Cursor;

import java.util.Calendar;
import java.util.Date;

public class Petek {
    public int id;
    public String title;
    public String content;
    public String status;
    public Date date;

    public Petek(){}
    public Petek(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.status = "sent";
        this.date = Calendar.getInstance().getTime();
    }

    public static String TABLE_NAME = "Peteks";
    public static String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT, content TEXT, status TEXT, date DATE)";

    public Petek(Cursor c) {
        id = c.getInt(0);
        title = c.getString(1);
        content = c.getString(2);
        status = c.getString(3);
        date = Calendar.getInstance().getTime();
    }
    
    public String getSQLInsertString(){
        return "INSERT INTO " + TABLE_NAME +
                " (title, content, status, date) VALUES('" + title + "','" + content + "','" + status + "','" + date + "')";
    }

    public static String setPetek(int id, String title, String content){
        return "UPDATE " + TABLE_NAME + " SET " /*+ "title = " + title +
                " and "*/ + "content = " + content + " WHERE " + "id = " + id;
    }

    public static String SELECT_ALL =
            "SELECT * FROM " + TABLE_NAME;

    public String toString(){
        return title + "\n" + content;
    }
}
