package com.example.cultureapp.Cerceve;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cultureapp.Adapter.GonderiAdapter;
import com.example.cultureapp.R;
import com.example.cultureapp.model.Gonderi;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class PostDetailFragment extends Fragment {
    String postId;
    private RecyclerView recyclerView;
    private GonderiAdapter gonderiAdapter;
    private List<Gonderi> postList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);
        SharedPreferences preferences = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        postId = preferences.getString("postId","none");
        recyclerView = view.findViewById(R.id.recyler_view_detailpost);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        postList = new ArrayList<>();
        gonderiAdapter = new GonderiAdapter(getContext(),postList);
        recyclerView.setAdapter(gonderiAdapter);

        readPost();





        return view;
    }
    private void readPost() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Gonderiler").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                Gonderi gonderi = dataSnapshot.getValue(Gonderi.class);
                postList.add(gonderi);

                gonderiAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
