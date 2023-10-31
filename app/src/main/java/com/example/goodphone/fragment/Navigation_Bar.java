package com.example.goodphone.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goodphone.Admin_Home;
import com.example.goodphone.Cart;
import com.example.goodphone.Home;
import com.example.goodphone.LogIn;
import com.example.goodphone.Profile;
import com.example.goodphone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Navigation_Bar extends Fragment {
    FirebaseAuth auth;
    FirebaseUser user;
    View view;
    Dialog dialog;
    Button btnConfirm, btnRefuse;
    TextView tvTitleConfirm,tvDetailConfirm;
    LinearLayout mbtnBar_Profile,btnBar_Convert,btnBar_Home,btnBar_Cart;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_navigation_bar, container, false);
        init();
        checkActivity();
        setClick();

        return view;
    }
    public  void setClick(){
        mbtnBar_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof Profile){

                }else if(getActivity() instanceof Home){
                    Intent i = new Intent(getContext(), Profile.class);
                    startActivity(i);
                    getActivity().overridePendingTransition(0,0);
                }else{
                    mbtnBar_Profile.setBackgroundColor(getResources().getColor(R.color.green_dark));
                    btnBar_Home.setBackgroundColor(getResources().getColor(R.color.white));
                    btnBar_Cart.setBackgroundColor(getResources().getColor(R.color.white));
                    btnBar_Convert.setBackgroundColor(getResources().getColor(R.color.white));
                    Intent i = new Intent(getContext(), Profile.class);
                    startActivity(i);
                    getActivity().finish();
                    getActivity().overridePendingTransition(0,0);
                }
            }
        });
        btnBar_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof Home){
                    mbtnBar_Profile.setBackgroundColor(getResources().getColor(R.color.white));
                    btnBar_Home.setBackgroundColor(getResources().getColor(R.color.green_dark));
                    btnBar_Cart.setBackgroundColor(getResources().getColor(R.color.white));
                    btnBar_Convert.setBackgroundColor(getResources().getColor(R.color.white));
                }else{
                    getActivity().finish();
                    getActivity().overridePendingTransition(0,0);
                    mbtnBar_Profile.setBackgroundColor(getResources().getColor(R.color.white));
                    btnBar_Home.setBackgroundColor(getResources().getColor(R.color.green_dark));
                    btnBar_Cart.setBackgroundColor(getResources().getColor(R.color.white));
                    btnBar_Convert.setBackgroundColor(getResources().getColor(R.color.white));
                }

            }
        });
        btnBar_Cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null){
                    if(getActivity() instanceof Cart){

                    }else if(getActivity() instanceof Home){
                        Intent i = new Intent(getContext(), Cart.class);
                        startActivity(i);
                        getActivity().overridePendingTransition(0,0);
                    }else{
                        mbtnBar_Profile.setBackgroundColor(getResources().getColor(R.color.white));
                        btnBar_Home.setBackgroundColor(getResources().getColor(R.color.white));
                        btnBar_Cart.setBackgroundColor(getResources().getColor(R.color.green_dark));
                        btnBar_Convert.setBackgroundColor(getResources().getColor(R.color.white));
                        Intent i = new Intent(getContext(), Cart.class);
                        startActivity(i);
                        getActivity().finish();
                        getActivity().overridePendingTransition(0,0);
                    }
                }else {
                    dialogOpenLogin();
                }


            }
        });
        btnBar_Convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Chức năng đang phát triển",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void dialogOpenLogin(){
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
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);
        tvDetailConfirm = dialog.findViewById(R.id.tv_Detail_Confirm);
        tvTitleConfirm = dialog.findViewById(R.id.tv_Title_Confirm);
        btnConfirm = dialog.findViewById(R.id.btn_Confirm);
        btnRefuse = dialog.findViewById(R.id.btn_Refuse);
        tvTitleConfirm.setText("Yêu cầu đăng nhập");
        tvDetailConfirm.setText("Để có thể thức hiện chức năng này bạn cần phải đăng nhập");
        btnConfirm.setText("Đăng nhập");
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(getContext(), LogIn.class);
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

    public void checkActivity(){
        if (getActivity() instanceof Cart){
            mbtnBar_Profile.setBackgroundColor(getResources().getColor(R.color.white));
            btnBar_Home.setBackgroundColor(getResources().getColor(R.color.white));
            btnBar_Cart.setBackgroundColor(getResources().getColor(R.color.green_dark));
            btnBar_Convert.setBackgroundColor(getResources().getColor(R.color.white));
        } else if (getActivity() instanceof Home) {
            mbtnBar_Profile.setBackgroundColor(getResources().getColor(R.color.white));
            btnBar_Home.setBackgroundColor(getResources().getColor(R.color.green_dark));
            btnBar_Cart.setBackgroundColor(getResources().getColor(R.color.white));
            btnBar_Convert.setBackgroundColor(getResources().getColor(R.color.white));
        }else if (getActivity() instanceof Profile){
            mbtnBar_Profile.setBackgroundColor(getResources().getColor(R.color.green_dark));
            btnBar_Home.setBackgroundColor(getResources().getColor(R.color.white));
            btnBar_Cart.setBackgroundColor(getResources().getColor(R.color.white));
            btnBar_Convert.setBackgroundColor(getResources().getColor(R.color.white));
        }

    }
    public void init(){
        mbtnBar_Profile = view.findViewById(R.id.btnBar_Profile);
        btnBar_Cart= view.findViewById(R.id.btnBar_Cart);
        btnBar_Convert= view.findViewById(R.id.btnBar_Convert);
        btnBar_Home= view.findViewById(R.id.btnBar_Home);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }
}