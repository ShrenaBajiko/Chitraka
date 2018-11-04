package com.example.dell.chitraka;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class upload_fragment extends Fragment {
    private static final int RequestPermissionCode = 1189;
    TextView imagePicker;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.uploadfragment, null, false);
        imagePicker = view.findViewById(R.id.image_picker);
        imagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPermission()) {
                    showPhotoAlertDialog();
                } else {
                    requestPermission();
                }
            }
        });
        return view;


    }

    private void showPhotoAlertDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.take_picture_dialog, null);
        dialogBuilder.setView(dialogView);
        LinearLayout takePhoto = dialogView.findViewById(R.id.take_photo);
        LinearLayout photoLibrary = dialogView.findViewById(R.id.photo_library);
        TextView cancel = dialogView.findViewById(R.id.cancel);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                openCamera();

            }
        });
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new
                String[]{CAMERA, WRITE_EXTERNAL_STORAGE}, RequestPermissionCode);
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getContext(),
                CAMERA);
        int result1 = ContextCompat.checkSelfPermission(getContext(),
                WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean CameraPermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean StoragePermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;
                    if (CameraPermission && StoragePermission) {
                        showPhotoAlertDialog();
                        Toast.makeText(getContext(), "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    private void openCamera() {
        //Use Intent to open camera.

        Toast.makeText(getContext(), "Open Camera", Toast.LENGTH_LONG).show();
    }


}







