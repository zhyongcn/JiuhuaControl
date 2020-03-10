package com.jiuhua.jiuhuacontrol.ui.room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.jiuhua.jiuhuacontrol.MyRepository;
import com.jiuhua.jiuhuacontrol.database.BasicInfoDB;

import java.util.List;

public class RoomViewModel extends AndroidViewModel {

    MyRepository myRepository;

    private String roomName;
    private int currentTemperature;
    private int settingTemperature;
    private int currentHumidity;
    private int settingHumidity;
    private String fanStatus;
    private String floorValveOpen;
    private String coilValveOpen;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentTemperature(int currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    public int getSettingTemperature() {
        return settingTemperature;
    }

    public void setSettingTemperature(int settingTemperature) {
        this.settingTemperature = settingTemperature;
    }

    public int getCurrentHumidity() {
        return currentHumidity;
    }

    public void setCurrentHumidity(int currentHumidity) {
        this.currentHumidity = currentHumidity;
    }

    public int getSettingHumidity() {
        return settingHumidity;
    }

    public void setSettingHumidity(int settingHumidity) {
        this.settingHumidity = settingHumidity;
    }

    public String getFanStatus() {
        return fanStatus;
    }

    public void setFanStatus(String fanStatus) {
        this.fanStatus = fanStatus;
    }

    public String getFloorValveOpen() {
        return floorValveOpen;
    }

    public void setFloorValveOpen(String floorValveOpen) {
        this.floorValveOpen = floorValveOpen;
    }

    public String getCoilValveOpen() {
        return coilValveOpen;
    }

    public void setCoilValveOpen(String coilValveOpen) {
        this.coilValveOpen = coilValveOpen;
    }

    public RoomViewModel(@NonNull Application application) {
        super(application);
        myRepository = new MyRepository(application);
    }


    //TODO　包装 Repository 里面的 Dao 方法
    public void insertRoomName(BasicInfoDB... basicInfoDBS) {
        myRepository.insertRoomName(basicInfoDBS);
    }
    public void deleteAllRoomsName(){
        myRepository.deleteAllRoomsName();
    }

    public LiveData<List<BasicInfoDB>> getAllRoomsName(){
        return myRepository.getAllRoomsNameLive();
    }

}
