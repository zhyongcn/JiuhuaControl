package com.jiuhua.jiuhuacontrol.ui.indoor;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.jiuhua.jiuhuacontrol.R;

import static java.lang.String.valueOf;

public class PeriodSettingFragment extends Fragment implements View.OnClickListener,
        TimePicker.OnTimeChangedListener, NumberPicker.OnValueChangeListener {
    private int roomNameId;
    private String roomName;
    private int weekday;
    private int hourInDay;
    private int hour;
    private int minute;
    private int seasonId;
    private int startHour, startMinute, endHour, endMinute;
    private int temperature;
    private int temp;

    private int[][] period = new int[7][30];

    private Context context;

    TextView runningTime, startTime, endTime, settingTemp;
    CheckBox isrepeat;
    RadioButton Sun, Mon, Tue, Wed, Thu, Fri, Sat;

    private IndoorViewModel indoorViewModel;

    public PeriodSettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_peroid_setting, container, false);

        roomNameId = getArguments().getInt("roomnameID");//bundle的参数接收：具体哪个房间。
        roomName = getArguments().getString("roomName");
        weekday = getArguments().getInt("weekday");
        hourInDay = getArguments().getInt("hour");

        indoorViewModel = new ViewModelProvider(this).get(IndoorViewModel.class);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        runningTime = view.findViewById(R.id.run_time);
        startTime = view.findViewById(R.id.starttime);
        endTime = view.findViewById(R.id.endtime);
        settingTemp = view.findViewById(R.id.setTemp_peroid);
        isrepeat = view.findViewById(R.id.checkBox);
        Sun = view.findViewById(R.id.radioButton7);
        Mon = view.findViewById(R.id.radioButton1);
        Tue = view.findViewById(R.id.radioButton2);
        Wed = view.findViewById(R.id.radioButton3);
        Thu = view.findViewById(R.id.radioButton4);
        Fri = view.findViewById(R.id.radioButton5);
        Sat = view.findViewById(R.id.radioButton6);

        runningTime.setText(roomName + "运行时段设置");
        startTime.setText("开始时间                 " + hourInDay + ":" + "00");
        endTime.setText("结束时间                 " + valueOf(hourInDay + 1) + ":" + "00");
        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        settingTemp.setOnClickListener(this);

        context = getContext();


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.starttime:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);//新建构造器
                builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {//设置构造器的正向按钮 名称， 功能
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//参数：dialog发生点击的对话，which发生点击的按钮
                        if (hour < 10 && minute < 10) {//这三个if用来给个位数的前面加上0。
                            startTime.setText("开始时间                " + "0" + valueOf(hour) + ":" + "0" + valueOf(minute));
                        } else if (hour < 10) {
                            startTime.setText("开始时间               " + "0" + valueOf(hour) + ":" + valueOf(minute));
                        } else if (minute < 10) {
                            startTime.setText("开始时间               " + valueOf(hour) + ":" + "0" + valueOf(minute));
                        } else {
                            startTime.setText("开始时间               " + valueOf(hour) + ":" + valueOf(minute));
                        }
//                        displayTimeTemperature();
                        startHour = hour;
                        startMinute = minute;
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                View dialogView = View.inflate(context, R.layout.dialog_time, null);
                TimePicker timePicker = dialogView.findViewById(R.id.timePicker);
                timePicker.setIs24HourView(true);
                timePicker.setOnTimeChangedListener(this);
                dialog.setTitle("设置时间");
                dialog.setView(dialogView);
                dialog.show();
                break;
            case R.id.endtime:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);//新建构造器
                builder1.setPositiveButton("设置", new DialogInterface.OnClickListener() {//设置构造器的正向按钮 名称， 功能
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//参数：dialog发生点击的对话，which发生点击的按钮
                        if (hour < 10 && minute < 10) {//这三个if用来给个位数的前面加上0。
                            endTime.setText("结束时间                " + "0" + valueOf(hour) + ":" + "0" + valueOf(minute));
                        } else if (hour < 10) {
                            endTime.setText("结束时间               " + "0" + valueOf(hour) + ":" + valueOf(minute));
                        } else if (minute < 10) {
                            endTime.setText("结束时间               " + valueOf(hour) + ":" + "0" + valueOf(minute));
                        } else {
                            endTime.setText("结束时间               " + valueOf(hour) + ":" + valueOf(minute));
                        }
//                        displayTimeTemperature();
                        endHour = hour;
                        endMinute = minute;
                    }
                });
                builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog1 = builder1.create();
                View dialogView1 = View.inflate(context, R.layout.dialog_time, null);
                TimePicker timePicker1 = dialogView1.findViewById(R.id.timePicker);
                timePicker1.setIs24HourView(true);
                timePicker1.setOnTimeChangedListener(this);
                dialog1.setTitle("设置时间");
                dialog1.setView(dialogView1);
                dialog1.show();
                break;
            case R.id.setTemp_peroid:
                //实现一个数字选择器
                AlertDialog.Builder builder3 = new AlertDialog.Builder(context);
                builder3.setPositiveButton("设置", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        settingTemp.setText("设置温度               " + valueOf(temp) + "  C");
                        //这里必须要valueof（temp）否则会崩溃
                        temperature = temp;
                    }
                });
                builder3.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog3 = builder3.create();
                View dialogView3 = View.inflate(context, R.layout.dialog_number, null);
                NumberPicker numberPicker = dialogView3.findViewById(R.id.numberpicker);
                numberPicker.setMaxValue(30);
                numberPicker.setMinValue(5);
                numberPicker.setValue(temp);
                numberPicker.setOnValueChangedListener(this);
                dialog3.setTitle("设置温度");
                dialog3.setView(dialogView3);
                dialog3.show();
                break;
        }

    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        this.temperature = newVal;

    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        this.hour =hourOfDay;
        this.minute = minute;

    }
}