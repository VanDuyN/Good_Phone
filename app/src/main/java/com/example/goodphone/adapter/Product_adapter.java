package com.example.goodphone.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.goodphone.DetailProduct;
import com.example.goodphone.R;
import com.example.goodphone.model.List_Product;



;


import java.security.PublicKey;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class Product_adapter extends RecyclerView.Adapter<Product_adapter.Product_View_Holder>{
    Context myContext;
    List<List_Product> arrProduct;

    public Product_adapter(Context context,List<List_Product> list_products){
        this.myContext = context;
        this.arrProduct = list_products;
    }
    @NonNull
    @Override
    public Product_View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Product_View_Holder product_view_holder = new Product_View_Holder(LayoutInflater.from(myContext).inflate(R.layout.item_list, parent, false));
        return product_view_holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Product_View_Holder holder, @SuppressLint("RecyclerView") int position) {
        double priceValue = arrProduct.get(position).price;
        Locale locale = new Locale("vi", "VN"); // Định dạng tiền tệ cho Việt Nam
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
        String formattedPrice = currencyFormat.format(priceValue);
        holder.price.setText(formattedPrice);
        holder.sold.setText("Đã bán: "+String.valueOf(arrProduct.get(position).sold));
        holder.txtNameProduct.setText(arrProduct.get(position).nameProduct);
        holder.sumRating.setText(String.valueOf(arrProduct.get(position).sumRating));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(myContext, DetailProduct.class);
                i.putExtra("id", arrProduct.get(position).id);
                myContext.startActivity(i);
            }
        });
        String url = arrProduct.get(position).url_img_product;
        Glide.with(myContext).load(url).fitCenter().into(holder.imgProduct);
    }


    @Override
    public int getItemCount() {
        return Math.min(arrProduct.size(), 8);
    }
    public class Product_View_Holder extends RecyclerView.ViewHolder{
        TextView txtNameProduct,price,sumRating,sold;
        ImageView favourite,imgProduct;
        CardView cardView;
        public Product_View_Holder(@NonNull View itemView) {
            super(itemView);

            txtNameProduct = itemView.findViewById(R.id.tv_Name_Product_Item);
            imgProduct =  itemView.findViewById(R.id.img_Product_Item);
            price = itemView.findViewById(R.id.tv_Price_Product_Item);
            sumRating= itemView.findViewById(R.id.tv_Rating_Item);
            sold= itemView.findViewById(R.id.tv_Sold_Item);
            favourite = itemView.findViewById(R.id.img_Favourite_Item);
            cardView = itemView.findViewById(R.id.cardView_Product_Item);
        }
    }
}
