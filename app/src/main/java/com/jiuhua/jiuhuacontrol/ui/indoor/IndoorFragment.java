package com.jiuhua.jiuhuacontrol.ui.indoor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jiuhua.jiuhuacontrol.Constants;
import com.jiuhua.jiuhuacontrol.R;
import com.jiuhua.jiuhuacontrol.databinding.FragmentIndoorBinding;

public class IndoorFragment extends Fragment {

    private FragmentIndoorBinding binding;
    private IndoorViewModel indoorViewModel;

    int roomId;
    String roomName;
    int temp_P;

    public IndoorFragment(int roomId, String roomName) {
        this.roomId = roomId;//这里传入的ID有问题，房间2传来的是 1。
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

        indoorViewModel.setCurrentlyRoomId(roomId);
        indoorViewModel.setCurrentlyRoomName(roomName);

        indoorViewModel.getAllLatestIndoorDBsLive(Constants.deviceType_floorwatershed).observe(getViewLifecycleOwner(), indoorDBS -> {
            indoorViewModel.setAllLatestIndoorDBs(indoorDBS);
            //****数据驱动界面改变,所以代码要放在fragment或者Activity里面。只处理界面****
            //显示当前温度
            binding.currentTemperatureView.setText(String.valueOf(indoorViewModel.currentlyIndoorDB.getCurrentTemperature() / 10) + "℃");//假浮点需要除以10

            //以下空调相关显示
            //显示空调设置温度（现在只有一个设置温度）
            binding.showAirconditionSettingTemperature.setText("空调设置温度  " + (indoorViewModel.currentlyIndoorDB.getSettingTemperature() / 10) + "℃");//假浮点需要除以10

            //TODO 湿度暂时不搞！！
            //显示当前湿度
//            binding.tempHumidityTextView.setText(String.valueOf(indoorViewModel.currentlyIndoorDB.getCurrentHumidity() / 10));//假浮点需要除以10

            //显示设置湿度
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                binding.humiditySeekBar.setProgress(indoorViewModel.currentlyIndoorDB.getSettingHumidity() / 10, true);//假浮点需要除以10
//            }

            //依据房间的状态改变显示的文字(停止，手动，自动)
            switch (indoorViewModel.currentlyIndoorDB.getRoomStatus()) {
                case Constants.roomState_OFF: //stop 0, manual 1, auto 2
                    binding.showAirconditionRunningModel.setText("运行模式      停止模式");
                    break;
                case Constants.roomState_MANUAL:
                    binding.showAirconditionRunningModel.setText("运行模式      手动模式");
                    break;
                case Constants.roomState_AUTO:
                    binding.showAirconditionRunningModel.setText("运行模式      自动模式");
                    break;
                case Constants.roomState_DEHUMIDITY:
                    binding.showAirconditionRunningModel.setText("运行模式      除湿模式");
                    break;
                case Constants.roomState_FEAST:
                    binding.showAirconditionRunningModel.setText("运行模式      宴会模式");
                    break;
                default:
                    break;
            }

            //风机状态数据驱动显示的文字变化（高中低及自动风）
            switch (indoorViewModel.currentlyIndoorDB.getCurrentFanStatus()) {
                case Constants.fanSpeed_STOP:
                    binding.showAirconditionRunningFanspeed.setText("风机状态    停止");
                    break;
                case Constants.fanSpeed_LOW:
                    binding.showAirconditionRunningFanspeed.setText("风机状态    低速风");
                    break;
                case Constants.fanSpeed_MEDIUM:
                    binding.showAirconditionRunningFanspeed.setText("风机状态    中速风");
                    break;
                case Constants.fanSpeed_HIGH:
                    binding.showAirconditionRunningFanspeed.setText("风机状态    高速风");
                    break;
                case Constants.fanSpeed_AUTO:
                    binding.showAirconditionRunningFanspeed.setText("风机状态    自动风");
                    break;
            }

            //显示空调的运行状态即 两通阀的开关状态
            if (indoorViewModel.currentlyIndoorDB.isCoilValveOpen()) {
                binding.showAirconditionRunningStateCoilvalve.setText(R.string.coilvalveopen);
            } else {
                binding.showAirconditionRunningStateCoilvalve.setText(R.string.coilvalveshut);
            }

            //以下地暖相关显示
            //显示地暖设置温度（现在只有一个设置温度）
            binding.showFloorheatSettingTemperature.setText("地暖设置温度  " + (indoorViewModel.currentlyIndoorDB.getSettingTemperature() / 10) + "℃");//假浮点需要除以10

            //依据房间的状态改变显示的文字(停止，手动，自动)
            switch (indoorViewModel.currentlyIndoorDB.getRoomStatus()) {
                case Constants.roomState_OFF: //stop 0, manual 1, auto 2
                    binding.showFloorheatRunningModel.setText("运行模式      停止模式");
                    break;
                case Constants.roomState_MANUAL:
                    binding.showFloorheatRunningModel.setText("运行模式      手动模式");
                    break;
                case Constants.roomState_AUTO:
                    binding.showFloorheatRunningModel.setText("运行模式      自动模式");
                    break;
                case Constants.roomState_DEHUMIDITY:
                    binding.showFloorheatRunningModel.setText("运行模式      除湿模式");
                    break;
                case Constants.roomState_FEAST:
                    binding.showFloorheatRunningModel.setText("运行模式      宴会模式");
                    break;
                default:
                    break;
            }

            //显示地暖的运行状态
            if (indoorViewModel.currentlyIndoorDB.isFloorValveOpen()) {
                binding.showFloorheatRunningStates.setText(R.string.floorvalveopen);
            } else {
                binding.showFloorheatRunningStates.setText(R.string.floorvalveshut);
            }

        });

        return binding.getRoot(); // getRoot() solved databinding problem.
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        temp_P = indoorViewModel.currentlyIndoorDB.getSettingTemperature() / 10;

        EditText setTemperature = binding.airconditionSetTemperatureNumber;
        setTemperature.setText(String.valueOf(temp_P));

        binding.airconditionDownTemperature.setOnClickListener(v -> {
            temp_P--;
            indoorViewModel.temperatureToRoomDevice(roomId, temp_P * 10);//fixme 假浮点？？
            Toast.makeText(getContext(), roomName + "设置温度为" + temp_P + "℃", Toast.LENGTH_SHORT).show();
        });
        binding.airconditionUpTemperature.setOnClickListener(v -> {
            temp_P++;
            indoorViewModel.temperatureToRoomDevice(roomId, temp_P * 10);//fixme 假浮点？？
            Toast.makeText(getContext(), roomName + "设置温度为" + temp_P + "℃", Toast.LENGTH_SHORT).show();
        });

        //空调运行模式：FIXME 命令需要抽象归纳一下
        binding.radioGroupAirconditionSetModel.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radioButton_aircondition_model_Off:
                    //TODO something
                    indoorViewModel.fanSpeedRoomDevice(roomId, Constants.roomState_OFF);
//                        Toast.makeText(getContext(), roomName + "风机盘管低风速运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                    break;
                case R.id.radioButton_aircondition_mode_Manual:
                    //TODO something
                    indoorViewModel.fanSpeedRoomDevice(roomId, Constants.roomState_MANUAL);
//                        Toast.makeText(getContext(), roomName + "风机盘管中风速运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                    break;
                case R.id.radioButton_aircondition_mode_Automatic:
                    //TODO something
                    indoorViewModel.fanSpeedRoomDevice(roomId, Constants.roomState_AUTO);
//                        Toast.makeText(getContext(), roomName + "风机盘管高风速运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                    break;
                case R.id.radioButton_aircondition_mode_Outside:
                    //TODO something
                    indoorViewModel.fanSpeedRoomDevice(roomId, Constants.roomState_OUTSIDE);
//                        Toast.makeText(getContext(), roomName + "风机盘管自动风速运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                case R.id.radioButton_aircondition_mode_Sleep:
                    //TODO something
                    indoorViewModel.fanSpeedRoomDevice(roomId, Constants.roomState_SLEEP);
//                        Toast.makeText(getContext(), roomName + "风机盘管自动风速运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                case R.id.radioButton_aircondition_mode_Humidity:
                    //TODO something
                    indoorViewModel.fanSpeedRoomDevice(roomId, Constants.roomState_DEHUMIDITY);
//                        Toast.makeText(getContext(), roomName + "风机盘管自动风速运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                    break;
//                    default:  //好像没有必要
//                        indoorViewModel.fanSpeedRoomDevice(roomNameId, Constants.fanSpeed_STOP);
//                        Toast.makeText(getContext(), roomName + "风机盘管停止运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
//                        break;
            }

