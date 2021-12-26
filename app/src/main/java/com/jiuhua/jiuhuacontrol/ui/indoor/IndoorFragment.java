package com.jiuhua.jiuhuacontrol.ui.indoor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jiuhua.jiuhuacontrol.Constants;
import com.jiuhua.jiuhuacontrol.R;
import com.jiuhua.jiuhuacontrol.database.FancoilSheet;
import com.jiuhua.jiuhuacontrol.database.PeriodSheet;
import com.jiuhua.jiuhuacontrol.database.SensorSheet;
import com.jiuhua.jiuhuacontrol.database.WatershedSheet;
import com.jiuhua.jiuhuacontrol.databinding.FragmentIndoorBinding;
import com.jiuhua.jiuhuacontrol.ui.HomeViewModel;

public class IndoorFragment extends Fragment {

    private FragmentIndoorBinding binding;
    private HomeViewModel homeViewModel;

    int roomId;
    String roomName;
    int currentTemperature;
    int displayAirSetTemperature;
    int displayFloorSetTemperature;
    int accessSetTemperature = 22;

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

        //下面三句也是可以的
        //binding = FragmentRoomBinding.inflate(LayoutInflater.from(getContext()), null, false); //从绑定类吹气
        //binding = FragmentRoomBinding.inflate(inflater, container, false);
        //binding = FragmentRoomBinding.inflate(inflater);
        //**这句不行**  binding = DataBindingUtil.setContentView(getActivity(), R.layout.fragment_indoor);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_indoor, container, false);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);  //涉及一个单例？？
        homeViewModel.setCurrentlyRoomId(roomId);
        homeViewModel.setCurrentlyRoomName(roomName);

        binding.setData(homeViewModel);
        binding.setLifecycleOwner(this);

        for (SensorSheet sensorSheet : homeViewModel.getAllLatestSensorSheets()) {
            if (sensorSheet.getRoomId() == roomId) {
                homeViewModel.setCurrentlySensorSheet(sensorSheet);
            }
        }
        for (FancoilSheet fancoilSheet : homeViewModel.getAllLatestFancoilSheets()) {
            if (fancoilSheet.getRoomId() == roomId) {
                homeViewModel.setCurrentlyFancoilSheet(fancoilSheet);
            }
        }
        for (WatershedSheet watershedSheet : homeViewModel.getAllLatestWatershedSheets()) {
            if (watershedSheet.getRoomId() == roomId) {
                homeViewModel.setCurrentlyWatershedSheet(watershedSheet);
            }
        }
        //不是一定要在这里实现的！！！
        for (PeriodSheet periodSheet : homeViewModel.getAllLatestPeriodSheets()) {
            if (periodSheet.getRoomId() == roomId) {
                homeViewModel.setCurrentRoomPeriodSheet(periodSheet);
            }
        }

        homeViewModel.getAllLatestSensorSheetsLive().observe(getViewLifecycleOwner(), sensorSheets -> {
            homeViewModel.setAllLatestSensorSheets(sensorSheets);
            //****数据驱动界面改变,所以代码要放在fragment或者Activity里面。只处理界面****
            //显示当前温度
            currentTemperature = homeViewModel.getCurrentlySensorSheet().getCurrentTemperature();
            binding.currentTemperatureView.setText(currentTemperature / 10 + "℃");//假浮点需要除以10
        });

