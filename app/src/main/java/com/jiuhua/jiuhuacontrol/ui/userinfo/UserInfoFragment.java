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
import com.jiuhua.jiuhuacontrol.database.BasicInfoDB;
import com.jiuhua.jiuhuacontrol.databinding.FragmentUserinfoBinding;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class UserInfoFragment extends Fragment {

    private String userName, userPhone, userAddress, userEmail, userWechat, equipmentGrade, equipmentType;
    private EditText etUserName, etUserPhone, etUserAddress, etUserEmail, etUserWechat, etEquipmentGrade, etEquipmentType;
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
        userAddress = sharedPreferences.getString("userAddress", "安装地址");
        userEmail = sharedPreferences.getString("userEmail", "电子邮件");
        userWechat = sharedPreferences.getString("userWechat", "微信号");
        equipmentGrade = sharedPreferences.getString("equipmentGrade", "主机品牌");
        equipmentType = sharedPreferences.getString("equipmentType", "主机设备型号");
        isBoiler = sharedPreferences.getBoolean("isBoiler",false);

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

        etUserName.setText(userName);
        etUserPhone.setText(userPhone);
        etUserAddress.setText(userAddress);
        etUserEmail.setText(userEmail);
        etUserWechat.setText(userWechat);
        etEquipmentGrade.setText(equipmentGrade);
        etEquipmentType.setText(equipmentType);

        radioButtonBoiler.setChecked(isBoiler);
        radioButtonHeatpump.setChecked(!isBoiler);

        recyclerView = view.findViewById(R.id.userinfo_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(userInfoAdapter);

        userInfoViewModel.getAllBasicInfoLive().observe(getViewLifecycleOwner(), new Observer<List<BasicInfoDB>>() {
            @Override
            public void onChanged(List<BasicInfoDB> basicInfoDBS) {
                userInfoAdapter.setAllBasicInfo(basicInfoDBS);
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

        SharedPreferences.Editor editor = getContext().getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putString("userName", userName);
        editor.putString("userPhone", userPhone);
        editor.putString("userAddress", userAddress);
        editor.putString("userEmail", userEmail);
        editor.putString("userWechat", userWechat);
        editor.putString("equipmentGrade", equipmentGrade);
        editor.putString("equipmentType", equipmentType);
        editor.putBoolean("isBoiler", isBoiler);
        editor.apply();
    }

}