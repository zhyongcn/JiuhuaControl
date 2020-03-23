package com.jiuhua.jiuhuacontrol.ui.indoor;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.jiuhua.jiuhuacontrol.R;
import com.jiuhua.jiuhuacontrol.databinding.FragmentIndoorBinding;

/**
 * 替换以前的RoomActivity
 * A simple {@link Fragment} subclass.
 */
public class IndoorFragment extends Fragment {

    private FragmentIndoorBinding binding;
    private IndoorViewModel indoorViewModel;

    public IndoorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //这里不再需要原始的创建视图的方法了,这个方法也生成视图但是没有绑定数据
//        View view = inflater.inflate(R.layout.fragment_indoor, container, false);

//        binding = FragmentRoomBinding.inflate(LayoutInflater.from(getContext()), null, false); //从绑定类吹气
//        binding = FragmentRoomBinding.inflate(inflater, container, false);
//        binding = FragmentRoomBinding.inflate(inflater);
//        //**这句不行**  binding = DataBindingUtil.setContentView(getActivity(), R.layout.fragment_indoor);
        //上面三句也是可以的
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_indoor, container, false);

        indoorViewModel = new ViewModelProvider(this).get(IndoorViewModel.class);
        binding.setData(indoorViewModel);
        binding.setLifecycleOwner(this);

        binding.temperatureSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                indoorViewModel.setSettingTemperature(progress);
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
                indoorViewModel.setSettingHumidity(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //数据驱动界面改变,所以代码要放在fragment或者Activity里面。只处理界面
        //风机状态数据驱动相关按钮颜色的变化（高中低及自动风）
        final Observer<IndoorViewModel.FanSpeed> stateOfFanSpeed = new Observer<IndoorViewModel.FanSpeed>() {
            @Override
            public void onChanged(IndoorViewModel.FanSpeed fanSpeed) {
                switch (fanSpeed) {
                    case STOP:
                        binding.fanspeed.clearCheck();
                        break;
                    case LOW:
                        binding.radioButtonlowfan.setChecked(true);
                        break;
                    case MEDIUM:
                        binding.radioButtonmiddlefan.setChecked(true);
                        break;
                    case HIGH:
                        binding.radioButtonhighfan.setChecked(true);
                        break;
                    case AUTO:
                        binding.radioButtonautofan.setChecked(true);
                        break;
                }
            }
        };
        indoorViewModel.getFanStatus().observe(getViewLifecycleOwner(), stateOfFanSpeed);

        //依据房间的状态改变按钮的颜色(停止，手动，自动)
        final Observer<IndoorViewModel.RoomState> stateOfRoom = new Observer<IndoorViewModel.RoomState>() {
            @Override
            public void onChanged(IndoorViewModel.RoomState roomState) {
                switch (roomState) {
                    case STOP:
                        binding.buttonStop.setBackgroundColor(Color.parseColor("#00FF00"));
                        binding.buttonManual.setBackgroundColor(Color.argb(20, 0, 0, 0));
                        binding.buttonAuto.setBackgroundColor(Color.argb(20, 0, 0, 0));
                        break;
                    case MANUAL:
                        binding.buttonStop.setBackgroundColor(Color.argb(20, 0, 0, 0));
                        binding.buttonManual.setBackgroundColor(Color.parseColor("#00FF00"));
                        binding.buttonAuto.setBackgroundColor(Color.argb(20, 0, 0, 0));
                        break;
                    case AUTO:
                        binding.buttonStop.setBackgroundColor(Color.argb(20, 0, 0, 0));
                        binding.buttonManual.setBackgroundColor(Color.argb(20, 0, 0, 0));
                        binding.buttonAuto.setBackgroundColor(Color.parseColor("#00FF00"));
                        break;
                }
            }
        };
        indoorViewModel.getRoomState().observe(getViewLifecycleOwner(), stateOfRoom);

        return binding.getRoot(); // getRoot() solved databinding problem.
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        //临时验证方法
//        indoorViewModel.setRoomName("客厅");
//        indoorViewModel.setSettingTemperature(22);
//        indoorViewModel.setSettingHumidity(50);
//        indoorViewModel.setCoilValveOpen("两通阀开");
//        indoorViewModel.setFloorValveOpen("地暖开");
    }
}
