package com.jiuhua.jiuhuacontrol.ui.indoor;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.jiuhua.jiuhuacontrol.R;
import com.jiuhua.jiuhuacontrol.database.IndoorDB;
import com.jiuhua.jiuhuacontrol.databinding.FragmentIndoorBinding;

import java.util.List;

public class IndoorFragment extends Fragment {

    private FragmentIndoorBinding binding;
    private IndoorViewModel indoorViewModel;

    int roomNameId;
    String roomName;

    public IndoorFragment(int roomNameId, String roomName) {
        this.roomNameId = roomNameId;//这里传入的ID有问题，房间2传来的是 1。
        this.roomName = roomName;
        // Required empty public constructor    传参了
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

        indoorViewModel = ViewModelProviders.of(this).get(IndoorViewModel.class);//涉及一个单例？？
        binding.setData(indoorViewModel);
        binding.setLifecycleOwner(this);

        indoorViewModel.setRoomNameId(roomNameId);
        indoorViewModel.setRoomName(roomName);

        indoorViewModel.getAllLatestIndoorDBsLive().observe(getViewLifecycleOwner(), new Observer<List<IndoorDB>>() {
            @Override
            public void onChanged(List<IndoorDB> indoorDBS) {
                indoorViewModel.setAllLatestIndoorDBs(indoorDBS);
//                indoorViewModel.setLatestIndoorDB(indoorDBS.get(roomNameId - 1));  //好像多余，viewmodel里面有赋值
                                                                         // 传入的时候再k值上加 1 了，现在要减 1 ，否则队列越界，
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
                binding.tempTemperaturextview.setText(String.valueOf(indoorViewModel.latestIndoorDB.getCurrentTemperature() / 10));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.temperatureSeekBar.setProgress(indoorViewModel.latestIndoorDB.getSettingTemperature(), true);
                }
                binding.tempHumidityTextView.setText(String.valueOf(indoorViewModel.latestIndoorDB.getCurrentHumidity() / 10));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.humiditySeekBar.setProgress(indoorViewModel.latestIndoorDB.getSettingHumidity(), true);
                }
                if (indoorViewModel.latestIndoorDB.isFloorValveOpen()) {
                    binding.buttonfloor.setBackgroundColor(Color.parseColor("#00FF00"));
                } else {
                    binding.buttonfloor.setBackgroundColor(Color.argb(20, 0, 0, 0));
                }
                if (indoorViewModel.latestIndoorDB.getRoomStatus() == Constants.roomState_DEHUMIDITY) {
                    binding.buttonhumidity.setBackgroundColor(Color.parseColor("#00FF00"));
                } else {
                    binding.buttonhumidity.setBackgroundColor(Color.argb(20, 0, 0, 0));
                }
                //风机状态数据驱动相关按钮颜色的变化（高中低及自动风）
                switch (indoorViewModel.latestIndoorDB.getCurrentFanStatus()) {
                    case Constants.fanSpeed_STOP:
                        binding.fanspeed.clearCheck();
                        break;
                    case Constants.fanSpeed_LOW:
                        binding.radioButtonlowfan.setChecked(true);
                        break;
                    case Constants.fanSpeed_MEDIUM:
                        binding.radioButtonmiddlefan.setChecked(true);
                        break;
                    case Constants.fanSpeed_HIGH:
                        binding.radioButtonhighfan.setChecked(true);
                        break;
                    case Constants.fanSpeed_AUTO:
                        binding.radioButtonautofan.setChecked(true);
                        break;
                }
                //依据房间的状态改变按钮的颜色(停止，手动，自动)
                switch (indoorViewModel.latestIndoorDB.getRoomStatus()) {
                    case Constants.roomState_OFF: //stop 0, manual 1, auto 2
                        binding.buttonStop.setBackgroundColor(Color.parseColor("#00FF00"));
                        binding.buttonManual.setBackgroundColor(Color.argb(20, 0, 0, 0));
                        binding.buttonAuto.setBackgroundColor(Color.argb(20, 0, 0, 0));
                        break;
                    case Constants.roomState_MANUAL:
                        binding.buttonStop.setBackgroundColor(Color.argb(20, 0, 0, 0));
                        binding.buttonManual.setBackgroundColor(Color.parseColor("#00FF00"));
                        binding.buttonAuto.setBackgroundColor(Color.argb(20, 0, 0, 0));
                        break;
                    case Constants.roomState_AUTO:
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

        //温度设置进度条，收取数据。
        binding.temperatureSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int temp_P;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                temp_P = progress;//这里的数字会不停的变，所以先存储一下。
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {//touch结束之后再执行逻辑。
                indoorViewModel.temperatureToRoomDevice(roomNameId, temp_P);
                Toast.makeText(getContext(), roomName + "设置温度为" + temp_P / 10 + "摄氏度", Toast.LENGTH_SHORT).show();
            }
        });
        //湿度设置进度条，收取数据。
        binding.humiditySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int temp_P;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                temp_P = progress;//这里的数字会不停的变，所以先存储一下。
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                indoorViewModel.humidityToRoomDevice(roomNameId, temp_P);
                Toast.makeText(getContext(), roomName + "设置湿度为" + temp_P / 10 + "%RH", Toast.LENGTH_SHORT).show();
            }
        });
        //地暖开关
        binding.buttonfloor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indoorViewModel.latestIndoorDB.isFloorValveOpen()) {
                    //关地暖
                    indoorViewModel.floorRoomDevice(roomNameId, Constants.roomState_OFF);
                    Toast.makeText(getContext(), roomName + "关闭地暖", Toast.LENGTH_SHORT).show();
                } else {
                    //开地暖
                    indoorViewModel.floorRoomDevice(roomNameId, Constants.roomState_FEAST);
                    Toast.makeText(getContext(), roomName + "打开地暖", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //除湿按钮：
        binding.buttonhumidity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indoorViewModel.latestIndoorDB.getRoomStatus() == Constants.roomState_DEHUMIDITY) {
                    //stop Dehumidify is turnoff fancoil。
                    indoorViewModel.dehumidityRoomDevice(roomNameId, Constants.roomState_OFF);
                    Toast.makeText(getContext(), roomName + "停止除湿", Toast.LENGTH_SHORT).show();
                } else {
                    //start Dehumidity
                    //TODO 再传送一边设定湿度？？
                    indoorViewModel.dehumidityRoomDevice(roomNameId, Constants.roomState_DEHUMIDITY);
                    indoorViewModel.floorRoomDevice(roomNameId, Constants.roomState_OFF); //除湿的时候水温很低，需要关闭地暖。
                    Toast.makeText(getContext(), roomName + "开始除湿", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //风速：
        binding.fanspeed.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButtonlowfan:
                        indoorViewModel.fanSpeedRoomDevice(roomNameId, Constants.fanSpeed_LOW);
                        Toast.makeText(getContext(), roomName + "风机盘管低风速运行", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonmiddlefan:
                        indoorViewModel.fanSpeedRoomDevice(roomNameId, Constants.fanSpeed_MEDIUM);
                        Toast.makeText(getContext(), roomName + "风机盘管中风速运行", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonhighfan:
                        indoorViewModel.fanSpeedRoomDevice(roomNameId, Constants.fanSpeed_HIGH);
                        Toast.makeText(getContext(), roomName + "风机盘管高风速运行", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonautofan:
                        indoorViewModel.fanSpeedRoomDevice(roomNameId, Constants.fanSpeed_AUTO);
                        Toast.makeText(getContext(), roomName + "风机盘管自动风速运行", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        indoorViewModel.fanSpeedRoomDevice(roomNameId, Constants.fanSpeed_STOP);
                        Toast.makeText(getContext(), roomName + "风机盘管停止运行", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });
        //房间状态：停止/手动/周期自动
        binding.buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indoorViewModel.stopRoomDevice(roomNameId);
                Toast.makeText(getContext(), roomName + "设备停止运行", Toast.LENGTH_SHORT).show();//挡住了按钮，不好看
            }
        });
        binding.buttonManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indoorViewModel.manualRoomDevice(roomNameId);
                Toast.makeText(getContext(), roomName + "设备手动运行", Toast.LENGTH_SHORT).show();
            }
        });
        binding.buttonAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indoorViewModel.autoRoomDevice(roomNameId);
                Toast.makeText(getContext(), roomName + "设备自动按周期运行", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //FIXME 停止使用一段时间再启动，从堆栈中，或者图标，只要不是全新的启动，不能使用MQTT发布信息。
    //fixme 挂接电脑调试，始终有mqtt进来的信息，不存在上面一行的问题。
    //TODO   下面的方法没有尝试，不知道效果。
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser){
//            //可见时的代码
//        }else {
//            //不可见时的代码
//        }
//    }


}
