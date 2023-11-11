package com.example.goodphone.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.goodphone.LogIn;
import com.example.goodphone.Profile;
import com.example.goodphone.QLSanPhamActivity;
import com.example.goodphone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Admin_Home extends AppCompatActivity {
    LinearLayout btnLogout, btnQLSP, btnQLKH, btnProfile,btnShipping;
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
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Admin_Home.this, Profile.class);
                startActivity(i);
            }
        });
        btnShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Admin_Home.this, Admin_Order.class);
                startActivity(i);
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
        btnShipping = findViewById(R.id.btn_Shipping);
        btnQLKH = findViewById(R.id.btnQLKH);
        btnQLSP = findViewById(R.id.btnQLSP);
        auth = FirebaseAuth.getInstance();
        user  = auth.getCurrentUser();
        btnProfile = findViewById(R.id.btn_Profile_Admin);

    }
}