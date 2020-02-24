package com.jiuhua.jiuhuacontrol;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jiuhua.jiuhuacontrol.database.RoomNameDB;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeHostFragment extends Fragment {

    MyViewModel myViewModel;
    RecyclerView recyclerView;
    HomepageAdapter homepageAdapter;
    Button buttonInHome, buttonOutHome, buttonECO, buttonSleep;

    public HomeHostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_host, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);

        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        homepageAdapter = new HomepageAdapter(myViewModel);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(homepageAdapter);

        myViewModel.getAllRoomsName().observe(getViewLifecycleOwner(), new Observer<List<RoomNameDB>>() {
            @Override
            public void onChanged(List<RoomNameDB> roomNameDBS) {
//                int temp = homepageAdapter.getItemCount();
                homepageAdapter.setAllroomsName(roomNameDBS);   //设置数据
                homepageAdapter.notifyDataSetChanged();     //去刷新视图
            }
        });

        buttonInHome = view.findViewById(R.id.buttonInhome);
        buttonOutHome = view.findViewById(R.id.button_Outhome);
        buttonECO = view.findViewById(R.id.buttonEco);
        buttonSleep = view.findViewById(R.id.buttonSleep);

        buttonInHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewModel.insertRoomName(new RoomNameDB("主卧室"));
                myViewModel.insertRoomName(new RoomNameDB("客厅"));
                myViewModel.insertRoomName(new RoomNameDB("儿童房"));
            }
        });
        buttonOutHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewModel.deleteAllRoomsName();
            }
        });
    }
}

