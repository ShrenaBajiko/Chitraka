package com.example.dell.chitraka;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private upload_fragment uploadFragment;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return new homefragment();

            case 1:
                return new contest_fragment();
            case 2:
               uploadFragment = new upload_fragment();
                return uploadFragment;
            case 3:
                return new notification_fragment();
           case 4:
                return new profilefragment();
        }
        return null;
    }

    @Override
    public int getCount()
    {
        return 5;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(uploadFragment!=null){
            uploadFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
