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



    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        mView=itemView;



    }



    public void setDetails(Context context, String det, String imageUrl,String likeCount,String likebtn) {

        TextView mDetail=mView.findViewById(R.id.description);
        ImageView mImageTv=mView.findViewById(R.id.imageview);
        Button mLikebtn= mView.findViewById(R.id.likebutton);
       final TextView count= mView.findViewById(R.id.counter);

        mDetail.setText(det);
       count.setText(likeCount);
       mLikebtn.setText(likebtn);



       Picasso.get()
                .load(imageUrl)
                .resize(1500, 0)
                .placeholder(R.drawable.disclaimer)
                .error(R.drawable.disclaimer)
                .into(mImageTv);




    }

}
