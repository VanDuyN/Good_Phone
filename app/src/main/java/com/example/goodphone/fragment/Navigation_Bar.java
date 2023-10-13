package com.example.goodphone.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_bar, container, false);
        mbtnBar_Profile = view.findViewById(R.id.btnBar_Profile);
        mbtnBar_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Profile.class);
                startActivity(i);
            }
        });
        return view;
    }
}