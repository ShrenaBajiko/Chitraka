package com.example.dell.chitraka;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

//import de.hdodenhof.circleimageview.CircleImageView;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class profilefragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final int CHOOSE_IMAGE=101;
    Uri uriProfileImage;
    ProgressBar progressBar;
    String profileImageUrl;



    TextView name, address, memo;
    Button logout;
    Button save;
    TextView welcome;
    CircleImageView image;

    FirebaseAuth mAuth;

    ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profilefragment, null, false);

        Spinner spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        mAuth=FirebaseAuth.getInstance();

        name =view.findViewById(R.id.name);
        address = view.findViewById(R.id.address);
        memo =  view.findViewById(R.id.memo);
        logout = view.findViewById(R.id.logout);
        save =  view.findViewById(R.id.save);
        welcome =  view.findViewById(R.id.textViewUserEmail);
        image=view.findViewById(R.id.profilepic);

        progressBar=view.findViewById(R.id.progressbar);



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
              saveUserInformation();
            }
        });


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });





        return view;
    }

    private void saveUserInformation()
    {
        //progress dialog
        String Name= name.getText().toString().trim();
        String Address= address.getText().toString().trim();
        String Memo= memo.getText().toString().trim();

        if(Name.isEmpty() && Address.isEmpty() && Memo.isEmpty())
        {
            name.setError("NAME CANNOT BE EMPTY...");
            name.requestFocus();
            address.setError("ADDRESS CANNOT BE EMPTY...");
            address.requestFocus();
            memo.setError("MEMO CANNOT BE EMPTY...");
            memo.requestFocus();
            return;
        }

        FirebaseUser user=mAuth.getCurrentUser();

        if(user!=null && profileImageUrl !=null)
        {
            UserProfileChangeRequest profile=new UserProfileChangeRequest.Builder()
                    .setDisplayName(Name)
                    .setDisplayName(Address)
                    .setDisplayName(Memo)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if(task.isSuccessful())
                    {
                        Log.d("TEST","SUccess");
                        Toast.makeText(getActivity(),"PROFILE ADDED SUCCESSFUL",Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==CHOOSE_IMAGE && resultCode == RESULT_OK && data != null  && data.getData()!=null)
        {
           uriProfileImage= data.getData();

           try {
               Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uriProfileImage);
               image.setImageBitmap(bitmap);

               uploadImageToFirebaseStorage();

           } catch (IOException e)
           {
               e.printStackTrace();
           }

        }
    }

    private void uploadImageToFirebaseStorage() {

        StorageReference profileImageRef=FirebaseStorage.getInstance().getReference("profilepic/"+System.currentTimeMillis()+ ".jpg");

        if(uriProfileImage != null)
        {
            progressBar.setVisibility(View.VISIBLE);
            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    progressBar.setVisibility(View.GONE);

                    profileImageUrl=taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                }
            })

            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(),"ERROR UPLOADING THE IMAGE PLEASE TRY AGAIN...",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showImageChooser()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Profile Picture"),CHOOSE_IMAGE);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }



}