package com.example.goodphone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goodphone.R;
import com.example.goodphone.model.SanPham;

import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.SanPhamViewHolder>{
    private List<SanPham> mListSanPham;
    public SanPhamAdapter(List<SanPham> mListSanPham){
        this.mListSanPham = mListSanPham;
    }
    @NonNull
    @Override
    public SanPhamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sanpham,parent,false);
        return new SanPhamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamViewHolder holder, int position) {
        SanPham sanPham = mListSanPham.get(position);
        if (sanPham == null){
            return;
        }
        holder.tvName.setText(sanPham.getName());
        holder.imageView.setImageResource(sanPham.getImageId());
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
        LinearLayout layoutForeGround;

        public SanPhamViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            imageView = itemView.findViewById(R.id.item_image_view);
            layoutForeGround = itemView.findViewById(R.id.layout_foreground);
        }
    }
    public void removeItem (int index){
        mListSanPham.remove(index);
        notifyItemRemoved(index);
    }
    public void undoItem(SanPham sanPham, int index){
        mListSanPham.add(index,sanPham);
        notifyItemInserted(index);
    }
}
