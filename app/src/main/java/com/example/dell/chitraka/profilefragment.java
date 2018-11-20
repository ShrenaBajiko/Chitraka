package com.example.dell.chitraka;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

/*import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;*/

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class profilefragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final int REQUEST_CAMERA = 112;
    private static final int SELECT_FILE = 122;
    private  Uri imageHoldUri;

    TextView name,address,memo;
    Button logout;
    Button save;
    CircleImageView userImageProfileView;
    TextView welcome;
    Button showupload;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    DatabaseReference mUserDatabase;
    StorageReference mStorageRef;

    ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.profilefragment,null,false);

        Spinner spinner=view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getActivity(),R.array.Gender,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        name=(TextView)view.findViewById(R.id.name);
        address=(TextView)view.findViewById(R.id.address);
        memo=(TextView) view.findViewById(R.id.memo);
        userImageProfileView=(CircleImageView)view.findViewById(R.id.profilepic);
        logout=(Button)view.findViewById(R.id.logout);
        save=(Button) view.findViewById(R.id.save);
        welcome=(TextView)view.findViewById(R.id.textViewUserEmail) ;
        showupload=(Button) view.findViewById(R.id.text_view_show_upload);

        showupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageActivity();
            }

            private void openImageActivity() {
                Intent intent = new Intent(getActivity(),ImageActivity.class);
                startActivity(intent);

            }
        });




        mAuth=FirebaseAuth.getInstance();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser user=firebaseAuth.getCurrentUser();

                if(user !=null) {
                    getActivity().finish();
                    Intent moveToHome = new Intent(getActivity(), HomePage.class);
                    moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(moveToHome);

                }
            }
        };

        progressDialog=new ProgressDialog(getActivity());

        mUserDatabase=FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mStorageRef=FirebaseStorage.getInstance().getReference();


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == logout) {
                    mAuth.signOut();
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), Login.class));
                }
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                saveUserProfile();

            }
        });


        userImageProfileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                profilePicSelection();
            }
        });


        return view;
    }

    private void saveUserProfile()
    {

        final String Name,Memo,Address;

        Name=name.getText().toString().trim();
        Memo=memo.getText().toString().trim();
        Address=address.getText().toString().trim();

        if(!TextUtils.isEmpty(Name) && !TextUtils.isEmpty(Memo) && !TextUtils.isEmpty(Address))
        {
            if(imageHoldUri != null)
            {

                progressDialog.setTitle("SAVING PROFILE");
                progressDialog.setMessage("PLEASE WAIT......");
                progressDialog.show();


                StorageReference mChildStorage=mStorageRef.child("User_Profile").child(imageHoldUri.getLastPathSegment());
                String profilePicUrl=imageHoldUri.getLastPathSegment();

                mChildStorage.putFile(imageHoldUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {

                        final Task<Uri> imageUrl=taskSnapshot.getMetadata().getReference().getDownloadUrl();

                        mUserDatabase.child("Username").setValue(Name);
                        mUserDatabase.child("Memo").setValue(Memo);
                        mUserDatabase.child("Address").setValue(Address);
                        mUserDatabase.child("userid").setValue(mAuth.getCurrentUser().getUid());
                        mUserDatabase.child("Imageurl").setValue(imageUrl.toString());


                        progressDialog.dismiss();

                        getActivity().finish();
                        Intent moveToHome=new Intent(getActivity(),HomePage.class);
                        moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(moveToHome);

                    }
                });

            }
            else
            {
                Toast.makeText(getActivity(),"Please select an image",Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(getActivity(),"Fields cannot be empty",Toast.LENGTH_LONG).show();
        }
    }



    private void profilePicSelection()
    {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");

        //SET ITEMS AND THERE LISTENERS
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    private void cameraIntent() {

        //CHOOSE CAMERA
        Log.d("gola", "entered here");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {

        //CHOOSE IMAGE FROM GALLERY
        Log.d("gola", "entered here");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //SAVE URI FROM GALLERY
       /* if(requestCode == SELECT_FILE && resultCode == RESULT_OK)
        {
            Uri imageUri = data.getData();
            userImageProfileView.setImageURI(imageUri);

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(getActivity());

        }else if ( requestCode == REQUEST_CAMERA && resultCode == RESULT_OK ){
            //SAVE URI FROM CAMERA

            Uri imageUri = data.getData();
            userImageProfileView.setImageURI(imageUri);

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(getActivity());

        }


        //image crop library code
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageHoldUri = result.getUri();

                userImageProfileView.setImageURI(imageHoldUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }*/

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        String text=parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}