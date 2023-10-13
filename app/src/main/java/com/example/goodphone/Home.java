package com.example.goodphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.goodphone.adapter.Product_adapter;
import com.example.goodphone.model.List_Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseStorage storage;
    Button btnLogin,btnLogout;
    FirebaseFirestore dbProduct;
    ArrayList<List_Product> arrProduct= new ArrayList<>();
    RecyclerView recyclerView;
    StorageReference storageRef, imageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_home);
        init();
        button();
        checkUser();
        showProduct();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showProduct();
    }

    public void checkUser(){
        if(user != null){
            btnLogin.setVisibility(View.GONE);
        }else{
            btnLogout.setVisibility(View.GONE);
        }

    }
    public void button(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, LogIn.class);
                startActivity(i);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent i= new Intent(Home.this, Home.class);
                startActivity(i);
                finishAffinity();
            }
        });

    }
    public void  showProduct(){
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        dbProduct.collection("Product").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                String name = document.getString("Name".trim());
                                double price =document.getDouble("price");
                                double sold = document.getDouble("sold");
                                double sumRating = document.getDouble("SumRating");
                                FirebaseStorage storage = FirebaseStorage.getInstance("gs://goodphone-687e7.appspot.com/");
                                storageRef = storage.getReference().child("Product");
                                imageRef = storageRef.child(name +".jpg");
                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String fileUrl = uri.toString();
                                        arrProduct.add(new List_Product(id,fileUrl,name,price,sold,sumRating));
                                        Product_adapter adapter= new Product_adapter(Home.this, arrProduct);
                                        recyclerView.setAdapter(adapter);
                                    }
                               }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {

                                    }
                                });
                            }
                        } else {
                            Toast.makeText(Home.this, "lỗi",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void init(){
        recyclerView = findViewById(R.id.recycleView_home);
        btnLogin = findViewById(R.id.btn_Login);
        btnLogout = findViewById(R.id.btnLogOut);
        dbProduct = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }
}