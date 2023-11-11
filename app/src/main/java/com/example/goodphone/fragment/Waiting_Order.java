package com.example.goodphone.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.example.goodphone.R;
import com.example.goodphone.adapter.Oder_Adapter;
import com.example.goodphone.model.List_Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Waiting_Order extends Fragment {
    View view;
    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseStorage storage;
    DocumentReference reference;
    RecyclerView recyclerView;
    Timestamp timestamp;
    Oder_Adapter adapter;
    String idOder,status;
    Double getSumPrice;
    int sumPrice = 0;
    List<List_Product> arrProduct;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_waiting__order, container, false);
        init();
        getDataDB();
        return view;
    }
    public void getDataDB(){
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(layoutManager);
        CollectionReference collectionRe = db.collection("User").document(user.getUid()).collection("Other");
        collectionRe.orderBy("bookingDate", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        idOder = document.getId();
                        timestamp = document.getTimestamp("bookingDate");
                        getSumPrice = document.getDouble("sumPrice");
                        status = document.getString("status");
                        sumPrice = getSumPrice != null ? getSumPrice.intValue() : 0;
                        if (!status.equals("Thành công") && !status.equals("Quản lý hủy") && !status.equals("Khách hàng hủy")){
                            arrProduct.add(new List_Product(idOder,timestamp,sumPrice,status));
                            adapter.Oder_Adapter(arrProduct,getContext());
                            recyclerView.setAdapter(adapter);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("12345","That bai");
                    }
                });


    }

    public void init(){
        recyclerView = view.findViewById(R.id.recycleViewWaiting);
        auth = FirebaseAuth.getInstance();
        arrProduct = new ArrayList<>();
        user = auth.getCurrentUser();
        adapter = new Oder_Adapter();
        db = FirebaseFirestore.getInstance();
    }
}