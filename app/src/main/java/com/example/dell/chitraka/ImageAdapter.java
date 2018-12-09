package com.example.dell.chitraka;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload> mUploads;
    private Boolean mProcessLike = false;
    private OnRowListener onRowListener;

    public ImageAdapter(Context context, List<Upload> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, viewGroup, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, final int i) {
        Upload uploadCurrent = mUploads.get(i);
        imageViewHolder.details.setText(uploadCurrent.getDet());
        // imageViewHolder.count.setText(uploadCurrent.getLikecount());
        //uploadCurrent.getImageUrl() give this type of value com.google.android.gms.tasks.zzu@7faef41,
        // this is not image url, please look at the firebase docs. Look at firebase storage docs for image url.. It is returning object value
        Picasso.with(mContext)
                .load(uploadCurrent.getImageUrl())
                .placeholder(R.drawable.disclaimer)
                .fit()
                .centerInside()
                .into(imageViewHolder.imageView);

        Picasso.with(mContext)
                .load(uploadCurrent.getLikebutton())
                .into(imageViewHolder.mLikebtn);

        imageViewHolder.mLikebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRowListener.likeButton(i);
                onRowListener.likecount(i);

            }
        });


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

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView details;
        public ImageView imageView;
        public ImageButton mLikebtn;
        public TextView count;

        DatabaseReference mDatabaseLike;
        FirebaseAuth mAuth;


        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view_upload);
            details = itemView.findViewById(R.id.detail);
            mLikebtn = itemView.findViewById(R.id.likebutton);
            count = itemView.findViewById(R.id.counter);

            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
            mAuth = FirebaseAuth.getInstance();
            mDatabaseLike.keepSynced(true);
        }



        public void setlikecount(String likecount) {
            count = (TextView) itemView.findViewById(R.id.counter);

            count.setText(Integer.toString(Integer.parseInt((likecount))));
        }

        public void setLikeBtn(final String post_key) {
            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {
                        mLikebtn.setImageResource(R.drawable.red_heart);

                    } else {
                        mLikebtn.setImageResource(R.drawable.cards_heart);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    public interface OnRowListener {
        void likeButton(int i);
        void likecount(int i);

    }

    public void setOnRowListener(OnRowListener onRowListener) {
        this.onRowListener = onRowListener;
    }

}