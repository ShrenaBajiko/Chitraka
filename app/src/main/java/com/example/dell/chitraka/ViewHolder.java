package com.example.dell.chitraka;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {

    View mView;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        mView=itemView;

    }


    public void setDetails(Context applicationContext, String det, String imageUrl) {
        TextView mDetail=mView.findViewById(R.id.description);
        ImageView mImageTv=mView.findViewById(R.id.imageview);


        mDetail.setText(det);
        Picasso.get().load(imageUrl).into(mImageTv);

    }
}
