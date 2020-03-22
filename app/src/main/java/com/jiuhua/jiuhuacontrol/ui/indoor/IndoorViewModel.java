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
    private int roomId;
    private MutableLiveData<String> roomName = new MutableLiveData<>();
    public static final MutableLiveData<String> currentTemperature = new MutableLiveData<>();
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

    public MutableLiveData<String> getCurrentTemperature() {
        return this.currentTemperature;
    }

    public void setCurrentTemperature(Integer i) {
        this.currentTemperature.setValue(String.valueOf(i));
    }

    public MutableLiveData<Integer> getSettingTemperature() {
        return this.settingTemperature;
    }

    public void setSettingTemperature(Integer i) {
        this.settingTemperature.setValue(i);
    }

    public MutableLiveData<Integer> getCurrentHumidity() {
        return this.currentHumidity;
    }

    public void setCurrentHumidity(Integer i) {
        currentHumidity.setValue(i);
    }

    public MutableLiveData<Integer> getSettingHumidity() {
        return this.settingHumidity;
    }

    public void setSettingHumidity(Integer i) {
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
        myRepository.stopRoomEquipment(String.valueOf(roomId));
    }

    public void setRoomStateManual() {
        this.roomState.setValue(RoomState.MANUAL);
    }

    public void setRoomStateAuto() {
        this.roomState.setValue(RoomState.AUTO);
    }

    //下面五个方法用来设定风机状态
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
        this.myRepository = new MyRepository(application);
        this.currentIndoorInfo = new IndoorDB();
    }

    //TODO　包装 Repository 里面的 Dao 方法
    public void stopRoomEquipment(String roomid){
        myRepository.stopRoomEquipment(roomid);
    }

    public void insertRoomName(BasicInfoDB... basicInfoDBS) {
        myRepository.insertBasicInfo(basicInfoDBS);
    }

    public void deleteAllRoomsName() {
        myRepository.deleteAllBasicInfo();
    }

    public LiveData<List<BasicInfoDB>> getAllRoomsName() {
        return myRepository.getAllBasicInfoLive();
    }

}
