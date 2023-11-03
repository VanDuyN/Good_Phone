package com.example.goodphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.ViewUtils;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goodphone.adapter.Product_adapter;
import com.example.goodphone.adapter.Search_Adapter;
import com.example.goodphone.model.List_Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Product extends AppCompatActivity {
    Button btnExit;

    FirebaseFirestore DBProduct;
    StorageReference storageRef, imageRef;


    RecyclerView recyclerView;
    SearchView searchView;
    TextView textView;
    String search, productionCompany,id ,name,getProductionCompany;
    int sold,price;

    Double sumRating;
    Search_Adapter adapter;
    List<List_Product> arrProductAll,arrProduct,arrXiaomi,arrIphone,arrOther,arrSamSung,arrOPPO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product);
        init();
        checkPutExtra();
        getDataDB();
        button();
        searchView();
        startCountdownTimer();
    }
    public void checkSearchView(){
        if (search != null){
            filterList(search);
        }
    }
    private void startCountdownTimer() {
        CountDownTimer countDownTimer = new CountDownTimer(1 * 500, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                checkSearchView();
                recyclerView.setAdapter(adapter);
            }
        };

        countDownTimer.start();
    }
    public void checkPutExtra(){
        Intent i = getIntent();
        search = i.getStringExtra("search");
        getProductionCompany = i.getStringExtra("productionCompany");
        if (productionCompany != null){
            if (search == null)
             {
                if (productionCompany.equals("Xiaomi")) {
                    textView.setText("Hãng điện thoại Xiaomi");
                } else if (productionCompany.equals("IPhone")) {
                    textView.setText("Hãng điện thoại Iphone");
                } else if (productionCompany.equals("OPPO")) {
                    textView.setText("Hãng điện thoại OPPO");
                }else if (productionCompany.equals("SamSung")) {
                    textView.setText("Hãng điện thoại SamSung");
                }else if (productionCompany.equals("other")) {
                    textView.setText("Một số điện thoại có hãng khác");
                }else {
                    textView.setText("Tất cả điện thoại");
                }
            }
        }else {
            if (search != null)
            {
                textView.setText("Tìm kiếm điện thoại '" +search+"'");
            }
        }
    }
    public void getDataDB(){
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        DBProduct.collection("Product").get()
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
                                        if (getProductionCompany == null){
                                            arrProductAll.add(new List_Product(id,fileUrl,name,price,sold,sumRating));
                                            adapter= new Search_Adapter(Product.this, arrProductAll);
                                        }else if (getProductionCompany.equals(productionCompany) && productionCompany.equals("Xiaomi")) {
                                            arrXiaomi.add(new List_Product(id,fileUrl,name,price,sold,sumRating));
                                            adapter= new Search_Adapter(Product.this, arrXiaomi);
                                        } else if (getProductionCompany.equals(productionCompany) &&productionCompany.equals("OPPO")) {
                                            arrOPPO.add(new List_Product(id,fileUrl,name,price,sold,sumRating));
                                            adapter= new Search_Adapter(Product.this, arrOPPO);
                                        } else if (getProductionCompany.equals(productionCompany) && productionCompany.equals("IPhone")) {
                                            arrIphone.add(new List_Product(id,fileUrl,name,price,sold,sumRating));
                                            adapter= new Search_Adapter(Product.this, arrIphone);

                                        }else if (getProductionCompany.equals(productionCompany) && productionCompany.equals("SamSung")) {
                                            arrSamSung.add(new List_Product(id,fileUrl,name,price,sold,sumRating));
                                            adapter= new Search_Adapter(Product.this, arrSamSung);

                                        }else if (getProductionCompany.equals("other") && !productionCompany.equals("Xiaomi") && !productionCompany.equals("IPhone")&& !productionCompany.equals("SamSung") && !productionCompany.equals("OPPO")){
                                            arrOther.add(new List_Product(id,fileUrl,name,price,sold,sumRating));
                                            adapter= new Search_Adapter(Product.this, arrOther);
                                        }
                                        if (search != null){

                                        }else {
                                            recyclerView.setAdapter(adapter);
                                        }


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        Toast.makeText(Product.this, "anh lỗi",Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }

                        } else {
                            Toast.makeText(Product.this, "lỗi",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void button(){
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void searchView(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search = query;
                if(getProductionCompany == null){
                    textView.setText("Tìm kiếm ' " +search+"'" );
                }else if (getProductionCompany.equals("Xiaomi")) {
                    textView.setText("Tìm kiếm ' " +search+"'" + " trong hãng " + productionCompany);
                } else if (getProductionCompany.equals("IPhone")) {
                    textView.setText("Tìm kiếm ' " +search+"'" + " trong hãng " + productionCompany);
                } else if (getProductionCompany.equals("OPPO")) {
                    textView.setText("Tìm kiếm ' " +search+"'" + " trong hãng " + productionCompany);
                }else if (getProductionCompany.equals("SamSung")) {
                    textView.setText("Tìm kiếm ' " +search+"'" + " trong hãng " + productionCompany);
                }else if (getProductionCompany.equals("other")) {
                    textView.setText("Tìm kiếm ' " +search+"'" +" trong hãng khác");
                }else {
                    textView.setText("Tìm kiếm ' " +search+"'" );
                }
                filterList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search = newText;
                if(getProductionCompany == null){
                    textView.setText("Tìm kiếm ' " +search+"'" );
                }else if (getProductionCompany.equals("Xiaomi")) {
                    textView.setText("Tìm kiếm ' " +search+"'" + " trong hãng " + getProductionCompany);
                } else if (getProductionCompany.equals("IPhone")) {
                    textView.setText("Tìm kiếm ' " +search+"'" + " trong hãng " + getProductionCompany);
                } else if (getProductionCompany.equals("OPPO")) {
                    textView.setText("Tìm kiếm ' " +search+"'" + " trong hãng " + getProductionCompany);
                }else if (getProductionCompany.equals("SamSung")) {
                    textView.setText("Tìm kiếm ' " +search+"'" + " trong hãng " + getProductionCompany);
                }else if (getProductionCompany.equals("other")) {
                    textView.setText("Tìm kiếm ' " +search+"'" +" trong hãng khác");
                }else {
                    textView.setText("Tìm kiếm ' " +search+"'" );
                }
                filterList(newText);
                return true;
            }
        });
    }
    public void checkSearch(){

        if (getProductionCompany == null){
            for (List_Product listProduct : arrProductAll){
                if (arrProduct.size() < arrProductAll.size()){
                    arrProduct.add(listProduct);
                }
            }
        }else if (getProductionCompany.equals(productionCompany) && productionCompany.equals("Xiaomi")) {
            for (List_Product listProduct : arrXiaomi){
                if (arrProduct.size() < arrXiaomi.size()){
                    arrProduct.add(listProduct);
                }
            }
        } else if (getProductionCompany.equals(productionCompany) &&productionCompany.equals("OPPO")) {
            for (List_Product listProduct : arrOPPO){
                if (arrProduct.size() < arrOPPO.size()){
                    arrProduct.add(listProduct);
                }
            }
        } else if (getProductionCompany.equals(productionCompany) && productionCompany.equals("IPhone")) {
            for (List_Product listProduct : arrIphone){
                if (arrProduct.size() < arrIphone.size()){
                    arrProduct.add(listProduct);
                }
            }
        }else if (getProductionCompany.equals(productionCompany) && productionCompany.equals("SamSung")) {
            for (List_Product listProduct : arrSamSung){
                if (arrProduct.size() < arrSamSung.size()){
                    arrProduct.add(listProduct);
                }
            }
        }else if (getProductionCompany.equals("other") && !productionCompany.equals("Xiaomi") && !productionCompany.equals("IPhone")&& !productionCompany.equals("SamSung") && !productionCompany.equals("OPPO")){
            for (List_Product listProduct : arrOther){
                if (arrProduct.size() < arrOther.size()){
                    arrProduct.add(listProduct);
                }
            }
        }
    }
    public void filterList(String txt){
        checkSearch();
        List<List_Product> filterList = new ArrayList<>();
        for (List_Product listProduct : arrProduct){
            if(listProduct.getNameProduct().toLowerCase().contains(txt.toLowerCase())){
                filterList.add(listProduct);
            }
        }
        if (filterList.isEmpty()){

        }else {
            adapter.setFilterAdapter(filterList);
        }
    }
    public void init(){
        arrProductAll = new ArrayList<>();
        arrSamSung= new ArrayList<>();
        arrIphone= new ArrayList<>();
        arrXiaomi = new ArrayList<>();
        arrOPPO = new ArrayList<>();
        arrOther= new ArrayList<>();
        arrProduct= new ArrayList<>();
        btnExit = findViewById(R.id.btnExitProduct);
        recyclerView = findViewById(R.id.recycleView_product);
        searchView = findViewById(R.id.search_view_product);
        textView = findViewById(R.id.tvText);
        DBProduct = FirebaseFirestore.getInstance();
    }
}