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

//    enum Fanspeed {STOP, LOW, MEDIUM, HIGH, AUTO }

    MyRepository myRepository;
    IndoorDB currentIndoorInfo;

    //变量及其getter & setter 方法
    private MutableLiveData<String> roomName = new MutableLiveData<>();
    private MutableLiveData<Integer> currentTemperature = new MutableLiveData<>();
    private MutableLiveData<Integer> settingTemperature = new MutableLiveData<>();
    private MutableLiveData<Integer> currentHumidity = new MutableLiveData<>();
    private MutableLiveData<Integer> settingHumidity = new MutableLiveData<>();
    private MutableLiveData<String> fanStatus = new MutableLiveData<>();
    private MutableLiveData<String> floorValveOpen = new MutableLiveData<>();
    private MutableLiveData<String> coilValveOpen = new MutableLiveData<>();

    public MutableLiveData<String> getRoomName() {
        return roomName;
    }

    public void setRoomName(String s) {
        this.roomName.setValue(s);
    }

    /*这个方法是一个临时验证方法*/
    public void setName() {
//        roomName.postValue("canting");//也可以运行，主要用于worker线程中
        roomName.setValue("canting");
        coilValveOpen.setValue("kkkk");
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

    public MutableLiveData<String> getFanStatus() {
        return this.fanStatus;
    }

    public void setFanStatus(String s) {
        this.fanStatus.setValue(s);
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
