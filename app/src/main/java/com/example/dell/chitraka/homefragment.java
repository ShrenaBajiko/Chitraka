package com.example.dell.chitraka;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
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



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homefragment, null, false);


      /*  likeref=FirebaseDatabase.getInstance().getReference().child("Likes");
        likeref.keepSynced(true);
        mAuth=FirebaseAuth.getInstance();*/

        android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Chitraka");

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        progressBar = view.findViewById(R.id.progress_circle);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        muploads = new ArrayList<>();


        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("uploads");



        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                muploads.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());
                    muploads.add(upload);
                }

                mAdapter = new ImageAdapter(getActivity(), muploads);
                mRecyclerView.setAdapter(mAdapter);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);

            }
        });


        return view;
    }





   /*@Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<Model, ViewHolder>(Model.class,R.layout.row,ViewHolder.class,mRef) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Model model, int position) {

                        final String post_key=getRef(position).getKey();
                        viewHolder.like(post_key);

                        Log.d("IMAGE",model.getImageUrl());

                        //Search on net
                        viewHolder.setDetails(getActivity().getApplicationContext(),model.det,model.imageUrl);

                        //FOR LIKE
                        viewHolder.mLikebtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mProcessLike=true;

                                    likeref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if(mProcessLike){
                                                if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {

                                                    likeref.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
                                                    mProcessLike = false;

                                                } else {
                                                    likeref.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("Random");
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

                    }
                };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }*/

}
