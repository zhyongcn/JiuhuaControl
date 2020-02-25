package com.jiuhua.jiuhuacontrol.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.jiuhua.jiuhuacontrol.R;
import com.jiuhua.jiuhuacontrol.database.RoomNameDB;

import java.util.ArrayList;
import java.util.List;


public class HomepageAdapter extends RecyclerView.Adapter<HomepageAdapter.MyViewHolder> {

    private List<RoomNameDB> allroomsName = new ArrayList<>();
    private HomeViewModel homeViewModel;

    public HomepageAdapter(HomeViewModel homeViewModel) {
        this.homeViewModel = homeViewModel;
    }

    public void setAllroomsName(List<RoomNameDB> allroomsName) {
        this.allroomsName = allroomsName;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.cell_layout, parent, false);
        final MyViewHolder holder = new MyViewHolder(itemView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int k = holder.getAdapterPosition();   //获取具体哪个条目了
                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_roomHostFragment);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //具体视图元素赋值。
        RoomNameDB roomNameDB = allroomsName.get(position);
        holder.textViewRoomName.setText(roomNameDB.getRoomName());
        //如果是耗时很少的操作可以在这里出现，或者item的内部有很多部件需要绑定，也在这里。
    }

    @Override
    public int getItemCount() {
        return allroomsName.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewRoomName, textViewRoomStatus, textViewRoomTemperature, textViewRoomHumidity;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRoomName = itemView.findViewById(R.id.roomName);
            textViewRoomStatus = itemView.findViewById(R.id.roomStatus);
            textViewRoomTemperature = itemView.findViewById(R.id.roomTemp);
            textViewRoomHumidity = itemView.findViewById(R.id.roomHumidity);
        }
    }


}
