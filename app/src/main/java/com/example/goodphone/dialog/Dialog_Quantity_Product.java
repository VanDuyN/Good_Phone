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
    int quantity,sumQuantity;
    int getQuantity;
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
                getData();
                if (quantity > 0 ){
                    checkCart();
                }else {
                    Toast.makeText(getContext(), "Số lượng phải lớn hơn 0" ,Toast.LENGTH_SHORT).show();

                }

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
        quantity = Integer.parseInt(edtAddQuantity.getText().toString());
        data = new HashMap<>();
        dbProduct.collection("User")
                .document(idUser)
                .collection("Cart")
                .document(idProduct)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Double getQuantityDB = document.getDouble("quantity");
                            getQuantity = getQuantityDB != null ? getQuantityDB.intValue() : 0;
                            sumQuantity = getQuantity + quantity;
                            if (sumQuantity <= 10){
                                data.put("quantity", sumQuantity);

                                addCart();
                            }else {
                                Toast.makeText(getContext(), "Thêm tối đa 10 bạn đang có " + getQuantity + " trong giỏ hàng" ,Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (quantity <= 10){
                                data.put("quantity", quantity);
                                addCart();
                            }else {
                                Toast.makeText(getContext(), "Thêm tối đa 10 sản phẩm" ,Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
    public void addCart(){
        dbProduct.collection("User")
                .document(idUser)
                .collection("Cart")
                .document(idProduct)
                .set(data);
        Toast.makeText(getContext(), "Thêm thành công " +quantity+ " sản phẩm",Toast.LENGTH_SHORT).show();
        dismiss();
    }
}
