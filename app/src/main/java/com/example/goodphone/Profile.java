package com.example.goodphone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.goodphone.fragment.Edit_Profile;
import com.example.goodphone.fragment.Info_Profile;
import com.example.goodphone.fragment.Navigation_Bar;

public class Profile extends AppCompatActivity {
    Fragment info_profile,bottomBar;
    ImageButton mbtn_editProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        fragment();




    }
    public void click(){
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
    public void fragment(){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.layout_profile, info_profile);
        fragmentTransaction.commit();


        FragmentTransaction fragmentTransactionBar = getSupportFragmentManager().beginTransaction();
        fragmentTransactionBar.add(R.id.nevigation_bar, bottomBar).commit();
    }
    public void init(){
        mbtn_editProfile = findViewById(R.id.btn_editProfile);
        info_profile = new Info_Profile();
        bottomBar = new Navigation_Bar();

    }
}