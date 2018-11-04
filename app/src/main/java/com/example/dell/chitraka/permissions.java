package com.example.dell.chitraka;


import android.Manifest;

public class permissions {

    public static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    public static final String[] READ_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    public static final String[] WRITE_PERMISSIONS = {

            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    public static final String[] CAMERA_PERMISSIONS = {


            Manifest.permission.CAMERA
    };
}
