package com.jiuhua.jiuhuacontrol.ui.userinfo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jiuhua.jiuhuacontrol.R;
import com.jiuhua.jiuhuacontrol.database.BasicInfoSheet;
import com.jiuhua.jiuhuacontrol.Constants;

import java.util.ArrayList;
import java.util.List;


public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.MyViewHolder> {

    private List<BasicInfoSheet> allBasicInfo = new ArrayList<>();
    private UserInfoViewModel userInfoViewModel;

    public UserInfoAdapter(UserInfoViewModel userInfoViewModel) {
        this.userInfoViewModel = userInfoViewModel;
    }

    //在数据库里面插入一个新的房间，会自动获取主键
    public void addBasicInfo() {
        userInfoViewModel.insertBasicInfo(new BasicInfoSheet());
    }

    public void setAllBasicInfo(List<BasicInfoSheet> allBasicInfo) {
        this.allBasicInfo = allBasicInfo;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.userinfo_cell_layout, parent, false);

        final MyViewHolder holder = new MyViewHolder(itemView);

//        holder.editTextRoomName.addTextChangedListener(new TextWatcher() {
//            int k = holder.getAdapterPosition();   //获取具体哪个条目了
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
////                allBasicInfoToUpdate.get(k).setRoomName(s.toString());
////                allBasicInfoToUpdate.get(k).setId(allBasicInfo.get(k).getId());
//            }
//        });

        //这部分代码完成历史使命，帮助搞清楚原理，
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int k = holder.getAdapterPosition();   //获取具体哪个条目了
//                //下面五条运行没有问题
////                Log.d("userinfo_position", String.valueOf(k));
////                allBasicInfo.get(k).setFancoilTrademark("yangzi");
////                userInfoViewModel.updateBasicInfo(allBasicInfo.get(k));
////                userInfoViewModel.insertBasicInfo(new BasicInfoSheet());
////                Navigation.findNavController(v).navigate(R.id.action_nav_userinfo_to_peroidFragment);
//            }
//        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        //具体视图元素赋值。
        BasicInfoSheet basicInfoSheet = allBasicInfo.get(position);

        holder.editTextRoomName.setText(basicInfoSheet.getRoomName());
        holder.editTextRoomId.setText(String.valueOf(basicInfoSheet.getRoomId()));
        holder.editTextSensorCalibration.setText(String.valueOf(basicInfoSheet.getTemperatureSensorCalibration()));
        holder.editTextFancoilTradeMark.setText(basicInfoSheet.getFancoilTrademark());
        holder.editTextFancoilType.setText(basicInfoSheet.getFanCoilType());
        holder.checkBoxCoilvalve.setChecked(basicInfoSheet.isHasCoilValve());
        holder.checkBoxHasFloorHeating.setChecked(basicInfoSheet.isFloorHeat());
        holder.checkBoxIsFloorHeatingAuto.setChecked(basicInfoSheet.isFloorAuto());
        holder.editTextRadiatorTradeMark.setText(basicInfoSheet.getRadiatorTrademark());
        holder.editTextRadiatorType.setText(basicInfoSheet.getRadiatorType());
        holder.checkBoxIsRadiatorValveAuto.setChecked(basicInfoSheet.isRadiatorAuto());

        //如果是耗时很少的操作可以在这里出现，或者item的内部有很多部件需要绑定，也在这里。

        holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
            int k = holder.getAdapterPosition();   //获取具体哪个条目了

            @Override
            public void onClick(View v) {
                //TODO 检查一下，房间编号必填。同时不能重复。
                if (Integer.parseInt(holder.editTextRoomId.getText().toString()) != 0) {
                    BasicInfoSheet basicInfoSheet = new BasicInfoSheet();
                    basicInfoSheet.setId(allBasicInfo.get(k).getId());//update是通过主键 id 匹配的。
                    basicInfoSheet.setRoomName(holder.editTextRoomName.getText().toString());
                    basicInfoSheet.setRoomId(Integer.parseInt(holder.editTextRoomId.getText().toString()));
                    basicInfoSheet.setTemperatureSensorCalibration(Integer.parseInt(holder.editTextSensorCalibration.getText().toString()));
                    basicInfoSheet.setFancoilTrademark(holder.editTextFancoilTradeMark.getText().toString());
                    basicInfoSheet.setFanCoilType(holder.editTextFancoilType.getText().toString());
                    basicInfoSheet.setHasCoilValve(holder.checkBoxCoilvalve.isChecked());
                    basicInfoSheet.setFloorHeat(holder.checkBoxHasFloorHeating.isChecked());
                    basicInfoSheet.setFloorAuto(holder.checkBoxIsFloorHeatingAuto.isChecked());
                    basicInfoSheet.setRadiatorTrademark(holder.editTextRadiatorTradeMark.getText().toString());
                    basicInfoSheet.setRadiatorType(holder.editTextRadiatorType.getText().toString());
                    basicInfoSheet.setRadiatorAuto(holder.checkBoxIsRadiatorValveAuto.isChecked());

                    userInfoViewModel.updateBasicInfo(basicInfoSheet);
                    Toast.makeText(v.getContext(), "你修改了房间信息", Toast.LENGTH_SHORT).show();

                    //添加校准温湿度的逻辑。发送的MQTTmessage 的qos必须是1，或2，0是不行的。！！
                    // 接收端的.disable_clean_session = 1 禁止清除会话，可以理解为mqtt的缓存
                    Gson gson = new Gson();
                    JsonObject jsonObject = new JsonObject(); //temp object for send temperatureSensorCalibration information.
                    jsonObject.addProperty("roomId", basicInfoSheet.getRoomId());
                    jsonObject.addProperty("deviceType", Constants.deviceType_phone);//手机来的命令才接受。DHT，NTC模块接收不在mqttconfig里面？
                    //假浮点，在手机上转换，减轻模块压力。
                    jsonObject.addProperty("adjustingTemperature", basicInfoSheet.getTemperatureSensorCalibration() * 10);
                    String msg = gson.toJson(jsonObject);
                    //TODO：需要走myRepository里的commandtodevice命令。?? 重启服务, 好像不是必要。
//                    MQTTService.myPublishToDevice(basicInfoSheet.getRoomId(), msg, 1, false);

                } else {
                    Toast.makeText(v.getContext(), "房间编号必须填写", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            int k = holder.getAdapterPosition();   //获取具体哪个条目了

            @Override
            public void onClick(View v) {
                BasicInfoSheet basicInfoSheet = new BasicInfoSheet();
                basicInfoSheet.setId(allBasicInfo.get(k).getId());//删除是通过主键 id 匹配的。
                userInfoViewModel.deleteBasicInfo(basicInfoSheet);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allBasicInfo.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        EditText editTextRoomName, editTextRoomId, editTextSensorCalibration, editTextFancoilTradeMark, editTextFancoilType;
        CheckBox checkBoxCoilvalve, checkBoxHasFloorHeating, checkBoxIsFloorHeatingAuto;
        EditText editTextRadiatorTradeMark, editTextRadiatorType;
        CheckBox checkBoxIsRadiatorValveAuto;
        Button buttonEdit, buttonDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            editTextRoomName = itemView.findViewById(R.id.user_roomname);
            editTextRoomId = itemView.findViewById(R.id.user_roomId);
            editTextSensorCalibration = itemView.findViewById(R.id.sensorcalibration);
            editTextFancoilTradeMark = itemView.findViewById(R.id.room_fancoil_brand);
            editTextFancoilType = itemView.findViewById(R.id.room_fancoil_type);
            checkBoxCoilvalve = itemView.findViewById(R.id.room_coilvalve_checkBox);
            checkBoxHasFloorHeating = itemView.findViewById(R.id.room_floor_checkBox);
            checkBoxIsFloorHeatingAuto = itemView.findViewById(R.id.room_autofloor_checkBox);
            editTextRadiatorTradeMark = itemView.findViewById(R.id.room_radiator_brand);
            editTextRadiatorType = itemView.findViewById(R.id.room_radiator_type);
            checkBoxIsRadiatorValveAuto = itemView.findViewById(R.id.room_radiatorvalve_auto);
            buttonEdit = itemView.findViewById(R.id.button_edit);
            buttonDelete = itemView.findViewById(R.id.button_delete);
        }
    }

}
