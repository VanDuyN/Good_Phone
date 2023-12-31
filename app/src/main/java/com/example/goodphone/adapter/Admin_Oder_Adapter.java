package com.example.goodphone.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goodphone.R;
import com.example.goodphone.admin.Admin_Detail_Order;
import com.example.goodphone.fragment.Detail_Order;
import com.example.goodphone.model.List_Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Admin_Oder_Adapter extends RecyclerView.Adapter<Admin_Oder_Adapter.AdminOderVewHolder>{
    EditText edtConfirm;
    Context context;
    FirebaseFirestore db;
    Boolean user = true;
    List<List_Product> data = new ArrayList<>();
    Timestamp timestamp,timeNow;
    int quantity,pst;
    Button btnConfirm, btnRefuse;
    TextView tvTitleConfirm,tvDetailConfirm;
    Dialog dialog;
    Bundle bundle = new Bundle();
    String reason;
    Admin_Detail_Order adminDetailOrder = new Admin_Detail_Order();

    public void Admin_Oder_Adapter(Context context, List<List_Product> list){
        this.data = list;
        this.context = context;
    }
    @NonNull
    @Override
    public Admin_Oder_Adapter.AdminOderVewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Admin_Oder_Adapter.AdminOderVewHolder adminOderVewHolder = new Admin_Oder_Adapter.AdminOderVewHolder(LayoutInflater.from(context).inflate(R.layout.item_order_admin,parent,false));
        return adminOderVewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Admin_Oder_Adapter.AdminOderVewHolder holder, @SuppressLint("RecyclerView") int position) {
        Locale locale = new Locale("vi", "VN");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
        String formattedPrice = currencyFormat.format(data.get(position).price);
        user = data.get(position).isUser();
        timestamp = data.get(position).timestamp;

        holder.tvSumPrice.setText(formattedPrice);
        holder.tvIdOder.setText("Mã đơn hàng: "+data.get(position).id);
        holder.tvNameUser.setText(data.get(position).nameU);
        holder.tvStatus.setText("Trạng thái: "+data.get(position).status);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime dateTime = LocalDateTime.ofInstant(timestamp.toDate().toInstant(), ZoneId.systemDefault());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            holder.tvDateTimeBooking.setText("Ngày đặt: "+dateTime.format(formatter).toString());
        }
        quantity = 0;
        db = FirebaseFirestore.getInstance();
        if (user){
            db.collection("User")
                    .document(data.get(position).idUser)
                    .collection("Other")
                    .document(data.get(position).id)
                    .collection("OtherDetail")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                quantity = 0;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    quantity += 1;
                                }
                                holder.tvAmount.setText(String.valueOf(quantity));
                            } else {

                            }
                        }
                    });

        }else {
            db.collection("Other")
                    .document(data.get(position).id)
                    .collection("Detail")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                quantity = 0;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    quantity += 1;
                                }
                                holder.tvAmount.setText(String.valueOf(quantity));
                            } else {

                            }
                        }
                    });
        }
        if (data.get(position).status.equals("Thành công") || data.get(position).status.equals("Quản lý hủy")|| data.get(position).status.equals("Khách hàng hủy")){
            holder.btnConfirmA.setBackground(context.getDrawable(R.drawable.line_grey_light));
            holder.btnConfirmA.setTextColor(context.getResources().getColor(R.color.black));
            holder.btnCancel.setBackground(context.getDrawable(R.drawable.line_grey_light));
            holder.btnCancel.setTextColor(context.getResources().getColor(R.color.black));
        }else if (data.get(position).status.equals("Chờ xác nhận")){
            holder.btnConfirmA.setText("Xác nhận");
        } else if (data.get(position).status.equals("Chờ giao hàng")) {
            holder.btnConfirmA.setText("Giao hàng");
        }else if(data.get(position).status.equals("Đang giao hàng")){
            holder.btnConfirmA.setText("Xác nhận giao");
        }else {
            holder.btnConfirmA.setText("Xác nhận");
            holder.btnConfirmA.setBackground(context.getDrawable(R.drawable.line_grey_light));
            holder.btnConfirmA.setTextColor(context.getResources().getColor(R.color.black));
        }
        holder.btnConfirmA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(data.get(position).status.equals("Chờ xác nhận")){
                    if (user){
                        confirm(data.get(position).idUser,data.get(position).id);
                    }else {
                        confirmNoUser(data.get(position).id);
                    }
                    List_Product product =  data.get(position);
                    product.setStatus("Chờ giao hàng");
                    data.set(position,product);
                    notifyDataSetChanged();
                } else if (data.get(position).status.equals("Chờ giao hàng")) {
                    if (user){
                        shipping(data.get(position).idUser,data.get(position).id);
                    }else {
                        shippingNoUser(data.get(position).id);
                    }
                    List_Product product =  data.get(position);
                    product.setStatus("Đang giao hàng");
                    data.set(position,product);
                    notifyDataSetChanged();
                }else if (data.get(position).status.equals("Đang giao hàng")){
                    if (user){
                        shipping2(data.get(position).idUser,data.get(position).id);
                    }else {
                        shipping2NoUser(data.get(position).id);
                    }
                    List_Product product =  data.get(position);
                    product.setStatus("Dã giao cho khách");
                    data.set(position,product);
                    notifyDataSetChanged();
                }
            }
        });
        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.get(position).status.equals("Thành công") || data.get(position).status.equals("Quản lý hủy")|| data.get(position).status.equals("Khách hàng hủy") ){

                }else {
                    openDialog(data.get(position).idUser,data.get(position).id,data.get(position).isUser);
                    pst = position;

                }
            }
        });
        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.get(position).isUser == true){
                    bundle.putBoolean("isUser", data.get(position).isUser);
                    bundle.putString("idUser",data.get(position).idUser);
                    bundle.putString("idOder", data.get(position).id);
                    adminDetailOrder.setArguments(bundle);
                } else{
                    bundle.putString("idOder",data.get(position).id);
                    bundle.putBoolean("isUser",data.get(position).isUser);
                    adminDetailOrder.setArguments(bundle);
                }
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.adminOder,adminDetailOrder).addToBackStack(null).commit();
            }
        });
    }
    public void addQuantity(Double qt,String id){
        int qtt = qt != null ? qt.intValue() : 0;
        db.collection("Product")
                .document(id)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Double quantity = documentSnapshot.getDouble("Quantity");
                        DocumentReference reference1 = db.collection("Product").document(id);
                        reference1.update("Quantity",  quantity + qtt);
                    }
                });

    }
    public void addSold(Double qt,String id){
        int qtt = qt != null ? qt.intValue() : 0;
        db.collection("Product")
                .document(id)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Double sold = documentSnapshot.getDouble("Sold");
                        DocumentReference reference1 = db.collection("Product").document(id);
                        reference1.update("Sold", sold + qtt);
                    }
                });

    }
    public void openDialog(String idU,String idO, boolean isUser){
        dialog =  new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_comfirm);
        Window window = dialog.getWindow();
        if(window == null ){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);
        tvDetailConfirm = dialog.findViewById(R.id.tv_Detail_Confirm);
        tvTitleConfirm = dialog.findViewById(R.id.tv_Title_Confirm);
        btnConfirm = dialog.findViewById(R.id.btn_Confirm);
        btnRefuse = dialog.findViewById(R.id.btn_Refuse);
        edtConfirm = dialog.findViewById(R.id.edt_Confirm);
        edtConfirm.setVisibility(View.VISIBLE);
        tvTitleConfirm.setText("Xác nhận hủy");
        btnConfirm.setText("Hủy");
        tvDetailConfirm.setText("Bạn có chắc là muốn hủy đơn hàng này không ?");
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reason = edtConfirm.getText().toString();
                if(!reason.isEmpty() && reason.length() >= 10){
                    dialog.dismiss();
                    if(isUser){
                        cancelOther(idU, idO,reason);
                        returnQuantity(idU,idO);
                    }else {
                        cancelOtherNoUser(idO,reason);
                        returnQuantityNoUser(idO);
                    }
                    data.remove(pst);
                    notifyDataSetChanged();
                }
                if (reason.length() < 10){
                    Toast.makeText(context, "Nhập trên 10 ký tự", Toast.LENGTH_SHORT).show();
                }


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
    public void returnQuantityNoUser(String idO){
        db.collection("Other")
                .document(idO)
                .collection("OtherDetail")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                addQuantity(document.getDouble("quantity"), document.getString("idP"));
                            }
                        } else {

                        }
                    }
                });
    }
    public void returnQuantity(String idU,String idO){
        db.collection("User")
                .document(idU)
                .collection("Other")
                .document(idO)
                .collection("OtherDetail")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                addQuantity(document.getDouble("quantity"), document.getString("idP"));
                            }
                        } else {

                        }
                    }
                });
    }
    public void cancelOther(String idU,String idO,String reason){
        DocumentReference reference = db.collection("User").document(idU).collection("Other").document(idO);
        reference.update("status","Quản lý hủy");
        reference.update("reason",reason);
    }
    public void cancelOtherNoUser(String idO, String reason){
        DocumentReference reference = db.collection("Other").document(idO);
        reference.update("status","Quản lý hủy");
        reference.update("reason",reason);
    }
    public void shipping2(String idU,String idO){
        DocumentReference reference = db.collection("User").document(idU).collection("Other").document(idO);
        reference.update("status","Đã giao cho khách");
        db.collection("User")
                .document(idU)
                .collection("Other")
                .document(idO)
                .collection("OtherDetail")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                addSold(document.getDouble("quantity"), document.getString("idP"));
                            }
                        } else {

                        }
                    }
                });

    }
    public void shipping2NoUser(String idO){
        timeNow = Timestamp.now();
        DocumentReference reference = db.collection("Other").document(idO);
        reference.update("status","Thành công");
        reference.update("receivedDate",timeNow);
        db.collection("Other")
                .document(idO)
                .collection("OtherDetail")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                addSold(document.getDouble("quantity"), document.getString("idP"));
                            }
                        } else {

                        }
                    }
                });
    }
    public void shipping(String idU,String idO){
        DocumentReference reference = db.collection("User").document(idU).collection("Other").document(idO);
        reference.update("status","Đang giao hàng");
    }
    public void shippingNoUser(String idO){
        DocumentReference reference = db.collection("Other").document(idO);
        reference.update("status","Đang giao hàng");
    }
    public void confirm(String idU,String idO){
        timeNow = Timestamp.now();
        DocumentReference reference = db.collection("User").document(idU).collection("Other").document(idO);
        reference.update("status","Chờ giao hàng");
        reference.update("confirmationDate", timeNow);
    }
    public void confirmNoUser(String idO){
        timeNow = Timestamp.now();
        DocumentReference reference = db.collection("Other").document(idO);
        reference.update("status","Chờ giao hàng");
        reference.update("confirmationDate", timeNow);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    public class AdminOderVewHolder extends RecyclerView.ViewHolder {
        Button btnConfirmA, btnCancel, btnDetail;
        TextView tvNameUser, tvIdOder , tvDateTimeBooking, tvStatus,tvSumPrice,tvAmount;
        public AdminOderVewHolder(@NonNull View itemView) {
            super(itemView);
            btnCancel = itemView.findViewById(R.id.btn_cancel_admin);
            btnDetail= itemView.findViewById(R.id.btn_Detail_Oder);
            btnConfirmA = itemView.findViewById(R.id.btn_Confirm_Admin);
            tvStatus = itemView.findViewById(R.id.tv_Status_Admin_Oder);
            tvIdOder = itemView.findViewById(R.id.tv_Id_Admin_Oder);
            tvNameUser = itemView.findViewById(R.id.tv_name_user);
            tvDateTimeBooking = itemView.findViewById(R.id.tv_DateTime_Admin_Order);
            tvSumPrice = itemView.findViewById(R.id.tv_total_price_item_admin_order);
            tvAmount = itemView.findViewById(R.id.tv_amount_product_item_order);
        }
    }
}
