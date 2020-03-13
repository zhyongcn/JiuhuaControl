package com.jiuhua.jiuhuacontrol.ui.indoor;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.jiuhua.jiuhuacontrol.R;
import com.jiuhua.jiuhuacontrol.databinding.FragmentIndoorBinding;

/**
 * 替换以前的RoomActivity
 * A simple {@link Fragment} subclass.
 */
public class IndoorFragment extends Fragment {

    private FragmentIndoorBinding binding;
    private IndoorViewModel indoorViewModel;

    public IndoorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //这里不再需要原始的创建视图的方法了,这个方法也生成视图但是没有绑定数据
//        View view = inflater.inflate(R.layout.fragment_indoor, container, false);

//        binding = FragmentRoomBinding.inflate(LayoutInflater.from(getContext()), null, false); //从绑定类吹气
//        binding = FragmentRoomBinding.inflate(inflater, container, false);
//        binding = FragmentRoomBinding.inflate(inflater);
//        binding = DataBindingUtil.setContentView(getActivity(), R.layout.fragment_indoor);//这句不行
        //上面三句也是可以的
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_indoor, container,false);

        indoorViewModel = new ViewModelProvider(this).get(IndoorViewModel.class);
        binding.setData(indoorViewModel);
        binding.setLifecycleOwner(this);

        //数据驱动界面改变,所以代码要放在fragment或者Activity里面。只处理界面
        //TODO 风机地暖状态数据驱动相关按钮颜色的变化
        final Observer<String> nameOfRoom = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.imageView2.setVisibility(View.INVISIBLE);
            }
        };
        indoorViewModel.getRoomName().observe(getViewLifecycleOwner(), nameOfRoom);

        return binding.getRoot(); // getRoot() solved databinding problem.
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //临时验证方法
//        indoorViewModel.setRoomName("客厅");
        indoorViewModel.setSettingTemperature(24);
        indoorViewModel.setSettingHumidity(40);
        indoorViewModel.setCoilValveOpen("两通阀开");
        indoorViewModel.setFloorValveOpen("地暖开");
    }
}