package com.example.goodphone.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.goodphone.R;
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
    StorageReference storageReference;
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
        Glide.with(context).load(image).centerCrop().into(holder.imageView);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbFirestore.collection("Product").document(mListSanPham.get(position).id)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                storage = FirebaseStorage.getInstance();
                                storageReference = storage.getReference();
                                storageReference.child("Product/"+mListSanPham.get(position).nameProduct+".jpg");
                                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // File deleted successfully
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Uh-oh, an error occurred!
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
        });
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

        }
    }
    public void removeItem (int index){
        mListSanPham.remove(index);
        notifyItemRemoved(index);
        notifyItemRangeChanged(index,mListSanPham.size());
    }

}
