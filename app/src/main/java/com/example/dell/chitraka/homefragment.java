package com.example.dell.chitraka;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class homefragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mRef;
    private List<Upload> muploads;
    private ProgressBar progressBar;
    DatabaseReference mDatabaseLike;

    private Boolean mProcessLike = false;
    private DatabaseReference likeref, databaseforlikecount;
    private FirebaseAuth mAuth;

    private TextView count;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.homefragment, null, false);
        android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Chitraka");

        count=view.findViewById(R.id.counter);


        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        progressBar = view.findViewById(R.id.progress_circle);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        muploads = new ArrayList<>();


        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");

        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("uploads");

        likeref = FirebaseDatabase.getInstance().getReference().child("Likes");
        likeref.keepSynced(true);

        databaseforlikecount = FirebaseDatabase.getInstance().getReference().child("Likes");
        databaseforlikecount.keepSynced(true);

        mAuth = FirebaseAuth.getInstance();
        mAdapter = new ImageAdapter(getActivity(), new ArrayList<Upload>());
        mRecyclerView.setAdapter(mAdapter);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                muploads.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());
                    muploads.add(upload);
                }
                mAdapter.setData(muploads);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);

            }
        });

        mAdapter.setOnRowListener(new ImageAdapter.OnRowListener() {


            @Override
            public void likeButton(final int position) {

                Toast.makeText(getContext(), "Liked", Toast.LENGTH_SHORT).show();

                count = view.findViewById(R.id.counter);

                mProcessLike=true;

                databaseforlikecount.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(mProcessLike){
                            if (dataSnapshot.hasChild(mAuth.getCurrentUser().getUid())) {

                                int likecount=0;

                                likecount=dataSnapshot.child("likecount").getValue(Integer.class);
                                databaseforlikecount.child("likecount").setValue(likecount-1);
                                count.setText(Integer.toString(Integer.parseInt(String.valueOf((likecount)))));

                                databaseforlikecount.child(mAuth.getCurrentUser().getUid()).removeValue();
                                mProcessLike = false;

                            } else {

                                int likecount=0;

                                databaseforlikecount.child("likecount").setValue(likecount+1);
                                likecount=dataSnapshot.child("likecount").getValue(Integer.class);
                                count.setText(Integer.toString(Integer.parseInt(String.valueOf((likecount)))));


                                databaseforlikecount.child(mAuth.getCurrentUser().getUid());
                                mProcessLike = false;
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }




        });




        return view;
    }




}