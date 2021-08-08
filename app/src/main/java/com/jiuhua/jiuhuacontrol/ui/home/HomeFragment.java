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
import com.jiuhua.jiuhuacontrol.database.BasicInfoSheet;
import com.jiuhua.jiuhuacontrol.database.FancoilSheet;
import com.jiuhua.jiuhuacontrol.database.SensorSheet;
import com.jiuhua.jiuhuacontrol.Constants;

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
        recyclerView = view.findViewById(R.id.recyclerView_esp);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homepageAdapter = new HomepageAdapter(homeViewModel);//这个类以及需要的参数是自己写的。

        //RecyclerView是需要管理器的，网格管理，两行。
        // TODO: 老年人需要两行，字大一些，年轻人需要一行，内容多一些。依据用户情况调整。
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setAdapter(homepageAdapter);//adapter肯定是必须的！！

        //
        homeViewModel.getAllBasicInfoLive().observe(getViewLifecycleOwner(), basicInfoSheets -> {
                //int temp = homepageAdapter.getItemCount();
            homepageAdapter.setAllBasicInfo(basicInfoSheets);   //设置数据
                homepageAdapter.notifyDataSetChanged();     //没有必要两次去刷新视图
        });

        homeViewModel.getAllLatestSensorSheetsLive(Constants.deviceType_DHTsensor).observe(getViewLifecycleOwner(),
                new Observer<List<SensorSheet>>() {
                    @Override
                    public void onChanged(List<SensorSheet> sensorSheets) {
                        homepageAdapter.setAllLatestSensorSheets(sensorSheets);//设置数据
                        homepageAdapter.notifyDataSetChanged();  //去刷新视图，没有重复刷新，新数据来了，需要刷新。
                    }
                });

        homeViewModel.getAllLatestFancoilSheetsLive().observe(getViewLifecycleOwner(),
                new Observer<List<FancoilSheet>>() {
                    @Override
                    public void onChanged(List<FancoilSheet> fancoilSheets) {
                        homepageAdapter.setAllLatestFancoilSheets(fancoilSheets);//设置数据
                        homepageAdapter.notifyDataSetChanged();  //去刷新视图
                    }
                });

        buttonInHome = view.findViewById(R.id.buttonInhome);
        buttonOutHome = view.findViewById(R.id.button_Outhome);
        buttonECO = view.findViewById(R.id.buttonEco);
        buttonSleep = view.findViewById(R.id.buttonSleep);

        buttonInHome.setOnClickListener(v -> {
            //temporary test code
            homeViewModel.insertRoomName(new BasicInfoSheet(1, "房间一", 0, "扬子风盘", "FP-51", true, true,
                    true, null, null, false));
            homeViewModel.insertRoomName(new BasicInfoSheet(2, "房间二", 0, "约克", "FP-68", false, true,
                    true, null, null, false));
            homeViewModel.insertRoomName(new BasicInfoSheet(3, "房间三", 0, "麦克维尔", "FP-120", true, false,
                    true, null, null, false));
            homeViewModel.insertRoomName(new BasicInfoSheet(4, "房间四", 0, "约克", "FP-68", false, true,
                    true, null, null, false));
            homeViewModel.insertRoomName(new BasicInfoSheet(5, "房间五", 0, "麦克维尔", "FP-120", true, false,
                    true, null, null, false));
            homeViewModel.insertRoomName(new BasicInfoSheet(6, "房间六", 0, "扬子风盘", "FP-51", true, true,
                    true, null, null, false));
            homeViewModel.insertRoomName(new BasicInfoSheet(7, "房间七", 0, "约克", "FP-68", false, true,
                    true, null, null, false));
            homeViewModel.insertRoomName(new BasicInfoSheet(8, "房间八", 0, "麦克维尔", "FP-120", true, false,
                    true, null, null, false));
            homeViewModel.insertRoomName(new BasicInfoSheet(9, "房间九", 0, "约克", "FP-68", false, true,
                    true, null, null, false));
            homeViewModel.insertRoomName(new BasicInfoSheet(10, "房间十", 0, "麦克维尔", "FP-120", true, false,
                    true, null, null, false));
            homeViewModel.insertRoomName(new BasicInfoSheet(11, "房间十一", 0, "麦克维尔", "FP-120", true, false,
                    true, null, null, false));
            homeViewModel.insertRoomName(new BasicInfoSheet(12, "房间十二", 0, "麦克维尔", "FP-120", true, false,
                    true, null, null, false));
        });

        buttonOutHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeViewModel.deleteAllRoomsName();
            }
        });
        buttonECO.setOnClickListener(v -> {
            //pass
        });
        buttonSleep.setOnClickListener(v -> {
            //pass
        });
    }
}

