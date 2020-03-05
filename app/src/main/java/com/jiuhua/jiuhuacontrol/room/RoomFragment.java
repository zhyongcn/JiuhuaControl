package com.jiuhua.jiuhuacontrol.room;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jiuhua.jiuhuacontrol.R;

/**
 * 替换以前的RoomActivity
 * A simple {@link Fragment} subclass.
 */
public class RoomFragment extends Fragment {


    public RoomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //放在view model中去绑定
//        TextView textViewCoilValve = view.findViewById(R.id.textViewCoilValve);
//        TextView textViewFloorValve = view.findViewById(R.id.textViewFloorValve);
//        TextView textViewRoomName = view.findViewById(R.id.textViewRoomName);
//        SeekBar temperatureSeekBar = view.findViewById(R.id.temperatureSeekBar);
//        SeekBar humiditySeekBar = view.findViewById(R.id.humiditySeekBar);
//        Button buttonFloor = view.findViewById(R.id.buttonfloor);
//        Button buttonHumidity = view.findViewById(R.id.buttonhumidity);
//        RadioGroup radioGroup = view.findViewById(R.id.fanspeed);
//        RadioButton radioButtonLowFan = view.findViewById(R.id.radioButtonlowfan);
//        RadioButton radioButtonMiddleFan = view.findViewById(R.id.radioButtonmiddlefan);
//        RadioButton radioButtonHighFan = view.findViewById(R.id.radioButtonhighfan);
//        RadioButton radioButtonAutoFan = view.findViewById(R.id.radioButtonautofan);
//        Button buttonStop = view.findViewById(R.id.buttonStop);
//        Button buttonManual = view.findViewById(R.id.buttonManual);
//        Button buttonAuto = view.findViewById(R.id.buttonAuto);

    }
}
