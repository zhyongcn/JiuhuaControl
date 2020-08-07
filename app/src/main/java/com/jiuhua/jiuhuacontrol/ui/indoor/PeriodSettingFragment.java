package com.jiuhua.jiuhuacontrol.ui.indoor;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jiuhua.jiuhuacontrol.R;
import com.jiuhua.jiuhuacontrol.database.DayPeriod;

import java.util.Iterator;

import static android.widget.Toast.LENGTH_SHORT;
import static java.lang.String.valueOf;

public class PeriodSettingFragment extends Fragment implements View.OnClickListener,
        TimePicker.OnTimeChangedListener, NumberPicker.OnValueChangeListener {
    private int roomId;
    private String roomName;
    private int clickedWeekday;
    private int clickedHour;
    private int hour;
    private int minute;
    private int temperature = 24;//default temperature is 24 C .
    private int temp;//临时存储的温度值

    private DayPeriod dayPeriod = new DayPeriod();

    private Context context;
    private IndoorViewModel indoorViewModel;

    TextView textViewTitle, textViewStartTime, textViewEndTime, textViewSettingTemperature, textViewRepeat;
    CheckBox SunCheckBox, MonCheckBox, TueCheckBox, WedCheckBox, ThuCheckBox, FriCheckBox, SatCheckBox;
    Button buttonPeriodComfirm, buttonPeriodCancel;


    public PeriodSettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_peroid_setting, container, false);
        //get the passed parameters
        roomId = getArguments().getInt("roomId");//bundle的参数接收：具体哪个房间。
        roomName = getArguments().getString("roomName");
        clickedWeekday = getArguments().getInt("clickedweekday");//0是周一，6是周日
        clickedHour = getArguments().getInt("clickedhour"); // 0是0:00  23是23:00

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
        //TODO： 新建使用下面的参数，如果是点击已有时段，需要传入该时段的参数。
        dayPeriod.setStartMinuteStamp(clickedHour * 60);
        dayPeriod.setEndMinuteStamp((clickedHour + 1) * 60);
        dayPeriod.setTempreature(temperature);
        dayPeriod.setWeekday(clickedWeekday);

        textViewTitle = view.findViewById(R.id.run_time_title);
        textViewStartTime = view.findViewById(R.id.starttime);
        textViewEndTime = view.findViewById(R.id.endtime);
        textViewSettingTemperature = view.findViewById(R.id.setPeroidTemperature);
