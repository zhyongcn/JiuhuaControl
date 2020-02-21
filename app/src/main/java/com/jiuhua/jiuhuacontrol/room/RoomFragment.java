package com.jiuhua.jiuhuacontrol.room;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

}
