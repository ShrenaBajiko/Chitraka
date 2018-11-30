package com.example.dell.chitraka;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


import com.rd.PageIndicatorView;
import com.rd.animation.type.AnimationType;

import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

public class MainActivity extends AppCompatActivity {
    AutoScrollViewPager viewPager;
    SwipePagerAdapter adapter;
    TextView goToLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (AutoScrollViewPager) findViewById(R.id.view_pager);
        goToLogin = (TextView) findViewById(R.id.go_to_login);
        List<Slider> sliders = new ArrayList<>();
        Slider slider1 = new Slider("Image1", R.drawable.birds);
        Slider slider2 = new Slider("Image2", R.drawable.cap);
        Slider slider3 = new Slider("Image3", R.drawable.chii);
        sliders.add(slider1);
        sliders.add(slider2);
        sliders.add(slider3);
        adapter = new SwipePagerAdapter(sliders, this);
        viewPager.setAdapter(adapter);

        final PageIndicatorView pageIndicatorView = findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setCount(sliders.size()); // specify total count of indicators
        pageIndicatorView.setSelection(0);
        pageIndicatorView.setAnimationType(AnimationType.DROP);
        goToLogin.setVisibility(View.GONE);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                pageIndicatorView.setSelection(i);
                if (i == 2) {
                    goToLogin.setVisibility(View.VISIBLE);
                } else {
                    goToLogin.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                startActivity(new Intent(MainActivity.this, Front.class));
            }
        });
    }
}
