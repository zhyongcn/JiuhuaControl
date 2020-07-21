package com.jiuhua.jiuhuacontrol.ui.indoor;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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

        indoorViewModel = new ViewModelProvider(this).get(IndoorViewModel.class);  //涉及一个单例？？
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

                //****数据驱动界面改变,所以代码要放在fragment或者Activity里面。只处理界面****
                //显示两通阀的开关
                if (indoorViewModel.latestIndoorDB.isCoilValveOpen()) {
                    binding.textViewCoilValve.setText(R.string.coilvalveopen);
                } else {
                    binding.textViewCoilValve.setText(R.string.coilvalveshut);
                }
                //显示地暖的开关
                if (indoorViewModel.latestIndoorDB.isFloorValveOpen()) {
                    binding.textViewFloorValve.setText(R.string.floorvalveopen);
                } else {
                    binding.textViewFloorValve.setText(R.string.floorvalveshut);
                }
                //显示当前温度
                binding.tempTemperaturextview.setText(String.valueOf(indoorViewModel.latestIndoorDB.getCurrentTemperature() / 10));//假浮点需要除以10
                //显示设置温度
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.temperatureSeekBar.setProgress(indoorViewModel.latestIndoorDB.getSettingTemperature() / 10, true);//假浮点需要除以10
                }
                //显示当前湿度
                binding.tempHumidityTextView.setText(String.valueOf(indoorViewModel.latestIndoorDB.getCurrentHumidity() / 10));//假浮点需要除以10
                //显示设置湿度
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.humiditySeekBar.setProgress(indoorViewModel.latestIndoorDB.getSettingHumidity() / 10, true);//假浮点需要除以10
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
                        binding.buttonStop.setBackgroundColor(Color.parseColor("#FF0000"));
                        binding.switchManualAuto.setChecked(false);
                        binding.buttonFeastDehumidity.setBackgroundColor(Color.argb(20, 0, 0, 0));//除湿&宴会按钮灰色
                        break;
                    case Constants.roomState_MANUAL:
                        binding.buttonStop.setBackgroundColor(Color.argb(20, 0, 0, 0));
                        binding.switchManualAuto.setChecked(false);
                        binding.buttonFeastDehumidity.setBackgroundColor(Color.argb(20, 0, 0, 0));//除湿&宴会按钮灰色
                        break;
                    case Constants.roomState_AUTO:
                        binding.buttonStop.setBackgroundColor(Color.argb(20, 0, 0, 0));
                        binding.switchManualAuto.setChecked(true);
                        binding.buttonFeastDehumidity.setBackgroundColor(Color.argb(20, 0, 0, 0));//除湿&宴会按钮灰色
                        break;
                    case Constants.roomState_DEHUMIDITY:
                        //除湿按钮的显示
                        binding.buttonFeastDehumidity.setBackgroundColor(Color.parseColor("#00FF00"));
                        binding.buttonFeastDehumidity.setText("除湿");
                        break;
                    case Constants.roomState_FEAST:
                        //宴会按钮的显示
                        binding.buttonFeastDehumidity.setBackgroundColor(Color.parseColor("#00FF00"));
                        binding.buttonFeastDehumidity.setText("宴会");
                        break;
                    default:
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
        //风速：
        binding.fanspeed.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButtonlowfan:
                        indoorViewModel.fanSpeedRoomDevice(roomNameId, Constants.fanSpeed_LOW);
//                        Toast.makeText(getContext(), roomName + "风机盘管低风速运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                        break;
                    case R.id.radioButtonmiddlefan:
                        indoorViewModel.fanSpeedRoomDevice(roomNameId, Constants.fanSpeed_MEDIUM);
//                        Toast.makeText(getContext(), roomName + "风机盘管中风速运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                        break;
                    case R.id.radioButtonhighfan:
                        indoorViewModel.fanSpeedRoomDevice(roomNameId, Constants.fanSpeed_HIGH);
//                        Toast.makeText(getContext(), roomName + "风机盘管高风速运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                        break;
                    case R.id.radioButtonautofan:
                        indoorViewModel.fanSpeedRoomDevice(roomNameId, Constants.fanSpeed_AUTO);
//                        Toast.makeText(getContext(), roomName + "风机盘管自动风速运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                        break;
//                    default:  //好像没有必要
//                        indoorViewModel.fanSpeedRoomDevice(roomNameId, Constants.fanSpeed_STOP);
//                        Toast.makeText(getContext(), roomName + "风机盘管停止运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
//                        break;
                }

                //还是使用从数据库中提取的返回数据来驱动界面，不要多此一举在这里修改了。
            }
        });


        //除湿和宴会 按钮功能的实现
        binding.buttonFeastDehumidity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indoorViewModel.commandESP.getDeviceType() == Constants.deviceType_floorheater) {
                    indoorViewModel.feastRoomDevice(roomNameId);
                    binding.buttonFeastDehumidity.setBackgroundColor(Color.parseColor("#00FF00"));//先显示，模块数据回来会更改的
                    binding.buttonStop.setBackgroundColor(Color.argb(20, 0, 0, 0));
                } else if (indoorViewModel.commandESP.getDeviceType() == Constants.deviceType_fancoil) {
                    indoorViewModel.dehumidityRoomDevice(roomNameId);
                    binding.buttonFeastDehumidity.setBackgroundColor(Color.parseColor("#00FF00"));//先显示，模块数据回来会更改的
                    binding.buttonStop.setBackgroundColor(Color.argb(20, 0, 0, 0));
                    binding.radioButtonlowfan.setChecked(true);
                }
            }
        });

        //自动&手动切换按钮功能
        binding.switchManualAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    indoorViewModel.autoRoomDevice(roomNameId);
                    binding.buttonStop.setBackgroundColor(Color.argb(20, 0, 0, 0));
                } else {
                    indoorViewModel.manualRoomDevice(roomNameId);
                    binding.buttonStop.setBackgroundColor(Color.argb(20, 0, 0, 0));
                }
            }
        });

        //停止按钮
        binding.buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indoorViewModel.stopRoomDevice(roomNameId);
                binding.buttonStop.setBackgroundColor(Color.parseColor("#FF0000"));
                binding.buttonFeastDehumidity.setBackgroundColor(Color.argb(20, 0, 0, 0));
                binding.fanspeed.clearCheck();
//                Toast.makeText(getContext(), roomName + "设备停止运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
            }
        });

        //空调&地暖切换按钮功能
        binding.switchFancoilOrFloor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    indoorViewModel.floorRoomDevice(roomNameId);
                    binding.buttonFeastDehumidity.setText("宴会");
                    binding.buttonStop.setBackgroundColor(Color.argb(20, 0, 0, 0));
                } else {
                    indoorViewModel.fancoilRoomDevice(roomNameId);
                    binding.buttonFeastDehumidity.setText("除湿");
                    binding.buttonStop.setBackgroundColor(Color.argb(20, 0, 0, 0));
                }
            }
        });


    }


}
