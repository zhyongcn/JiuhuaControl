package com.jiuhua.jiuhuacontrol.ui.indoor;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jiuhua.jiuhuacontrol.R;
import com.jiuhua.jiuhuacontrol.database.DayPeriod;
import com.jiuhua.jiuhuacontrol.database.PeriodDB;


/**
 * 一周运行时间段的显示和设置
 * A simple {@link Fragment} subclass.
 */
public class PeriodFragment extends Fragment {

    private IndoorViewModel indoorViewModel;

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
        indoorViewModel = new ViewModelProvider(this).get(IndoorViewModel.class);
        indoorViewModel.setCurrentlyRoomId(roomId);
        indoorViewModel.setCurrentlyRoomName(roomName);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myView = view.findViewById(R.id.myview);//从传入的view中获取

        indoorViewModel.getAllLatestPeriodDBsLive().observe(getViewLifecycleOwner(), periodDBS -> {
            indoorViewModel.setAllLatestPeriodDBs(periodDBS); //viewmodel是单例，这个保存是有价值的。
            myView.getWeeklyPeriod(indoorViewModel.currentlyPeriodDB.getOneRoomWeeklyPeriod());
        });

        myView.setClickCrossListener((weekday, hour, dayPeriod) -> {
            int clickedweekday = weekday - 1;//调整为： 周一 0
            int clickedhour = hour;
            DayPeriod checkedDayPeriod = dayPeriod;
            if (dayPeriod != null){
                int st = dayPeriod.getStartMinuteStamp();
                int et = dayPeriod.getEndMinuteStamp();
                int tm = dayPeriod.getTempreature();
                int we = dayPeriod.getWeekday();
                Bundle bundle = new Bundle();
                bundle.putInt("roomId", roomId);
                bundle.putString("roomName", roomName);
                bundle.putInt("startMinute", st);
                bundle.putInt("endMinute", et);
                bundle.putInt("temperature", tm);
                bundle.putInt("weekday", we);
                Navigation.findNavController(getView()).navigate(R.id.periodDeleteFragment, bundle);
            }else {
                Bundle bundle = new Bundle();
                bundle.putInt("roomId", roomId);
                bundle.putString("roomName", roomName);
                bundle.putInt("clickedweekday", clickedweekday);
                bundle.putInt("clickedhour", clickedhour);
                Navigation.findNavController(getView()).navigate(R.id.peroidSettingFragment, bundle);
                //id是目的地的id，不是动作的id，fuck,一天的时间。
            }
        });

    }
}
