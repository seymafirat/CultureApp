package com.example.cultureapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class AyarlarEkrani extends AppCompatActivity {
    EditText etUsername,etChangePassword,etMail,etPhone;
    TextView tvLogout;
    Switch switch1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayarlar_ekrani);

       /* etUsername = findViewById(R.id.etUsername);
        etChangePassword = findViewById(R.id.etChangePassword);
        etMail = findViewById(R.id.etMail);
        etPhone = findViewById(R.id.etPhone);
        tvLogout = findViewById(R.id.tvLogout);
        switch1 = findViewById(R.id.switch1);*/



    }
}
