package com.example.goodphone.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.goodphone.Home;
import com.example.goodphone.Profile;
import com.example.goodphone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Edit_Profile extends Fragment {
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore dbUser;
    View view;
    Button btnSave;
    EditText edtAddress,edtPhoneNumber,edtLastName,edtFirstName;
    TextView tvRole,tvEmail;
    String idUser,address, phoneNumber,lastName,firstName,email,addressOld, phoneNumberOld,lastNameOld,firstNameOld,emailOld;
    Profile profile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        init();
        click();
        getDataDB();
        return view;
    }
    public void click(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
                setDataDB();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.remove(Edit_Profile.this);
                transaction.commit();
                profile = (Profile) getActivity();
                profile.reLoading();
            }
        });
    }
    public void getData(){
        phoneNumber = edtPhoneNumber.getText().toString().trim();
        address = edtAddress.getText().toString().trim();
        lastName = edtLastName.getText().toString().trim();
        firstName = edtFirstName.getText().toString().trim();
    }
    public void setData(){
        tvEmail.setText(emailOld.trim());
        edtPhoneNumber.setText(phoneNumberOld.trim());
        edtAddress.setText(addressOld.trim());
        edtLastName.setText(lastNameOld.trim());
        edtFirstName.setText(firstNameOld.trim());
    }
    public void setDataDB(){
        Map<String,Object> data = new HashMap<>();
        if(firstName != firstNameOld){
            data.put("firstName",firstName);
        }
        if(lastName != lastNameOld){
            data.put("lastName",lastName);
        }
        if(phoneNumber != phoneNumberOld){
            data.put("phoneNumber",phoneNumber);
        }
        if (address != addressOld) {
            data.put("address",address);
        }
        if (data != null){
            DocumentReference updateUser =  dbUser.collection("User").document(idUser);
            updateUser.update(data);
        }
    }
    public void getDataDB(){
        dbUser.collection("User")
                .document(idUser)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                addressOld = document.getString("address");
                                emailOld = document.getString("email");
                                tvRole.setText(document.getString("role"));
                                firstNameOld = document.getString("firstName");
                                lastNameOld = document.getString("lastName");
                                phoneNumberOld = document.getString("phoneNumber");
                                setData();
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    public void init(){
        btnSave = view.findViewById(R.id.btn_Save_Profile);
        edtAddress = view.findViewById(R.id.edt_Address_edit_Profile);
        edtFirstName=view.findViewById(R.id.edt_FirstName_edit_Profile);
        edtLastName = view.findViewById(R.id.edt_LastName_edit_Profile);
        edtPhoneNumber = view.findViewById(R.id.edt_PhoneNumber_edit_Profile);
        tvRole = view.findViewById(R.id.tv_Role_edit_Profile);
        tvEmail = view.findViewById(R.id.tv_Email_edit_Profile);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        dbUser = FirebaseFirestore.getInstance();
        idUser = user.getUid();
    }

}