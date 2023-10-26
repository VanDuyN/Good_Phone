package com.example.goodphone.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.goodphone.Cart;
import com.example.goodphone.R;
import com.example.goodphone.model.List_Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Struct;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart_Adapter extends RecyclerView.Adapter<Cart_Adapter.CartProductViewHolder> {
    String idProduct, idUser;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore DBUser;
    Context myContext;
    List<List_Product> arrProduct;
    Cart mCart = new Cart();
    boolean isAllSelected = false;
    List<List_Product> list_products = new ArrayList<>();
    private List<Integer> selectedPositions = new ArrayList<>();
    public Cart_Adapter (Context context, List<List_Product> list_products,Cart cart){
        this.myContext = context;
        this.arrProduct = list_products;
        this.mCart = cart;
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
        Locale locale = new Locale("vi", "VN");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);

        String formattedPrice = currencyFormat.format(priceValue);
        String quantity = String.valueOf(arrProduct.get(position).quantity);

        auth= FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        holder.tvNameProduct.setText(arrProduct.get(position).nameProduct);
        holder.tvQuantity.setText(quantity);
        holder.tvPrice.setText(formattedPrice);
        Glide.with(myContext).load(arrProduct.get(position).url_img_product).fitCenter().into(holder.imgProduct);

        if (selectedPositions.contains(position)) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                List_Product item = arrProduct.get(position);
                item.setChecked(holder.checkBox.isChecked());
                if (isChecked) {
                    selectedPositions.add(position);
                    mCart.getDataAdapter();
                } else {
                    selectedPositions.remove(Integer.valueOf(position));
                    holder.checkBox.setChecked(false);
                    mCart.updateCheckBox(isChecked);
                    mCart.getDataAdapter();
                }

            }
        });

    }
    public void deleteCart(){

    }
    public List<List_Product> getItemsProduct() {
        list_products.clear();
        for (int i = 0; i < arrProduct.size(); i++) {
            if (arrProduct.isEmpty()) {
                break;
            }
            List_Product item = arrProduct.get(i);
            if(item.isChecked() == true){
                list_products.add(item);
            }

        }
        return list_products;
    }
    public void selectAllItems() {
        isAllSelected = true;
        selectedPositions.clear();
        for (int i = 0; i < arrProduct.size(); i++) {
            selectedPositions.add(i);
        }
        notifyDataSetChanged();
    }

    public void clearSelection() {
        isAllSelected = false;
        selectedPositions.clear();
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return arrProduct.size();
    }

    public class CartProductViewHolder extends RecyclerView.ViewHolder{
        TextView tvNameProduct,tvPrice,tvQuantity;
        ImageView imgProduct;
        ImageButton btnDelete;
        Button btnAdd,btnSubtract;
        CheckBox checkBox;
        RelativeLayout relativeLayout;

        public CartProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameProduct = itemView.findViewById(R.id.tv_Name_Product_Cart);
            tvPrice = itemView.findViewById(R.id.tv_Price_Product_Cart);
            tvQuantity= itemView.findViewById(R.id.tv_Quantity_Product_Cart);
            imgProduct = itemView.findViewById(R.id.img_Product_Cart);
            btnDelete = itemView.findViewById(R.id.btn_Delete_Product_Cart);
            btnAdd = itemView.findViewById(R.id.btn_decrease_cart);
            btnSubtract= itemView.findViewById(R.id.btn_increase_Cart);
            relativeLayout = itemView.findViewById(R.id.relativeLayout_Cart);
            checkBox = itemView.findViewById(R.id.radio_item_cart);
        }

    }
}
