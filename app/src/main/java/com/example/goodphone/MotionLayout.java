package com.example.goodphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MotionLayout extends AppCompatActivity {
    FirebaseUser user;
    ProgressDialog progressDialog;
    String uid;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.motion_layout);
        unit();
        motion();
    }
    public void checkUserLogin(){


        user =  FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            uid = user.getUid();
            Toast.makeText(MotionLayout.this, uid,Toast.LENGTH_SHORT).show();
            checkUser();
        }else {
            progressDialog.dismiss();
            Intent i =  new Intent(MotionLayout.this,ListProduct.class);
            startActivity(i);
            finishAffinity();
        }
    }

    public void motion (){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.setMessage("Đang khởi động vui lòng chờ");
                progressDialog.setCancelable(false);
                progressDialog.show();
                checkUserLogin();
            }
        },3000);
    }
    public void unit(){
        progressDialog = new ProgressDialog(this);
        firestore = FirebaseFirestore.getInstance();
    }
    public void checkUser(){
        firestore.collection("User")
                .document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Lấy giá trị của một trường con trong document
                                String role = document.getString("role");
                                Toast.makeText(MotionLayout.this, role,Toast.LENGTH_SHORT).show();
                                if (role.equals("admin") ){
                                    progressDialog.dismiss();
                                    Intent i =  new Intent(MotionLayout.this,Admin_Home.class);
                                    startActivity(i);
                                    finishAffinity();
                                }else {
                                    progressDialog.dismiss();
                                    Intent i =  new Intent(MotionLayout.this,MainActivity.class);
                                    startActivity(i);
                                    finishAffinity();
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(MotionLayout.this, "Tài khoản lỗi",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(MotionLayout.this, "Tài khoản lỗi",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}