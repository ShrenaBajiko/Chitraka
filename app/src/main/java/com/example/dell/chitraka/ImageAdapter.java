package com.example.dell.chitraka;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder>{
    private Context mContext;
    private List<Upload> mUploads;

    public ImageAdapter(Context context,List<Upload>  uploads){
        mContext = context;
        mUploads = uploads;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item,viewGroup,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
        Upload uploadCurrent = mUploads.get(i);
        imageViewHolder.details.setText(uploadCurrent.getDet());
        Log.d("TEXT",uploadCurrent.getImageUrl());
        //uploadCurrent.getImageUrl() give this type of value com.google.android.gms.tasks.zzu@7faef41,
        // this is not image url, please look at the firebase docs. Look at firebase storage docs for image url.. It is returning object value
      /*Picasso.get()
                .load(uploadCurrent.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerInside()
                .into(imageViewHolder.imageView);*/

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    // This method set the mUploads after the data has came from serve
    public void setData(List<Upload> uploads) {
        // first clear the list
        mUploads.clear();
        // then add the list that come from server
        mUploads.addAll(uploads);
        // finally notify
        notifyDataSetChanged();
    }

    public class ImageViewHolder extends  RecyclerView.ViewHolder{
        public TextView details;
        public ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view_upload);
            details = itemView.findViewById(R.id.detail);
        }
    }
}