            //还是使用从数据库中提取的返回数据来驱动界面，不要多此一举在这里修改了。
        });

        //风速：
        binding.fanspeed.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radioButtonlowfan:
                    indoorViewModel.fanSpeedRoomDevice(roomId, Constants.fanSpeed_LOW);
//                        Toast.makeText(getContext(), roomName + "风机盘管低风速运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                    break;
                case R.id.radioButtonmiddlefan:
                    indoorViewModel.fanSpeedRoomDevice(roomId, Constants.fanSpeed_MEDIUM);
//                        Toast.makeText(getContext(), roomName + "风机盘管中风速运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                    break;
                case R.id.radioButtonhighfan:
                    indoorViewModel.fanSpeedRoomDevice(roomId, Constants.fanSpeed_HIGH);
//                        Toast.makeText(getContext(), roomName + "风机盘管高风速运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                    break;
                case R.id.radioButtonautofan:
                    indoorViewModel.fanSpeedRoomDevice(roomId, Constants.fanSpeed_AUTO);
//                        Toast.makeText(getContext(), roomName + "风机盘管自动风速运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                    break;
//                    default:  //好像没有必要
//                        indoorViewModel.fanSpeedRoomDevice(roomNameId, Constants.fanSpeed_STOP);
//                        Toast.makeText(getContext(), roomName + "风机盘管停止运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
//                        break;
            }

            //还是使用从数据库中提取的返回数据来驱动界面，不要多此一举在这里修改了。
        });


