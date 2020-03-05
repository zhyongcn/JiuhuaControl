package com.jiuhua.jiuhuacontrol.ui.home;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jiuhua.jiuhuacontrol.R;
import com.jiuhua.jiuhuacontrol.database.BasicInfoDB;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    HomeViewModel homeViewModel;
    RecyclerView recyclerView;
    HomepageAdapter homepageAdapter;
    Button buttonInHome, buttonOutHome, buttonECO, buttonSleep;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        homepageAdapter = new HomepageAdapter(homeViewModel);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(homepageAdapter);

        homeViewModel.getAllRoomsName().observe(getViewLifecycleOwner(), new Observer<List<BasicInfoDB>>() {
            @Override
            public void onChanged(List<BasicInfoDB> basicInfoDBS) {
//                int temp = homepageAdapter.getItemCount();
                homepageAdapter.setAllroomsName(basicInfoDBS);   //设置数据
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
                homeViewModel.insertRoomName(new BasicInfoDB("主卧室"));
                homeViewModel.insertRoomName(new BasicInfoDB("客厅"));
                homeViewModel.insertRoomName(new BasicInfoDB("儿童房"));
            }
        });
        buttonOutHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeViewModel.deleteAllRoomsName();
            }
        });
    }
}

