package com.example.dell.chitraka;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private TextView Login, Log;
    private EditText Email1, Password1;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {

            finish();
            startActivity(new Intent(getApplicationContext(), HomePage.class));

        }

        progressDialog = new ProgressDialog(this);

        Login = (TextView) findViewById(R.id.login);
        Email1 = (EditText) findViewById(R.id.email1);
        Password1 = (EditText) findViewById(R.id.password1);
        Log = (TextView) findViewById(R.id.not);

        Login.setOnClickListener(this);
        Log.setOnClickListener(this);
    }


    private void userLogin() {
        String email1 = Email1.getText().toString().trim();
        String password1 = Password1.getText().toString().trim();

        if (TextUtils.isEmpty(email1)) {
            Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password1)) {

            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }


        progressDialog.setMessage("Login process on progress....");
        progressDialog.show();

        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                progressDialog.cancel();
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(progressRunnable, 800);

        firebaseAuth.signInWithEmailAndPassword(email1, password1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(Login.this, "LOGIN COMPLETE", Toast.LENGTH_SHORT).show();
                           finish();
                           startActivity(new Intent(getApplicationContext(), HomePage.class));

                        } else
                            {

                            Toast.makeText(Login.this, "LOGIN FAILED...PLEASE TRY AGAIN", Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }

   /* @Override
    protected void onStart() {
        super.onStart();

        if(firebaseAuth.getCurrentUser() !=null)
        {
            finish();
        }
    }*/

    @Override
    public void onClick(View view) {

        if (view == Login) {
            userLogin();
        }

        if (view == Log) {
            //finish();
            startActivity(new Intent(this, CreateAccount.class));
        }
    }


}
