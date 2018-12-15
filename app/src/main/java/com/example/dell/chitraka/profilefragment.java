package com.example.dell.chitraka;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class profilefragment extends Fragment {

    private static final int REQUEST_CAMERA = 0;
    private static final int SELECT_FILE = 1;
    Uri imageHoldUri = null;


    Button logout;
    Button save;
    TextView name,address,memo;
    CircleImageView userImageProfileView;
    TextView welcome;

    private String st1,st2,st3;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;


    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference reference = firebaseDatabase.getReference();
    private DatabaseReference childreference =reference.child("Imageurl");

    DatabaseReference mUserDatabase;
    StorageReference mStorageRef;

    ProgressDialog progressDialog;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.profilefragment,null,false);


        name=(TextView)view.findViewById(R.id.name);
        address=(TextView)view.findViewById(R.id.address);
        memo=(TextView) view.findViewById(R.id.memo);
        userImageProfileView=(CircleImageView)view.findViewById(R.id.profilepic);
        logout=(Button)view.findViewById(R.id.logout);
        save=(Button) view.findViewById(R.id.save);
        welcome=(TextView)view.findViewById(R.id.textViewUserEmail) ;


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


        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(getActivity());
        String st4=prefs.getString("st1","");
        name.setText(st4);
        String st5=prefs.getString("st2","");
        address.setText(st5);
        String st6=prefs.getString("st3","");
        memo.setText(st6);



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == logout) {

                    mAuth.signOut();
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), Login.class));


                    st1=name.getText().toString();
                    st2=address.getText().toString();
                    st3=memo.getText().toString();



                    SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor=prefs.edit();

                    editor.putString("st1",st1);
                    editor.putString("st2",st2);
                    editor.putString("st3",st3);
                    editor.apply();

                }
            }
        });



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                saveUserProfile();

                st1=name.getText().toString();
                st2=address.getText().toString();
                st3=memo.getText().toString();



                SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor=prefs.edit();

                editor.putString("st1",st1);
                editor.putString("st2",st2);
                editor.putString("st3",st3);
                editor.apply();


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


        if(!TextUtils.isEmpty(Name) && !TextUtils.isEmpty(Memo) && !TextUtils.isEmpty(Address) && imageHoldUri!=null)
        {

            progressDialog.setTitle("SAVING PROFILE");
            progressDialog.setMessage("PLEASE WAIT......");
            progressDialog.show();


            StorageReference mChildStorage=mStorageRef.child("User_Profile").child(imageHoldUri.getLastPathSegment());
            final String profilePicUrl=imageHoldUri.getLastPathSegment();

            mChildStorage.putFile(imageHoldUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot)
                {


                              String downloaduri= taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                              mUserDatabase.child("Username").setValue(Name);
                              mUserDatabase.child("Memo").setValue(Memo);
                              mUserDatabase.child("Address").setValue(Address);
                              mUserDatabase.child("userid").setValue(mAuth.getCurrentUser().getUid());
                              //mUserDatabase.child("Imageurl").setValue(imageUrl.toString());
                              mUserDatabase.child("Imageurl").setValue(downloaduri);



                              progressDialog.dismiss();

                              Toast.makeText(getActivity()," SUCCESSFULLY SAVED",Toast.LENGTH_SHORT).show();

                              getActivity().finish();
                              Intent moveToHome=new Intent(getActivity(),HomePage.class);
                              moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                              startActivity(moveToHome);
                          }


                   // final Task<Uri> imageUrl=taskSnapshot.getMetadata().getReference().getDownloadUrl();

            });

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
        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);*/

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CAMERA);
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
        if(requestCode == SELECT_FILE && resultCode == RESULT_OK)
        {
            Uri imageUri = data.getData();
            userImageProfileView.setImageURI(imageUri);
            userImageProfileView.invalidate();
            imageHoldUri = imageUri;


            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("image", String.valueOf(imageUri));
            editor.apply();

        }
        if ( requestCode == REQUEST_CAMERA && resultCode == RESULT_OK ){
            //SAVE URI FROM CAMERA

            Uri imageUri = data.getData();
            userImageProfileView.setImageURI(imageUri);
            userImageProfileView.invalidate();
            imageHoldUri = imageUri;

            Bitmap photo = (Bitmap) data.getExtras().get("data");
           userImageProfileView.setImageBitmap(photo);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("image", String.valueOf(imageUri));
           editor.apply();



            }

    }



    @Override
    public void onStart() {
        super.onStart();
        childreference.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                for(com.google.firebase.database.DataSnapshot ds : dataSnapshot.getChildren()) {
                    String url = ds.getValue(String.class);
                    Log.d("TEST",url);

                    Picasso.with(getActivity())
                            .load(imageHoldUri)
                            .error(R.drawable.disclaimer)
                            .into(userImageProfileView);

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("image", String.valueOf(userImageProfileView));
                    editor.apply();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}