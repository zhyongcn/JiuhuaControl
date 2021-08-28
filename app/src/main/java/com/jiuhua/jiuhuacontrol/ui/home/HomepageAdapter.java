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
import com.jiuhua.jiuhuacontrol.database.BasicInfoSheet;
import com.jiuhua.jiuhuacontrol.database.FancoilSheet;
import com.jiuhua.jiuhuacontrol.database.SensorSheet;
import com.jiuhua.jiuhuacontrol.Constants;
import com.jiuhua.jiuhuacontrol.ui.HomeViewModel;

import java.util.ArrayList;
import java.util.List;


public class HomepageAdapter extends RecyclerView.Adapter<HomepageAdapter.MyViewHolder> {//多态使用的内部类

    private List<BasicInfoSheet> allBasicInfo = new ArrayList<>();
    private List<SensorSheet> allLatestSensorSheets = new ArrayList<>();
    private List<FancoilSheet> allLatestFancoilSheets = new ArrayList<>();
    private HomeViewModel homeViewModel;

    public HomepageAdapter(HomeViewModel homeViewModel) {//构造函数
        this.homeViewModel = homeViewModel;
    }

    public void setAllBasicInfo(List<BasicInfoSheet> allBasicInfo) { // homeFragment 观察到变化就调用此方法设置 basicinfo。
        this.allBasicInfo = allBasicInfo;
    }

    public void setAllLatestSensorSheets(List<SensorSheet> allLatestSensorSheets) {
        this.allLatestSensorSheets = allLatestSensorSheets;
    }

    public void setAllLatestFancoilSheets(List<FancoilSheet> allLatestFancoilSheets) {
        this.allLatestFancoilSheets = allLatestFancoilSheets;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //创建时，首先要获取父图层的内容
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        //view是在父图层的基础上再吹气子图层
        View itemView = layoutInflater.inflate(R.layout.home_cell_layout, parent, false);
        final MyViewHolder holder = new MyViewHolder(itemView);  //赋值于一个句柄以方便其他操作。
        //单个条目的整体点击。 跳转并传参。
        holder.itemView.setOnClickListener(v -> {
            //int k = holder.getAdapterPosition();   //google舍弃的API
            int k = holder.getBindingAdapterPosition();   //获取具体哪个条目了
            int roomId = allBasicInfo.get(k).getRoomId();
            String currentRoomName = allBasicInfo.get(k).getRoomName();
            Bundle bundle = new Bundle();
            bundle.putInt("roomId", roomId);
            bundle.putString("roomName", currentRoomName);
            Navigation.findNavController(v).navigate(R.id.action_nav_home_to_indoorHostFragment, bundle);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {//绑定时，position位置参数指定了使用哪个具体数据。
        //具体视图元素赋值。
        BasicInfoSheet basicInfoSheet = allBasicInfo.get(position);
        holder.textViewRoomName.setText(basicInfoSheet.getRoomName());
        //display sensor`s temperature and humidity.
        for (SensorSheet sensorSheet:allLatestSensorSheets ) {
            if (sensorSheet.getRoomId() == basicInfoSheet.getRoomId()) {
                holder.textViewRoomTemperature.setText("当前温度：" + sensorSheet.getCurrentTemperature() / 10 + " C");
                holder.textViewRoomHumidity.setText("当前湿度：" + sensorSheet.getCurrentHumidity() / 10 + "%RH");
            }
        }
        //display fancoil`s roomstatus
        for (FancoilSheet fancoilSheet:allLatestFancoilSheets) {
            if (fancoilSheet.getRoomId() == basicInfoSheet.getRoomId()) {
                switch (fancoilSheet.getRoomStatus()) {
                    case Constants.roomState_OFF:
                        holder.textViewRoomStatus.setText("当前状态：停止运行");
                        break;
                    case Constants.roomState_MANUAL:
                        holder.textViewRoomStatus.setText("当前状态：手动运行");
                        break;
                    case Constants.roomState_AUTO:
                        holder.textViewRoomStatus.setText("当前状态：自动运行");
                        break;
                    case Constants.roomState_DEHUMIDITY:
                        holder.textViewRoomStatus.setText("当前状态： 除湿运行");
                        break;
                    case Constants.roomState_FEAST:
                        holder.textViewRoomStatus.setText("当前状态： 宴会运行");
                        break;
                }
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
