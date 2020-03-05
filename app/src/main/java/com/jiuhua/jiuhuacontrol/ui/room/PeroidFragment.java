package com.jiuhua.jiuhuacontrol.ui.room;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiuhua.jiuhuacontrol.R;

/**
 * 一周运行时间段的显示和设置
 * A simple {@link Fragment} subclass.
 */
public class PeroidFragment extends Fragment {


    public PeroidFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_peroid, container, false);
    }

}
