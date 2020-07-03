package com.jiuhua.jiuhuacontrol.ui.indoor;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.jiuhua.jiuhuacontrol.CommandESP;
import com.jiuhua.jiuhuacontrol.MyRepository;
import com.jiuhua.jiuhuacontrol.database.IndoorDB;

import java.util.List;

public class IndoorViewModel extends AndroidViewModel {

    MyRepository myRepository;
    List<IndoorDB> allLatestIndoorDBs;
    IndoorDB latestIndoorDB;
    CommandESP commandESP = new CommandESP();

    //变量及其getter & setter 方法
    private int roomNameId;
    private String roomName;

    public int getRoomNameId() {
        return roomNameId;
    }

    public void setRoomNameId(int roomNameId) {
        this.roomNameId = roomNameId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public IndoorDB getLatestIndoorDB() {
        return latestIndoorDB;
    }

    public void setLatestIndoorDB(IndoorDB latestIndoorDB) {
        this.latestIndoorDB = latestIndoorDB;
    }

    public void setAllLatestIndoorDBs(List<IndoorDB> allLatestIndoorDBsLive) {
        this.allLatestIndoorDBs = allLatestIndoorDBsLive;
        if (roomNameId < allLatestIndoorDBsLive.size()) {  //TODO 临时的需要解决超出队列边界的问题
            this.latestIndoorDB = allLatestIndoorDBsLive.get(roomNameId-1);
            commandESP.setRoomId(latestIndoorDB.getRoomId());
            commandESP.setDeviceType(latestIndoorDB.getDeviceType());
            commandESP.setRoomState(latestIndoorDB.getRoomStatus());
            commandESP.setSetting_temp(latestIndoorDB.getSettingTemperature());
            commandESP.setSetting_humidity(latestIndoorDB.getSettingHumidity());
            commandESP.setSettingfanSpeed(latestIndoorDB.getSettingFanStatus());
        }else {//fixme: 单纯非空还是不解决问题
            this.latestIndoorDB = new IndoorDB();
        }
    }

    //构造方法
    public IndoorViewModel(@NonNull Application application) {
        super(application);
        this.myRepository = new MyRepository(application);
    }

    //停止按钮实现方法，停止房间所有设备
    public void stopRoomDevice(int roomid){
        commandESP.setRoomId(roomid);
        commandESP.setDeviceType(Constants.deviceType_fancoil);
        commandESP.setRoomState(Constants.roomState_OFF);
        commandESP.setSettingfanSpeed(Constants.fanSpeed_STOP);
        myRepository.jsonToDevice(commandESP);
        commandESP.setRoomId(roomid);
        commandESP.setDeviceType(Constants.deviceType_floorheater);
        commandESP.setRoomState(Constants.roomState_OFF);
        myRepository.jsonToDevice(commandESP);
    }
    //手动按钮实现方法
    public void manualRoomDevice(int roomid){
        commandESP.setRoomId(roomid);
        commandESP.setDeviceType(Constants.deviceType_fancoil);
        commandESP.setRoomState(Constants.roomState_MANUAL);
        commandESP.setSettingfanSpeed(Constants.fanSpeed_MEDIUM);
        myRepository.jsonToDevice(commandESP);
        commandESP.setRoomId(roomid);
        commandESP.setDeviceType(Constants.deviceType_floorheater);
        commandESP.setRoomState(Constants.roomState_MANUAL);
        myRepository.jsonToDevice(commandESP);
    }
    //周期自动按钮实现方法
    public void autoRoomDevice(int roomid){
        commandESP.setRoomId(roomid);
        commandESP.setDeviceType(Constants.deviceType_fancoil);
        commandESP.setRoomState(Constants.roomState_AUTO);
        commandESP.setSettingfanSpeed(Constants.fanSpeed_AUTO);
        myRepository.jsonToDevice(commandESP);
        commandESP.setRoomId(roomid);
        commandESP.setDeviceType(Constants.deviceType_floorheater);
        commandESP.setRoomState(Constants.roomState_AUTO);
        myRepository.jsonToDevice(commandESP);
    }
    //宴会按钮实现方法，这个好像没有用到。
    public void feastRoomDevice(int roomid){
        commandESP.setRoomId(roomid);
        commandESP.setDeviceType(Constants.deviceType_floorheater);
        commandESP.setRoomState(Constants.roomState_FEAST);
        myRepository.jsonToDevice(commandESP);
    }
    //除湿按钮实现方法
    public void dehumidityRoomDevice(int roomid, int roomState){
        commandESP.setRoomId(roomid);
        commandESP.setDeviceType(Constants.deviceType_fancoil);
        commandESP.setRoomState(roomState);
        if (roomState == Constants.roomState_OFF) commandESP.setSettingfanSpeed(Constants.fanSpeed_STOP);
        if (roomState == Constants.roomState_DEHUMIDITY) commandESP.setSettingfanSpeed(Constants.fanSpeed_LOW);
        myRepository.jsonToDevice(commandESP);
    }
    //地暖按钮实现方法
    public void floorRoomDevice(int roomid, int roomState){
        commandESP.setRoomId(roomid);
        commandESP.setDeviceType(Constants.deviceType_floorheater);
        commandESP.setRoomState(roomState);
        myRepository.jsonToDevice(commandESP);
    }
    //风速按钮实现方法
    public void fanSpeedRoomDevice(int roomid, int fanSpeed){
        commandESP.setRoomId(roomid);
        commandESP.setDeviceType(Constants.deviceType_fancoil);
        commandESP.setRoomState(Constants.roomState_MANUAL);
        commandESP.setSettingfanSpeed(fanSpeed);
        myRepository.jsonToDevice(commandESP);
    }
    //传送温度
    public void temperatureToRoomDevice(int roomid, int temp) {
        commandESP.setRoomId(roomid);
        commandESP.setDeviceType(Constants.deviceType_fancoil);
        commandESP.setSetting_temp(temp);//传输的X10 的假浮点
        myRepository.jsonToDevice(commandESP);
        commandESP.setRoomId(roomid);
        commandESP.setDeviceType(Constants.deviceType_floorheater);
        commandESP.setSetting_temp(temp);//传输的X10 的假浮点
        myRepository.jsonToDevice(commandESP);
    }
    //传送湿度
    public void humidityToRoomDevice(int roomid, int temp) {
        commandESP.setRoomId(roomid);
        commandESP.setDeviceType(Constants.deviceType_fancoil);
        commandESP.setSetting_humidity(temp);//传输的X10 的假浮点
        myRepository.jsonToDevice(commandESP);
        commandESP.setRoomId(roomid);
        commandESP.setDeviceType(Constants.deviceType_floorheater);
        commandESP.setSetting_humidity(temp);//传输的X10 的假浮点
        myRepository.jsonToDevice(commandESP);
    }



    public LiveData<List<IndoorDB>> getAllLatestIndoorDBsLive() {
        return myRepository.getAllLatestIndoorDBsLive();
    }
}
