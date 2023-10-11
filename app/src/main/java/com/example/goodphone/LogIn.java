package com.example.goodphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.StartupTime;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogIn extends AppCompatActivity {
    private Button btnLogin;
    private EditText edtEmail,edtPassword;
    private TextView btnRegisterNow;
    private ProgressDialog progressDialog;
    String uid;
    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        init();
        button();

    }
    public void button(){
        //Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        //Register now
        btnRegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LogIn.this,Register.class);
                startActivity(i);
            }
        });
    }

    public void login(){
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        //Goi firebas Auth


        //validate email
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        boolean emailValidate = patternMatches(email, regexPattern);
        if (emailValidate){

            //kiểm tra tài khoản mật khẩu
            if (password.trim().length() >= 8){
                progressDialog.setMessage("Đang đăng nhập vui lòng chờ");
                progressDialog.setCancelable(false);
                progressDialog.show();

                // kiểm tra email password trên hệ thống
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    user = FirebaseAuth.getInstance().getCurrentUser();
                                    uid = user.getUid();
                                    checkUser();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    progressDialog.dismiss();
                                    Toast.makeText(LogIn.this, "Kiểm tra lại Email và mật khẩu", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            else {
                Toast.makeText(LogIn.this, "Vui lòng Kiểm tra lại mật khẩu mật khẩu phải từ 8 kí tự trở lên",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }
        }else {
            Toast.makeText(LogIn.this, "vui lòng iểm tra lại email",Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }
    }
    public static boolean patternMatches(String input, String regexPattern){
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
    public void init(){
        btnLogin = findViewById(R.id.btnLogin);
        btnRegisterNow = findViewById(R.id.RegisterNow);
        edtEmail = findViewById(R.id.edtEmailLogin);
        edtPassword = findViewById(R.id.edtPassLogin);
        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();

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
                                Toast.makeText(LogIn.this, role,Toast.LENGTH_SHORT).show();
                                if (role.equals("admin") ){
                                    progressDialog.dismiss();
                                    Intent i =  new Intent(LogIn.this,Admin_Home.class);
                                    startActivity(i);
                                    finishAffinity();
                                }else {
                                    progressDialog.dismiss();
                                    Intent i =  new Intent(LogIn.this,Home.class);
                                    startActivity(i);
                                    finishAffinity();
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(LogIn.this, "Tài khoản lỗi",Toast.LENGTH_SHORT).show();
                                auth.signOut();
                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(LogIn.this, "Tài khoản lỗi",Toast.LENGTH_SHORT).show();
                            auth.signOut();
                        }
                    }
                });
    }
}