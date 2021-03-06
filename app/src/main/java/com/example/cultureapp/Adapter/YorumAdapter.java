package com.example.cultureapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cultureapp.Anasayfa;
import com.example.cultureapp.R;
import com.example.cultureapp.model.Kullanici;
import com.example.cultureapp.model.Yorum;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class YorumAdapter extends RecyclerView.Adapter<YorumAdapter.ViewHolder>{
    private Context mContext;

    public YorumAdapter(Context mContext, List<Yorum> mYorumListesi) {
        this.mContext = mContext;
        this.mYorumListesi = mYorumListesi;
    }

    private List<Yorum> mYorumListesi;

    private FirebaseUser mevcutKullanici;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.yorum_ogesi,parent,false);
        return new YorumAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();
        final Yorum yorum = mYorumListesi.get(position);
        holder.txt_yorum.setText(yorum.getYorum());

        kullaniciBilgisiAl(holder.profil_resmi,holder.txt_kullanici_adi,yorum.getGonderen());

        holder.txt_yorum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Anasayfa.class);
                intent.putExtra("gonderenId",yorum.getGonderen());
                mContext.startActivity(intent);
            }
        });
        holder.profil_resmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Anasayfa.class);
                intent.putExtra("gonderenId",yorum.getGonderen());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mYorumListesi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView profil_resmi;
        public TextView txt_kullanici_adi,txt_yorum;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profil_resmi = itemView.findViewById(R.id.profil_resmi_yorumOgesi);
            txt_kullanici_adi = itemView.findViewById(R.id.txt_kullaniciadi_yorumOgesi);
            txt_yorum = itemView.findViewById(R.id.txt_yorum_yorumOgesi);
        }
    }

    private void kullaniciBilgisiAl(final ImageView imageView, final TextView kullaniciAdi, String gonderenId) {
        DatabaseReference gonderenIdYolu = FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(gonderenId);
        gonderenIdYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                System.out.println("/////////////////////////// "  + kullanici.getKullaniciAdi());
                Glide.with(mContext).load(kullanici.getResimurl()).into(imageView);

                kullaniciAdi.setText(kullanici.getKullaniciAdi());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
