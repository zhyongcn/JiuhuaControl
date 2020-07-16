package com.jiuhua.jiuhuacontrol.ui.indoor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiuhua.jiuhuacontrol.R;

public class PeroidSettingFragment extends Fragment {



    public PeroidSettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_peroid_setting, container, false);
        int roomNameId = getArguments().getInt("roomnameID");//bundle的参数接收：具体哪个房间。
        String roomName = getArguments().getString("roomName");
        int weekday = getArguments().getInt("weekday");
        int hour = getArguments().getInt("hour");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


}