package com.example.cultureapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GirisEkrani extends AppCompatActivity {
    public static EditText etUsername, etPassword;
    Button btnLogin;
    TextView textView, textView2, tvRegister;
    FirebaseAuth firebaseAuth;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris_ekrani);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        textView = findViewById(R.id.textView2);
        textView2 = findViewById(R.id.textView2);
        tvRegister = findViewById(R.id.tvRegister);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void SignInClick(View v) {
        pd = new ProgressDialog(GirisEkrani.this);
        pd.setMessage("Lütfen Bekleyin");
        pd.show();
        String email = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(GirisEkrani.this,"Please enter your email and password",Toast.LENGTH_LONG).show();
        }
        else {
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        DatabaseReference yolGiris = FirebaseDatabase.getInstance().getReference().child("Kullanicilar")
                                .child(firebaseAuth.getCurrentUser().getUid());
                        yolGiris.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Intent intent = new Intent(GirisEkrani.this,Anasayfa.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                    else {
                        pd.dismiss();
                        Toast.makeText(GirisEkrani.this,"E-mail adresiniz veya şifreniz hatalı.Lütfen tekrar deneyin",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