        homeViewModel.getAllLatestFancoilSheetsLive().observe(getViewLifecycleOwner(), fancoilSheets -> {
            homeViewModel.setAllLatestFancoilSheets(fancoilSheets);

            //****数据驱动界面改变,所以代码要放在fragment或者Activity里面。只处理界面****

            //以下空调相关显示
            //显示空调设置温度（现在只有一个设置温度）//假浮点需要除以10
            displayAirSetTemperature = homeViewModel.getCurrentlyFancoilSheet().getSettingTemperature() / 10;
            binding.showAirconditionSettingTemperature.setText("空调设置温度             " + displayAirSetTemperature + "℃");
            binding.airconditionSetTemperatureNumber.setText(String.valueOf(accessSetTemperature));
            binding.floorheatTemperatureSetNumber.setText(String.valueOf(accessSetTemperature));

            //TODO 湿度暂时不搞！！
            //显示当前湿度
            //binding.tempHumidityTextView.setText(String.valueOf(homeViewModel.currentlyFancoilSheet.getCurrentHumidity() / 10));
            // 假浮点需要除以10

            //显示设置湿度
            //binding.XXXX.setText(homeViewModel.currentlyFancoilSheet.getSettingHumidity() / 10);
            // 假浮点需要除以10

            //依据房间的状态改变显示的文字(停止，手动，自动)
            switch (homeViewModel.getCurrentlyFancoilSheet().getRoomStatus()) {
                case Constants.roomState_OFF: //stop 0, manual 1, auto 2
                    binding.showAirconditionRunningModel.setText("运行模式                停止模式");
                    break;
                case Constants.roomState_MANUAL:
                    binding.showAirconditionRunningModel.setText("运行模式                手动模式");
                    break;
                case Constants.roomState_AUTO:
                    binding.showAirconditionRunningModel.setText("运行模式                自动模式");
                    break;
                case Constants.roomState_OUTSIDE:
                    binding.showAirconditionRunningModel.setText("运行模式                外出模式");
                    break;
                case Constants.roomState_SLEEP:
                    binding.showAirconditionRunningModel.setText("运行模式                睡眠模式");
                    break;
                case Constants.roomState_DEHUMIDITY:
                    binding.showAirconditionRunningModel.setText("运行模式                除湿模式");
                    break;
                case Constants.roomState_FEAST:
                    binding.showAirconditionRunningModel.setText("运行模式                宴会模式");
                    break;
                default:
                    break;
            }

            //风机状态数据驱动显示的文字变化（高中低及自动风）
            switch (homeViewModel.getCurrentlyFancoilSheet().getCurrentFanStatus()) {
                case Constants.fanSpeed_STOP:
                    binding.showAirconditionRunningFanspeed.setText("风机状态                   停止");
                    break;
                case Constants.fanSpeed_LOW:
                    binding.showAirconditionRunningFanspeed.setText("风机状态                  低速风");
                    break;
                case Constants.fanSpeed_MEDIUM:
                    binding.showAirconditionRunningFanspeed.setText("风机状态                  中速风");
                    break;
                case Constants.fanSpeed_HIGH:
                    binding.showAirconditionRunningFanspeed.setText("风机状态                  高速风");
                    break;
                case Constants.fanSpeed_AUTO:
                    binding.showAirconditionRunningFanspeed.setText("风机状态                  自动风");
                    break;
            }

            //显示空调的运行状态即 两通阀的开关状态
            if (homeViewModel.getCurrentlyFancoilSheet().isCoilValveOpen()) {
                binding.showAirconditionRunningStateCoilvalve.setText(R.string.coilvalveopen);
            } else {
                binding.showAirconditionRunningStateCoilvalve.setText(R.string.coilvalveshut);
            }

        });

        homeViewModel.getAllLatestWatershedSheetsLive().observe(getViewLifecycleOwner(), watershedSheets -> {
            homeViewModel.setAllLatestWatershedSheets(watershedSheets);
            //****数据驱动界面改变,所以代码要放在fragment或者Activity里面。只处理界面****
            //以下地暖参数显示
            //显示地暖设置温度（现在只有一个设置温度）
            displayFloorSetTemperature = homeViewModel.getCurrentlyWatershedSheet().getSettingTemperature() / 10;
            binding.floorheatTemperatureSetNumber.setText(String.valueOf(accessSetTemperature));
            binding.showFloorheatSettingTemperature.setText("地暖设置温度  " + displayFloorSetTemperature + "℃");

            //依据房间的状态改变显示的文字(停止，手动，自动)
            switch (homeViewModel.getCurrentlyWatershedSheet().getRoomStatus()) {
                case Constants.roomState_OFF: //stop 0, manual 1, auto 2
                    binding.showFloorheatRunningModel.setText("运行模式                 停止模式");
                    break;
                case Constants.roomState_MANUAL:
                    binding.showFloorheatRunningModel.setText("运行模式                 手动模式");
                    //binding.showFloorheatRunningModel.setTextColor(0xFFFF0000);
                    break;
                case Constants.roomState_AUTO:
                    binding.showFloorheatRunningModel.setText("运行模式                 自动模式");
                    break;
                case Constants.roomState_OUTSIDE:
                    binding.showFloorheatRunningModel.setText("运行模式                 外出模式");
                    break;
                case Constants.roomState_SLEEP:
                    binding.showFloorheatRunningModel.setText("运行模式                 睡眠模式");
                    break;
                case Constants.roomState_DEHUMIDITY:
                    binding.showFloorheatRunningModel.setText("运行模式                 除湿模式");
                    break;
                case Constants.roomState_FEAST:
                    binding.showFloorheatRunningModel.setText("运行模式                 宴会模式");
                    break;
                default:
                    break;
            }

            //显示地暖的运行状态
            if (homeViewModel.getCurrentlyWatershedSheet().isFloorValveOpen()) {
                binding.showFloorheatRunningStates.setText(R.string.floorvalveopen);
            } else {
                binding.showFloorheatRunningStates.setText(R.string.floorvalveshut);
            }

        });


