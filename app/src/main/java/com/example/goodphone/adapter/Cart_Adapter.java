package com.example.goodphone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.goodphone.Cart;
import com.example.goodphone.Profile;
import com.example.goodphone.R;
import com.example.goodphone.model.List_Product;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart_Adapter extends RecyclerView.Adapter<Cart_Adapter.CartProductViewHolder> {
    String idProduct, idUser;
    Context myContext;
    List<List_Product> arrProduct;
    public Cart_Adapter (Context context, List<List_Product> list_products){
        this.myContext = context;
        this.arrProduct = list_products;
    }



    @NonNull
    @Override
    public CartProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CartProductViewHolder cartProductViewHolder = new CartProductViewHolder(LayoutInflater.from(myContext).inflate(R.layout.item_cart, parent, false));
        return cartProductViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Cart_Adapter.CartProductViewHolder holder, int position) {
        double priceValue = arrProduct.get(position).price;
        Locale locale = new Locale("vi", "VN"); // Định dạng tiền tệ cho Việt Nam
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
        String formattedPrice = currencyFormat.format(priceValue);
        String quantity = String.valueOf(arrProduct.get(position).quantity);

        holder.tvNameProduct.setText(arrProduct.get(position).nameProduct);
        holder.tvQuantity.setText(quantity);
        holder.tvPrice.setText(formattedPrice);
        Glide.with(myContext).load(arrProduct.get(position).url_img_product).fitCenter().into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return arrProduct.size();
    }

    public class CartProductViewHolder extends RecyclerView.ViewHolder{
        TextView tvNameProduct,tvPrice,tvQuantity;
        ImageView imgProduct;
        Button btnDelete, btnAdd,btnSubtract;
        RelativeLayout relativeLayout;
        public CartProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameProduct = itemView.findViewById(R.id.tv_Name_Product_Cart);
            tvPrice = itemView.findViewById(R.id.tv_Price_Product_Cart);
            tvQuantity= itemView.findViewById(R.id.tv_Quantity_Product_Cart);
            imgProduct = itemView.findViewById(R.id.img_Product_Cart);
            //btnDelete = itemView.findViewById(R.id.btn_Delete_Product_Cart);
            btnAdd = itemView.findViewById(R.id.btn_decrease_cart);
            btnSubtract= itemView.findViewById(R.id.btn_increase_Cart);
            relativeLayout = itemView.findViewById(R.id.relativeLayout_Cart);
        }
    }
}
