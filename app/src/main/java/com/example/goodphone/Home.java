package com.example.goodphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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

import org.checkerframework.checker.lock.qual.LockHeld;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    FirebaseUser user;
    FirebaseAuth auth;
    SearchView searchView;
    FirebaseStorage storage;
    Button btnLogin,btnViewProduct,btnViewXiaomi,btnViewIPhone, btnViewOther,btnViewOPPO, btnViewSamSung;
    FirebaseFirestore dbProduct;
    ArrayList<List_Product> arrAllProduct, arrXiaomi,arrIphone,arrOther,arrSamSung,arrOPPO;
    RecyclerView recyclerViewXiaomi,recyclerViewAll, recyclerViewIphone,recyclerViewSamSung, recyclerViewOther,recyclerViewOPPO;
    StorageReference storageRef, imageRef;
    String id,name,productionCompany;
    AlertDialog.Builder builder;
    int sold,price;

    Double sumRating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_home);

        init();
        fragmentHome();
        button();
        searchView();
        checkUser();
        showProduct();


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        arrAllProduct.clear();
        arrIphone.clear();
        arrXiaomi.clear();
        arrOPPO.clear();
        arrSamSung.clear();
        arrOther.clear();
        showProduct();
    }

    public void checkUser(){
        if(user != null) {
            btnLogin.setVisibility(View.GONE);
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
        btnViewOPPO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, Product.class);
                i.putExtra("productionCompany","OPPO");
                startActivity(i);
            }
        });
        btnViewIPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, Product.class);
                i.putExtra("productionCompany","IPhone");
                startActivity(i);
            }
        });
        btnViewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, Product.class);
                startActivity(i);
            }
        });
        btnViewXiaomi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, Product.class);
                i.putExtra("productionCompany","Xiaomi");
                startActivity(i);
            }
        });
        btnViewSamSung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, Product.class);
                i.putExtra("productionCompany","SamSung");
                startActivity(i);
            }
        });
        btnViewOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, Product.class);
                i.putExtra("productionCompany","other");
                startActivity(i);
            }
        });
    }
    public void searchView(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent i = new Intent(Home.this, Product.class);
                i.putExtra("search",query);
                startActivity(i);
                searchView.setQuery("",false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void  showProduct(){
        GridLayoutManager layoutManagerXiaomi = new GridLayoutManager(this, 2,GridLayoutManager.HORIZONTAL,false);
        GridLayoutManager layoutManagerIPhone = new GridLayoutManager(this, 2,GridLayoutManager.HORIZONTAL,false);
        GridLayoutManager layoutManagerOPPO= new GridLayoutManager(this, 2,GridLayoutManager.HORIZONTAL,false);
        GridLayoutManager layoutManagerSamSung = new GridLayoutManager(this, 2,GridLayoutManager.HORIZONTAL,false);
        GridLayoutManager layoutManagerOther = new GridLayoutManager(this, 2,GridLayoutManager.HORIZONTAL,false);
        GridLayoutManager layoutManagerAll = new GridLayoutManager(this, 2,GridLayoutManager.HORIZONTAL,false);
        recyclerViewIphone.setLayoutManager(layoutManagerIPhone);
        recyclerViewOther.setLayoutManager(layoutManagerOther);
        recyclerViewXiaomi.setLayoutManager(layoutManagerXiaomi);
        recyclerViewOPPO.setLayoutManager(layoutManagerOPPO);
        recyclerViewSamSung.setLayoutManager(layoutManagerSamSung);

        recyclerViewAll.setLayoutManager(layoutManagerAll);
        dbProduct.collection("Product").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            FirebaseStorage storage = FirebaseStorage.getInstance("gs://goodphone-687e7.appspot.com/");
                            storageRef = storage.getReference().child("Product");

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                imageRef = storageRef.child(document.getId() +".jpg");
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
                                        String fileUrl = uri.toString();
                                        productionCompany = document.getString("ProductionCompany");
                                        if (productionCompany.equals("Xiaomi")){
                                            Log.e("Log", productionCompany);
                                            arrXiaomi.add(new List_Product(id,fileUrl,name,price,sold,sumRating));
                                            Product_adapter adapterXiaomi= new Product_adapter(Home.this, arrXiaomi);
                                            recyclerViewXiaomi.setAdapter(adapterXiaomi);
                                        }else if (productionCompany.equals("IPhone")) {
                                            arrIphone.add(new List_Product(id,fileUrl,name,price,sold,sumRating));
                                            Product_adapter adapterIphone= new Product_adapter(Home.this, arrIphone);
                                            recyclerViewIphone.setAdapter(adapterIphone);
                                        }else if (productionCompany.equals("OPP0")){
                                            Log.e("Log", productionCompany);
                                            arrOPPO.add(new List_Product(id,fileUrl,name,price,sold,sumRating));
                                            Product_adapter adapterOPPO= new Product_adapter(Home.this, arrOPPO);
                                            recyclerViewOPPO.setAdapter(adapterOPPO);
                                        }else if (productionCompany.equals("SamSung")){
                                            arrSamSung.add(new List_Product(id,fileUrl,name,price,sold,sumRating));
                                            Product_adapter adapterSamSung= new Product_adapter(Home.this, arrAllProduct);
                                            recyclerViewSamSung.setAdapter(adapterSamSung);
                                        }else {
                                            arrOther.add(new List_Product(id,fileUrl,name,price,sold,sumRating));
                                            Product_adapter adapterOther= new Product_adapter(Home.this, arrOther);
                                            recyclerViewOther.setAdapter(adapterOther);
                                        }
                                        arrAllProduct.add(new List_Product(id,fileUrl,name,price,sold,sumRating));
                                        Product_adapter adapter= new Product_adapter(Home.this, arrAllProduct);
                                        recyclerViewAll.setAdapter(adapter);
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
    public void fragmentHome(){
        Fragment bottomBar = new Navigation_Bar();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nevigation_bar, bottomBar);
        fragmentTransaction.commit();
    }

    public void init(){
        btnViewProduct = findViewById(R.id.btnViewAllProduct);
        btnViewXiaomi = findViewById(R.id.btnViewAllXiaomi);
        btnViewIPhone = findViewById(R.id.btnViewAllIPhone);
        btnViewOther = findViewById(R.id.btnViewAllOther);
        btnViewOPPO = findViewById(R.id.btnViewAllOPPO);
        btnViewSamSung = findViewById(R.id.btnViewAllSamSung);
        recyclerViewAll = findViewById(R.id.recycleView_home_All);
        recyclerViewOPPO = findViewById(R.id.recycleView_home_OPPO);
        recyclerViewSamSung = findViewById(R.id.recycleView_home_Samsung);
        recyclerViewIphone = findViewById(R.id.recycleView_home_Iphone);
        recyclerViewXiaomi = findViewById(R.id.recycleView_home_Xiaomi);
        recyclerViewOther = findViewById(R.id.recycleView_home_other);
        arrAllProduct = new ArrayList<>();
        arrXiaomi = new ArrayList<>();
        arrIphone = new ArrayList<>();
        arrOther= new ArrayList<>();
        arrSamSung = new ArrayList<>();
        arrOPPO = new ArrayList<>();
        btnLogin = findViewById(R.id.btn_Login);
        dbProduct = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        builder = new AlertDialog.Builder(Home.this);
        searchView = findViewById(R.id.search_view_home);

    }
}