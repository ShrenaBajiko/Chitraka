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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class homefragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mRef;
    private List<Upload> muploads;
    private ProgressBar progressBar;

    private Boolean mProcessLike=false;
    private DatabaseReference likeref,databaseforlikecount;
    private FirebaseAuth mAuth;


    private ImageView likebutton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homefragment, null, false);


        android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Chitraka");


        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        progressBar = view.findViewById(R.id.progress_circle);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        muploads = new ArrayList<>();


        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("uploads");

        likeref=FirebaseDatabase.getInstance().getReference().child("Likes");
        likeref.keepSynced(true);
        databaseforlikecount=FirebaseDatabase.getInstance().getReference().child("Likes");
        databaseforlikecount.keepSynced(true);
        mAuth=FirebaseAuth.getInstance();

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


    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Upload, ImageAdapter.ImageViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Upload, ImageAdapter.ImageViewHolder>(Upload.class, R.layout.image_item, ImageAdapter.ImageViewHolder.class, mRef) {

                    @Override
                    protected void populateViewHolder(ImageAdapter.ImageViewHolder imageViewHolder, Upload model, int position) {


                        final String post_key = getRef(position).getKey();

                        imageViewHolder.setLikeBtn(post_key);
                        imageViewHolder.setlikecount(String.valueOf(model.getLikecount()));
                        imageViewHolder.setpersonname(model.getUsername());


                        imageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getActivity(), "Liked", Toast.LENGTH_SHORT).show();


                            }
                        });

                        //FOR LIKE
                        imageViewHolder.mLikebtn.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        mProcessLike = true;

                                        databaseforlikecount.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                if (mProcessLike) {
                                                    if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {

                                                        int likecount = 0;
                                                        likecount = dataSnapshot.child(post_key).child("likecount").getValue(Integer.class);
                                                        databaseforlikecount.child(post_key).child("likecount").setValue(likecount - 1);

                                                        databaseforlikecount.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
                                                        mProcessLike = false;

                                                    } else {

                                                        int likecount = 0;
                                                        likecount = dataSnapshot.child(post_key).child("likecount").getValue(Integer.class);
                                                        databaseforlikecount.child(post_key).child("likecount").setValue(likecount + 1);


                                                        databaseforlikecount.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("Random");
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
    }
    }



