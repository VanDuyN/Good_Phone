package com.example.goodphone.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.goodphone.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.Locale;

public class Dialog_Successful_Payment extends Dialog {
    String idProduct;
    Context context;
    FirebaseFirestore DBUser;
    Button btnContinue;
    int sumPrice;
    String address,nameU,dateTime,phoneNumber;
    TextView tvName,tvPhoneNumber,tvAddress,tvSumPrice,tvID,tvDateTime;
    public Dialog_Successful_Payment(@NonNull Context context, String idP,String address, String nameUN,String dateTime,int sumPrice,String phoneNumberN) {
        super(context);
        this.context = context;
        this.idProduct = idP;
        this.address = address;
        this.sumPrice = sumPrice;
        this.nameU = nameUN;
        this.dateTime = dateTime;
        this.phoneNumber = phoneNumberN;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_successful_payment);
        init();
        setData();
        click();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }

    public void init(){
        tvAddress = findViewById(R.id.tvAddress);
        tvID = findViewById(R.id.tv_idOther);
        tvDateTime = findViewById(R.id.tv_DateTime);
        tvName = findViewById(R.id.tvNameUser);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvSumPrice = findViewById(R.id.tvSumPrice);
        btnContinue = findViewById(R.id.btnContinue);
    }
    public void setData(){
        Locale locale = new Locale("vi", "VN");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
        String formatSumPrice = currencyFormat.format(sumPrice);
        tvAddress.setText(address);
        tvSumPrice.setText(formatSumPrice);
        tvDateTime.setText(dateTime);
        tvID.setText(idProduct);
        tvPhoneNumber.setText(phoneNumber);
        tvName.setText(nameU);

    }
    public void click(){
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
