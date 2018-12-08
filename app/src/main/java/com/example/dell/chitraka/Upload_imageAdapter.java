package com.example.dell.chitraka;

import android.view.LayoutInflater;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Upload_imageAdapter extends BaseAdapter {

Context c;
ArrayList<Upload> upload;
public Upload_imageAdapter(upload_fragment c, ArrayList<Upload> upload) {
    c = c;
    this.upload = upload;

}
    @Override
    public int getCount() {
        return upload.size();
    }

    @Override
    public Object getItem(int position) {
        return upload.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(c).inflate(R.layout.model, parent, false);

        }
        final Upload U = (Upload) this.getItem(position);
        TextView nameText = (TextView) view.findViewById(R.id.nameText);
        ImageView img = (ImageView) view.findViewById(R.id.upload);

        //bind data
        nameText.setText(U.getDet());
        Picasso.get()
                .load(U.getImageUrl())
                .placeholder(R.mipmap.ic_launcher).into(img);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(c,U.getDet(),Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
