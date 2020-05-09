package com.example.cultureapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class OkumaListesi extends AppCompatActivity {
    ListView listView;
    ArrayList<String> nameArray;
    ArrayList<Integer> idArray;
    ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okuma_listesi);
        listView = findViewById(R.id.listView);
        nameArray = new ArrayList<String>();
        idArray = new ArrayList<Integer>();

        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,nameArray);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(OkumaListesi.this,OkumaListOlustur.class);
                intent.putExtra("kitapId",idArray.get(position));
                intent.putExtra("info", "old");

                startActivity(intent);
            }
        });
        getData();
    }

    //daha düzenli olsun diye method içinde yapıp çağıralım.
    public void getData() {
        try {
            SQLiteDatabase database = this.openOrCreateDatabase("kitap", MODE_PRIVATE, null);
            Cursor cursor = database.rawQuery("SELECT * FROM kitaplar", null);
            int nameIx = cursor.getColumnIndex("kitapad");
            int idIx = cursor.getColumnIndex("id");

            while (cursor.moveToNext()) {
                nameArray.add(cursor.getString(nameIx));
                idArray.add(cursor.getInt(idIx));
            }
            //dizilerime yeni veri ekledim bunu listende göster:
            arrayAdapter.notifyDataSetChanged();
            cursor.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}