//        //除湿和宴会 按钮功能的实现
//        binding.buttonFeastDehumidity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (indoorViewModel.commandESP.getDeviceType() == Constants.deviceType_floorwatershed) {
//                    indoorViewModel.feastRoomDevice(roomId);
//                    binding.buttonFeastDehumidity.setBackgroundColor(Color.parseColor("#00FF00"));//先显示，模块数据回来会更改的
//                    binding.buttonStop.setBackgroundColor(Color.argb(20, 0, 0, 0));
//                } else if (indoorViewModel.commandESP.getDeviceType() == Constants.deviceType_fancoil) {
//                    indoorViewModel.dehumidityRoomDevice(roomId);
//                    binding.buttonFeastDehumidity.setBackgroundColor(Color.parseColor("#00FF00"));//先显示，模块数据回来会更改的
//                    binding.buttonStop.setBackgroundColor(Color.argb(20, 0, 0, 0));
//                    binding.radioButtonlowfan.setChecked(true);
//                }
//            }
//        });
//
//        //自动&手动切换按钮功能
//        binding.switchManualAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    indoorViewModel.autoRoomDevice(roomId);
//                    binding.buttonStop.setBackgroundColor(Color.argb(20, 0, 0, 0));
//                } else {
//                    indoorViewModel.manualRoomDevice(roomId);
//                    binding.buttonStop.setBackgroundColor(Color.argb(20, 0, 0, 0));
//                }
//            }
//        });
//
//        //停止按钮
//        binding.buttonStop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                indoorViewModel.stopRoomDevice(roomId);
//                binding.buttonStop.setBackgroundColor(Color.parseColor("#FF0000"));
//                binding.buttonFeastDehumidity.setBackgroundColor(Color.argb(20, 0, 0, 0));
//                binding.fanspeed.clearCheck();
////                Toast.makeText(getContext(), roomName + "设备停止运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
//            }
//        });
//
//        //空调&地暖切换按钮功能
//        binding.switchFancoilOrFloor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    indoorViewModel.floorRoomDevice(roomId);
//                    binding.buttonFeastDehumidity.setText("宴会");
//                    binding.buttonStop.setBackgroundColor(Color.argb(20, 0, 0, 0));
//                } else {
//                    indoorViewModel.fancoilRoomDevice(roomId);
//                    binding.buttonFeastDehumidity.setText("除湿");
//                    binding.buttonStop.setBackgroundColor(Color.argb(20, 0, 0, 0));
//                }
//            }
//        });
//
//
        binding.floorheatTemperatureSetNumber.setText(String.valueOf(temp_P));

        binding.floorheatTemperatureDown.setOnClickListener(v -> {
            temp_P--;
            indoorViewModel.temperatureToRoomDevice(roomId, temp_P * 10);//fixme 假浮点？？
            Toast.makeText(getContext(), roomName + "设置温度为" + temp_P + "℃", Toast.LENGTH_SHORT).show();
        });
        binding.floorheatTemperatureUp.setOnClickListener(v -> {
            temp_P++;
            indoorViewModel.temperatureToRoomDevice(roomId, temp_P * 10);//fixme 假浮点？？
            Toast.makeText(getContext(), roomName + "设置温度为" + temp_P + "℃", Toast.LENGTH_SHORT).show();
        });

        //地暖运行模式：FIXME 命令需要抽象归纳一下
        binding.radioGroupFloorHeatSetModel.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radioButton_floorheat_model_Off:
                    //TODO something
                    indoorViewModel.fanSpeedRoomDevice(roomId, Constants.roomState_OFF);
