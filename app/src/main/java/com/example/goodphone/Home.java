package com.example.goodphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goodphone.adapter.Product_adapter;
import com.example.goodphone.fragment.Navigation_Bar;
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
    String id,name, nameImg;
    AlertDialog.Builder builder;

    Dialog dialog;
    TextView tvTitleConfirm,tvDetailConfirm;
    Button btnConfirm, btnRefuse;
    int sold,price;

    Double sumRating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_home);
//        Fragment bottomBar = new Navigation_Bar();
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.fragment_navigationBar, bottomBar).commit();
        init();
        button();
        checkUser();
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
                openDialog(Gravity.CENTER);
            }
        });

    }
    public void openDialog(int gravity){
        dialog =  new Dialog(Home.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_comfirm);
        Window window = dialog.getWindow();
        if(window == null ){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);
        tvDetailConfirm = dialog.findViewById(R.id.tv_Detail_Confirm);
        tvTitleConfirm = dialog.findViewById(R.id.tv_Title_Confirm);
        btnConfirm = dialog.findViewById(R.id.btn_Confirm);
        btnRefuse = dialog.findViewById(R.id.btn_Refuse);
        tvTitleConfirm.setText("Xác nhận đăng xuất");
        tvDetailConfirm.setText("Bạn có chắc là muốn đăng xuất không");
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent= new Intent(Home.this, Home.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        btnRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void  showProduct(){
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        dbProduct.collection("Product").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            FirebaseStorage storage = FirebaseStorage.getInstance("gs://goodphone-687e7.appspot.com/");
                            storageRef = storage.getReference().child("Product");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                nameImg = document.getString("Name");
                                imageRef = storageRef.child(nameImg +".jpg");

                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        id = document.getId();
                                        name = document.getString("Name");
                                        Double getPrice = document.getDouble("Price");
                                        Double getSold = document.getDouble("Sold");
                                        price = getPrice != null ? getPrice.intValue() : 0;
                                        sold = getSold != null ? getSold.intValue() : 0;
                                        sumRating = document.getDouble("SumRating");
                                        Log.e("price", String.valueOf(price));
                                        String fileUrl = uri.toString();

                                        arrProduct.add(new List_Product(id,fileUrl,name,price,sold,sumRating));

                                        Product_adapter adapter= new Product_adapter(Home.this, arrProduct);
                                        recyclerView.setAdapter(adapter);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        Toast.makeText(Home.this, "anh lỗi",Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }

                        } else {
                            Toast.makeText(Home.this, "lỗi",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void getImage(){
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
                Toast.makeText(Home.this, "anh lỗi",Toast.LENGTH_SHORT).show();
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
        builder = new AlertDialog.Builder(Home.this);
    }
}