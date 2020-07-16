package com.jiuhua.jiuhuacontrol.ui.indoor;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.jiuhua.jiuhuacontrol.R;


/**
 * 一周运行时间段的显示和设置
 * A simple {@link Fragment} subclass.
 */
public class PeroidFragment extends Fragment {
    private int roomNameId;
    private String roomName;

    public PeroidFragment(int roomNameId, String roomName) {
        this.roomNameId = roomNameId;
        this.roomName = roomName;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_peroid, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MyView myView = view.findViewById(R.id.myview);//从传入的view中获取
        myView.setClickCrossListener(new MyView.ClickCrossListener() {
            @Override
            public void onClick(int weekday, int hour) {
                int myweekday = weekday;
                int myhour = hour;
                Bundle bundle = new Bundle();
                //TODO roomid roomname
                bundle.putInt("roomnameID", roomNameId);
                bundle.putString("roomName", roomName);
                bundle.putInt("weekday", myweekday);
                bundle.putInt("hour", myhour);

                Navigation.findNavController(getView()).navigate(R.id.peroidSettingFragment, bundle);//id是目的地的id，不是动作的id，fuck一天的时间。
                Toast.makeText(getContext(), "weekday = " + myweekday + "  hour = " + myhour, Toast.LENGTH_SHORT).show();

            }
        });

    }
}
