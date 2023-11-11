package com.example.goodphone.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.goodphone.R;
import com.example.goodphone.adapter.Admin_Oder_Adapter;
import com.example.goodphone.model.List_Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Admin_Cancel_Order extends Fragment {
    FirebaseFirestore db,dbOther;
    RecyclerView recyclerView, recyclerViewNoUser;
    Timestamp timestamp;
    Admin_Oder_Adapter adapter,adapterNoUser;
    String idOder,status, nameU;
    Double getSumPrice;
    int sumPrice = 0;
    List<List_Product> arrProduct,arrProductNoUser;

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_admin__cancel__order, container, false);
        init();
        getDataDB();
        getOderNoUserDB();
        return view;
    }
    public void getDataDB(){
        db.collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document :task.getResult()){
                            getOderDB( document.getId(),document.getString("lastName")+" "+document.getString("firstName"));
                        }
                    }
                });

    }
    public void getOderNoUserDB(){
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),1);
        recyclerViewNoUser.setLayoutManager(layoutManager);
        CollectionReference collectionRe = dbOther.collection("Other");
        collectionRe.orderBy("bookingDate", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener( queryDocumentSnapshots -> {
                    for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                        idOder = document.getId();
                        timestamp = document.getTimestamp("bookingDate");
                        nameU = document.getString("Name");
                        getSumPrice = document.getDouble("sumPrice");
                        status = document.getString("status");
                        boolean isUser = false;
                        sumPrice = getSumPrice != null ? getSumPrice.intValue() : 0;
                        if (status.equals("Quản lý hủy") || status.equals("Khách hàng hủy")){
                            arrProductNoUser.add(new List_Product(idOder,nameU,timestamp,sumPrice,status,isUser));
                            adapterNoUser.Admin_Oder_Adapter(getContext(), arrProductNoUser);
                            recyclerViewNoUser.setAdapter(adapterNoUser);
                        }
                    }
                });
    }
    public void getOderDB(String id,String name){
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(layoutManager);
        CollectionReference collectionRe = dbOther.collection("User").document(id).collection("Other");
        collectionRe.orderBy("bookingDate", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener( queryDocumentSnapshots -> {
                    for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                        idOder = document.getId();
                        timestamp = document.getTimestamp("bookingDate");
                        getSumPrice = document.getDouble("sumPrice");
                        status = document.getString("status");
                        sumPrice = getSumPrice != null ? getSumPrice.intValue() : 0;
                        if (status.equals("Quản lý hủy") || status.equals("Khách hàng hủy")){
                            arrProduct.add(new List_Product(idOder,id,name,timestamp,sumPrice,status,true));
                            adapter.Admin_Oder_Adapter(getContext(), arrProduct);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                });
    }
    public void  init(){
        arrProduct = new ArrayList<>();
        recyclerViewNoUser = view.findViewById(R.id.recycleView_oder_no_user);
        recyclerView = view.findViewById(R.id.recycleView_oder_user);
        db = FirebaseFirestore.getInstance();
        adapter = new Admin_Oder_Adapter();
        adapterNoUser = new Admin_Oder_Adapter();
        arrProductNoUser = new ArrayList<>();
        dbOther = FirebaseFirestore.getInstance();
    }
}