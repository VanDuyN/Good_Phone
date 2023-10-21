package com.example.goodphone.fragment;

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

import com.example.goodphone.Home;
import com.example.goodphone.LogIn;
import com.example.goodphone.Profile;
import com.example.goodphone.R;


public class Navigation_Bar extends Fragment {
LinearLayout mbtnBar_Profile;
Info_Profile info_profile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_bar, container, false);
        init();
        mbtnBar_Profile = view.findViewById(R.id.btnBar_Profile);
        mbtnBar_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mbtnBar_Profile.setBackgroundColor(getResources().getColor(R.color.green_dark));
                FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.loHome, info_profile);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        mbtnBar_Profile.setBackgroundColor(getResources().getColor(R.color.white));
        super.onDestroy();
    }

    public void init(){
        info_profile = new Info_Profile();
    }
}