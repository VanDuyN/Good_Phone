package com.example.goodphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogIn extends AppCompatActivity {
    private Button btnLogin;
    private EditText edtEmail,edtPassword;
    private TextView btnRegisterNow;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        unit();
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
        FirebaseAuth auth = FirebaseAuth.getInstance();

        //validate email
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        boolean emailValidate = patternMatches(email, regexPattern);
        if (emailValidate){

            //kiểm tra tài khoản mật khẩu
            if (password.trim().length() >= 8){
                progressDialog.show();

                // kiểm tra email password trên hệ thống
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Intent i = new Intent(LogIn.this, MainActivity.class);
                                    Toast.makeText(LogIn.this, "Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                                    startActivity(i);
                                    finishAffinity();
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
                return;
            }
        }else {
            Toast.makeText(LogIn.this, "vui lòng iểm tra lại email",Toast.LENGTH_SHORT).show();
            return;
        }
    }
    public static boolean patternMatches(String input, String regexPattern){
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
    public void unit(){
        btnLogin = findViewById(R.id.btnLogin);
        btnRegisterNow = findViewById(R.id.RegisterNow);
        edtEmail = findViewById(R.id.edtEmailLogin);
        edtPassword = findViewById(R.id.edtPassLogin);
        progressDialog = new ProgressDialog(this);
    }
}