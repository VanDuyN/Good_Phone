package com.example.goodphone;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goodphone.adapter.RecyclerViewItemTouchHelper;
import com.example.goodphone.adapter.SanPhamAdapter;
import com.example.goodphone.model.SanPham;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class QLSanPhamActivity extends AppCompatActivity implements ItemTouchHelperListener {
    private RecyclerView rcvSanPham;
    private SanPhamAdapter adapter;
    private List<SanPham> mListSanPham;
    private RelativeLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlsanpham);
        rootView= findViewById(R.id.root_view);

        rcvSanPham = findViewById(R.id.rcv_sanpham);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvSanPham.setLayoutManager(linearLayoutManager);

        mListSanPham = getListSanPham();
        adapter = new SanPhamAdapter(mListSanPham);
        rcvSanPham.setAdapter(adapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        rcvSanPham.addItemDecoration(itemDecoration);
        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerViewItemTouchHelper(0,ItemTouchHelper.LEFT,this);

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rcvSanPham);
    }
    private List<SanPham> getListSanPham(){
        List<SanPham> list = new ArrayList<>();
        for(int i = 0; i<20; i++){
            list.add(new SanPham("Phone 1"+(i +1),R.drawable.img));
        }
        return list;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
        if(viewHolder instanceof SanPhamAdapter.SanPhamViewHolder){
            String nameSanPhamDelete = mListSanPham.get(viewHolder.getAdapterPosition()).getName();
            Integer imageSanPhamDelete = mListSanPham.get(viewHolder.getAdapterPosition()).getImageId();

            SanPham sanPhamDelete = mListSanPham.get(viewHolder.getAdapterPosition());
            int indexDelete = viewHolder.getAdapterPosition();

            adapter.removeItem(indexDelete);

            Snackbar snackbar = Snackbar.make(rootView, nameSanPhamDelete + "removed!",Snackbar.LENGTH_LONG);
            snackbar.setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.undoItem(sanPhamDelete,indexDelete);
                    if(indexDelete == 0 || indexDelete == mListSanPham.size()-1){
                        rcvSanPham.scrollToPosition(indexDelete);
                    }
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }

    }
}