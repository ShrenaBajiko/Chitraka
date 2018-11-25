package com.example.dell.chitraka;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomePage extends AppCompatActivity implements View.OnClickListener {
    private ViewPager viewpager;
    private ViewPagerAdapter viewPagerAdapter;
    private LinearLayout home, profile, contest,upload;
    private TextView hometv, profiletv, contesttv,uploadtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        viewpager = findViewById(R.id.viewpager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(viewPagerAdapter);
        home = findViewById(R.id.home);
        hometv = findViewById(R.id.home_tv);
        contest = findViewById(R.id.contest);
        contesttv = findViewById(R.id.contest_tv);
        upload = findViewById(R.id.upload);
        uploadtv = findViewById(R.id.upload_tv);
        profile = findViewById(R.id.profile);
        profiletv = findViewById(R.id.profile_tv);

        home.setOnClickListener(this);
        profile.setOnClickListener(this);
        contest.setOnClickListener(this);
        upload.setOnClickListener(this);
        changecolor(0);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                changecolor(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.home:

                changecolor(0);
                break;
            case R.id.contest:
                changecolor(1);
                break;
            case R.id.upload:
                changecolor(2);
                break;
            case R.id.profile:
                changecolor(3);
                break;
        }

    }

    public void changecolor(int position) {
        hometv.setTextColor(getResources().getColor(R.color.black));
       contesttv.setTextColor(getResources().getColor(R.color.black));
        uploadtv.setTextColor(getResources().getColor(R.color.black));
        profiletv.setTextColor(getResources().getColor(R.color.black));
        if (position == 0) {
            hometv.setTextColor(getResources().getColor(R.color.blue));
        } else if (position == 1) {
            contesttv.setTextColor(getResources().getColor(R.color.blue));
        }
        else if (position==2)
        {
            uploadtv.setTextColor(getResources().getColor(R.color.blue));
        }
        else {
            profiletv.setTextColor(getResources().getColor(R.color.blue));
        }

        viewpager.setCurrentItem(position);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        viewPagerAdapter.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}
