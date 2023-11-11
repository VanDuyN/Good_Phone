package com.example.goodphone.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.goodphone.R;
import com.example.goodphone.fragment.Waiting_Order;

public class Admin_Order extends AppCompatActivity {
    FrameLayout frameLayout, fragmentOther;
    Fragment fragmentWaiting,fragmentComplete, fragmentCancel;
    LinearLayout btnWaiting, btnDelivered,btnCancel;
    TextView tvWaiting,tvDelivered,tvCancel;
    View vWaiting, vDelivered, vCancel;
    ImageView btnBack;
    Fragment frmAdminWaitingOder;
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order);
        init();
        button();
        openFragmentAWTO();
    }
    public void button(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvCancel.setTextColor(getResources().getColor(R.color.green_dark));
                vCancel.setBackgroundColor(getResources().getColor(R.color.green_dark));
                tvDelivered.setTextColor(getResources().getColor(R.color.black));
                vDelivered.setBackgroundColor(getResources().getColor(R.color.black));
                tvWaiting.setTextColor(getResources().getColor(R.color.black));
                vWaiting.setBackgroundColor(getResources().getColor(R.color.black));

                    Log.e("n","m");
                    openFragmentCancel();

            }
        });
        btnDelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvCancel.setTextColor(getResources().getColor(R.color.black));
                vCancel.setBackgroundColor(getResources().getColor(R.color.black));
                tvDelivered.setTextColor(getResources().getColor(R.color.green_dark));
                vDelivered.setBackgroundColor(getResources().getColor(R.color.green_dark));
                tvWaiting.setTextColor(getResources().getColor(R.color.black));
                vWaiting.setBackgroundColor(getResources().getColor(R.color.white));

                    openFragmentComplete();

            }
        });
        btnWaiting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvCancel.setTextColor(getResources().getColor(R.color.black));
                vCancel.setBackgroundColor(getResources().getColor(R.color.black));
                tvDelivered.setTextColor(getResources().getColor(R.color.black));
                vDelivered.setBackgroundColor(getResources().getColor(R.color.black));
                tvWaiting.setTextColor(getResources().getColor(R.color.green_dark));
                vWaiting.setBackgroundColor(getResources().getColor(R.color.green_dark));
                openFragmentAWTO();
            }
        });
    }
    public void openFragmentComplete(){
        fragmentComplete  = new Admin_Complete_Order();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmnet_Admin_Other,fragmentComplete);
        if(fragmentWaiting != null ){
            fragmentTransaction.remove(fragmentWaiting);
        }
        if (fragmentCancel != null){
            fragmentTransaction.remove(fragmentCancel);
        }
        fragmentTransaction.commit();
    }
    public void openFragmentCancel(){
        fragmentCancel = new Admin_Cancel_Order();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmnet_Admin_Other,fragmentCancel);
        if(fragmentWaiting != null ){
            fragmentTransaction.remove(fragmentWaiting);
            Log.e("nn","m");
        }
        if (fragmentComplete != null){
            fragmentTransaction.remove(fragmentComplete);
            Log.e("nnn","m");

        }
        fragmentTransaction.commit();
    }
    public void openFragmentAWTO(){
        fragmentWaiting = new Admin_Waiting_Order();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmnet_Admin_Other,fragmentWaiting);
        if(fragmentCancel != null ){
            fragmentTransaction.remove(fragmentCancel);
        }
        if (fragmentComplete != null){
            fragmentTransaction.remove(fragmentComplete);
        }
        fragmentTransaction.commit();
    }
    public void init(){
        fragmentOther = findViewById(R.id.fragment_Other);
        btnWaiting = findViewById(R.id.btn_Waiting_for_delivery);
        btnDelivered = findViewById(R.id.btn_delivered);
        btnCancel = findViewById(R.id.btn_cancel);
        tvWaiting = findViewById(R.id.tv_Waiting_for_delivery);
        tvDelivered = findViewById(R.id.tv_delivered);
        tvCancel = findViewById(R.id.tv_cancel);
        vWaiting = findViewById(R.id.v_Waiting_for_delivery);
        vDelivered = findViewById(R.id.v_delivered) ;
        vCancel = findViewById(R.id.v_cancel);
        relativeLayout = findViewById(R.id.fragmnet_Admin_Other);
        btnBack = findViewById(R.id.ic_back);
    }
}