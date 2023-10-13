package com.example.goodphone;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.goodphone.adapter.SanPhamAdapter;
import com.example.goodphone.fragment.Add_Product;
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
import java.util.List;

public class QLSanPhamActivity extends AppCompatActivity{
    private RecyclerView rcvSanPham;
    private SanPhamAdapter adapter;
    private List<List_Product> mListSanPham;
    private RelativeLayout rootView;
    StorageReference storageRef, imageRef;
    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseFirestore dbProduct;
    ImageButton btnExit;
    Button btnAddProduct;
    Add_Product add_product;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlsanpham);
        init();
        setClick();
        getListSanPham();

    }
    public void setClick(){
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDataToFragment();
            }
        });

    }
    public void sendDataToFragment(){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.root_view,new Add_Product());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void getListSanPham(){
        List<List_Product> arrProduct= new ArrayList<>();
        dbProduct.collection("Product").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                String name = document.getString("Name".trim());
                                FirebaseStorage storage = FirebaseStorage.getInstance("gs://goodphone-687e7.appspot.com/");
                                storageRef = storage.getReference().child("Product");
                                imageRef = storageRef.child(name +".jpg");
                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String fileUrl = uri.toString();
                                        arrProduct.add(new List_Product(id,fileUrl,name));
                                        adapter = new SanPhamAdapter(QLSanPhamActivity.this,arrProduct);
                                        rcvSanPham.setAdapter(adapter);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {

                                    }
                                });
                            }
                        } else {
                            Toast.makeText(QLSanPhamActivity.this, "lỗi",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void init(){
        auth = FirebaseAuth.getInstance();
        user  = auth.getCurrentUser();
        dbProduct = FirebaseFirestore.getInstance();
        btnAddProduct = findViewById(R.id.btn_Add_Product);
        rootView= findViewById(R.id.root_view);
        rcvSanPham = findViewById(R.id.rcv_sanpham);
        add_product = new Add_Product();
        btnExit =  findViewById(R.id.btn_Exit_QLSP);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        rcvSanPham.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        rcvSanPham.addItemDecoration(itemDecoration);


    }


}