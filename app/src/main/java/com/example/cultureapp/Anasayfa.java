package com.example.cultureapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.example.cultureapp.Cerceve.AramaFragment;
import com.example.cultureapp.Cerceve.BildirimFragment;
import com.example.cultureapp.Cerceve.HomeFragment;
import com.example.cultureapp.Cerceve.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.Map;

public class Anasayfa extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<String> userEmailfromFB;
    ArrayList<String> userCommentfromFB;
    ArrayList<String> userImagefromFB;
    //RecyclerActivity recyclerActivity;
    BottomNavigationView bottomNavigationView;
    Fragment seciliCerceve = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);

        userEmailfromFB = new ArrayList<>();
        userCommentfromFB = new ArrayList<>();
        userImagefromFB = new ArrayList<>();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        Bundle intent = getIntent().getExtras();
        if(intent != null) {
            String gonderen = intent.getString("gonderenId");
            SharedPreferences.Editor editor = getSharedPreferences("PREFS",MODE_PRIVATE).edit();
            editor.putString("profileId",gonderen);
            editor.apply();
            getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayicisi,new ProfileFragment()).commit();

        }
        else {
            getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayicisi,new HomeFragment()).commit();
        }





        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

    }//hangi idye tıkladıgımda neyi göstersin
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            seciliCerceve = new HomeFragment();
                         break;

                        case R.id.nav_arama:
                            seciliCerceve = new AramaFragment();
                            break;

                        case R.id.nav_ekle:
                            seciliCerceve = null;
                            startActivity(new Intent(Anasayfa.this,GonderiActivity.class));
                            break;


                        case R.id.nav_kalp:
                            seciliCerceve = new BildirimFragment();
                            break;
                        case R.id.nav_profil:
                            SharedPreferences.Editor editor = getSharedPreferences("PREFS",MODE_PRIVATE).edit();
                            editor.putString("profileid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                            editor.apply();
                            seciliCerceve = new ProfileFragment();
                            break;
                    }

                    if(seciliCerceve != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayicisi,seciliCerceve).commit();
                    }



                    return true;
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.gezi_list) {
            Intent intent = new Intent(Anasayfa.this,GeziEkleme.class);
            intent.putExtra("info","new");
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.ayarlar_menu) {
            Intent intent = new Intent(Anasayfa.this,AyarlarEkrani.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.ani_list) {
            Intent intent = new Intent(Anasayfa.this, GonderiActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.konum_list) {
            Intent intent = new Intent(Anasayfa.this, MapsActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.okuma_list) {
            Intent intent = new Intent(Anasayfa.this,OkumaListOlustur.class);
            intent.putExtra("info","new");
            startActivity(intent);
        }

        else if(item.getItemId() == R.id.cikis_menu) {
            firebaseAuth.signOut();
            Intent intent = new Intent(Anasayfa.this,GirisEkrani.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

   /* public void ClickKonum(View view) {
        Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
        intent.putExtra("info","new");
        startActivity(intent);
    }*/
}