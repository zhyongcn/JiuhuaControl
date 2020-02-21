package com.jiuhua.jiuhuacontrol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jiuhua.jiuhuacontrol.database.RoomNameDB;

import java.util.List;

public class MainActivity1 extends AppCompatActivity {

    RoomViewModel roomViewModel;
    RecyclerView recyclerView;
    HomepageAdapter homepageAdapter;
    Button buttonInHome, buttonOutHome, buttonECO, buttonSleep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        recyclerView = findViewById(R.id.recyclerView);

        roomViewModel = new ViewModelProvider(this).get(RoomViewModel.class);
        homepageAdapter = new HomepageAdapter(roomViewModel);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(homepageAdapter);

        roomViewModel.getAllRoomsName().observe(this, new Observer<List<RoomNameDB>>() {
            @Override
            public void onChanged(List<RoomNameDB> roomNameDBS) {
//                int temp = homepageAdapter.getItemCount();
                homepageAdapter.setAllroomsName(roomNameDBS);   //设置数据
                homepageAdapter.notifyDataSetChanged();     //去刷新视图
            }
        });

        buttonInHome = findViewById(R.id.buttonInhome);
        buttonOutHome = findViewById(R.id.button_Outhome);
        buttonECO = findViewById(R.id.buttonEco);
        buttonSleep = findViewById(R.id.buttonSleep);

        buttonInHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roomViewModel.insertRoomName(new RoomNameDB("主卧室"));
                roomViewModel.insertRoomName(new RoomNameDB("客厅"));
                roomViewModel.insertRoomName(new RoomNameDB("儿童房"));
            }
        });
        buttonOutHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roomViewModel.deleteAllRoomsName();
            }
        });
    }

//    RoomDao
}
