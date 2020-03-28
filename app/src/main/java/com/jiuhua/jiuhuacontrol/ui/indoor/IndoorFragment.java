package com.jiuhua.jiuhuacontrol.ui.indoor;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.jiuhua.jiuhuacontrol.R;
import com.jiuhua.jiuhuacontrol.database.IndoorDB;
import com.jiuhua.jiuhuacontrol.databinding.FragmentIndoorBinding;

import java.util.List;

/**
 * 替换以前的RoomActivity
 * A simple {@link Fragment} subclass.
 */
public class IndoorFragment extends Fragment {

    private FragmentIndoorBinding binding;
    private IndoorViewModel indoorViewModel;

    int roomNameId;
    String roomName;

    public IndoorFragment(int roomNameId, String roomName) {
        this.roomNameId = roomNameId;
        this.roomName = roomName;
        // Required empty public constructor 传参了
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //这里不再需要原始的创建视图的方法了,这个方法也生成视图但是没有绑定数据
        //View view = inflater.inflate(R.layout.fragment_indoor, container, false);

//        binding = FragmentRoomBinding.inflate(LayoutInflater.from(getContext()), null, false); //从绑定类吹气
//        binding = FragmentRoomBinding.inflate(inflater, container, false);
//        binding = FragmentRoomBinding.inflate(inflater);
//        //**这句不行**  binding = DataBindingUtil.setContentView(getActivity(), R.layout.fragment_indoor);
        //上面三句也是可以的
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_indoor, container, false);

        indoorViewModel = ViewModelProviders.of(this).get(IndoorViewModel.class);
        binding.setData(indoorViewModel);
        binding.setLifecycleOwner(this);

        indoorViewModel.setRoomNameId(roomNameId);
        indoorViewModel.setRoomName(roomName);

        indoorViewModel.getAllLatestIndoorDBsLive().observe(getViewLifecycleOwner(), new Observer<List<IndoorDB>>() {
            @Override
            public void onChanged(List<IndoorDB> indoorDBS) {
//                indoorViewModel.setAllLatestIndoorDBs(indoorDBS);
                indoorViewModel.setLatestIndoorDB(indoorDBS.get(roomNameId));
//                IndoorDB indoorDB = indoorViewModel.latestIndoorDB;

                //数据驱动界面改变,所以代码要放在fragment或者Activity里面。只处理界面
                if (indoorViewModel.latestIndoorDB.isCoilValveOpen()) {
                    binding.textViewCoilValve.setText(R.string.coilvalveopen);
                } else {
                    binding.textViewCoilValve.setText(R.string.coilvalveshut);
                }
                if (indoorViewModel.latestIndoorDB.isFloorValveOpen()) {
                    binding.textViewFloorValve.setText(R.string.floorvalveopen);
                } else {
                    binding.textViewFloorValve.setText(R.string.floorvalveshut);
                }
                binding.tempTemperaturextview.setText(String.valueOf(indoorViewModel.latestIndoorDB.getCurrentTemperature()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.temperatureSeekBar.setProgress(indoorViewModel.latestIndoorDB.getSettingTemperature(), true);
                }
                binding.tempHumidityTextView.setText(String.valueOf(indoorViewModel.latestIndoorDB.getCurrentHumidity()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.humiditySeekBar.setProgress(indoorViewModel.latestIndoorDB.getSettingHumidity(), true);
                }
                if (indoorViewModel.latestIndoorDB.isFloorValveOpen()){
                    binding.buttonfloor.setBackgroundColor(Color.parseColor("#00FF00"));
                }else {
                    binding.buttonfloor.setBackgroundColor(Color.argb(20, 0, 0, 0));
                }
                if (indoorViewModel.latestIndoorDB.isDehumidityStatus()){
                    binding.buttonhumidity.setBackgroundColor(Color.parseColor("#00FF00"));
                }else {
                    binding.buttonhumidity.setBackgroundColor(Color.argb(20, 0, 0, 0));
                }
                //风机状态数据驱动相关按钮颜色的变化（高中低及自动风）
                switch (indoorViewModel.latestIndoorDB.getFanStatus()) {
                    case 0: //stop 0, low 1, middle 2, high 3, auto 4.
                        binding.fanspeed.clearCheck();
                        break;
                    case 1:
                        binding.radioButtonlowfan.setChecked(true);
                        break;
                    case 2:
                        binding.radioButtonmiddlefan.setChecked(true);
                        break;
                    case 3:
                        binding.radioButtonhighfan.setChecked(true);
                        break;
                    case 4:
                        binding.radioButtonautofan.setChecked(true);
                        break;
                }
                //依据房间的状态改变按钮的颜色(停止，手动，自动)
                switch (indoorViewModel.latestIndoorDB.getRoomStatus()) {
                    case 0: //stop 0, manual 1, auto 2
                        binding.buttonStop.setBackgroundColor(Color.parseColor("#00FF00"));
                        binding.buttonManual.setBackgroundColor(Color.argb(20, 0, 0, 0));
                        binding.buttonAuto.setBackgroundColor(Color.argb(20, 0, 0, 0));
                        break;
                    case 1:
                        binding.buttonStop.setBackgroundColor(Color.argb(20, 0, 0, 0));
                        binding.buttonManual.setBackgroundColor(Color.parseColor("#00FF00"));
                        binding.buttonAuto.setBackgroundColor(Color.argb(20, 0, 0, 0));
                        break;
                    case 2:
                        binding.buttonStop.setBackgroundColor(Color.argb(20, 0, 0, 0));
                        binding.buttonManual.setBackgroundColor(Color.argb(20, 0, 0, 0));
                        binding.buttonAuto.setBackgroundColor(Color.parseColor("#00FF00"));
                        break;
                }
            }
        });

        return binding.getRoot(); // getRoot() solved databinding problem.
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //温度和湿度的设置进度条，收取数据。
        binding.temperatureSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                indoorViewModel.setSettingTemperature(progress);
                Toast.makeText(getContext(),"设置要求温度为"+progress+"摄氏度", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.humiditySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                indoorViewModel.setSettingHumidity(progress);
                Toast.makeText(getContext(),"设置要求湿度为"+progress+"%RH", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //地暖开关，除湿开关，风速选择，房间状态选择实现。
        binding.buttonfloor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indoorViewModel.latestIndoorDB.isFloorValveOpen()){
                    //TODO 关地暖
                    Toast.makeText(getContext(),"guan地暖", Toast.LENGTH_SHORT).show();
                }else {
                    //开地暖
                    Toast.makeText(getContext(),"开地暖", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.buttonhumidity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indoorViewModel.latestIndoorDB.isDehumidityStatus()){
                    //stop 除湿
                    Toast.makeText(getContext(),"turn off 除湿", Toast.LENGTH_SHORT).show();
                }else {
                    //start Dehumidity
                    Toast.makeText(getContext(),"start 除湿", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.fanspeed.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButtonlowfan:
                        Toast.makeText(getContext(),"start 低风速", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonmiddlefan:
                        Toast.makeText(getContext(),"start 中风速", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonhighfan:
                        Toast.makeText(getContext(),"start 高风速", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonautofan:
                        Toast.makeText(getContext(),"start zidong风速", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getContext(),"start 风速wu", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });
        binding.buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(getView(), "停止房间所有设备的运行", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        binding.buttonManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(getView(), "房间设备手动运行", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        binding.buttonAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(getView(), "自动运行房间所有设备", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
