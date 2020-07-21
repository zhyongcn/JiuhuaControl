package com.jiuhua.jiuhuacontrol.ui.indoor;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.jiuhua.jiuhuacontrol.R;
import com.jiuhua.jiuhuacontrol.database.PeriodDB;

import java.util.List;


/**
 * 一周运行时间段的显示和设置
 * A simple {@link Fragment} subclass.
 */
public class PeriodFragment extends Fragment {

    private IndoorViewModel indoorViewModel;

    private int roomNameId;
    private String roomName;
    private MyView myView;

    public PeriodFragment(int roomNameId, String roomName) {
        this.roomNameId = roomNameId;
        this.roomName = roomName;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_peroid, container, false);
        indoorViewModel = new ViewModelProvider(this).get(IndoorViewModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myView = view.findViewById(R.id.myview);//从传入的view中获取

        indoorViewModel.getAllLatestPeriodDBs().observe(getViewLifecycleOwner(), new Observer<List<PeriodDB>>() {
            @Override
            public void onChanged(List<PeriodDB> periodDBS) {
                indoorViewModel.setAllLatestPeriodDBs(periodDBS);
                //TODO 这个数组传的有问题，现在是periodDB的对象，需要的是一个二维数组，传来的对象究竟如何表示的，roomid，和在list中的位置有区别吗？
//                myView.getdata(indoorViewModel.allLatestPeriodDBs.get(roomNameId-1)); //fixme: modify it from string to int[][].
            }
        });

        myView.setClickCrossListener(new MyView.ClickCrossListener() {
            @Override
            public void onClick(int weekday, int hour) {
                int myweekday = weekday;
                int myhour = hour;
                Bundle bundle = new Bundle();
                bundle.putInt("roomnameID", roomNameId);
                bundle.putString("roomName", roomName);
                bundle.putInt("weekday", myweekday);
                bundle.putInt("hour", myhour);
                Navigation.findNavController(getView()).navigate(R.id.peroidSettingFragment, bundle);//id是目的地的id，不是动作的id，fuck一天的时间。
//                Toast.makeText(getContext(), "weekday = " + myweekday + "  hour = " + myhour, Toast.LENGTH_SHORT).show();

            }
        });

    }
}
