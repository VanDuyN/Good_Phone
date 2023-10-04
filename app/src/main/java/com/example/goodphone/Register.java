package com.example.goodphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.UserHandle;
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
import com.google.firebase.auth.UserProfileChangeRequest;

public class Register extends AppCompatActivity {
    Button btnRegister;
    EditText edtFirstName, edtLastName, edtEmail,edtPassword, edtAddress, edtPhoneNumber;
    TextView btnLoginNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        unit();
        btn();
    }
    public void btn(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnLoginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void Register(){

    }
    public void createUser(){
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        String firstName = edtFirstName.getText().toString();
        String lastName = edtLastName.getText().toString();
        String phone = edtPhoneNumber.getText().toString();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                        } else {
                            Toast.makeText(Register.this, "Đăng ký thành công.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void unit(){
        btnRegister = findViewById(R.id.btnRegister);
        btnLoginNow = findViewById(R.id.LoginNow);
        edtFirstName = findViewById(R.id.edtTenRe);
        edtLastName = findViewById(R.id.edtHoRe);
        edtEmail = findViewById(R.id.edtEmailRe);
        edtAddress = findViewById(R.id.edtAddressRe);
        edtPhoneNumber = findViewById(R.id.edtSdtRe);
        edtPassword = findViewById(R.id.edtPassRe);
    }
}