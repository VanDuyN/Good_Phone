package com.example.goodphone.fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goodphone.Payment;
import com.example.goodphone.R;
import com.example.goodphone.adapter.Payment_Adapter;
import com.example.goodphone.model.List_Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Document;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Detail_Order extends Fragment {
    FirebaseAuth auth;
    FirebaseUser user;
    String idOder;
    FirebaseFirestore db;
    Button btnBack, btnRating;
    StorageReference storageRef, imgRef;
    FirebaseStorage img;
    TextView tvStatus, tvBookingDate, tvConfirmationDate,tvReceivedDate,tvName,tvSdt,tvAddress, tvSumPrice,tvPriceShipping,tvSumMoney,tvReason;
    RecyclerView recyclerView;
    Timestamp bookingDate,confirmationDate, receivedDate;
    View view;
    int sumPrice,priceShip,sumMoney;
    String status,url;
    Payment_Adapter adapter ;
    List<List_Product> arrProducts = new ArrayList<>();
    RelativeLayout relativeLayoutReason;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail__order, container, false);
        init();
        getOderDetail();
        button();
        return view;
    }
    public void button(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.remove(Detail_Order.this);
                transaction.commit();
            }
        });
    }
    public void getOderDetail(){
        arrProducts.clear();
        db.collection("User")
                .document(user.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        tvName.setText(documentSnapshot.getString("lastName")+ " " + documentSnapshot.getString("firstName"));
                        tvSdt.setText(documentSnapshot.getString("phoneNumber"));
                    }
                });

        db.collection("User")
                .document(user.getUid())
                .collection("Other")
                .document(idOder)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        priceShip = 30000;
                        sumMoney = documentSnapshot.getDouble("sumPrice").intValue();
                        sumPrice = documentSnapshot.getDouble("sumPrice").intValue() - priceShip;

                        Locale locale = new Locale("vi", "VN");
                        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);

                        String formattedPrice = currencyFormat.format(sumPrice);
                        String formattedPriceShipping = currencyFormat.format(priceShip);
                        String formattedSumMoney = currencyFormat.format(sumMoney);

                        tvSumPrice.setText(formattedPrice);
                        tvPriceShipping.setText(formattedPriceShipping);
                        tvSumMoney.setText(formattedSumMoney);

                        status = documentSnapshot.getString("status");
                        bookingDate = documentSnapshot.getTimestamp("bookingDate");
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            LocalDateTime dateTime = LocalDateTime.ofInstant(bookingDate.toDate().toInstant(), ZoneId.systemDefault());
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            tvBookingDate.setText(dateTime.format(formatter).toString());
                        }
                        if (!status.equals("Chờ xác nhận") && !status.equals("Quản lý hủy") && !status.equals("Khách hàng hủy")){
                            confirmationDate = documentSnapshot.getTimestamp("confirmationDate");
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                LocalDateTime dateTime = LocalDateTime.ofInstant(confirmationDate.toDate().toInstant(), ZoneId.systemDefault());
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                tvConfirmationDate.setText(dateTime.format(formatter).toString());
                            }
                        }
                        if (status.equals("Thành công")){
                            receivedDate = documentSnapshot.getTimestamp("receivedDate");
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                LocalDateTime dateTime = LocalDateTime.ofInstant(receivedDate.toDate().toInstant(), ZoneId.systemDefault());
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                tvReceivedDate.setText(dateTime.format(formatter).toString());
                            }
                        }else {
                            btnRating.setVisibility(View.GONE);
                        }
                        tvAddress.setText(documentSnapshot.getString("Address"));
                        tvStatus.setText(documentSnapshot.getString("status"));
                        if (status.equals("Quản lý hủy") || status.equals("Khách hàng hủy")){
                            relativeLayoutReason.setVisibility(View.VISIBLE);
                            tvReason.setText(documentSnapshot.getString("reason"));
                        }
                    }
                });
        db.collection("User")
                .document(user.getUid())
                .collection("Other")
                .document(idOder)
                .collection("OtherDetail")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                getProductDB(document.getString("idP"),document.getDouble("quantity").intValue(),document.getDouble("price").intValue());
                            }
                        } else {

                        }
                    }
                });
    }
    public void getProductDB(String id, int quantity,int price){
        db.collection("Product")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            ;
                            getImg(id,quantity,price,document.getString("Name"));
                        }
                    }
                });
    }
    public void getImg(String id,int quantity,int Price, String name){
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        imgRef = storageRef.child("Product/"+id +".jpg");
        imgRef.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        url = uri.toString();
                        arrProducts.add(new List_Product(id, quantity,Price,name,url));
                        adapter= new Payment_Adapter(getContext(), arrProducts);
                        recyclerView.setAdapter(adapter);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });
    }
    public void init(){
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        img = FirebaseStorage.getInstance();
        storageRef = img.getReference();

        idOder = getArguments().getString("idOder");

        relativeLayoutReason = view.findViewById(R.id.relativeLayout_Reason);
        btnBack = view.findViewById(R.id.btnExit);
        tvReason = view.findViewById(R.id.tv_Reason);
        btnRating = view.findViewById(R.id.btnRating);
        tvStatus = view.findViewById(R.id.tvStatus);
        tvBookingDate =view.findViewById(R.id.tvBookingDate);
        tvConfirmationDate = view.findViewById(R.id.tvConfirmationDate);
        tvReceivedDate = view.findViewById(R.id.tvReceivedDate);
        tvName = view.findViewById(R.id.tvNameUser);
        tvSdt = view.findViewById(R.id.tvPhoneNumberUser);
        tvAddress = view.findViewById(R.id.tvAddressUser);
        tvSumPrice = view.findViewById(R.id.tvSumPrice);
        tvPriceShipping= view.findViewById(R.id.tvPriceShipping);
        tvSumMoney = view.findViewById(R.id.tvSumMoney);
        recyclerView = view.findViewById(R.id.list_product);
        db = FirebaseFirestore.getInstance();
    }
}