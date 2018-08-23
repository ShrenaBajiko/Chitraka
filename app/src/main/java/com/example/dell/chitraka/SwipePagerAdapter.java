package com.example.dell.chitraka;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SwipePagerAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    private int currentPosition = -1;
    private Context context;
    List<Slider> sliders;

    public SwipePagerAdapter(List<Slider> sliders, Context context) {
        this.context = context;
        this.sliders = sliders;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View view = inflater.inflate(R.layout.item_slide, null);
        TextView title = view.findViewById(R.id.title);
        ImageView imageView = view.findViewById(R.id.image_view);
        Slider slider = sliders.get(position);
        title.setText(slider.getTitle());
        imageView.setImageResource(slider.getImage());
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return sliders.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (currentPosition == position) return;
        this.currentPosition = position;
        View view = (View) object;
    }
}