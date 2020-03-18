package com.jiuhua.jiuhuacontrol.ui.userinfo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jiuhua.jiuhuacontrol.R;
import com.jiuhua.jiuhuacontrol.database.BasicInfoDB;

import java.util.ArrayList;
import java.util.List;


public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.MyViewHolder> {

    private List<BasicInfoDB> allroomsName = new ArrayList<>();
    private UserInfoViewModel userInfoViewModel;

    public UserInfoAdapter(UserInfoViewModel userInfoViewModel) {
        this.userInfoViewModel = userInfoViewModel;
    }

    public void setAllroomsName(List<BasicInfoDB> allroomsName) {
        this.allroomsName = allroomsName;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.userinfo_cell_layout, parent, false);

        final MyViewHolder holder = new MyViewHolder(itemView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int k = holder.getAdapterPosition();   //获取具体哪个条目了
//                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_roomHostFragment);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //具体视图元素赋值。
        BasicInfoDB basicInfoDB = allroomsName.get(position);

        holder.editTextRoomName.setText(basicInfoDB.getRoomName());
//        holder.editTextFancoilGrade.setText(basicInfoDB.getEngineTrademark());
        holder.editTextFancoilGrade.setText(basicInfoDB.getFancoilTrademark());
        holder.editTextFancoilType.setText(basicInfoDB.getFanCoilType());
        holder.checkBoxCoilvalve.setChecked(basicInfoDB.isHasCoilValve());
        holder.checkBoxHasFloorHeating.setChecked(basicInfoDB.isFloorHeat());
        holder.checkBoxIsFloorHeatingAuto.setChecked(basicInfoDB.isFloorAuto());
        holder.editTextRadiatorGrade.setText(basicInfoDB.getRadiatorTrademark());
        holder.editTextRadiatorType.setText(basicInfoDB.getRadiatorType());
        holder.checkBoxIsRadiatorValveAuto.setChecked(basicInfoDB.isRadiatorAuto());
        //如果是耗时很少的操作可以在这里出现，或者item的内部有很多部件需要绑定，也在这里。
    }

    @Override
    public int getItemCount() {
        return allroomsName.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        EditText editTextRoomName, editTextFancoilGrade, editTextFancoilType;
        CheckBox checkBoxCoilvalve, checkBoxHasFloorHeating, checkBoxIsFloorHeatingAuto;
        EditText editTextRadiatorGrade, editTextRadiatorType;
        CheckBox checkBoxIsRadiatorValveAuto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            editTextRoomName = itemView.findViewById(R.id.user_roomname);
            editTextFancoilGrade = itemView.findViewById(R.id.room_fancoil_grade);
            editTextFancoilType = itemView.findViewById(R.id.room_fancoil_type);
            checkBoxCoilvalve = itemView.findViewById(R.id.room_coilvalve_checkBox);
            checkBoxHasFloorHeating = itemView.findViewById(R.id.room_floor_checkBox);
            checkBoxIsFloorHeatingAuto = itemView.findViewById(R.id.room_autofloor_checkBox);
            editTextRadiatorGrade = itemView.findViewById(R.id.room_radiator_grade);
            editTextRadiatorType = itemView.findViewById(R.id.room_radiator_type);
            checkBoxIsRadiatorValveAuto = itemView.findViewById(R.id.room_radiatorvalve_auto);


        }
    }


}
