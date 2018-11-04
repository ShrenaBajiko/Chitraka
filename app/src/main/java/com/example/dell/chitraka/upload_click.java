package com.example.dell.chitraka;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class upload_click extends AppCompatActivity {
    private static final String TAG = "AddActivity";
    private static final int VERIFY_PERMISSION_REQUEST = 5 ;
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.uploadfragment);


        if(checkPermissionList(permissions.PERMISSIONS)){
        }
        else
        if(verifyPermissionList(permissions.PERMISSIONS)){
        }
        setupViewPager();
    }


    private void setupViewPager()
    {
        SectionPagerAdapter sectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        sectionPagerAdapter.addFragment(new Gallery_Fragment());
        sectionPagerAdapter.addFragment(new Photo_Fragment());

        viewPager =(ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(sectionPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Gallery");
        tabLayout.getTabAt(1).setText("Photo");
    }




    private boolean checkPermissionList(String[] permissions) {
        for(int i = 0; i<permissions.length; i++)
        {
            String check = permissions[i];
            if(!checkPermission(check))
            {
                return false;
            }
        }

        return true;
    }

    private boolean checkPermission(String check) {

        int permissionRequest = ActivityCompat.checkSelfPermission(upload_click.this,check);
        if(permissionRequest != PackageManager.PERMISSION_GRANTED){
            Log.d(TAG,"checkpermission: Permission not granted");
            return false;
        }
        else
            Log.d(TAG,"checkpermission: Permission  granted");
        return true;
    }


    private boolean verifyPermissionList(String[] permissions) {

        ActivityCompat.requestPermissions(upload_click.this,permissions,VERIFY_PERMISSION_REQUEST);
        return true;
    }
}




