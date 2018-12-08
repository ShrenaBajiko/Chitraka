package com.example.dell.chitraka;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class contest_fragment extends Fragment {
    private TextView mvalueView;
    private Firebase mRef;

    private TextView url;
    private ImageView img;


    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference reference = firebaseDatabase.getReference();
    private DatabaseReference childrefrence = reference.child("url");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contestfragment, null, false);
        {
            url=(TextView)view.findViewById(R.id.text);
            img=(ImageView)view.findViewById(R.id.image);



            mvalueView=(TextView) view.findViewById(R.id.textView);
            mRef=new Firebase("https://chitraka-3a983.firebaseio.com/Name");

            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    String  value=dataSnapshot.getValue(String.class);
                    mvalueView.setText(value);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });


            return view;

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        childrefrence.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                String message=dataSnapshot.getValue(String.class);
                url.setText(message);
                Picasso.with(getActivity())
                        .load(message)
                        .into(img);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}