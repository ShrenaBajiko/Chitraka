package com.example.dell.chitraka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private TextView Login;
    private EditText Email,Password;
    private TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Login=(TextView)findViewById(R.id.login);
        Login.setOnClickListener(this);
        Email=(EditText)findViewById(R.id.email);
        Password=(EditText)findViewById(R.id.password);
    }

    @Override
    public void onClick(View view) {

        String email =Email.getText().toString();
        String pass = Password.getText().toString();
        if (email.isEmpty()) {
            Toast.makeText(this, "username cannot empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pass.isEmpty()) {
            Toast.makeText(this, "password cannot empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.equals("admin") && pass.equals("admin123")) {
            Toast.makeText(this, "login sucessful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,HomePage.class));
        } else {
            Toast.makeText(this, "login unsucessful", Toast.LENGTH_SHORT).show();

        }

    }
}
