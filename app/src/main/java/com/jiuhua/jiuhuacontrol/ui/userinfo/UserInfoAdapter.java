package com.jiuhua.jiuhuacontrol.ui.userinfo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jiuhua.jiuhuacontrol.R;
import com.jiuhua.jiuhuacontrol.database.BasicInfoDB;

import java.util.ArrayList;
import java.util.List;


public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.MyViewHolder> {

    private List<BasicInfoDB> allBasicInfo = new ArrayList<>();
    private UserInfoViewModel userInfoViewModel;

    public UserInfoAdapter(UserInfoViewModel userInfoViewModel) {
        this.userInfoViewModel = userInfoViewModel;
    }

    //在数据库里面插入一个新的房间，会自动获取主键
    public void addBasicInfo(){
        userInfoViewModel.insertBasicInfo(new BasicInfoDB());
    }

    public void setAllBasicInfo(List<BasicInfoDB> allBasicInfo) {
        this.allBasicInfo = allBasicInfo;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.userinfo_cell_layout, parent, false);

        final MyViewHolder holder = new MyViewHolder(itemView);

//        holder.editTextRoomName.addTextChangedListener(new TextWatcher() {
//            int k = holder.getAdapterPosition();   //获取具体哪个条目了
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
////                allBasicInfoToUpdate.get(k).setRoomName(s.toString());
////                allBasicInfoToUpdate.get(k).setId(allBasicInfo.get(k).getId());
//            }
//        });

          //这部分代码完成历史使命，帮助搞清楚原理，
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int k = holder.getAdapterPosition();   //获取具体哪个条目了
//                //下面五条运行没有问题
////                Log.d("userinfo_position", String.valueOf(k));
////                allBasicInfo.get(k).setFancoilTrademark("yangzi");
////                userInfoViewModel.updateBasicInfo(allBasicInfo.get(k));
////                userInfoViewModel.insertBasicInfo(new BasicInfoDB());
////                Navigation.findNavController(v).navigate(R.id.action_nav_userinfo_to_peroidFragment);
//            }
//        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        //具体视图元素赋值。
        BasicInfoDB basicInfoDB = allBasicInfo.get(position);

        holder.editTextRoomName.setText(basicInfoDB.getRoomName());
        holder.editTextFancoilTradeMark.setText(basicInfoDB.getFancoilTrademark());
        holder.editTextFancoilType.setText(basicInfoDB.getFanCoilType());
        holder.checkBoxCoilvalve.setChecked(basicInfoDB.isHasCoilValve());
        holder.checkBoxHasFloorHeating.setChecked(basicInfoDB.isFloorHeat());
        holder.checkBoxIsFloorHeatingAuto.setChecked(basicInfoDB.isFloorAuto());
        holder.editTextRadiatorTradeMark.setText(basicInfoDB.getRadiatorTrademark());
        holder.editTextRadiatorType.setText(basicInfoDB.getRadiatorType());
        holder.checkBoxIsRadiatorValveAuto.setChecked(basicInfoDB.isRadiatorAuto());

        //如果是耗时很少的操作可以在这里出现，或者item的内部有很多部件需要绑定，也在这里。

        holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
            int k = holder.getAdapterPosition();   //获取具体哪个条目了
            @Override
            public void onClick(View v) {
                BasicInfoDB basicInfoDB = new BasicInfoDB();
                basicInfoDB.setId(allBasicInfo.get(k).getId());
                basicInfoDB.setRoomName(holder.editTextRoomName.getText().toString());
                basicInfoDB.setFancoilTrademark(holder.editTextFancoilTradeMark.getText().toString());
                basicInfoDB.setFanCoilType(holder.editTextRadiatorType.getText().toString());
                basicInfoDB.setHasCoilValve(holder.checkBoxCoilvalve.isChecked());
                basicInfoDB.setFloorHeat(holder.checkBoxHasFloorHeating.isChecked());
                basicInfoDB.setFloorAuto(holder.checkBoxIsFloorHeatingAuto.isChecked());
                basicInfoDB.setRadiatorTrademark(holder.editTextRadiatorTradeMark.getText().toString());
                basicInfoDB.setRadiatorType(holder.editTextRadiatorType.getText().toString());
                basicInfoDB.setRadiatorAuto(holder.checkBoxIsRadiatorValveAuto.isChecked());

                userInfoViewModel.updateBasicInfo(basicInfoDB);
                Toast.makeText(v.getContext(), "你修改了房间信息", Toast.LENGTH_SHORT).show();
            }
        });

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            int k = holder.getAdapterPosition();   //获取具体哪个条目了
            @Override
            public void onClick(View v) {
                BasicInfoDB basicInfoDB = new BasicInfoDB();
                basicInfoDB.setId(allBasicInfo.get(k).getId());
                userInfoViewModel.deleteBasicInfo(basicInfoDB);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allBasicInfo.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        EditText editTextRoomName, editTextFancoilTradeMark, editTextFancoilType;
        CheckBox checkBoxCoilvalve, checkBoxHasFloorHeating, checkBoxIsFloorHeatingAuto;
        EditText editTextRadiatorTradeMark, editTextRadiatorType;
        CheckBox checkBoxIsRadiatorValveAuto;
        Button buttonEdit, buttonDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            editTextRoomName = itemView.findViewById(R.id.user_roomname);
            editTextFancoilTradeMark = itemView.findViewById(R.id.room_fancoil_brand);
            editTextFancoilType = itemView.findViewById(R.id.room_fancoil_type);
            checkBoxCoilvalve = itemView.findViewById(R.id.room_coilvalve_checkBox);
            checkBoxHasFloorHeating = itemView.findViewById(R.id.room_floor_checkBox);
            checkBoxIsFloorHeatingAuto = itemView.findViewById(R.id.room_autofloor_checkBox);
            editTextRadiatorTradeMark = itemView.findViewById(R.id.room_radiator_brand);
            editTextRadiatorType = itemView.findViewById(R.id.room_radiator_type);
            checkBoxIsRadiatorValveAuto = itemView.findViewById(R.id.room_radiatorvalve_auto);
            buttonEdit = itemView.findViewById(R.id.button_edit);
            buttonDelete = itemView.findViewById(R.id.button_delete);
        }
    }

}
