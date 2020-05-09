package com.example.cultureapp.Cerceve;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cultureapp.Adapter.MyFotoAdapter;
import com.example.cultureapp.R;
import com.example.cultureapp.model.Gonderi;
import com.example.cultureapp.model.Kullanici;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
   ImageView image_profile,options;
   TextView alintilar,geziler,following,fullname,bio,username;
   Button edit_profile;
   FirebaseUser mevcutKullanici;
   String profileId;
   ImageButton my_fotos,saved_fotos;

   RecyclerView recyclerView;
   MyFotoAdapter myFotoAdapter;
   List<Gonderi> postList;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profileId = prefs.getString("profileid","none");

        image_profile = view.findViewById(R.id.image_profile);
        options = view.findViewById(R.id.options);
        alintilar = view.findViewById(R.id.alintilar);
        geziler = view.findViewById(R.id.geziler);
        following = view.findViewById(R.id.following);
        fullname = view.findViewById(R.id.fullname);
        bio = view.findViewById(R.id.bio);
        username = view.findViewById(R.id.username);
        edit_profile = view.findViewById(R.id.edit_profile);
        my_fotos = view.findViewById(R.id.my_fotos);
        saved_fotos = view.findViewById(R.id.saved_fotos);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        myFotoAdapter = new MyFotoAdapter(getContext(),postList);
        recyclerView.setAdapter(myFotoAdapter);

        userInfo();
        getFollowers();
        getNrPosts();
        myFotos();

        if(profileId.equals(mevcutKullanici.getUid())) {
            edit_profile.setText("Profili Düzenle");
        }
        else {
            checkFollow();
            saved_fotos.setVisibility(View.GONE);
        }

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn = edit_profile.getText().toString();
                if(btn.equals("Profili Düzenle")) {

                }
                else if(btn.equals("takip et")) {
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(mevcutKullanici.getUid())
                            .child("takipEdilenler").child(profileId).setValue(true);

                    FirebaseDatabase.getInstance().getReference().child("Takip").child(profileId)
                            .child("takipciler").child(mevcutKullanici.getUid()).setValue(true);
                }
                else if(btn.equals("takip ediliyor"))
                {
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(mevcutKullanici.getUid())
                            .child("takipEdilenler").child(profileId).removeValue();

                    FirebaseDatabase.getInstance().getReference().child("Takip").child(profileId)
                            .child("takipciler").child(mevcutKullanici.getUid()).removeValue();

                }
            }
        });


        return view;
    }
    private void userInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kullanicilar").child(profileId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(getContext() == null) {
                    return;
                }
                Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                Glide.with(getContext()).load(kullanici.getResimurl()).into(image_profile);
                username.setText(kullanici.getKullaniciAdi());
                fullname.setText(kullanici.getAdSoyad());
                bio.setText(kullanici.getBio());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkFollow() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Takip")
                .child(mevcutKullanici.getUid()).child("takipEdilenler");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(profileId).exists()) {
                    edit_profile.setText("takip ediliyor");
                }
                else {
                    edit_profile.setText("takip et");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void getFollowers() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Takip").child(profileId).child("takipciler");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                geziler.setText(""+dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference()
                .child("Takip").child(profileId).child("takipEdilenler");

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                following.setText(""+dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void getNrPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Gonderiler");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Gonderi gonderi = snapshot.getValue(Gonderi.class);
                    if(gonderi.getGonderen().equals(profileId)) {
                        i++;
                    }
                }
                alintilar.setText(""+i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void myFotos() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Gonderiler");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Gonderi gonderi = snapshot.getValue(Gonderi.class);
                    if(gonderi.getGonderen().equals(profileId)) {
                        postList.add(gonderi);
                    }
                }
                Collections.reverse(postList);
                myFotoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
