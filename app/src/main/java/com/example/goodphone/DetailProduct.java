package com.example.goodphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.goodphone.adapter.Product_adapter;
import com.example.goodphone.dialog.Dialog_Quantity_Product;
import com.example.goodphone.dialog.Dialog_Specifications;
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

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class DetailProduct extends AppCompatActivity {
    TextView tvPriceDetail, tvNameProductDetail, btnBuyNow, tvProductInformation,tvScreenSize,tvScreenTechnology,tvRearCamera,tvFontCamera,tvRom,tvChipset,tvScreenFeature,btnDetail,tvSold;
    ImageView btnReturn, imgMain,btnAddCart;
    String idUser,idProduct,nameProduct,screenTechnology,rearCamera,frontCamera,rom,chipset,screenFeature,screenSize;
    Double  sumRating;
    int price;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    FirebaseFirestore dbProduct = FirebaseFirestore.getInstance();
    StorageReference storageRef, imageRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_product);
        getPutExtra();
        init();
        button();
        loadingData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadingData();
    }
    public void getPutExtra(){
        idProduct = getIntent().getStringExtra("id");

        Log.e("get",idProduct);

    }
    public void init(){
        btnReturn = findViewById(R.id.img_return);
        imgMain = findViewById(R.id.img_main);
        tvPriceDetail= findViewById(R.id.priceDetail);
        tvNameProductDetail = findViewById(R.id.name_Product_Detail);
        btnBuyNow = findViewById(R.id.btn_Buy_Now);
        tvProductInformation= findViewById(R.id.tv_Product_Information);
        tvScreenSize= findViewById(R.id.tv_Screen_Size);
        tvScreenTechnology= findViewById(R.id.tv_Screen_Technology);
        tvRearCamera= findViewById(R.id.tv_Rear_Camera);
        tvFontCamera= findViewById(R.id.tv_Front_Camera);
        tvRom= findViewById(R.id.tv_Rom);
        tvChipset= findViewById(R.id.tv_Chipset);
        tvScreenFeature= findViewById(R.id.tv_Screen_Feature);
        btnDetail = findViewById(R.id.tv_Detail);
        btnAddCart = findViewById(R.id.btn_Add_Cart);
        dbProduct = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

    }
    public void button(){
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogQuantity();

            }
        });
        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetailProduct.this, "Tính năng đang phát triển",Toast.LENGTH_SHORT).show();
            }
        });
        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogSpecifications();
            }
        });

    }
    public void getData(){
        if (user!=null){
            idUser = user.getUid();
        }
        Locale locale = new Locale("vi", "VN");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
        String formattedNumber = currencyFormat.format(price);
        tvNameProductDetail.setText(nameProduct);
        tvPriceDetail.setText(formattedNumber);
        tvScreenSize.setText(screenSize.toString());
        tvChipset.setText(chipset);
        tvFontCamera.setText(frontCamera);
        tvRearCamera.setText(rearCamera);
        tvRom.setText(rom );
        tvScreenTechnology.setText(screenTechnology);
        tvScreenFeature.setText(screenFeature);
    }
    public void openDialogSpecifications(){
        Dialog_Specifications dialog_specifications = new Dialog_Specifications(DetailProduct.this, idProduct);
        dialog_specifications.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(DetailProduct.this, android.R.color.transparent)));
        dialog_specifications.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog_specifications.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog_specifications.show();
        dialog_specifications.getWindow().setAttributes(lp);

    }
    public  void openDialogQuantity(){
        final Dialog_Quantity_Product dialogQuantityProduct = new Dialog_Quantity_Product(DetailProduct.this, idProduct,idUser);
        dialogQuantityProduct.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(DetailProduct.this, android.R.color.transparent)));
        dialogQuantityProduct.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogQuantityProduct.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        dialogQuantityProduct.show();
        dialogQuantityProduct.getWindow().setAttributes(lp);
    }

    public void loadingData(){
        dbProduct.collection("Product")
                .document(idProduct)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // lay du lieu tu database
                                nameProduct = document.getString("Name".trim());
                                Double getPrice = document.getDouble("Price");
                                price = getPrice != null ? getPrice.intValue() : 0;
                                sumRating = document.getDouble("SumRating");
                                screenSize = document.getString("ScreenSize");
                                screenFeature = document.getString("ScreenFeature");
                                screenTechnology = document.getString("ScreenTechnology");
                                rearCamera  = document.getString("RearCamera");
                                frontCamera  = document.getString("FrontCamera");
                                rom  = document.getString("Rom");
                                chipset  = document.getString("Chipset");
                                getData();

                                FirebaseStorage storage = FirebaseStorage.getInstance("gs://goodphone-687e7.appspot.com/");
                                storageRef = storage.getReference().child("Product");
                                imageRef = storageRef.child(idProduct +".jpg");

                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String fileUrl = uri.toString();
                                        Glide.with(DetailProduct.this).load(fileUrl).fitCenter().into(imgMain);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {

                                    }
                                });

                            } else {
                                Toast.makeText(DetailProduct.this, "Lỗi lấy dữ liệu",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(DetailProduct.this, "Lỗi lấy dữ liệu",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void addCart(){
    }


}