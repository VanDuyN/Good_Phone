package com.example.goodphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.goodphone.fragment.Edit_Profile;
import com.example.goodphone.fragment.Info_Profile;
import com.example.goodphone.fragment.Navigation_Bar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends AppCompatActivity {
    Fragment info_profile,bottomBar,editProfile;
    ImageButton btn_editProfile;
    ImageView btnExitProfile;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore dbUser;
    String role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        click();
        checkProfile();
        fragment();


    }

    public void click(){
        btn_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.remove(editProfile);
                fragmentTransaction.commit();

                //mở ra trang edit profile
                btn_editProfile.setVisibility(View.INVISIBLE);
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
    }
    public void fragmentNavigationBar(){
        FragmentTransaction fragmentTransactionBar = getSupportFragmentManager().beginTransaction();
        fragmentTransactionBar.add(R.id.nevigation_bar, bottomBar).commit();
    }
    public void checkProfile(){
        if (user == null){
            btn_editProfile.setVisibility(View.GONE);
            btnExitProfile.setVisibility(View.GONE);
        }else{
            getDataDB();
            btn_editProfile.setVisibility(View.VISIBLE);
        }
    }
    public void checkUser(){
        if (role.equals("admin")){
            btnExitProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
        else {
            btnExitProfile.setVisibility(View.GONE);
            fragmentNavigationBar();
        }
    }
    public void getDataDB(){
        dbUser.collection("User")
                .document(user.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                role =  document.getString("role");
                                checkUser();
                            }
                        }
                    }
                });
    }
    public void reLoading() {
        btn_editProfile.setVisibility(View.VISIBLE);
        fragment();
    }

    public void init(){
        btn_editProfile = findViewById(R.id.btn_editProfile);
        info_profile = new Info_Profile();
        bottomBar = new Navigation_Bar();
        editProfile = new Edit_Profile();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        dbUser= FirebaseFirestore.getInstance();
        btnExitProfile = findViewById(R.id.btn_Exit_Profile);
    }
}