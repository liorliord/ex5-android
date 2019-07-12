package com.example.androidex4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static int VERSION = 1;

    public DBHelper(Context context) {
        super(context, "db", null, VERSION);
    }

    
    @Override
    public void onCreate(SQLiteDatabase db) {
        if(db == null)
            return;
        db.execSQL(Petek.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        if(db == null)
            return;
        //change db according to version
    }
}
