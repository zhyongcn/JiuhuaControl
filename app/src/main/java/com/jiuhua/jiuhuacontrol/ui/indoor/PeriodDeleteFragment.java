package com.jiuhua.jiuhuacontrol.ui.indoor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jiuhua.jiuhuacontrol.Constants;
import com.jiuhua.jiuhuacontrol.R;
import com.jiuhua.jiuhuacontrol.database.DayPeriod;

import static java.lang.String.valueOf;

/**
 * 删除一个已经存在的 DayPeriod 。
 */
public class PeriodDeleteFragment extends Fragment implements View.OnClickListener {

    private int roomId;
    private String roomName;
    private String dayPeriodName;
    private int startMinute;
    private int endMinute;
    private int weekday;
    private int temperature;

    private DayPeriod dayPeriod = new DayPeriod();

    private IndoorViewModel indoorViewModel;

    TextView textViewTitle, textViewPeriodName, textViewStartTime, textViewEndTime, textViewSettingTemperature;
    CheckBox SunCheckBox, MonCheckBox, TueCheckBox, WedCheckBox, ThuCheckBox, FriCheckBox, SatCheckBox;
    Button buttonPeriodDelete, buttonPeriodCancel;

    public PeriodDeleteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_period_delete, container, false);
        //get the passed parameters
        roomId = getArguments().getInt("roomId");//bundle的参数接收：具体哪个房间。
        roomName = getArguments().getString("roomName");
        weekday = getArguments().getInt("weekday");//0是周一，6是周日
        temperature = getArguments().getInt("temperature");
        startMinute = getArguments().getInt("startMinute");// 0是0:00  23是23:00
        endMinute = getArguments().getInt("endMinute");
        dayPeriodName = getArguments().getString("dayPeriodName");

        indoorViewModel = new ViewModelProvider(this).get(IndoorViewModel.class);
        indoorViewModel.setCurrentlyRoomId(roomId);
        indoorViewModel.setCurrentlyRoomName(roomName);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        indoorViewModel.getAllLatestPeriodDBsLive().observe(getViewLifecycleOwner(), periodDBS -> {
            indoorViewModel.setAllLatestPeriodDBs(periodDBS); //viewmodel是单例，这个保存是有价值的。
        });

        //创建一个DayPeriod，供将来写入，或者删除使用。
        dayPeriod.setStartMinuteStamp(startMinute);
        dayPeriod.setEndMinuteStamp(endMinute);
        dayPeriod.setTempreature(temperature);
        dayPeriod.setWeekday(weekday);

        textViewTitle = view.findViewById(R.id.run_title);
        textViewPeriodName = view.findViewById(R.id.period_name);
        textViewStartTime = view.findViewById(R.id.start_time);
        textViewEndTime = view.findViewById(R.id.end_time);
        textViewSettingTemperature = view.findViewById(R.id.set_PeroidTemperature);

        MonCheckBox = view.findViewById(R.id.checkBox_1);
        TueCheckBox = view.findViewById(R.id.checkBox_2);
        WedCheckBox = view.findViewById(R.id.checkBox_3);
        ThuCheckBox = view.findViewById(R.id.checkBox_4);
        FriCheckBox = view.findViewById(R.id.checkBox_5);
        SatCheckBox = view.findViewById(R.id.checkBox_6);
        SunCheckBox = view.findViewById(R.id.checkBox_7);

        switch (weekday) {
            case Constants.Monday:
                MonCheckBox.setChecked(true);
                break;
            case Constants.Tuesday:
                TueCheckBox.setChecked(true);
                break;
            case Constants.Wednesday:
                WedCheckBox.setChecked(true);
                break;
            case Constants.Thursday:
                ThuCheckBox.setChecked(true);
                break;
            case Constants.Friday:
                FriCheckBox.setChecked(true);
                break;
            case Constants.Saturday:
                SatCheckBox.setChecked(true);
                break;
            case Constants.Sunday:
                SunCheckBox.setChecked(true);
                break;
        }

        buttonPeriodCancel = view.findViewById(R.id.button_period_cancel);
        buttonPeriodDelete = view.findViewById(R.id.button_period_delete);

        textViewTitle.setText(roomName + "运行时段详情");
        textViewPeriodName.setText("时段名称：                 " + dayPeriodName);
        textViewStartTime.setText("开始时间：                 " + startMinute / 60 + ":" + startMinute % 60);
        textViewEndTime.setText("结束时间：                 " + endMinute / 60 + ":" + endMinute % 60);
        textViewSettingTemperature.setText("设置温度：                " + temperature + " C");

        buttonPeriodCancel.setOnClickListener(this);
        buttonPeriodDelete.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_period_cancel:
                getActivity().onBackPressed();
                break;
            case R.id.button_period_delete:
                remove_daily_fragment_from_weekly_list(weekday);
                //新的周期写入数据库
                indoorViewModel.insertPeriodDB(roomId);
                //send MQTT message
                indoorViewModel.periodToDevice(roomId, indoorViewModel.currentlyPeriodSheet.getOneRoomWeeklyPeriod());
                getActivity().onBackPressed();
                break;
        }

    }

    private void remove_daily_fragment_from_weekly_list(int weekday) {
        Gson gson = new Gson();
        DayPeriod dayPeriodFromJson;
        String s = gson.toJson(this.dayPeriod);
        dayPeriodFromJson = gson.fromJson(s, DayPeriod.class);
        dayPeriodFromJson.setWeekday(weekday);

        for (int i = 0; i < indoorViewModel.currentlyPeriodSheet.getOneRoomWeeklyPeriod().size(); i++) {
            if (dayPeriodFromJson.getStartMinuteStamp() == indoorViewModel.currentlyPeriodSheet.getOneRoomWeeklyPeriod().get(i).getStartMinuteStamp()
                    && dayPeriodFromJson.getEndMinuteStamp() == indoorViewModel.currentlyPeriodSheet.getOneRoomWeeklyPeriod().get(i).getEndMinuteStamp()
                    && dayPeriodFromJson.getWeekday() == indoorViewModel.currentlyPeriodSheet.getOneRoomWeeklyPeriod().get(i).getWeekday()) {
                indoorViewModel.currentlyPeriodSheet.getOneRoomWeeklyPeriod().remove(i);
                i--;
            }
        }
        Log.d("remove", gson.toJson(indoorViewModel.currentlyPeriodSheet));

    }

}