package com.jiuhua.jiuhuacontrol.ui.userinfo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jiuhua.jiuhuacontrol.R;
import com.jiuhua.jiuhuacontrol.database.BasicInfoSheet;
import com.jiuhua.jiuhuacontrol.databinding.FragmentUserinfoBinding;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class UserInfoFragment extends Fragment {

    private String userName, userPhone, userAddress, userEmail, userWechat, equipmentGrade, equipmentType;
    private String mqtt_host, mqtt_client_name, mqtt_client_passwd, mqtt_family_topic, mqtt_publish_topic_prefix, mqtt_client_id;//MQTT相关
    private EditText etUserName, etUserPhone, etUserAddress, etUserEmail, etUserWechat, etEquipmentGrade, etEquipmentType;
    private EditText etMQTT_Host, etMQTT_client_name, etMQTT_client_passwd, etMQTTt_family_topic, etMQTT_publish_topic_prefix, etMQTT_client_id;//MQTT相关
    private boolean isBoiler;
    private RadioButton radioButtonBoiler, radioButtonHeatpump;

    private UserInfoViewModel userInfoViewModel;
    private FragmentUserinfoBinding binding;

    Button buttonAddUserInfo;

    RecyclerView recyclerView;
    UserInfoAdapter userInfoAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //store some simple information.
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("data", MODE_PRIVATE);
        userName = sharedPreferences.getString("userName", "姓名");
        userPhone = sharedPreferences.getString("userPhone", "手机号码");
        //TODO: 安装地址拆分为86518，小区名，楼号单元户号
        userAddress = sharedPreferences.getString("userAddress", "安装地址");
        userEmail = sharedPreferences.getString("userEmail", "电子邮件");
        userWechat = sharedPreferences.getString("userWechat", "微信号");
        equipmentGrade = sharedPreferences.getString("equipmentGrade", "主机品牌");
        equipmentType = sharedPreferences.getString("equipmentType", "主机设备型号");
        isBoiler = sharedPreferences.getBoolean("isBoiler", false);
        //MQTT相关
        mqtt_host = sharedPreferences.getString("mqtt_host", "");
        mqtt_client_name = sharedPreferences.getString("mqtt_client_name", "");
        mqtt_client_passwd = sharedPreferences.getString("mqtt_client_passwd", "");
        mqtt_family_topic = sharedPreferences.getString("mqtt_family_topic", "");
        mqtt_publish_topic_prefix = sharedPreferences.getString("mqtt_publish_topic_prefix", "");
        mqtt_client_id = sharedPreferences.getString("mqtt_client_id", "");

        userInfoViewModel = new ViewModelProvider(this).get(UserInfoViewModel.class);
        userInfoAdapter = new UserInfoAdapter(userInfoViewModel);
        binding = FragmentUserinfoBinding.inflate(LayoutInflater.from(getContext()), null, false); //从绑定类吹气
        binding.setData(userInfoViewModel);
        binding.setLifecycleOwner(this);

        //增加一个房间
        buttonAddUserInfo = binding.buttonAddUserinfo;
        buttonAddUserInfo.setOnClickListener(v -> {
            userInfoAdapter.addBasicInfo();  //调用adapter的方法
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etUserName = binding.username;
        etUserPhone = binding.userphone;
        etUserAddress = binding.useraddress;
        etUserEmail = binding.useremail;
        etUserWechat = binding.userwechat;
        etEquipmentGrade = binding.equipmetgrade;
        etEquipmentType = binding.equipmenttype;
        radioButtonBoiler = binding.userinfoBoiler;
        radioButtonHeatpump = binding.userinfoHeatpump;
        //MQTT相关
        etMQTT_Host = binding.host;
        etMQTT_client_name = binding.mqttClientName;
        etMQTT_client_passwd = binding.mqttClientPasswd;
        etMQTTt_family_topic = binding.mqttFamilyTopic;
        etMQTT_publish_topic_prefix = binding.MQTTPublishTopicPrefix;
        etMQTT_client_id = binding.mqttClientId;

        etUserName.setText(userName);
        etUserPhone.setText(userPhone);
        etUserAddress.setText(userAddress);
        etUserEmail.setText(userEmail);
        etUserWechat.setText(userWechat);
        etEquipmentGrade.setText(equipmentGrade);
        etEquipmentType.setText(equipmentType);
        //MQTT相关
        etMQTT_Host.setText(mqtt_host);
        etMQTT_client_name.setText(mqtt_client_name);
        etMQTT_client_passwd.setText(mqtt_client_passwd);
        etMQTTt_family_topic.setText(mqtt_family_topic);
        etMQTT_publish_topic_prefix.setText(mqtt_publish_topic_prefix);
        etMQTT_client_id.setText(mqtt_client_id);

        radioButtonBoiler.setChecked(isBoiler);
        radioButtonHeatpump.setChecked(!isBoiler);

        recyclerView = view.findViewById(R.id.userinfo_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(userInfoAdapter);

        userInfoViewModel.getAllBasicInfoLive().observe(getViewLifecycleOwner(), new Observer<List<BasicInfoSheet>>() {
            @Override
            public void onChanged(List<BasicInfoSheet> basicInfoSheets) {
                userInfoAdapter.setAllBasicInfo(basicInfoSheets);
                userInfoAdapter.notifyDataSetChanged();
            }
        });

    }


    @Override
    public void onPause() {
        super.onPause();
        userName = etUserName.getText().toString();
        userPhone = etUserPhone.getText().toString();
        userAddress = etUserAddress.getText().toString();
        userEmail = etUserEmail.getText().toString();
        userWechat = etUserWechat.getText().toString();
        equipmentGrade = etEquipmentGrade.getText().toString();
        equipmentType = etEquipmentType.getText().toString();
        isBoiler = radioButtonBoiler.isChecked();
        //获取MQTT的相关信息
        mqtt_host = etMQTT_Host.getText().toString();
        mqtt_client_name = etMQTT_client_name.getText().toString();
        mqtt_client_passwd = etMQTT_client_passwd.getText().toString();
        mqtt_family_topic = etMQTTt_family_topic.getText().toString();
        mqtt_publish_topic_prefix = etMQTT_publish_topic_prefix.getText().toString();
        mqtt_client_id = etMQTT_client_id.getText().toString();

        SharedPreferences.Editor editor = getContext().getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putString("userName", userName);
        editor.putString("userPhone", userPhone);
        editor.putString("userAddress", userAddress);
        editor.putString("userEmail", userEmail);
        editor.putString("userWechat", userWechat);
        editor.putString("equipmentGrade", equipmentGrade);
        editor.putString("equipmentType", equipmentType);
        editor.putBoolean("isBoiler", isBoiler);
        //MQTT相关
        editor.putString("mqtt_host", mqtt_host);
        editor.putString("mqtt_client_name", mqtt_client_name);
        editor.putString("mqtt_client_passwd", mqtt_client_passwd);
        editor.putString("mqtt_family_topic", mqtt_family_topic);
        editor.putString("mqtt_publish_topic_prefix", mqtt_publish_topic_prefix);
        editor.putString("mqtt_client_id", mqtt_client_id);
        editor.apply();
    }

}