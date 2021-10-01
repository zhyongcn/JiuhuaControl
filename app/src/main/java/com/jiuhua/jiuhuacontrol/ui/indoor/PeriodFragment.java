package com.jiuhua.jiuhuacontrol.ui.indoor;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.jiuhua.jiuhuacontrol.R;
import com.jiuhua.jiuhuacontrol.database.DayPeriod;
import com.jiuhua.jiuhuacontrol.ui.HomeViewModel;


/**
 * 一周运行时间段的显示和设置，
 *   *** 周期就是周期，不需要关心冷热以及设备，设置的温度就代表了要求，各个设备可以自己判断 ***
 * A simple {@link Fragment} subclass.
 */
public class PeriodFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private int roomId;
    private String roomName;
    private MyView myView;

    public PeriodFragment(int roomId, String roomName) {
        this.roomId = roomId;
        this.roomName = roomName;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_peroid, container, false);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.setCurrentlyRoomId(roomId);
        homeViewModel.setCurrentlyRoomName(roomName);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myView = view.findViewById(R.id.myview);//从传入的view中获取

        homeViewModel.getAllLatestPeriodSheetsLive().observe(getViewLifecycleOwner(), periodSheets -> {
            homeViewModel.setAllLatestPeriodSheets(periodSheets); //viewmodel是单例，这个保存是有价值的。
            myView.getWeeklyPeriod(homeViewModel.getCurrentRoomPeriodSheet().getOneRoomWeeklyPeriod());
        });

        myView.setClickCrossListener((weekday, hour, dayPeriod) -> {
            int clickedweekday = weekday - 1;//周日 0 开始
            int clickedhour = hour;
            DayPeriod checkedDayPeriod = dayPeriod;
            if (dayPeriod != null) {
                String dn = dayPeriod.getDayPeriodName();
                int st = dayPeriod.getStartMinutes();
                int et = dayPeriod.getEndMinutes();
                int tm = dayPeriod.getTempreature();
                int we = dayPeriod.getWeekday();//TODO:这里获取的weekday是不是周日0开始？？
                Bundle bundle = new Bundle();
                bundle.putInt("roomId", roomId);
                bundle.putString("roomName", roomName);
                bundle.putString("dayPeriodName", dn);
                bundle.putInt("startMinute", st);
                bundle.putInt("endMinute", et);
                bundle.putInt("temperature", tm);
                bundle.putInt("weekday", we);//TODO: 上面获取的weekday是不是周日0开始？？
                Navigation.findNavController(getView()).navigate(R.id.periodDeleteFragment, bundle);
            } else {
                Bundle bundle = new Bundle();
                bundle.putInt("roomId", roomId);
                bundle.putString("roomName", roomName);
//                bundle.putString("roomName", roomName);
                //MyView.java送来的是周日1开始，现在传过去的周日0开始
                bundle.putInt("clickedweekday", clickedweekday);//上面减过1了，不用再减了。
                bundle.putInt("clickedhour", clickedhour);
                Navigation.findNavController(getView()).navigate(R.id.peroidSettingFragment, bundle);
                //id是目的地的id，不是动作的id，fuck,一天的时间。
            }
        });


    }

//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
