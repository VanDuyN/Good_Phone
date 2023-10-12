package com.example.goodphone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Admin_Home extends AppCompatActivity {
    RelativeLayout btnLogout, btnQLSP, btnQLKH;
    FirebaseAuth auth ;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        init();
        button();
    }
    public void button(){
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout();
            }
        });
        btnQLKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Admin_Home.this, "Chức năng đang phát triển",Toast.LENGTH_SHORT).show();
            }
        });
        btnQLSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QLSP();
            }
        });
    }
    public void Logout(){
        auth.signOut();
        Intent i= new Intent(Admin_Home.this, LogIn.class);
        startActivity(i);
        finishAffinity();
    }
    public void QLSP(){
        Intent i= new Intent(Admin_Home.this, QLSanPhamActivity.class);
        startActivity(i);
    }
    public void init(){
        btnLogout = findViewById(R.id.btn_Logout);
        btnQLKH = findViewById(R.id.btnQLKH);
        btnQLSP = findViewById(R.id.btnQLSP);
        auth = FirebaseAuth.getInstance();
        user  = auth.getCurrentUser();

    }
}