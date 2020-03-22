package com.jiuhua.jiuhuacontrol.ui.userinfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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


public class UserInfoFragment extends Fragment {

    private UserInfoViewModel userInfoViewModel;
    private FragmentUserinfoBinding binding;

    Button buttonAddUserInfo;

    RecyclerView recyclerView;
    UserInfoAdapter userInfoAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        userInfoViewModel = new ViewModelProvider(this).get(UserInfoViewModel.class);
        userInfoAdapter = new UserInfoAdapter(userInfoViewModel);
        binding = FragmentUserinfoBinding.inflate(LayoutInflater.from(getContext()), null, false); //从绑定类吹气
        binding.setData(userInfoViewModel);
        binding.setLifecycleOwner(this);

        //增加一个房间
        buttonAddUserInfo = binding.buttonAddUserinfo;
        buttonAddUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfoAdapter.addBasicInfo();  //调用adapter的方法
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
}