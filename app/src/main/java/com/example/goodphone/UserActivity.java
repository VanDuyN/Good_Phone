package com.example.goodphone;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goodphone.adapter.UserAdapter;
import com.example.goodphone.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {
    private RecyclerView rcvUser;
    private UserAdapter userAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        rcvUser = findViewById(R.id.rcv_user);
        userAdapter = new UserAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        rcvUser.setLayoutManager(linearLayoutManager);

        userAdapter.setData(getListUser());
        rcvUser.setAdapter(userAdapter);
    }
    private List<User> getListUser() {
        List<User> list = new ArrayList<>();
        list.add(new User(R.drawable.profile,"User name"));
        list.add(new User(R.drawable.profile,"User name"));
        list.add(new User(R.drawable.profile,"User name"));
        list.add(new User(R.drawable.profile,"User name"));
        list.add(new User(R.drawable.profile,"User name"));
        list.add(new User(R.drawable.profile,"User name"));
        list.add(new User(R.drawable.profile,"User name"));
        list.add(new User(R.drawable.profile,"User name"));
        list.add(new User(R.drawable.profile,"User name"));
        list.add(new User(R.drawable.profile,"User name"));
        list.add(new User(R.drawable.profile,"User name"));
        list.add(new User(R.drawable.profile,"User name"));
        list.add(new User(R.drawable.profile,"User name"));
        return list;
    }
}
