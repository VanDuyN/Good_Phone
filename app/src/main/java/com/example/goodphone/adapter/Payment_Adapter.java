package com.example.goodphone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.goodphone.R;
import com.example.goodphone.model.List_Product;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Payment_Adapter extends RecyclerView.Adapter<Payment_Adapter.SearchViewHolder> {
    Context context;
    List<List_Product> arrProduct = new ArrayList<>();

    public Payment_Adapter(Context context, List<List_Product> list_products) {
        this.context = context;
        this.arrProduct = list_products;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SearchViewHolder  searchViewHolder = new SearchViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product_payment, parent, false));
        return  searchViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        double priceValue = arrProduct.get(position).price;
        Locale locale = new Locale("vi", "VN");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
        String formattedPrice = currencyFormat.format(priceValue);
        String quantity = String.valueOf(arrProduct.get(position).quantity);

        holder.tvPrice.setText(formattedPrice);
        holder.tvName.setText(arrProduct.get(position).nameProduct);
        holder.tvQuantity.setText(quantity);
        Glide.with(context).load(arrProduct.get(position).url_img_product).fitCenter().into(holder.img);
    }

    @Override
    public int getItemCount() {
        return arrProduct.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder{
        TextView tvName,tvPrice,tvQuantity;
        ImageView img;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_Name_Product_Payment);
            tvPrice = itemView.findViewById(R.id.tv_Price_Product_Payment);
            tvQuantity = itemView.findViewById(R.id.tv_Quantity_Product_Payment);
            img = itemView.findViewById(R.id.img_Product_Payment);
        }
    }
}
