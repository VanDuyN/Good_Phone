package com.example.goodphone.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.goodphone.DetailProduct;
import com.example.goodphone.Home;
import com.example.goodphone.LogIn;
import com.example.goodphone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class Info_Profile extends Fragment {
    LinearLayout btnLogout;
    View view;
    FirebaseAuth auth;
    FirebaseFirestore dbUser;
    FirebaseUser user;
    Dialog dialog;
    TextView tvTitleConfirm,tvDetailConfirm,tvFirstName,tvLastName,tvEmail,tvPhoneNumber,tvAddress,tvRole,tvLogout;
    Button btnConfirm, btnRefuse;
    String role;
    ImageButton imgLogout;
    String uid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_info_profile, container, false);
        init();
        checkUser();
        return view;
    }
    public void checkUser(){
        if (user != null){
            getDataUser();

        }else{
            imgLogout.setVisibility(View.GONE);
            tvLogout.setText("Đăng nhập");
            btnLogout.setGravity(Gravity.CENTER);
            btnLogout.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_line_green));

            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getContext(), LogIn.class);
                    startActivity(i);
                }
            });
        }

    }
    public void click(){
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogConfirm(Gravity.CENTER);
            }
        });
    }
    public void getDataUser(){
        uid = user.getUid();
        dbUser.collection("User").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        tvAddress.setText(document.getString("address"));
                        tvEmail.setText(document.getString("email"));
                        role = document.getString("role");
                        tvRole.setText(role);
                        tvFirstName.setText(document.getString("firstName"));
                        tvLastName.setText(document.getString("lastName"));
                        tvPhoneNumber.setText(document.getString("phoneNumber"));
                        if (role.equals("admin")){
                            btnLogout.setVisibility(View.GONE);

                        }
                        else {
                            click();
                        }
                    } else {
                        Toast.makeText(getContext(), "Lỗi lấy dữ liệu",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Lỗi lấy dữ liệu",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void openDialogConfirm(int gravity){
        dialog =  new Dialog(getContext());
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
        tvTitleConfirm.setText("Xác nhận đăng xuất");
        tvDetailConfirm.setText("Bạn có chắc là muốn đăng xuất không");
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                dialog.dismiss();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                requireActivity().finishAffinity();
                Intent i = new Intent(getContext(),Home.class);
                startActivity(i);

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
    public void init(){
        tvAddress = view.findViewById(R.id.tv_Address_Profile);
        tvFirstName= view.findViewById(R.id.tv_FirstName_Profile);
        tvLastName = view.findViewById(R.id.tv_LastName_Profile);
        tvEmail = view.findViewById(R.id.tv_Email_Profile);
        tvPhoneNumber = view.findViewById(R.id.tv_PhoneNumber_Profile);
        tvRole = view.findViewById(R.id.tv_Role_Profile);
        auth = FirebaseAuth.getInstance();
        dbUser = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();
        btnLogout = view.findViewById(R.id.btn_LogOut_Profile);
        imgLogout = view.findViewById(R.id.img_Logout);
        tvLogout = view.findViewById(R.id.tv_Logout);

    }
}