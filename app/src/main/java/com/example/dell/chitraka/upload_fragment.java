package com.example.dell.chitraka;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;


public class upload_fragment extends Fragment {
    private static final int RequestPermissionCode = 1189;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAN_REQUEST = 1313;
    TextView imagePicker;
    ImageView imageView;
    private Uri ImageUri;
    private TextView buttonUpload;
    private ProgressBar progressBar;
    private EditText description;


    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.uploadfragment, null, false);
        imagePicker = view.findViewById(R.id.image_picker);
        imageView = view.findViewById(R.id.image_view);
        progressBar = view.findViewById(R.id.progress_bar);
        buttonUpload = view.findViewById(R.id.upload_image);
        description = view.findViewById(R.id.description);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();

            }


        });


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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
            ;
        if (data.getClipData() != null) {

            int totalItemSelected = data.getClipData().getItemCount();
            for (int i = 0; i < totalItemSelected; i++) {
                Uri fileuri = data.getClipData().getItemAt(i).getUri();
                String filename = getFileName(fileuri);


            }
            //Toast.makeText(getActivity(), "Selected Multiple Files", Toast.LENGTH_SHORT).show();
        } else if (data.getData() != null) {
            Toast.makeText(getActivity(), "Selected Single Image", Toast.LENGTH_SHORT).show();
            ImageUri = data.getData();
            imageView.setImageURI(ImageUri);
        }
    }


    private void showPhotoAlertDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.picture_dialog, null);
        dialogBuilder.setView(dialogView);
        LinearLayout takePhoto = dialogView.findViewById(R.id.take_photo);
        LinearLayout photoLibrary = dialogView.findViewById(R.id.photo_library);
        TextView cancel = dialogView.findViewById(R.id.cancel);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();


        photoLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
                alertDialog.dismiss();
            }

            private void openFileChooser() {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }

        });

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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
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

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAN_REQUEST);
        Toast.makeText(getContext(), "Open Camera", Toast.LENGTH_LONG).show();
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (ImageUri != null) {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(ImageUri));
            fileReference.putFile(ImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            }, 500);
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Upload upload = new Upload(uri.toString(), description.getText().toString());
                                    String uploadId = mDatabaseRef.push().getKey();
                                    mDatabaseRef.child(uploadId).setValue(upload);
                                }
                            });
                            Toast.makeText(getActivity(), "upload sucessfull", Toast.LENGTH_SHORT).show();

                            getActivity().finish();
                            Intent moveToHome=new Intent(getActivity(),HomePage.class);
                            moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(moveToHome);
                            Log.d("TEST", taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());


                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    })

                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);


                        }
                    });

        } else {
            Toast.makeText(getActivity(), "no file selected", Toast.LENGTH_SHORT).show();
        }

    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != 1)
                result = result.substring(cut + 1);

        }


        return result;
    }

}








