package com.example.goodphone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.goodphone.fragment.Edit_Profile;
import com.example.goodphone.fragment.Info_Profile;
import com.example.goodphone.fragment.Navigation_Bar;

public class Profile extends AppCompatActivity {
ImageButton mbtn_editProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mbtn_editProfile = findViewById(R.id.btn_editProfile);

        Fragment infoProfile = new Info_Profile();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.layout_profile, infoProfile).commit();

        Fragment bottomBar = new Navigation_Bar();
        FragmentTransaction fragmentTransactionBar = getSupportFragmentManager().beginTransaction();
        fragmentTransactionBar.add(R.id.nevigation_bar, bottomBar).commit();

        mbtn_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mở ra trang edit profile
                mbtn_editProfile.setVisibility(View.INVISIBLE);
                Fragment editProfileFragment = new Edit_Profile();
                FragmentTransaction fragmentEditProfile = getSupportFragmentManager().beginTransaction();
                fragmentEditProfile.replace(R.id.layout_profile, editProfileFragment).addToBackStack(null).commit();

            }
        });
    }
}