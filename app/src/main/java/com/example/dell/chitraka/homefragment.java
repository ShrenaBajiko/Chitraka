package com.example.dell.chitraka;

import android.app.ActionBar;
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

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class homefragment extends Fragment {

    RecyclerView mRecyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mRef;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.homefragment,null,false);




        android.support.v7.app.ActionBar actionBar=((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Post list");


        mRecyclerView=view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        firebaseDatabase=FirebaseDatabase.getInstance();
        mRef=firebaseDatabase.getReference("uploads");


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<Model, ViewHolder>(Model.class,R.layout.row,ViewHolder.class,mRef) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Model model, int position) {

                        viewHolder.setDetails(getActivity().getApplicationContext(),model.getDet(),model.getImageUrl());
                    }
                };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
