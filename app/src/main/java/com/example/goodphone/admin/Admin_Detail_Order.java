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


public class Admin_Detail_Order extends Fragment {
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
        view =  inflater.inflate(R.layout.fragment_admin__detail__order, container, false);
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

    }
    public void getOderDB(String id,String name){

    }
    public void  init(){
    }
}