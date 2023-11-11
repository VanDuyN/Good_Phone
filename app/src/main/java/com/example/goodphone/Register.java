package com.example.goodphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.UserHandle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    Button btnRegister,btnRefuse,btnConfirm;
    EditText edtFirstName, edtLastName, edtEmail,edtPassword, edtPhoneNumber;
    TextView btnLoginNow, tvDetailConfirm,tvTitleConfirm;
    ProgressDialog progressDialog;

    String email,password,firstName,lastName, phone,regexPattern ;
    private static final String PHONE_NUMBER_PATTERN = "^(\\+?84|0)(1[2689]|3[2-9]|5[2689]|7[06789]|8[0-9])(\\d{7})$";
    boolean emailValidate,phoneValidate;
    Dialog dialog;
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
                validate();
            }
        });
        btnLoginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void validate(){
        email = edtEmail.getText().toString().trim();
        password = edtPassword.getText().toString().trim();
        firstName = edtFirstName.getText().toString().trim();
        lastName = edtLastName.getText().toString().trim();
        phone = edtPhoneNumber.getText().toString().trim();

        regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        emailValidate = patternMatches(email.trim(), regexPattern);
        phoneValidate = validatePhoneNumber(phone.trim(),PHONE_NUMBER_PATTERN);
        //validate email
        if (emailValidate){
            //validate password
            if (password.length() >= 8 && password.length() <= 50) {
                //validate name
                if (!firstName.isEmpty() && !lastName.isEmpty() && lastName.length() <= 30 && firstName.length() <= 30){
                    //valudate phone
                    if(phoneValidate){
                        //validate dia chi

                            openDialog(Gravity.CENTER);

                    }else {
                        Toast.makeText(Register.this, "Số điện thoại có 10 số và bắt đầu bằng 0 hoặc +84", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(Register.this, "Họ và tên không được để trống và không quá 30 ký tự", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(Register.this, "Mật khẩu phải từ 8 kí tự trở lên và không quá 50 ký tự", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(Register.this, "Email không đúng định dạng", Toast.LENGTH_SHORT).show();
        }

    }
    public void createUser(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        progressDialog.setMessage("Hệ thống đang đăng ký vui lòng chờ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                String uid = user.getUid();
                                Map<String, Object> users = new HashMap<>();
                                users.put("id",uid);
                                users.put("firstName",firstName);
                                users.put("lastName",lastName);
                                users.put("email",email);
                                users.put("phoneNumber",phone);
                                users.put("password",password);
                                users.put("role","Khách hàng");
                                db.collection("User").document(uid)
                                        .set(users)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(Register.this, "Đăng ký Thành công",
                                                        Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                                auth.signOut();
                                                finish();

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Register.this, "Kiểm tra Lại thông tin đăng ký",
                                                        Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                                return;
                                            }
                                        });
                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(Register.this, "email đã tồn tại",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void openDialog(int gravity){
        dialog =  new Dialog(Register.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_comfirm);
        Window window = dialog.getWindow();
        if(window == null ){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);
        tvDetailConfirm = dialog.findViewById(R.id.tv_Detail_Confirm);
        tvTitleConfirm = dialog.findViewById(R.id.tv_Title_Confirm);
        btnConfirm = dialog.findViewById(R.id.btn_Confirm);
        btnRefuse = dialog.findViewById(R.id.btn_Refuse);
        tvTitleConfirm.setText("Xác nhận đăng ký tài khoản mới");
        tvDetailConfirm.setText("Bạn có chắc là muốn đăng ký tài khoản này với thông tin trên không ?");
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });
        btnRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public static boolean patternMatches(String input, String regexPattern){
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
    public static boolean validatePhoneNumber(String input, String phoneNumberPattern) {
        Pattern pattern = Pattern.compile(phoneNumberPattern);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
    public void unit(){
        btnRegister = findViewById(R.id.btnRegister);
        btnLoginNow = findViewById(R.id.LoginNow);
        edtFirstName = findViewById(R.id.edtTenRe);
        edtLastName = findViewById(R.id.edtHoRe);
        edtEmail = findViewById(R.id.edtEmailRe);
        edtPhoneNumber = findViewById(R.id.edtSdtRe);
        edtPassword = findViewById(R.id.edtPassRe);
        progressDialog = new ProgressDialog(this);
    }
}