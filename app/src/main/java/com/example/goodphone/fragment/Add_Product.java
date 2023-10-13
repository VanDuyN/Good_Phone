package com.example.goodphone.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.goodphone.QLSanPhamActivity;
import com.example.goodphone.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Add_Product#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Add_Product extends Fragment {
    private View view;
    private QLSanPhamActivity qlSanPhamActivity;
    public Add_Product(){
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add__product, container, false);
        qlSanPhamActivity = (QLSanPhamActivity) getActivity();
        return view;
    }
    public void init(){

    }
}