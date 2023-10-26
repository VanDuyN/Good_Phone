package com.example.goodphone.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.goodphone.Cart;
import com.example.goodphone.Home;
import com.example.goodphone.LogIn;
import com.example.goodphone.Profile;
import com.example.goodphone.R;


public class Navigation_Bar extends Fragment {
View view;
LinearLayout mbtnBar_Profile,btnBar_Convert,btnBar_Home,btnBar_Cart;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_navigation_bar, container, false);
        init();
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
                }else{
                    Intent i = new Intent(getContext(), Profile.class);
                    startActivity(i);
                    getActivity().finish();
                }
            }
        });
        btnBar_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof Home){

                }else{
                    getActivity().finish();
                }

            }
        });
        btnBar_Cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity() instanceof Cart){

                }else if(getActivity() instanceof Home){
                    Intent i = new Intent(getContext(), Cart.class);
                    startActivity(i);
                }else{
                    Intent i = new Intent(getContext(), Cart.class);
                    startActivity(i);
                    getActivity().finish();
                }

            }
        });
        btnBar_Convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onDestroy() {
        mbtnBar_Profile.setBackgroundColor(getResources().getColor(R.color.white));
        super.onDestroy();
    }

    public void init(){
        mbtnBar_Profile = view.findViewById(R.id.btnBar_Profile);
        btnBar_Cart= view.findViewById(R.id.btnBar_Cart);
        btnBar_Convert= view.findViewById(R.id.btnBar_Convert);
        btnBar_Home= view.findViewById(R.id.btnBar_Home);
    }
}