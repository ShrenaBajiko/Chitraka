package com.example.dell.chitraka;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.internal.service.Common;
import com.squareup.picasso.Picasso;

import static java.security.AccessController.getContext;

public class ViewHolder extends RecyclerView.ViewHolder {

    View mView;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        mView=itemView;
    }


    public void setDetails(Context context, String det, String imageUrl) {
        TextView mDetail=mView.findViewById(R.id.description);
        ImageView mImageTv=mView.findViewById(R.id.imageview);
        mDetail.setText(det);
       // Picasso.get().load(imageUrl).into(mImageTv);
        Picasso.get()
                .load(imageUrl)
                .resize(1500, 0)
                .placeholder(R.drawable.disclaimer)
                .error(R.drawable.disclaimer)
                .into(mImageTv);


    }
}
