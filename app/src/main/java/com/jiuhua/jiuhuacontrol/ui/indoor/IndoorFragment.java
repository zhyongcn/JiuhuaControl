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

import com.google.android.material.snackbar.Snackbar;
import com.jiuhua.jiuhuacontrol.R;
import com.jiuhua.jiuhuacontrol.database.IndoorDB;
import com.jiuhua.jiuhuacontrol.databinding.FragmentIndoorBinding;
import com.jiuhua.mqttsample.MQTTService;

import java.util.List;

public class IndoorFragment extends Fragment {

    private FragmentIndoorBinding binding;
    private IndoorViewModel indoorViewModel;

    int roomNameId;
    String roomName;

    public IndoorFragment(int roomNameId, String roomName) {
        this.roomNameId = roomNameId;//这里传入的ID有问题，房间2传来的是 1。
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
                indoorViewModel.setAllLatestIndoorDBs(indoorDBS);
                indoorViewModel.setLatestIndoorDB(indoorDBS.get(roomNameId-1));//传入的时候再k值上加 1 了，现在要减 1 ，否则队列越界，
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
            int temp_P;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                temp_P = progress;//这里的数字会不停的变，所以先存储一下。
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {//touch结束之后再执行逻辑。
                MQTTService.publish("86518/JYCFGC/6-2-3401/Room" + roomNameId,
                        temp_P + "Room"+roomNameId+"set_temp", 1, true);
                Toast.makeText(getContext(),roomName+"设置温度为"+ temp_P +"摄氏度", Toast.LENGTH_SHORT).show();
            }
        });
        binding.humiditySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int temp_P;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                temp_P = progress;//这里的数字会不停的变，所以先存储一下。
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MQTTService.publish("86518/JYCFGC/6-2-3401/Room" + roomNameId,
                        temp_P + "Room"+roomNameId+"set_humidity", 1, true);
                Toast.makeText(getContext(),roomName+"设置湿度为"+ temp_P +"%RH", Toast.LENGTH_SHORT).show();
            }
        });
        //地暖开关，除湿开关，风速选择，房间状态选择实现。
        binding.buttonfloor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indoorViewModel.latestIndoorDB.isFloorValveOpen()){
                    //关地暖
                    MQTTService.publish("86518/JYCFGC/6-2-3401/Room" + roomNameId,
                             "Room" + roomNameId + "turn-offfloor", 1, true);
                    Toast.makeText(getContext(),roomName+"关闭地暖", Toast.LENGTH_SHORT).show();
                }else {
                    //开地暖
                    MQTTService.publish("86518/JYCFGC/6-2-3401/Room" + roomNameId,
                            "Room" + roomNameId + "manual-onfloor", 1, true);
                    Toast.makeText(getContext(),roomName+"打开地暖", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.buttonhumidity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indoorViewModel.latestIndoorDB.isDehumidityStatus()){
                    //stop Dehumidify is turnoff fancoil。
                    MQTTService.publish("86518/JYCFGC/6-2-3401/Room" + roomNameId,
                            "Room" + roomNameId + "turn-offFP", 1, true);
                    Toast.makeText(getContext(),roomName+"停止除湿", Toast.LENGTH_SHORT).show();
                }else {
                    //start Dehumidity
                    //TODO 再传送一边设定湿度？？
                    MQTTService.publish("86518/JYCFGC/6-2-3401/Room" + roomNameId,
                            "Room" + roomNameId + "deHumidity", 1, true);
                    MQTTService.publish("86518/JYCFGC/6-2-3401/Room" + roomNameId,
                            "Room" + roomNameId + "turn-offfloor", 1, true);//除湿的时候水温很低，需要关闭地暖。
                    Toast.makeText(getContext(),roomName+"开始除湿", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.fanspeed.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButtonlowfan:
                        //TODO 具体设置代码
                        Toast.makeText(getContext(),roomName+"风机盘管低风速运行", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonmiddlefan:
                        //TODO 具体设置代码
                        Toast.makeText(getContext(),roomName+"风机盘管中风速运行", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonhighfan:
                        //TODO 具体设置代码
                        Toast.makeText(getContext(),roomName+"风机盘管高风速运行", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonautofan:
                        //TODO 具体设置代码
                        Toast.makeText(getContext(),roomName+"风机盘管自动风速运行", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        //TODO 具体设置代码
                        Toast.makeText(getContext(),roomName+"风机盘管停止运行", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });
        binding.buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 具体设置代码，使用Json传输的时候需要再调整！！
                indoorViewModel.stopRoomEquipment(roomNameId);
                Snackbar.make(getView(), roomName+"设备停止运行", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });
        binding.buttonManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 具体设置代码
                Snackbar.make(getView(), roomName+"设备手动运行", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });
        binding.buttonAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 具体设置代码
                Snackbar.make(getView(), roomName+"设备自动按周期运行", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

    }
}
