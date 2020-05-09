package com.example.cultureapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.strictmode.SqliteObjectLeakedViolation;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Sehirler";
    public static final int DATABASE_VERSION = 2;
    public static final String SEHIR_TABLE_NAME = "sehir";
    public static final String SEHIR_COLUMN_ID = "sehirId";
    public static final String SEHIR_COLUMN_NAME = "sehiradi";



    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SEHIR_CREATE_TABLE = "CREATE TABLE " + SEHIR_TABLE_NAME + "("+ SEHIR_COLUMN_ID+ "INTEGER PRIMARY KEY AUTOINCREMENT," +
        SEHIR_COLUMN_NAME + "TEXT" + ")";

        db.execSQL(SEHIR_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void Add(String sehiradi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SEHIR_COLUMN_NAME,sehiradi);
        db.insert(SEHIR_TABLE_NAME,null,values);
        db.close();
    }

    public List<String> GetAll() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT sehiradi FROM sehir ",null);
        List<String> sehirler = new ArrayList<>();
        if(cursor.moveToFirst()) {
            do {
                sehirler.add(cursor.getString(1));
            }
            while (cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return sehirler;
    }

}

