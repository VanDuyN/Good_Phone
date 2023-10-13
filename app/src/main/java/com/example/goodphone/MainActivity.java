package com.example.goodphone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.BaseMenuPresenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button btnLogin, btnLogOut, btnsp;
    FirebaseAuth auth ;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin = findViewById(R.id.btnLogIn);
        btnLogOut = findViewById(R.id.btnLogOut);
        btnsp = findViewById(R.id.btnSanpham);
        auth = FirebaseAuth.getInstance();
        user  = auth.getCurrentUser();
        if(user != null){
            btnLogin.setVisibility(View.GONE);
        }


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,LogIn.class);
                startActivity(i);
            }
        });
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent i = new Intent(MainActivity.this, LogIn.class);
                startActivity(i);
            }
        });
        btnsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, DetailProduct.class);
                startActivity(i);
            }
        });
    }
}