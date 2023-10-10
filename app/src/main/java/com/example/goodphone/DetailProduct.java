package com.example.goodphone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class DetailProduct extends AppCompatActivity {
    ImageView btnReturn, imgMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_product);
        init();
        button();
    }
    public void init(){
        btnReturn = findViewById(R.id.img_return);
        imgMain = findViewById(R.id.img_main);
    }
    public void button(){
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}