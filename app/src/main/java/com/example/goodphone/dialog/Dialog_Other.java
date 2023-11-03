package com.example.goodphone.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.example.goodphone.Payment;
import com.example.goodphone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Dialog_Other extends Dialog {
    Context context;
    String idProduct;
    EditText edtAddQuantity;
    TextView tvDetail;
    Button btnAddQuantity,btnDeleteQuantity, btnAddQuantityCart;
    int quantity;
    FirebaseFirestore dbProduct;
    FirebaseAuth auth;
    FirebaseUser user;

    public Dialog_Other(@NonNull Context context, String idProduct) {
        super(context);
        this.context = context;
        this.idProduct = idProduct;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_quantity_product);
        init();
        getData();
        click();

    }
    public void click(){
        btnAddQuantityCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
                if (user != null){
                    if (quantity > 0 && quantity <= 10 ){
                        Intent i = new Intent(getContext(), Payment.class);
                        i.putExtra("id", idProduct);
                        i.putExtra("quantity",quantity);
                        getContext().startActivity(i);
                        dismiss();
                    }else {
                        Toast.makeText(getContext(), "Số lượng phải lớn hơn 0 và nhỏ hơn hoặc bằng 10 " ,Toast.LENGTH_SHORT).show();
                    }
                }else {
                    if (quantity > 0 && quantity<=5 ){
                        Intent i = new Intent(getContext(), Payment.class);
                        i.putExtra("id", idProduct);
                        i.putExtra("quantity",quantity);
                        getContext().startActivity(i);
                        dismiss();
                    }else {
                        Toast.makeText(getContext(), "Số lượng phải lớn hơn 0 và nhỏ hơn hoặc bằng 5 " ,Toast.LENGTH_SHORT).show();

                    }
                }


            }
        });
        btnAddQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null)
                    if (quantity < 10){
                        quantity++;
                        edtAddQuantity.setText(String.valueOf(quantity));
                    }else {

                    }
                else {
                    if (quantity < 5){
                        quantity++;
                        edtAddQuantity.setText(String.valueOf(quantity));
                    }else {

                    }
                }
            }
        });
        btnDeleteQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity == 1){

                }else {
                    quantity--;
                    edtAddQuantity.setText(String.valueOf(quantity));
                }
            }
        });

    }
    public void init(){
        btnAddQuantity = findViewById(R.id.btn_Add_Quantity);
        btnDeleteQuantity = findViewById(R.id.btn_Delete_Quantity);
        btnAddQuantityCart = findViewById(R.id.btn_Add_Quantity_Cart_Product);
        edtAddQuantity = findViewById(R.id.edt_Quantity);
        dbProduct= FirebaseFirestore.getInstance();
        tvDetail = findViewById(R.id.tvDialogQuantity);
        tvDetail.setText("Nhập số lượng muốn mua");
        btnAddQuantityCart.setText("Mua ngay");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

    }
    public void getData(){
        quantity = Integer.parseInt(edtAddQuantity.getText().toString());
    }
}
