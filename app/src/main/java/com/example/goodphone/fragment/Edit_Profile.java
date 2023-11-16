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

import android.util.Log;
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
import android.widget.Toast;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Edit_Profile extends Fragment {
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore dbUser;
    View view;
    Button btnSave;
    EditText edtDistrict, edtWards, edtNumber,edtPhoneNumber,edtLastName,edtFirstName;
    TextView tvRole,tvEmail;
    String idUser,district,wards,number, phoneNumber,lastName,firstName,districtOld,wardsOld,numberOld, phoneNumberOld,lastNameOld,firstNameOld,emailOld;
    Profile profile;
    String PHONE_NUMBER_PATTERN = "^(\\+?84|0)(1[2689]|3[2-9]|5[2689]|7[06789]|8[0-9]|9[0-9])(\\d{7})$";
    boolean phoneValidate;

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
            }
        });
    }
    public void getData(){
        phoneNumber = edtPhoneNumber.getText().toString().trim();
        lastName = edtLastName.getText().toString().trim();
        firstName = edtFirstName.getText().toString().trim();
        district = edtDistrict.getText().toString().trim();
        wards = edtWards.getText().toString();
        number = edtNumber.getText().toString();
    }
    public void setData(){
        tvEmail.setText(emailOld.trim());
        edtPhoneNumber.setText(phoneNumberOld.trim());
        edtLastName.setText(lastNameOld.trim());
        edtFirstName.setText(firstNameOld.trim());
    }
    public void setDataDB(){
        Map<String,Object> data = new HashMap<>();
        Map<String,Object> dataAddress = new HashMap<>();
        if(firstName != firstNameOld){
            data.put("firstName",firstName);
        }
        if(lastName != lastNameOld){
            data.put("lastName",lastName);
        }
        if(phoneNumber != phoneNumberOld){
            data.put("phoneNumber",phoneNumber);
        }
        if (districtOld != district) {
            dataAddress.put("district",district);
        }
        if (wards != wardsOld);{
            dataAddress.put("wards",wards);
        }
        if (number != numberOld);{
            dataAddress.put("addressNumber",number);
        }
        phoneValidate = validatePhoneNumber(phoneNumber,PHONE_NUMBER_PATTERN);
        if (!phoneValidate){
            Toast.makeText(getContext(), "Kiểm tra lại số điện thoại", Toast.LENGTH_SHORT).show();
            return;
        }else {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.remove(Edit_Profile.this);
            transaction.commit();
            profile = (Profile) getActivity();
            profile.reLoading();
        }
        if (data != null){
            DocumentReference updateUser =  dbUser.collection("User").document(idUser);
            updateUser.update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
        if (dataAddress != null){
            DocumentReference updateUser =  dbUser.collection("User").document(idUser).collection("Address").document("Home");
            updateUser.update(dataAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dbUser.collection("User").document(idUser).collection("Address").document("Home").set(dataAddress);
                        }
                    });
        }

    }
    public static boolean validatePhoneNumber(String input, String phoneNumberPattern) {
        Pattern pattern = Pattern.compile(phoneNumberPattern);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
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
                                emailOld = document.getString("email");
                                String role = document.getString("role");
                                if (role.equals("Khách hàng")){
                                    tvRole.setText("Khách hàng");
                                }else {
                                    tvRole.setText("Quản lý");
                                }
                                firstNameOld = document.getString("firstName");
                                lastNameOld = document.getString("lastName");
                                phoneNumberOld = document.getString("phoneNumber");
                                getAddress();
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
    public void getAddress(){
        dbUser.collection("User")
                .document(idUser)
                .collection("Address")
                .document("Home")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                districtOld  = document.getString("district");
                                wardsOld = document.getString("wards");
                                numberOld = document.getString("addressNumber");
                                edtDistrict.setText(districtOld.trim());
                                edtWards.setText(wardsOld.trim());
                                edtNumber.setText(numberOld.trim());
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
        edtDistrict = view.findViewById(R.id.edt_Address_District_Profile);
        edtWards = view.findViewById(R.id.edt_Address_Wards_Profile);
        edtNumber= view.findViewById(R.id.edt_Address_Number_Profile);
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