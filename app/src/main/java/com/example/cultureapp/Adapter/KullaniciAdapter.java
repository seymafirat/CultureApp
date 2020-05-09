package com.example.cultureapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cultureapp.Cerceve.ProfileFragment;
import com.example.cultureapp.R;
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
import de.hdodenhof.circleimageview.CircleImageView;

public class KullaniciAdapter extends RecyclerView.Adapter<KullaniciAdapter.ViewHolder> {
    private Context mContext;
    private List<Kullanici> mKullanicilar;
    private FirebaseUser firebaseKullanici;

    public KullaniciAdapter(Context mContext, List<Kullanici> mKullanicilar) {
        this.mContext = mContext;
        this.mKullanicilar = mKullanicilar;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.kullanici_ogesi,parent,false);


        return new KullaniciAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        firebaseKullanici = FirebaseAuth.getInstance().getCurrentUser();
        //ogelere veritabanından verileri aktarıyoruz
        final Kullanici kullanici = mKullanicilar.get(position);
        System.out.println(kullanici.toString());
        holder.btn_takipet.setVisibility(View.VISIBLE);
        holder.kullaniciadi.setText(kullanici.getKullaniciAdi());
        holder.ad.setText(kullanici.getAdSoyad());
        Glide.with(mContext).load(kullanici.getResimurl()).into(holder.profil_resmi);
        takipEdiliyor(kullanici.getId(),holder.btn_takipet);

        if(kullanici.getId().equals(firebaseKullanici.getUid())) {
            holder.btn_takipet.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid",kullanici.getId());
                editor.apply();

               ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayicisi,new ProfileFragment()).commit();


            }
        });

        holder.btn_takipet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.btn_takipet.getText().toString().equals("Takip Et")) {
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(firebaseKullanici.getUid())
                            .child("takipEdilenler").child(kullanici.getId()).setValue(true);

                    FirebaseDatabase.getInstance().getReference().child("Takip").child(kullanici.getId())
                            .child("takipciler").child(firebaseKullanici.getUid()).setValue(true);
                }

                else {
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(firebaseKullanici.getUid())
                            .child("takipEdilenler").child(kullanici.getId()).removeValue();

                    FirebaseDatabase.getInstance().getReference().child("Takip").child(kullanici.getId())
                            .child("takipciler").child(firebaseKullanici.getUid()).removeValue();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mKullanicilar.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView kullaniciadi;
        public TextView ad;
        public CircleImageView profil_resmi;
        public Button btn_takipet;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            kullaniciadi = itemView.findViewById(R.id.txt_kullaniciadi_oge);
            ad = itemView.findViewById(R.id.txt_ad_oge);
            profil_resmi = itemView.findViewById(R.id.profil_resmi_Oge);
            btn_takipet = itemView.findViewById(R.id.btn_takipEt_oge);


        }
    }

    private void takipEdiliyor(final String kullaniciId, final Button button) {
        DatabaseReference takipYolu = FirebaseDatabase.getInstance().getReference().child("Takip").
                child(firebaseKullanici.getUid()).child("takipEdilenler");
        takipYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Toast.makeText(mContext,"Takibe girdi",Toast.LENGTH_SHORT).show();
               // System.out.println("*****************************" + " " + kullaniciId + " ************ "+button.getText());

                if(dataSnapshot.child(kullaniciId).exists() && dataSnapshot.getValue() != null) {
                    button.setText("Takip Ediliyor");
                    //System.out.println("/////////////////////////// "  + dataSnapshot.child(kullaniciId).exists());
                }
                else {
                    button.setText("Takip Et");
                    //System.out.println("/////////////////////////// "  + dataSnapshot.child(kullaniciId).exists());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
