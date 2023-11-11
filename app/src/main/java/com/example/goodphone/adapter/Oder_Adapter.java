package com.example.goodphone.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goodphone.DetailProduct;
import com.example.goodphone.R;
import com.example.goodphone.fragment.Detail_Order;
import com.example.goodphone.fragment.Navigation_Bar;
import com.example.goodphone.model.List_Product;
import com.example.goodphone.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Oder_Adapter extends RecyclerView.Adapter<Oder_Adapter.OderViewHolder> {
    private List<List_Product> data  = new ArrayList<>();
    FirebaseFirestore db;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    Context context;
    Double getQuantity;
    Timestamp timestamp;
    int quantity, pst;
    Button btnConfirm, btnRefuse;
    TextView tvTitleConfirm,tvDetailConfirm;
    AlertDialog.Builder builder;
    Dialog dialog;
    DocumentReference reference;
    boolean isFirstEven = true;
    Bundle bundle ;
    Detail_Order detailOrder = new Detail_Order();
    public void Oder_Adapter (List<List_Product> data, Context context){
        this.context = context;
        this.data = data;
    }
    @NonNull
    @Override
    public OderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Oder_Adapter.OderViewHolder oderViewHolder = new Oder_Adapter.OderViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order,parent,false));
        return oderViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OderViewHolder holder, int position) {

        Locale locale = new Locale("vi", "VN");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
        String formattedPrice = currencyFormat.format(data.get(position).price);
        timestamp = data.get(position).timestamp;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime dateTime = LocalDateTime.ofInstant(timestamp.toDate().toInstant(), ZoneId.systemDefault());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            holder.tvDateTime.setText("Ngày đặt: "+dateTime.format(formatter).toString());
        }
        quantity = 0;
        db = FirebaseFirestore.getInstance();
        db.collection("User")
                .document(user.getUid())
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

        holder.btnConfirm.setText("Xác nhận");
        holder.tvID.setText("Mã đơn hàng: "+data.get(position).id);
        holder.tvPrice.setText(formattedPrice);
        holder.tvStatus.setText("Trạng thái: "+data.get(position).status);
        if (data.get(position).status.equals("Thành công")){
            holder.btnCancel.setBackground(context.getDrawable(R.drawable.line_grey_light));
            holder.btnCancel.setTextColor(context.getResources().getColor(R.color.black));
            holder.btnConfirm.setBackground(context.getDrawable(R.drawable.line_grey_light));
            holder.btnConfirm.setTextColor(context.getResources().getColor(R.color.black));
        }else if (data.get(position).status.equals("Đã giao cho khách")){
            holder.btnCancel.setBackground(context.getDrawable(R.drawable.line_grey_light));
            holder.btnCancel.setTextColor(context.getResources().getColor(R.color.black));

        }else if (data.get(position).status.equals("Quản lý hủy") || data.get(position).status.equals("Khách hàng hủy")){
            holder.btnCancel.setBackground(context.getDrawable(R.drawable.line_grey_light));
            holder.btnCancel.setTextColor(context.getResources().getColor(R.color.black));
        }else  {
            holder.btnConfirm.setBackground(context.getDrawable(R.drawable.line_grey_light));
            holder.btnConfirm.setTextColor(context.getResources().getColor(R.color.black));
        }
        reference = db.collection("User").document(user.getUid()).collection("Other").document(data.get(position).id);
        holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.get(position).status.equals("Đã giao cho khách")){
                    confirm(data.get(position).id);
                    pst = position;
                    db = FirebaseFirestore.getInstance();
                    db.collection("User")
                            .document(user.getUid())
                            .collection("Other")
                            .document(data.get(position).id)
                            .collection("OtherDetail")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            deleteQuantity(document.getDouble("quantity"), document.getString("idP"));
                                        }
                                    } else {

                                    }
                                }
                            });
                }else {

                }
            }
        });
        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = data.get(position).id;
                bundle.putString("idOder",id);
                detailOrder.setArguments(bundle);
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.userOder,detailOrder).addToBackStack(null).commit();

            }
        });
        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.get(position).status.equals("Đã giao cho khách") || (data.get(position).status.equals("Thành công"))){

                }else {
                    openDialog(data.get(position).id);
                    pst = position;
                }

            }
        });

    }

    public void openDialog(String id){
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
        tvTitleConfirm.setText("Xác nhận hủy");
        btnConfirm.setText("Hủy");
        tvDetailConfirm.setText("Bạn có chắc là muốn hủy đơn hàng này không ?");
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                cancelOther(id);
                data.remove(pst);

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
    public void confirm(String id){
        DocumentReference reference = db.collection("User").document(user.getUid()).collection("Other").document(id);
        reference.update("status","Thành công");
    }
    public void deleteQuantity(Double qt,String id){
        int qtt = qt != null ? qt.intValue() : 0;
        db.collection("Product")
                .document(id)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Double getQtt = documentSnapshot.getDouble("Quantity");
                        Double sold = documentSnapshot.getDouble("Sold");
                        DocumentReference reference1 = db.collection("Product").document(id);
                        reference1.update("Quantity", getQtt - qtt);
                        reference1.update("Sold", sold + qtt);
                        data.remove(pst);
                        notifyDataSetChanged();

                    }
                });

    }
    public void cancelOther(String id){
        DocumentReference reference = db.collection("User").document(user.getUid()).collection("Other").document(id);
        reference.update("status","Khách hàng hủy");
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class OderViewHolder extends RecyclerView.ViewHolder{
        TextView tvStatus,tvAmount,tvPrice,tvID,tvDateTime;
        Button btnConfirm,btnCancel,btnDetail;
        public OderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.amount_product_item_order);
            tvStatus = itemView.findViewById(R.id.tv_Status_Oder);
            tvPrice = itemView.findViewById(R.id.total_price_item_order);
            tvID = itemView.findViewById(R.id.tv_id_Oder);
            tvDateTime = itemView.findViewById(R.id.tv_DateTime_Order);
            btnConfirm = itemView.findViewById(R.id.btnConfirm);
            btnCancel = itemView.findViewById(R.id.btnCancel_Oder);
            btnDetail = itemView.findViewById(R.id.btnDetail);
            bundle = new Bundle();
        }
    }
}
