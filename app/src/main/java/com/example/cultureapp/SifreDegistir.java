package com.example.cultureapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class SifreDegistir extends AppCompatActivity {
    EditText etEskiSifre, etYenisifre, etYenisifretekrar;
    Button btnSifreOnay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifre_degistir);

        etEskiSifre = findViewById(R.id.etEskiSifre);
        etYenisifre = findViewById(R.id.etYenisifre);
        etYenisifretekrar = findViewById(R.id.etYenisifretekrar);
        btnSifreOnay = findViewById(R.id.btnSifreOnay);

    }
}
