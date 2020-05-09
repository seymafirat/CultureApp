package com.example.cultureapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    EditText etuserName,etEmail,etPassword,etAdsoyad;
    Button btnRegister;
    TextView tvGiris;
    FirebaseAuth firebaseAuth;
    DatabaseReference yol;
    ProgressDialog pd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etuserName = findViewById(R.id.etuserName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etAdsoyad = findViewById(R.id.etAdSoyad);
        btnRegister = findViewById(R.id.btnRegister);
        tvGiris = findViewById(R.id.tvGiris);
        firebaseAuth = FirebaseAuth.getInstance();

    }



    public void girisYap(View v) {
        Intent intent = new Intent(MainActivity.this,GirisEkrani.class);
        startActivity(intent);
    }

    public void KayitOl(View v) {
        pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("Lütfen Bekleyin");
        pd.show();

        final String username = etuserName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        final String adsoyad = etAdsoyad.getText().toString();
        if(email.isEmpty() || password.isEmpty() || username.isEmpty()) {
                Toast.makeText(MainActivity.this,"Please enter your email and password",Toast.LENGTH_LONG).show();
        }
        else {
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        FirebaseUser firebaseKullanici = firebaseAuth.getCurrentUser();
                        String kullaniciId = firebaseKullanici.getUid();
                        yol = FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(kullaniciId);
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("id",kullaniciId);
                        hashMap.put("kullaniciAdi",username);
                        hashMap.put("AdSoyad",adsoyad);
                        hashMap.put("Bio","");
                        hashMap.put("resimurl","");

                        yol.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    pd.dismiss();
                                    Toast.makeText(MainActivity.this,"Hoşgeldiniz!",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(MainActivity.this,GirisEkrani.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);

                                }

                            }
                        });

                    }
                    else {
                        pd.dismiss();
                        Toast.makeText(MainActivity.this,"Hata",Toast.LENGTH_SHORT).show();
                    }




                }
            });
        }
    }


}