//                        Toast.makeText(getContext(), roomName + "风机盘管低风速运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                    break;
                case R.id.radioButton_floorheat_model_Manual:
                    //TODO something
                    indoorViewModel.fanSpeedRoomDevice(roomId, Constants.roomState_MANUAL);
//                        Toast.makeText(getContext(), roomName + "风机盘管中风速运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                    break;
                case R.id.radioButton_floorheat_model_Automatic:
                    //TODO something
                    indoorViewModel.fanSpeedRoomDevice(roomId, Constants.roomState_AUTO);
//                        Toast.makeText(getContext(), roomName + "风机盘管高风速运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                    break;
                case R.id.radioButton_floorheat_model_Outside:
                    //TODO something
                    indoorViewModel.fanSpeedRoomDevice(roomId, Constants.roomState_OUTSIDE);
//                        Toast.makeText(getContext(), roomName + "风机盘管自动风速运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                case R.id.radioButton_floorheat_model_Sleep:
                    //TODO something
                    indoorViewModel.fanSpeedRoomDevice(roomId, Constants.roomState_SLEEP);
//                        Toast.makeText(getContext(), roomName + "风机盘管自动风速运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                case R.id.radioButton_floorheat_model_Feast:
                    //TODO something
                    indoorViewModel.fanSpeedRoomDevice(roomId, Constants.roomState_FEAST);
//                        Toast.makeText(getContext(), roomName + "风机盘管自动风速运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                    break;
//                    default:  //好像没有必要
//                        indoorViewModel.fanSpeedRoomDevice(roomNameId, Constants.fanSpeed_STOP);
//                        Toast.makeText(getContext(), roomName + "风机盘管停止运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
//                        break;
            }

            //还是使用从数据库中提取的返回数据来驱动界面，不要多此一举在这里修改了。

        });
    }

}
