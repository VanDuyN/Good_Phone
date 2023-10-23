package com.example.goodphone.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.goodphone.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Dialog_Quantity_Product extends Dialog {
    Context context;
    String idProduct,idUser;
    EditText edtAddQuantity;
    Button btnAddQuantity,btnDeleteQuantity, btnAddQuantityCart;
    int quantity;
    Map<String,Object> data;
    FirebaseFirestore dbProduct;


    public Dialog_Quantity_Product(@NonNull Context context, String idProduct,String idUser) {
        super(context);
        this.context = context;
        this.idProduct = idProduct;
        this.idUser = idUser;
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

            }
        });
        btnAddQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity++;
                edtAddQuantity.setText(String.valueOf(quantity));
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
    }
    public void getData(){
        quantity = Integer.parseInt(edtAddQuantity.getText().toString());
    }
    public void checkCart(){
        data = new HashMap<>();
        data.put("idProduct",idProduct);
        data.put("idUser", idProduct);
        data.put("quantity", 1);
        dbProduct.collection("Product")
                .document(idUser)
                .collection("Cart")
                .document()
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            if (document.contains(idProduct)) {
                                Object quantity = document.getDouble("Quantity") + 1;
                                data.put("Quantity", quantity );
                            } else {

                            }
                        } else {
                            // Tài liệu không tồn tại trong Firestore
                        }
                    } else {
                        // Xảy ra lỗi khi truy vấn Firestore
                    }
                });
    }
}
