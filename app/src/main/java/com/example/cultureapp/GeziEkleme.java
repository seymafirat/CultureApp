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
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class GeziEkleme extends AppCompatActivity {
    EditText etSehir,etSebep;
    Button btnKaydet,btnListele;
    TextView textView,textView2;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gezi_ekleme);

        etSehir = findViewById(R.id.etSehir);
        etSebep = findViewById(R.id.etSebep);
        btnKaydet = findViewById(R.id.btnKaydet);
        btnListele = findViewById(R.id.btnListele);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);


        database = this.openOrCreateDatabase("GeziListem",MODE_PRIVATE,null);
        Intent intent = getIntent();
        String info = intent.getStringExtra("info");

        if(info.matches("new")) {
            etSehir.setText("");
            etSebep.setText("");
        }
        else {
            int gezid = intent.getIntExtra("gezid",1);

            try {
                Cursor cursor = database.rawQuery("SELECT * FROM GeziListem WHERE id = ?",new String[] {String.valueOf(gezid)});
                int sehirIx = cursor.getColumnIndex("yeradi");
                int sebepIx = cursor.getColumnIndex("sebep");

                while (cursor.moveToNext()) {
                    etSehir.setText(cursor.getString(sehirIx));
                    etSebep.setText(cursor.getString(sebepIx));
                }
                cursor.close();
            }

            catch (Exception e) {

            }
        }




    }
    public void save(View view) {
        String yeradi = etSehir.getText().toString();
        String sebep = etSebep.getText().toString();
        if(yeradi.isEmpty() || sebep.isEmpty()) {
            Toast.makeText(GeziEkleme.this,"Please enter your city",Toast.LENGTH_LONG).show();
        }
        else {
            try {
                database = this.openOrCreateDatabase("GeziListem", MODE_PRIVATE, null);
                database.execSQL("CREATE TABLE IF NOT EXISTS GeziListem (id INTEGER PRIMARY KEY,yeradi VARCHAR, sebep VARCHAR)");
                String sqlString = "INSERT INTO GeziListem(yeradi,sebep)VALUES(?,?)";
                SQLiteStatement sqLiteStatement = database.compileStatement(sqlString);

                sqLiteStatement.bindString(1, yeradi);
                sqLiteStatement.bindString(2, sebep);
                sqLiteStatement.execute();

                Toast.makeText(GeziEkleme.this, "successfully added", Toast.LENGTH_LONG).show();


            } catch (Exception e) {
                Toast.makeText(GeziEkleme.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }


        }
      /*  */
    }

    public void git(View view) {
        Intent intent = new Intent(GeziEkleme.this,GeziListesi.class);
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }






}
