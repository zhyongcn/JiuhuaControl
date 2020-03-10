package com.jiuhua.jiuhuacontrol.ui.room;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.jiuhua.jiuhuacontrol.R;
import com.jiuhua.jiuhuacontrol.databinding.FragmentRoomBinding;

/**
 * 替换以前的RoomActivity
 * A simple {@link Fragment} subclass.
 */
public class RoomFragment extends Fragment {

    FragmentRoomBinding binding;
    RoomViewModel roomViewModel;

    public RoomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //这里不再需要原始的创建视图的方法了,这个方法也生成视图但是没有绑定数据
//        View view = inflater.inflate(R.layout.fragment_room, container, false);

//        binding = FragmentRoomBinding.inflate(LayoutInflater.from(getContext()), null, false); //从绑定类吹气
//        binding = FragmentRoomBinding.inflate(inflater, container, false);
//        binding = FragmentRoomBinding.inflate(inflater);
//        binding = DataBindingUtil.setContentView(getActivity(), R.layout.fragment_room);//这句不行
        //上面三句也是可以的
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_room, container,false);

        roomViewModel = ViewModelProviders.of(this).get(RoomViewModel.class);
        binding.setData(roomViewModel);
        binding.setLifecycleOwner(this);
        return binding.getRoot(); // getRoot() solved databinding problem.
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
