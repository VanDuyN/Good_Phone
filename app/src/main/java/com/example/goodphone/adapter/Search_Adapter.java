package com.example.goodphone.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.goodphone.Cart;
import com.example.goodphone.DetailProduct;
import com.example.goodphone.R;
import com.example.goodphone.model.List_Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Search_Adapter extends RecyclerView.Adapter<Search_Adapter.SearchViewHolder> {
    String idProduct, idUser;
    int pst, quantityPrd;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore DBUser;
    Context myContext;
    List<List_Product> arrProduct;
    Cart mCart = new Cart();
    boolean isAllSelected = false;
    Dialog dialog;
    Button btnConfirm, btnRefuse;
    TextView tvTitleConfirm,tvDetailConfirm;
    List<List_Product> list_products = new ArrayList<>();
    private List<Integer> selectedPositions = new ArrayList<>();
    public void setFilterAdapter(List<List_Product> filterAdapter){
        this.arrProduct = filterAdapter;
        notifyDataSetChanged();
    }
    public Search_Adapter (Context context, List<List_Product> list_products){
        this.myContext = context;
        this.arrProduct = list_products;
    }

    @NonNull
    @Override
    public Search_Adapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Search_Adapter.SearchViewHolder searchViewHolder = new Search_Adapter.SearchViewHolder(LayoutInflater.from(myContext).inflate(R.layout.item_list, parent, false));
        return searchViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Search_Adapter.SearchViewHolder holder, int position) {
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
        return arrProduct.size();
    }
    public class SearchViewHolder extends RecyclerView.ViewHolder{
        TextView txtNameProduct,price,sumRating,sold;
        ImageView favourite,imgProduct;
        CardView cardView;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameProduct = itemView.findViewById(R.id.tv_Name_Product_Item);
            imgProduct =  itemView.findViewById(R.id.img_Product_Item);
            price = itemView.findViewById(R.id.tv_Price_Product_Item);
            sumRating= itemView.findViewById(R.id.tv_Rating_Item);
            sold= itemView.findViewById(R.id.tv_Sold_Item);
            favourite = itemView.findViewById(R.id.img_Favourite_Item);
            cardView = itemView.findViewById(R.id.cardView_Product_Item);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
