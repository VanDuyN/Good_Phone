package com.example.goodphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;

public class Cart extends AppCompatActivity {
    String pid;
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
    ArrayList<List_Product> arrProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        init();
        getDataUser();
        fragment();

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
                                getImgProduct(pId);
                            }
                        } else {

                        }
                    }
                });

    }
    public void getDataProduct(String productID, String image){
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
                                arrProduct.add(new List_Product(pId,image,nameProduct,price,quantity));
                                Cart_Adapter adapter= new Cart_Adapter(Cart.this, arrProduct);
                                recyclerView.setAdapter(adapter);
                            }
                        }

                    }
                });
    }
    public void getImgProduct( String productId){
        imgRef = storageRef.child("Product/"+productId +".jpg");
        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                imgProduct = uri.toString();
                getDataProduct(productId,imgProduct);
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
        fragmentTransactionBar.add(R.id.nevigation_bar, bottomBar).commit();
    }
    public void init(){
        bottomBar = new Navigation_Bar();
        recyclerView = findViewById(R.id.rcv_Cart);
        arrProduct= new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        dbUser = FirebaseFirestore.getInstance();
        dbProduct = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }
}