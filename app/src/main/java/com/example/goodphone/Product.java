package com.example.goodphone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.ViewUtils;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class Product extends AppCompatActivity {
    Button btnExit;

    FirebaseFirestore DBProduct;
    RecyclerView recyclerView;
    SearchView searchView;
    TextView textView;
    String search, productionCompany;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product);
        init();
        checkPutExtra();
        button();
        searchView();
    }
    public void checkPutExtra(){
        Intent i = getIntent();
        search = i.getStringExtra("search");
        productionCompany = i.getStringExtra("name");
        if (productionCompany != null){
            if (search == null)
             {
                if (productionCompany.equals("Xiaomi")) {
                    textView.setText("Hãng điện thoại Xiaomi");
                } else if (productionCompany.equals("IPhone")) {
                    textView.setText("Hãng điện thoại Iphone");
                } else if (productionCompany.equals("OPPO")) {
                    textView.setText("Hãng điện thoại OPPO");
                }else if (productionCompany.equals("SamSung")) {
                    textView.setText("Hãng điện thoại SamSung");
                }else if (productionCompany.equals("other")) {
                    textView.setText("Một số điện thoại có hãng khác");
                }else {
                    textView.setText("Tất cả điện thoại");
                }
            }
        }else {
            if (search != null)
            {
                textView.setText("Tìm kiếm điện thoại '" +search+"'");
            }
        }
    }
    public void button(){
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void searchView(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search = query;
                if (productionCompany.equals("Xiaomi")) {
                    textView.setText("Tìm kiếm ' " +search+"'" + "trong hãng " + productionCompany);
                } else if (productionCompany.equals("IPhone")) {
                    textView.setText("Tìm kiếm ' " +search+"'" + "trong hãng " + productionCompany);
                } else if (productionCompany.equals("OPPO")) {
                    textView.setText("Tìm kiếm ' " +search+"'" + "trong hãng " + productionCompany);
                }else if (productionCompany.equals("SamSung")) {
                    textView.setText("Tìm kiếm ' " +search+"'" + "trong hãng " + productionCompany);
                }else if (productionCompany.equals("other")) {
                    textView.setText("Tìm kiếm ' " +search+"'" +"trong hãng khác");
                }else {
                    textView.setText("Tìm kiếm ' " +search+"'" );
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
    public void init(){
        btnExit = findViewById(R.id.btnExitProduct);
        recyclerView = findViewById(R.id.recycleView_product);
        searchView = findViewById(R.id.search_view_product);
        textView = findViewById(R.id.tvText);
    }
}