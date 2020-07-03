package com.jiuhua.jiuhuacontrol.ui.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jiuhua.jiuhuacontrol.R;
import com.jiuhua.jiuhuacontrol.database.BasicInfoDB;
import com.jiuhua.jiuhuacontrol.database.IndoorDB;

import java.util.List;

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

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homepageAdapter = new HomepageAdapter(homeViewModel);//这个类以及需要的参数是自己写的。

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));//RecyclerView是需要管理器的，网格管理，两行。
        recyclerView.setAdapter(homepageAdapter);//adapter肯定是必须的！！

        //
        homeViewModel.getAllBasicInfoLive().observe(getViewLifecycleOwner(), new Observer<List<BasicInfoDB>>() {
            @Override
            public void onChanged(List<BasicInfoDB> basicInfoDBS) {
//                int temp = homepageAdapter.getItemCount();
                homepageAdapter.setAllBasicInfo(basicInfoDBS);   //设置数据
//                homepageAdapter.notifyDataSetChanged();     //没有必要两次去刷新视图
            }
        });

        homeViewModel.getAllLatestIndoorDBsLive().observe(getViewLifecycleOwner(), new Observer<List<IndoorDB>>() {
            @Override
            public void onChanged(List<IndoorDB> indoorDBS) {
                homepageAdapter.setAllLatestIndoorDBs(indoorDBS);//设置数据
//                homeViewModel.myRepository.
                homepageAdapter.notifyDataSetChanged();  //去刷新视图
            }
        });

        buttonInHome = view.findViewById(R.id.buttonInhome);
        buttonOutHome = view.findViewById(R.id.button_Outhome);
        buttonECO = view.findViewById(R.id.buttonEco);
        buttonSleep = view.findViewById(R.id.buttonSleep);

        buttonInHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //temporary test code
                homeViewModel.insertRoomName(new BasicInfoDB("客厅", "扬子风盘", "FP-51", true, true,
                        true, null, null, false));
                homeViewModel.insertRoomName(new BasicInfoDB("餐厅", "约克", "FP-68", false, true,
                        true, null, null, false));
                homeViewModel.insertRoomName(new BasicInfoDB("主卧室", "麦克维尔", "FP-120", true, false,
                        true, null, null, false));
                homeViewModel.insertRoomName(new BasicInfoDB("次卧室", "约克", "FP-68", false, true,
                        true, null, null, false));
                homeViewModel.insertRoomName(new BasicInfoDB("北卧室", "麦克维尔", "FP-120", true, false,
                        true, null, null, false));
            }
        });

        buttonOutHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeViewModel.deleteAllRoomsName();
            }
        });
        buttonECO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pass
            }
        });
        buttonSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pass
            }
        });
    }
}

