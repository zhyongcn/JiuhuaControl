package com.jiuhua.jiuhuacontrol.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.jiuhua.jiuhuacontrol.R;
import com.jiuhua.jiuhuacontrol.database.BasicInfoDB;
import com.jiuhua.jiuhuacontrol.database.IndoorDB;

import java.util.ArrayList;
import java.util.List;


public class HomepageAdapter extends RecyclerView.Adapter<HomepageAdapter.MyViewHolder> {//多态使用的内部类

    private List<BasicInfoDB> allBasicInfo = new ArrayList<>();
    private List<IndoorDB> allLatestIndoorDBs = new ArrayList<>();
    private HomeViewModel homeViewModel;

    public HomepageAdapter(HomeViewModel homeViewModel) {//构造函数
        this.homeViewModel = homeViewModel;
    }

    public void setAllBasicInfo(List<BasicInfoDB> allBasicInfo) {//homeFragment观察到变化就调用此方法设置basicinfo。
        this.allBasicInfo = allBasicInfo;
    }

    public void setAllLatestIndoorDBs(List<IndoorDB> allLatestIndoorDBs) {
        this.allLatestIndoorDBs = allLatestIndoorDBs;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//创建时
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());  //首先要获取父图层的内容
        View itemView = layoutInflater.inflate(R.layout.home_cell_layout, parent, false);  //view是在父图层的基础上再吹气子图层
        final MyViewHolder holder = new MyViewHolder(itemView);  //赋值于一个句柄以方便其他操作。
        holder.itemView.setOnClickListener(new View.OnClickListener() {  //单个条目的整体点击。
            @Override
            public void onClick(View v) {
                int k = holder.getAdapterPosition();   //获取具体哪个条目了
                int roomNameId = allBasicInfo.get(k).getId();
                String currentRoomName = allBasicInfo.get(k).getRoomName();
                Bundle bundle = new Bundle();
                bundle.putInt("roomNameId", k+1);//因为roomid还没有确定好，所以暂时不传这个参数。
                bundle.putString("roomName", currentRoomName);
                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_indoorHostFragment, bundle);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {//绑定时，position位置参数指定了使用哪个具体数据。
        //具体视图元素赋值。
        BasicInfoDB basicInfoDB = allBasicInfo.get(position);
        holder.textViewRoomName.setText(basicInfoDB.getRoomName());
        if (position < allLatestIndoorDBs.size()) {
            IndoorDB indoorDB = allLatestIndoorDBs.get(position);//在这个给 position+1 会导致闪退的。
            holder.textViewRoomTemperature.setText("当前温度：" + indoorDB.getCurrentTemperature() + " C");
            holder.textViewRoomHumidity.setText("当前湿度：" + indoorDB.getCurrentHumidity() + "%RH");
            switch (indoorDB.getRoomStatus()) {
                case 0:
                    holder.textViewRoomStatus.setText("当前状态：停止运行");
                    break;
                case 1:
                    holder.textViewRoomStatus.setText("当前状态：手动运行");
                    break;
                case 2:
                    holder.textViewRoomStatus.setText("当前状态：自动运行");
                    break;
            }
        }

        //如果是耗时很少的操作可以在这里出现，或者item的内部有很多部件需要绑定，也在这里。
    }

    @Override
    public int getItemCount() {
        return allBasicInfo.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {  //内部类实现了recyclerview的具体条目
        TextView textViewRoomName, textViewRoomStatus, textViewRoomTemperature, textViewRoomHumidity;

        public MyViewHolder(@NonNull View itemView) {//构造方法，具体实现。
            super(itemView);
            textViewRoomName = itemView.findViewById(R.id.roomName);
            textViewRoomStatus = itemView.findViewById(R.id.roomStatus);
            textViewRoomTemperature = itemView.findViewById(R.id.roomTemp);
            textViewRoomHumidity = itemView.findViewById(R.id.roomHumidity);
        }
    }


}
