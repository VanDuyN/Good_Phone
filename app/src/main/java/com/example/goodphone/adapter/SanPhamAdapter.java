package com.example.goodphone.adapter;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.goodphone.QLSanPhamActivity;
import com.example.goodphone.R;
import com.example.goodphone.fragment.Add_Product;
import com.example.goodphone.fragment.Edit_Product;
import com.example.goodphone.model.List_Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.SanPhamViewHolder>{
    private List<List_Product> mListSanPham;
    Context context;
    FirebaseFirestore dbFirestore;
    FirebaseStorage storage;
    private int pst;
    private String id ;
    StorageReference storageReference,imageRef;
    Edit_Product edit_product;
    Bundle bundle;
    Button btnConfirm, btnRefuse;
    TextView tvTitleConfirm,tvDetailConfirm;
    AlertDialog.Builder builder;
    Dialog dialog;

    public SanPhamAdapter(List<List_Product> mListSanPham){
        this.mListSanPham = mListSanPham;
    }
    public SanPhamAdapter(Context context,List<List_Product> mListSanPham){
        this.context = context;
        this.mListSanPham = mListSanPham;
    }
    @NonNull
    @Override
    public SanPhamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sanpham,parent,false);
        return new SanPhamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamViewHolder holder, @SuppressLint("RecyclerView") int position) {
        List_Product sanPham = mListSanPham.get(position);
        if (sanPham == null){
            return;
        }
        holder.tvName.setText(sanPham.nameProduct);
        holder.imageView.setImageResource(sanPham.image_Main);
        String image = mListSanPham.get(position).url_img_product;

        holder.layoutForeGround.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = mListSanPham.get(position).id;
                openFragment();
            }
        });
        Glide.with(context).load(image).centerCrop().into(holder.imageView);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = mListSanPham.get(position).id;
                pst = position;
                openDialogConfirm(Gravity.CENTER);
            }
        });
    }
    public void openFragment(){
        bundle.putString("id",id);
        edit_product.setArguments(bundle);
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.root_view, edit_product);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public int getItemCount() {
        if (mListSanPham != null){
            return  mListSanPham.size();
        }
        return 0;
    }

    public class SanPhamViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        ImageView imageView;
        Button btnDelete;
        LinearLayout layoutForeGround;

        public SanPhamViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            imageView = itemView.findViewById(R.id.item_image_view);
            layoutForeGround = itemView.findViewById(R.id.layout_foreground);
            btnDelete = itemView.findViewById(R.id.btn_Delete_SP);
            dbFirestore = FirebaseFirestore.getInstance();
            edit_product = new Edit_Product();
            bundle = new Bundle();

        }
    }
    public void openDialogConfirm(int gravity){
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
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);
        tvDetailConfirm = dialog.findViewById(R.id.tv_Detail_Confirm);
        tvTitleConfirm = dialog.findViewById(R.id.tv_Title_Confirm);
        btnConfirm = dialog.findViewById(R.id.btn_Confirm);
        btnRefuse = dialog.findViewById(R.id.btn_Refuse);
        tvTitleConfirm.setText("Xác nhận chỉnh sửa");
        tvDetailConfirm.setText("Bạn có chắc là lưu thông tin điện thoại như vậy không ?");
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                deleteProduct();

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
    public void deleteProduct(){
        dbFirestore.collection("Product").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        storage = FirebaseStorage.getInstance("gs://goodphone-687e7.appspot.com/");;
                        storageReference = storage.getReference().child("Product");
                        imageRef= storageReference.child(id+".jpg");
                        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mListSanPham.remove(pst);
                                notifyItemRemoved(pst);
                                notifyItemRangeChanged(pst, mListSanPham.size());

                            }
                        }).addOnFailureListener(new OnFailureListener() {

                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(context, "Xoá LOI",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }


}
