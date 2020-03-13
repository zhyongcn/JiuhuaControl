package com.jiuhua.jiuhuacontrol.ui.indoor;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.jiuhua.jiuhuacontrol.MyRepository;
import com.jiuhua.jiuhuacontrol.database.BasicInfoDB;
import com.jiuhua.jiuhuacontrol.database.IndoorDB;

import java.util.List;

public class IndoorViewModel extends AndroidViewModel {

    enum FanSpeed {STOP, LOW, MEDIUM, HIGH, AUTO}

    enum RoomState {STOP, MANUAL, AUTO}

    MyRepository myRepository;

    IndoorDB currentIndoorInfo;

    //变量及其getter & setter 方法
    private MutableLiveData<String> roomName = new MutableLiveData<>();
    private MutableLiveData<Integer> currentTemperature = new MutableLiveData<>();
    private MutableLiveData<Integer> settingTemperature = new MutableLiveData<>();
    private MutableLiveData<Integer> currentHumidity = new MutableLiveData<>();
    private MutableLiveData<Integer> settingHumidity = new MutableLiveData<>();
    private MutableLiveData<FanSpeed> fanStatus = new MutableLiveData<>();
    private MutableLiveData<String> floorValveOpen = new MutableLiveData<>();
    private MutableLiveData<String> coilValveOpen = new MutableLiveData<>();
    private MutableLiveData<RoomState> roomState = new MutableLiveData<>();

    public MutableLiveData<String> getRoomName() {
        return roomName;
    }

    public void setRoomName(String s) {
        this.roomName.setValue(s);
    }

    /*这个方法是一个临时验证方法*/
    public void setName() {
//        roomName.postValue("canting");//也可以运行，主要用于worker线程中
        roomName.setValue("餐厅");
        coilValveOpen.setValue("开开开");
    }

    public MutableLiveData<Integer> getCurrentTemperature() {
        return this.currentTemperature;
    }

    public void setCurrentTemperature(int i) {
        this.currentTemperature.setValue(i);
    }

    public MutableLiveData<Integer> getSettingTemperature() {
        return this.settingTemperature;
    }

    public void setSettingTemperature(int i) {
        this.settingTemperature.setValue(i);
    }

    public MutableLiveData<Integer> getCurrentHumidity() {
        return this.currentHumidity;
    }

    public void setCurrentHumidity(int i) {
        currentHumidity.setValue(i);
    }

    public MutableLiveData<Integer> getSettingHumidity() {
        return this.settingHumidity;
    }

    public void setSettingHumidity(int i) {
        this.settingHumidity.setValue(i);
    }

    public MutableLiveData<FanSpeed> getFanStatus() {
        return this.fanStatus;
    }

    public void setFanStatus(FanSpeed fanspeed) {
        this.fanStatus.setValue(fanspeed);
    }

    public MutableLiveData<String> getFloorValveOpen() {
        return this.floorValveOpen;
    }

    public void setFloorValveOpen(String s) {
        this.floorValveOpen.setValue(s);
    }

    public MutableLiveData<String> getCoilValveOpen() {
        return this.coilValveOpen;
    }

    public void setCoilValveOpen(String s) {
        this.coilValveOpen.setValue(s);
    }

    public MutableLiveData<RoomState> getRoomState() {
        return roomState;
    }

    public void setRoomState(RoomState roomState) {
        this.roomState.setValue(roomState);
    }

    //下面三个方法用来设定房间的状态
    public void setRoomStateStop() {
        this.roomState.setValue(RoomState.STOP);
    }

    public void setRoomStateManual() {
        this.roomState.setValue(RoomState.MANUAL);
    }

    public void setRoomStateAuto() {
        this.roomState.setValue(RoomState.AUTO);
    }

    //下面三个方法用来设定风机状态
    public void setFanStatusStop() {
        this.fanStatus.setValue(FanSpeed.STOP);
    }

    public void setFanStatusLow() {
        this.fanStatus.setValue(FanSpeed.LOW);
    }

    public void setFanStatusMedium() {
        this.fanStatus.setValue(FanSpeed.MEDIUM);
    }

    public void setFanStatusHigh() {
        this.fanStatus.setValue(FanSpeed.HIGH);
    }

    public void setFanStatusAuto() {
        this.fanStatus.setValue(FanSpeed.AUTO);
    }

    //构造方法
    public IndoorViewModel(@NonNull Application application) {
        super(application);
        myRepository = new MyRepository(application);
        currentIndoorInfo = new IndoorDB();
    }


    //TODO　包装 Repository 里面的 Dao 方法
    public void insertRoomName(BasicInfoDB... basicInfoDBS) {
        myRepository.insertRoomName(basicInfoDBS);
    }

    public void deleteAllRoomsName() {
        myRepository.deleteAllRoomsName();
    }

    public LiveData<List<BasicInfoDB>> getAllRoomsName() {
        return myRepository.getAllRoomsNameLive();
    }

}
