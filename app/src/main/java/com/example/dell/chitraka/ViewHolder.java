package com.example.dell.chitraka;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;



public class ViewHolder extends RecyclerView.ViewHolder {


    View mView;

    ImageButton mLikebtn;
    TextView count;

    DatabaseReference mDatabaseLike;
    FirebaseAuth mAuth;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        mView=itemView;


        mLikebtn=(ImageButton)mView.findViewById(R.id.likebutton);
        count=(TextView)mView.findViewById(R.id.counter);

        mDatabaseLike=FirebaseDatabase.getInstance().getReference().child("Likes");
        mAuth=FirebaseAuth.getInstance();
        mDatabaseLike.keepSynced(true);

        }

        public void setlikecount(int likecount)
        {
            count=(TextView) mView.findViewById(R.id.counter);
            count.setText(Integer.toString(likecount));
        }



        public void setLikeBtn(final String post_key)
        {
            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid()))
                    {
                        mLikebtn.setImageResource(R.drawable.red_heart);

                    }
                    else
                    {
                        mLikebtn.setImageResource(R.drawable.cards_heart);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    public void setDetails(Context context, String det, String imageUrl) {

        TextView mDetail=mView.findViewById(R.id.description);
        ImageView mImageTv=mView.findViewById(R.id.imageview);


        mDetail.setText(det);



       Picasso.with(context)
                .load(imageUrl)
                .resize(1500, 0)
                .placeholder(R.drawable.disclaimer)
                .error(R.drawable.disclaimer)
                .into(mImageTv);




    }

}