        return binding.getRoot(); // getRoot() solved databinding problem.
    }

    //这里有操作界面相关的，使用云端转发的方式重新实现具体的操作逻辑
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.airconditionDownTemperature.setOnClickListener(v -> {
            accessSetTemperature--;
            homeViewModel.temperatureToDevice(roomId, accessSetTemperature * 10, Constants.deviceType_toFancoils);
            binding.airconditionSetTemperatureNumber.setText(String.valueOf(accessSetTemperature));
            Toast.makeText(getContext(), roomName + "设置温度为" + accessSetTemperature + "℃", Toast.LENGTH_SHORT).show();
        });
        binding.airconditionUpTemperature.setOnClickListener(v -> {
            accessSetTemperature++;
            homeViewModel.temperatureToDevice(roomId, accessSetTemperature * 10, Constants.deviceType_toFancoils);
            binding.airconditionSetTemperatureNumber.setText(String.valueOf(accessSetTemperature));
            Toast.makeText(getContext(), roomName + "设置温度为" + accessSetTemperature + "℃", Toast.LENGTH_SHORT).show();
        });

        //空调运行模式：
        binding.radioGroupAirconditionSetModel.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radioButton_aircondition_model_Off:
                    homeViewModel.roomstateToDevice(roomId, Constants.roomState_OFF, Constants.deviceType_toFancoils);
                    //Toast.makeText(getContext(), roomName + "空调关闭模式", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                    break;
                case R.id.radioButton_aircondition_mode_Manual:
                    homeViewModel.roomstateToDevice(roomId, Constants.roomState_MANUAL, Constants.deviceType_toFancoils);
                    //Toast.makeText(getContext(), roomName + "空调手动模式", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                    break;
                case R.id.radioButton_aircondition_mode_Automatic:
                    homeViewModel.roomstateToDevice(roomId, Constants.roomState_AUTO, Constants.deviceType_toFancoils);
                    //Toast.makeText(getContext(), roomName + "空调自动模式", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                    break;
                //TODO:
                /**本版不实现。壁挂炉版，两联供高级版再实现
                 * 外出模式的核心：维持一定温度（假目标），防冻，提前升温。
                 *      应该覆盖手动、自动模式，结束时恢复原来的模式。
                 *      是否需要输入时间参数。手机app上还需要美化。
                 * 睡眠模式的核心： 温度降1--2℃
                 *      覆盖手动模式，自动模式吗？
                 *      需要输入时间参数，预升温吗？
                 */
                //case R.id.radioButton_aircondition_mode_Outside:
                //    homeViewModel.roomstateToDevice(roomId, Constants.roomState_OUTSIDE);
                //        Toast.makeText(getContext(), roomName + "空调外出模式", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                //    break;
                //case R.id.radioButton_aircondition_mode_Sleep:
                //    homeViewModel.roomstateToDevice(roomId, Constants.roomState_SLEEP);
                //        Toast.makeText(getContext(), roomName + "空调睡眠模式", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                //    break;
                case R.id.radioButton_aircondition_mode_Humidity:
                    homeViewModel.roomstateToDevice(roomId, Constants.roomState_DEHUMIDITY, Constants.deviceType_toFancoils);
                    //Toast.makeText(getContext(), roomName + "空调除湿模式", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                    break;
                //default:  //好像没有必要
                //    homeViewModel.fanSpeedRoomDevice(roomNameId, Constants.fanSpeed_STOP);
                //    Toast.makeText(getContext(), roomName + "风机盘管停止运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                //    break;
            }

        });

        //风速：
        binding.fanspeed.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radioButtonlowfan:
                    homeViewModel.fanSpeedToDevice(roomId, Constants.fanSpeed_LOW, Constants.deviceType_toFancoils);
                    //Toast.makeText(getContext(), roomName + "风机盘管低风速运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                    break;
                case R.id.radioButtonmiddlefan:
                    homeViewModel.fanSpeedToDevice(roomId, Constants.fanSpeed_MEDIUM, Constants.deviceType_toFancoils);
                    //Toast.makeText(getContext(), roomName + "风机盘管中风速运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                    break;
                case R.id.radioButtonhighfan:
                    homeViewModel.fanSpeedToDevice(roomId, Constants.fanSpeed_HIGH, Constants.deviceType_toFancoils);
                    //Toast.makeText(getContext(), roomName + "风机盘管高风速运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                    break;
                case R.id.radioButtonautofan:
                    homeViewModel.fanSpeedToDevice(roomId, Constants.fanSpeed_AUTO, Constants.deviceType_toFancoils);
                    //Toast.makeText(getContext(), roomName + "风机盘管自动风速运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                    break;
                //default:  //好像没有必要
                //    homeViewModel.fanSpeedRoomDevice(roomNameId, Constants.fanSpeed_STOP);
                //    Toast.makeText(getContext(), roomName + "风机盘管停止运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                //    break;
            }

        });


        //以下地暖的操作
        binding.floorheatTemperatureDown.setOnClickListener(v -> {
            accessSetTemperature--;
            homeViewModel.temperatureToDevice(roomId, accessSetTemperature * 10, Constants.deviceType_phone);
            binding.floorheatTemperatureSetNumber.setText(String.valueOf(accessSetTemperature));
            Toast.makeText(getContext(), roomName + "设置温度为       " + accessSetTemperature + "℃", Toast.LENGTH_SHORT).show();
        });
        binding.floorheatTemperatureUp.setOnClickListener(v -> {
            accessSetTemperature++;
            homeViewModel.temperatureToDevice(roomId, accessSetTemperature * 10, Constants.deviceType_phone);
            binding.floorheatTemperatureSetNumber.setText(String.valueOf(accessSetTemperature));
            Toast.makeText(getContext(), roomName + "设置温度为       " + accessSetTemperature + "℃", Toast.LENGTH_SHORT).show();
        });

        //地暖运行模式：
        binding.radioGroupFloorHeatSetModel.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radioButton_floorheat_model_Off:
                    homeViewModel.roomstateToDevice(roomId, Constants.roomState_OFF, Constants.deviceType_phone);
                    //Toast.makeText(getContext(), roomName + "地暖关闭模式", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                    break;
                case R.id.radioButton_floorheat_model_Manual:
                    homeViewModel.roomstateToDevice(roomId, Constants.roomState_MANUAL, Constants.deviceType_phone);
                    //Toast.makeText(getContext(), roomName + "地暖手动模式", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                    break;
                case R.id.radioButton_floorheat_model_Automatic:
                    homeViewModel.roomstateToDevice(roomId, Constants.roomState_AUTO, Constants.deviceType_phone);
                    //Toast.makeText(getContext(), roomName + "地暖自动模式", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                    break;
                //TODO:
                /**本版不实现。壁挂炉版，两联供高级版再实现
                 * 外出模式的核心：维持一定温度（假目标），防冻，提前升温。
                 *      应该覆盖手动、自动模式，结束时恢复原来的模式。
                 *      是否需要输入时间参数。手机app上还需要美化。
                 * 睡眠模式的核心： 温度降1--2℃
                 *      覆盖手动模式，自动模式吗？
                 *      需要输入时间参数，预升温吗？
                 */
                //case R.id.radioButton_floorheat_model_Outside:
                //    homeViewModel.roomstateToDevice(roomId, Constants.roomState_OUTSIDE);
                //    Toast.makeText(getContext(), roomName + "地暖外出模式", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                //    break;
                //case R.id.radioButton_floorheat_model_Sleep:
                //    homeViewModel.roomstateToDevice(roomId, Constants.roomState_SLEEP);
                //    Toast.makeText(getContext(), roomName + "地暖睡眠模式", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                //    break;
                case R.id.radioButton_floorheat_model_Feast:
                    homeViewModel.roomstateToDevice(roomId, Constants.roomState_FEAST, Constants.deviceType_phone);
                    Toast.makeText(getContext(), roomName + "地暖宴会模式", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                    break;
                //default:  //好像没有必要
                //    homeViewModel.fanSpeedRoomDevice(roomNameId, Constants.fanSpeed_STOP);
                //    Toast.makeText(getContext(), roomName + "风机盘管停止运行", Toast.LENGTH_SHORT).show();//点击就标出了，没有必要显示
                //    break;
            }

        });

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    //@Override
    //public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
    //    super.onViewStateRestored(savedInstanceState);
    //}

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}