//        textViewRepeat = view.findViewById(R.id.repeat);//仅提示而已，没有其他作用。

        MonCheckBox = view.findViewById(R.id.checkBox1);
        TueCheckBox = view.findViewById(R.id.checkBox2);
        WedCheckBox = view.findViewById(R.id.checkBox3);
        ThuCheckBox = view.findViewById(R.id.checkBox4);
        FriCheckBox = view.findViewById(R.id.checkBox5);
        SatCheckBox = view.findViewById(R.id.checkBox6);
        SunCheckBox = view.findViewById(R.id.checkBox7);

        //依据传入的clickedweekday 当前缺省的星期，显示默认的勾选日。
        switch (clickedWeekday) {
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

        buttonPeriodCancel = view.findViewById(R.id.buttonperiodcancel);
        buttonPeriodComfirm = view.findViewById(R.id.buttonperiodcomfirm);

        textViewTitle.setText(roomName + "运行时段设置");
        textViewStartTime.setText("开始时间                 " + clickedHour + ":" + "00");
        textViewEndTime.setText("结束时间                 " + valueOf(clickedHour + 1) + ":" + "00");
        textViewSettingTemperature.setText("设置温度                 " + temperature + " C");

        //TODO 业务逻辑混乱！！（数据架构渐趋合理）
        MonCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                //当前周期在周一重复,检查有无冲突，无冲突写入数组。
                check_daily_fragment_add_to_Weekly_list(Constants.Monday); //include write to List.
            } else {
                //取消当前周期在周一的重复。
                remove_daily_fragment_from_weekly_list(Constants.Monday);
            }
        });
        TueCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                check_daily_fragment_add_to_Weekly_list(Constants.Tuesday);
            } else {
                remove_daily_fragment_from_weekly_list(Constants.Tuesday);
            }
        });
        WedCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                check_daily_fragment_add_to_Weekly_list(Constants.Wednesday);
            } else {
                remove_daily_fragment_from_weekly_list(Constants.Wednesday);
            }
        });
        ThuCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                check_daily_fragment_add_to_Weekly_list(Constants.Thursday);
            } else {
                remove_daily_fragment_from_weekly_list(Constants.Thursday);
            }
        });
        FriCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                check_daily_fragment_add_to_Weekly_list(Constants.Friday);
            } else {
                remove_daily_fragment_from_weekly_list(Constants.Friday);
            }
        });
        SatCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                check_daily_fragment_add_to_Weekly_list(Constants.Saturday);
            } else {
                remove_daily_fragment_from_weekly_list(Constants.Saturday);
            }
        });
        SunCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                check_daily_fragment_add_to_Weekly_list(Constants.Sunday);
            } else {
                remove_daily_fragment_from_weekly_list(Constants.Sunday);
            }
        });


        textViewStartTime.setOnClickListener(this);
        textViewEndTime.setOnClickListener(this);
        textViewSettingTemperature.setOnClickListener(this);
        buttonPeriodCancel.setOnClickListener(this);
        buttonPeriodComfirm.setOnClickListener(this);

        context = getContext();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.starttime:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);//新建构造器
                //设置构造器的正向按钮 名称， 功能
                builder.setPositiveButton("设置", (dialog, which) -> {//参数：dialog发生点击的对话，which发生点击的按钮
                    if (hour < 10 && minute < 10) {//这三个if用来给个位数的前面加上0。
                        textViewStartTime.setText("开始时间                " + "0" + valueOf(hour) + ":" + "0" + valueOf(minute));
                    } else if (hour < 10) {
                        textViewStartTime.setText("开始时间               " + "0" + valueOf(hour) + ":" + valueOf(minute));
                    } else if (minute < 10) {
                        textViewStartTime.setText("开始时间               " + valueOf(hour) + ":" + "0" + valueOf(minute));
                    } else {
                        textViewStartTime.setText("开始时间               " + valueOf(hour) + ":" + valueOf(minute));
                    }
                    dayPeriod.setStartMinuteStamp(hour * 60 + minute);//****关键操作******
                });
                builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
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
                //设置构造器的正向按钮 名称， 功能
                builder1.setPositiveButton("设置", (dialog12, which) -> {//参数：dialog发生点击的对话，which发生点击的按钮
                    if (hour < 10 && minute < 10) {//这三个if用来给个位数的前面加上0。
                        textViewEndTime.setText("结束时间                " + "0" + valueOf(hour) + ":" + "0" + valueOf(minute));
                    } else if (hour < 10) {
                        textViewEndTime.setText("结束时间               " + "0" + valueOf(hour) + ":" + valueOf(minute));
                    } else if (minute < 10) {
                        textViewEndTime.setText("结束时间               " + valueOf(hour) + ":" + "0" + valueOf(minute));
                    } else {
                        textViewEndTime.setText("结束时间               " + valueOf(hour) + ":" + valueOf(minute));
                    }
                    dayPeriod.setEndMinuteStamp(hour * 60 + minute);//********关键操作********
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
            case R.id.setPeroidTemperature:
                //实现一个数字选择器
                AlertDialog.Builder builder3 = new AlertDialog.Builder(context);
                builder3.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textViewSettingTemperature.setText("设置温度               " + valueOf(temp) + "  C");
                        //这里必须要valueof（temp）否则会崩溃
                        temperature = temp;
                        dayPeriod.setTempreature(temp);
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
                temperature = temp;
                break;
            case R.id.buttonperiodcancel:
                getActivity().onBackPressed();
                break;
            case R.id.buttonperiodcomfirm:
                if (check_daily_fragment_add_to_Weekly_list(clickedWeekday) == 1) {
                    //TODO 参数应该是一个修改好的 list<Dayperiod> 。
                    indoorViewModel.insertPeriodDB(roomId);
                }
                break;
        }

    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        this.temp = newVal;
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
    }

    //检查周期是否冲突的方法
    private int check_daily_fragment_add_to_Weekly_list(int weekday) {  //传入weekday指定是哪一天的周期。0 is Monday 。
        Gson gson = new Gson();
        DayPeriod dayPeriodFromJson;
        String s = gson.toJson(this.dayPeriod);
        dayPeriodFromJson = gson.fromJson(s, DayPeriod.class);
        dayPeriodFromJson.setWeekday(weekday);

        int start = dayPeriodFromJson.getStartMinuteStamp();
        int end = dayPeriodFromJson.getEndMinuteStamp();
        //小于15分钟的设置不予执行。Not do the period less than 15 minute.
        if (dayPeriodFromJson.getEndMinuteStamp() - dayPeriodFromJson.getStartMinuteStamp() <= 15) {
            Toast.makeText(getContext(), "设置时间不对", LENGTH_SHORT).show();
            return -1;
        }
        if (indoorViewModel.currentlyPeriodDB.getOneRoomWeeklyPeriod().size() > 0) {
            //循环判断所有周期段
            for (DayPeriod d : indoorViewModel.currentlyPeriodDB.getOneRoomWeeklyPeriod()) {
                //同一天的才判断（星期几相同）
                if (d.getWeekday() == weekday) {
                    int Tstart = d.getStartMinuteStamp();//距离零点的分钟数
                    int Tend = d.getEndMinuteStamp();
                    //判断 开始时间 是否在已经设置的时间段之内
                    if (start > Tstart && start < Tend) {
                        Toast.makeText(getContext(), "运行时间段冲突", LENGTH_SHORT).show();//需要显示冲突多少次吗？
                        return -1;
                    }
                    //判断 结束时间 是否在已经设置的时间段之内
                    if (end > Tstart && end < Tend) {
                        Toast.makeText(getContext(), "运行时间段冲突", LENGTH_SHORT).show();//需要显示冲突多少次吗？
                        return -1;
                    }
                    //判断 是否 开始时间在时间段之前，结束时间在时间段之后。即跨了该时间段
                    if (start < Tstart && end > Tend) {
                        Toast.makeText(getContext(), "运行时间段冲突", LENGTH_SHORT).show();//需要显示冲突多少次吗？
                        return -1;
                    }
                }
            }//这个循环结束，时间段就没有冲突了
        }

        //通过之后就是合理的数据，先写入list，再排序，再删除超过6个的。
        indoorViewModel.currentlyPeriodDB.getOneRoomWeeklyPeriod().add(dayPeriodFromJson);

        //排序
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                indoorViewModel.currentlyPeriodDB.getOneRoomWeeklyPeriod()[wday].sort(comparingInt(DayPeriod::getStartMinuteStamp));
//            }
        //删除多于六个的
//            if (indoorViewModel.currentlyPeriodDB.getOneRoomWeeklyPeriod()[wday].size() > 6) {  //限制一天六个时段。Limited to six periods a day.
//                indoorViewModel.currentlyPeriodDB.getOneRoomWeeklyPeriod()[wday]
//                        = indoorViewModel.currentlyPeriodDB.getOneRoomWeeklyPeriod()[wday].subList(0,
//                        indoorViewModel.currentlyPeriodDB.getOneRoomWeeklyPeriod()[wday].size() - 1);
//            }


        return 1;
    }

    private void remove_daily_fragment_from_weekly_list(int weekday) {
        Gson gson = new Gson();
        DayPeriod dayPeriodFromJson;
        String s = gson.toJson(this.dayPeriod);
        dayPeriodFromJson = gson.fromJson(s, DayPeriod.class);
        dayPeriodFromJson.setWeekday(weekday);

        //这个方法不熟练，没有做好，不熟悉 iterator。
//        Iterator<DayPeriod> iterator = indoorViewModel.currentlyPeriodDB.getOneRoomWeeklyPeriod().iterator();
//        while (iterator.hasNext()) {
//            if (dayPeriodFromJson.getStartMinuteStamp() == iterator.next().getStartMinuteStamp()
//                    && dayPeriodFromJson.getEndMinuteStamp() == iterator.next().getEndMinuteStamp()
//                    && dayPeriodFromJson.getWeekday() == iterator.next().getWeekday()) {
//                iterator.remove();
//            }
//        }

        for (int i = 0; i < indoorViewModel.currentlyPeriodDB.getOneRoomWeeklyPeriod().size(); i++) {
            if (dayPeriodFromJson.getStartMinuteStamp() == indoorViewModel.currentlyPeriodDB.getOneRoomWeeklyPeriod().get(i).getStartMinuteStamp()
                    && dayPeriodFromJson.getEndMinuteStamp() == indoorViewModel.currentlyPeriodDB.getOneRoomWeeklyPeriod().get(i).getEndMinuteStamp()
                    && dayPeriodFromJson.getWeekday() == indoorViewModel.currentlyPeriodDB.getOneRoomWeeklyPeriod().get(i).getWeekday()) {
                indoorViewModel.currentlyPeriodDB.getOneRoomWeeklyPeriod().remove(i);
                i--;
            }
        }
        Log.d("remove", gson.toJson(indoorViewModel.currentlyPeriodDB));

    }


}