package com.jiuhua.jiuhuacontrol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class MainActivity1 extends AppCompatActivity {
    RecyclerView recyclerView;
    HomepageAdapter homepageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        recyclerView = findViewById(R.id.recyclerView);
        homepageAdapter = new HomepageAdapter();

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(homepageAdapter);
    }
}
