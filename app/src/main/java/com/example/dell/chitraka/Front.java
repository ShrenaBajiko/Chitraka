package com.example.dell.chitraka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Front extends AppCompatActivity {
    private Button Login,Create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);
        Login=(Button)findViewById(R.id.login);
        Create=(Button)findViewById(R.id.createaccount);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Front.this,Login.class));
            }
        });

        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Front.this,CreateAccount.class));
            }
        });
    }
}
