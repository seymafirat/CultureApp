package com.example.cultureapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cultureapp.Cerceve.PostDetailFragment;
import com.example.cultureapp.Cerceve.ProfileFragment;
import com.example.cultureapp.R;
import com.example.cultureapp.YorumlarActivity;
import com.example.cultureapp.model.Gonderi;
import com.example.cultureapp.model.Kullanici;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class GonderiAdapter extends RecyclerView.Adapter<GonderiAdapter.ViewHolder> {

    public Context mContext;
    public List<Gonderi> mGonderi;

    private FirebaseUser mevcutFirebaseUser;

    public GonderiAdapter(Context mContext, List<Gonderi> mGonderi) {
        this.mContext = mContext;
        this.mGonderi = mGonderi;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.gonderi_ogesi,parent,false);

        return new GonderiAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        mevcutFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Gonderi gonderi = mGonderi.get(position);
        Glide.with(mContext).load(gonderi.getGonderiResmi()).into(holder.gonderi_resmi);

        if(gonderi.getGonderiHakkinda().equals(""))
        {
            holder.txt_gonderiHakkinda.setVisibility(View.GONE);
        }
        else
        {
            holder.txt_gonderiHakkinda.setVisibility(View.VISIBLE);
            holder.txt_gonderiHakkinda.setText(gonderi.getGonderiHakkinda());
        }

        gonderenBilgileri(holder.profil_resmi,holder.txt_kullanici_adi,holder.txt_gonderen,gonderi.getGonderen());
        begenildi(gonderi.getGonderiId(),holder.begeni_resmi);
        begeniSayisi(holder.txt_begeni,gonderi.getGonderiId());
        yorumlariAl(gonderi.getGonderiId(),holder.txt_yorumlar);

        holder.profil_resmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileId",gonderi.getGonderen());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayicisi,
                        new ProfileFragment()).commit();
            }
        });
        holder.txt_kullanici_adi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileId",gonderi.getGonderen());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayicisi,
                        new ProfileFragment()).commit();
            }
        });
        holder.txt_gonderen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileId",gonderi.getGonderen());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayicisi,
                        new ProfileFragment()).commit();
            }
        });
        holder.gonderi_resmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("postId",gonderi.getGonderiId());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayicisi,
                        new PostDetailFragment()).commit();
            }
        });


        holder.begeni_resmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.begeni_resmi.getTag().equals("beğen"))
                {
                    FirebaseDatabase.getInstance().getReference().child("Begeniler").child(gonderi.getGonderiId())
                            .child(mevcutFirebaseUser.getUid()).setValue(true);
                }
                else {
                    FirebaseDatabase.getInstance().getReference().child("Begeniler").child(gonderi.getGonderiId())
                            .child(mevcutFirebaseUser.getUid()).removeValue();
                }
            }
        });
        holder.yorum_resmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, YorumlarActivity.class);
                intent.putExtra("gonderiId",gonderi.getGonderiId());
                intent.putExtra("gonderenId",gonderi.getGonderen());
                mContext.startActivity(intent);
            }
        });
        holder.txt_yorumlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, YorumlarActivity.class);
                intent.putExtra("gonderiId",gonderi.getGonderiId());
                intent.putExtra("gonderenId",gonderi.getGonderen());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGonderi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView profil_resmi,gonderi_resmi,begeni_resmi,yorum_resmi,kaydetme_resmi;

        public TextView txt_kullanici_adi,txt_begeni,txt_gonderen,txt_gonderiHakkinda,txt_yorumlar;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profil_resmi = itemView.findViewById(R.id.profil_resmi_Gonderi_Ogesi);
            gonderi_resmi = itemView.findViewById(R.id.post_image);
            begeni_resmi = itemView.findViewById(R.id.begeni_Gonderi_Ogesi);
            yorum_resmi = itemView.findViewById(R.id.yorum_Gonderi_Ogesi);
            kaydetme_resmi = itemView.findViewById(R.id.kaydet_Gonderi_Ogesi);

            txt_kullanici_adi = itemView.findViewById(R.id.txt_kullaniciadi_Gonderi_Ogesi);
            txt_begeni = itemView.findViewById(R.id.txt_begeniler_Gonderi_Ogesi);
            txt_gonderen = itemView.findViewById(R.id.txt_gonderen_Gonderi_Ogesi);
            txt_gonderiHakkinda = itemView.findViewById(R.id.txt_gonderiHakkinda_Gonderi_Ogesi);
            txt_yorumlar = itemView.findViewById(R.id.txt_yorum_Gonderi_Ogesi);


        }
    }
    private void yorumlariAl(String gonderiId, final TextView yorumlar) {
        DatabaseReference yorumlariAlmaYolu = FirebaseDatabase.getInstance().getReference("Yorumlar")
                .child(gonderiId);
        yorumlariAlmaYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                yorumlar.setText(dataSnapshot.getChildrenCount() + " yorumun hepsini gör");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void begenildi(String gonderiId, final ImageView imageView) {
        final FirebaseUser mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference begeniVeriTabaniYolu = FirebaseDatabase.getInstance().getReference()
                .child("Begeniler")
                .child(gonderiId);
        begeniVeriTabaniYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(mevcutKullanici.getUid()).exists())
                {
                    imageView.setImageResource(R.drawable.ic_begenildi);
                    imageView.setTag("beğenildi");

                }
                else{
                    imageView.setImageResource(R.drawable.ic_begeni);
                    imageView.setTag("beğen");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void begeniSayisi(final TextView begeniler, String gonderiId)
    {
        DatabaseReference begeniSayisiVeriTabaniYolu = FirebaseDatabase.getInstance().getReference()
               .child("Begeniler")
                .child(gonderiId);
        begeniSayisiVeriTabaniYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                begeniler.setText(dataSnapshot.getChildrenCount() +" beğeni");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void gonderenBilgileri(final ImageView profil_resmi, final TextView kullaniciAdi, final TextView gonderen, final String kullaniciId) {
        DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("Kullanicilar").child(kullaniciId);

        veriYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                kullaniciAdi.setText(kullanici.getAdSoyad());
                Glide.with(mContext).load(kullanici.getResimurl()).into(profil_resmi);
                gonderen.setText(kullanici.getKullaniciAdi());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
