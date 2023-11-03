package com.example.goodphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goodphone.adapter.Cart_Adapter;
import com.example.goodphone.adapter.Product_adapter;
import com.example.goodphone.fragment.Navigation_Bar;
import com.example.goodphone.model.List_Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Document;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {
    Double getPriceDB,getQuantityDB;
    int price,quantity;
    String uId, pId,nameProduct, imgProduct;
    Fragment bottomBar;
    RecyclerView recyclerView;
    FirebaseStorage storage;
    StorageReference storageRef, imgRef;
    FirebaseFirestore dbUser,dbProduct ;
    FirebaseUser user;
    FirebaseAuth auth;
    CheckBox ckbSelectAll;
    ArrayList<List_Product> arrProduct;
    Cart_Adapter adapter;
    int sumPrice;
    TextView tvSumPrice;
    Button btnOder;
    List<List_Product> selectedItems;
    ArrayList<String> listPut;
    List_Product item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        init();
        button();
        getDataUser();
        fragment();

    }
    public void button(){
        ckbSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ckbSelectAll.isChecked()) {
                    adapter.selectAllItems();
                } else {
                    adapter.clearSelection();
                }
            }
        });
        btnOder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Cart.this, Payment.class);
                i.putStringArrayListExtra("listId",  listPut) ;
                startActivity(i);
            }
        });
    }
    public void getDataUser(){
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        uId = user.getUid();
        dbUser.collection("User")
                .document(uId)
                .collection("Cart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                pId = document.getId();
                                Log.e("id", pId);
                                getQuantityDB = document.getDouble("quantity");
                                quantity = getQuantityDB != null ? getQuantityDB.intValue() : 0;
                                getImgProduct(pId,quantity);
                            }
                        } else {

                        }
                    }
                });

    }
    public void getDataAdapter() {
        selectedItems = adapter.getItemsProduct();
        listPut.clear();
        for (int i = 0; i < selectedItems.size(); i++) {
            item = selectedItems.get(i);
            listPut.add(item.getId());
            int price = item.getPrice();
            int quantity = item.getQuantity();
            sumPrice += price *quantity;
            Log.e("e", String.valueOf(sumPrice));
        }
        Locale locale = new Locale("vi", "VN");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
        String formattedNumber = currencyFormat.format(sumPrice);
        tvSumPrice.setText(String.valueOf(formattedNumber));
        sumPrice = 0;
    }
    public void getDataProduct(String productID, String image, int quantity){
        dbProduct.collection("Product")
                .document(productID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                getPriceDB = document.getDouble("Price");
                                price = getPriceDB != null ? getPriceDB.intValue() : 0;
                                nameProduct = document.getString("Name");
                                arrProduct.add(new List_Product(uId,productID,image,nameProduct,price,quantity));
                            }
                        }
                        adapter= new Cart_Adapter(Cart.this, arrProduct,Cart.this);
                        recyclerView.setAdapter(adapter);

                    }
                });

    }
    public void updateCheckBox(boolean isChecked){
        ckbSelectAll.setChecked(isChecked);
    }
    public void getImgProduct( String productId, int quantity){
        imgRef = storageRef.child("Product/"+productId +".jpg");
        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                imgProduct = uri.toString();
                getDataProduct(productId,imgProduct,quantity);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(Cart.this, "anh lỗi",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void fragment(){
        FragmentTransaction fragmentTransactionBar = getSupportFragmentManager().beginTransaction();
        fragmentTransactionBar.add(R.id.navigation_bar, bottomBar).commit();
    }
    public void init(){
        bottomBar = new Navigation_Bar();
        recyclerView = findViewById(R.id.rcv_Cart);
        listPut = new ArrayList<>();
        arrProduct= new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        dbUser = FirebaseFirestore.getInstance();
        dbProduct = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        ckbSelectAll = findViewById(R.id.checkbox_Select_All);
        tvSumPrice = findViewById(R.id.priceTemp);
        btnOder = findViewById(R.id.btn_Oder_Cart);
    }
}