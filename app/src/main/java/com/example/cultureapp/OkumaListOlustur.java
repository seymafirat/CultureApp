package com.example.cultureapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class OkumaListOlustur extends AppCompatActivity {
    EditText etKitapAd,etYazarAd,etKitapNot;
    Button btnKitapKaydet;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okuma_list_olustur);
        etKitapAd = findViewById(R.id.etKitapAd);
        etYazarAd = findViewById(R.id.etYazarAd);
        etKitapNot = findViewById(R.id.etKitapNot);
        btnKitapKaydet = findViewById(R.id.btnKitapKaydet);
        database = this.openOrCreateDatabase("kitap", MODE_PRIVATE, null);

        Intent intent = getIntent();
        String info = intent.getStringExtra("info");

        if (info.matches("new")) {
            etKitapAd.setText("");
            etYazarAd.setText("");
            etKitapNot.setText("");
            btnKitapKaydet.setVisibility(View.VISIBLE);


        } else {

            int kitapId = intent.getIntExtra("kitapId", 1);
            btnKitapKaydet.setVisibility(View.INVISIBLE);

            try {
                Cursor cursor = database.rawQuery("SELECT * FROM kitaplar WHERE id = ?", new String[]{String.valueOf(kitapId)});
                int kitapadIx = cursor.getColumnIndex("kitapad");
                int yazaradNameIx = cursor.getColumnIndex("yazarad");
                int kitapnotIx = cursor.getColumnIndex("kitapnot");


                while (cursor.moveToNext()) {
                    etKitapAd.setText(cursor.getString(kitapadIx));
                    etYazarAd.setText(cursor.getString(yazaradNameIx));
                    etKitapNot.setText(cursor.getString(kitapnotIx));

                }
                cursor.close();
            } catch (Exception e) {

            }
        }

    }

    public void ClickKitapKaydet(View view) {
        String kitapAd = etKitapAd.getText().toString();
        String yazarAd = etYazarAd.getText().toString();
        String kitapNot = etKitapNot.getText().toString();

        try {
            database = this.openOrCreateDatabase("kitap", MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS kitaplar (id INTEGER PRIMARY KEY,kitapad VARCHAR, yazarad VARCHAR, kitapnot VARCHAR)");


            String sqlString = "INSERT INTO kitaplar(kitapad,yazarad,kitapnot) VALUES (?,?,?)";


            SQLiteStatement sqliteStatement = database.compileStatement(sqlString);
            sqliteStatement.bindString(1, kitapAd);
            sqliteStatement.bindString(2, yazarAd);
            sqliteStatement.bindString(3, kitapNot);
            sqliteStatement.execute();

        } catch (Exception e) {

        }
    }

    public void ClickKitapListele(View view) {
        Intent intent = new Intent(OkumaListOlustur.this,OkumaListesi.class);
        startActivity(intent);
    }
}