package com.jiuhua.jiuhuacontrol.ui.indoor;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jiuhua.jiuhuacontrol.Constants;
import com.jiuhua.jiuhuacontrol.MyRepository;
import com.jiuhua.jiuhuacontrol.database.DayPeriod;
import com.jiuhua.jiuhuacontrol.database.IndoorDB;
import com.jiuhua.jiuhuacontrol.database.PeriodDB;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IndoorViewModel extends AndroidViewModel {

    MyRepository myRepository;
    List<IndoorDB> allLatestIndoorDBs = new ArrayList<>();   //room1,room2...`s settingtempreature etc.
    List<PeriodDB> allLatestPeriodDBs = new ArrayList<>();   //room1,room2...`s period.
    IndoorDB currentlyIndoorDB = new IndoorDB();
    PeriodDB currentlyPeriodDB;  //currently room`s one weekly period.


    private int currentlyRoomId;
    private String currentlyRoomName;

    //变量getter & setter 方法
    public void setCurrentlyRoomId(int currentlyRoomId) {
        this.currentlyRoomId = currentlyRoomId;
    }

    public String getCurrentlyRoomName() {
        return currentlyRoomName;
    }

    public void setCurrentlyRoomName(String currentlyRoomName) {
        this.currentlyRoomName = currentlyRoomName;
    }

    public void setAllLatestIndoorDBs(List<IndoorDB> allLatestIndoorDBsLive) {
        this.allLatestIndoorDBs = allLatestIndoorDBsLive;
        if (allLatestIndoorDBs.size() > 0) {
            for (IndoorDB indoorDB : allLatestIndoorDBs) {
                if (indoorDB.getRoomId() == currentlyRoomId) {
                    this.currentlyIndoorDB = indoorDB;

//                    commandESP.setRoomId(currentlyIndoorDB.getRoomId());
//                    commandESP.setDeviceType(Constants.deviceType_phone);//已经是来自手机了。
                    //TODO: 待验证！！ 如果这些缺省为 0 时，模块不写入，是否不要设置了，
//                    commandESP.setRoomState(currentlyIndoorDB.getRoomStatus());
//                    commandESP.setSettingTemperature(currentlyIndoorDB.getSettingTemperature());
//                    commandESP.setSettingHumidity(currentlyIndoorDB.getSettingHumidity());
//                    commandESP.setSettingFanSpeed(currentlyIndoorDB.getSettingFanStatus());
                }
            }
        }
        if (currentlyIndoorDB.getRoomId() == 0) {
            currentlyIndoorDB.setRoomId(currentlyRoomId);
//            commandESP.setRoomId(currentlyRoomId);
        }
    }

    public void setAllLatestPeriodDBs(List<PeriodDB> allLatestPeriodDBsLive) {
        this.allLatestPeriodDBs = allLatestPeriodDBsLive;
        if (allLatestPeriodDBs.size() > 0) {
            for (PeriodDB periodDB : allLatestPeriodDBs) {
                if (periodDB.getRoomId() == currentlyRoomId) {
                    this.currentlyPeriodDB = periodDB;
                }
            }
            if (currentlyPeriodDB == null){ //如果迭代完成还没有被赋值，说明没有这个房间的数据，新建一个房间的基础数据
                currentlyPeriodDB = new PeriodDB();
                currentlyPeriodDB.setRoomId(currentlyRoomId);
                currentlyPeriodDB.setOneRoomWeeklyPeriod(new ArrayList<>());
            }
        }else {//说明在开始的状态没有任何数据，新建一个房间的基础数据
            currentlyPeriodDB = new PeriodDB();
            currentlyPeriodDB.setRoomId(currentlyRoomId);
            currentlyPeriodDB.setOneRoomWeeklyPeriod(new ArrayList<>());
        }
    }

    //构造方法
    public IndoorViewModel(@NonNull Application application) {
        super(application);
        this.myRepository = new MyRepository(application);
    }

    /**
     * 设备设置调整页面的各种实现方法，供其调用。
     *  ***改变了需要改变的参数，其他参数不动。***
     */
    //传送房间设置状态的方法
    public void roomstateToDevice(int roomid, int roomstates) {
//        commandESP.setRoomId(roomid);
//        commandESP.setDeviceType(Constants.deviceType_phone);
//        commandESP.setRoomState(roomstates);
//        myRepository.commandToDevice(commandESP);
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject(); //temp object for send temperatureSensorCalibration information.
        jsonObject.addProperty("roomId", roomid);
        jsonObject.addProperty("deviceType", Constants.deviceType_phone);//手机来的命令才接受。DHT，NTC模块接收不在mqttconfig里面？
        jsonObject.addProperty("roomState", roomstates);
        String msg = gson.toJson(jsonObject);
        myRepository.commandToDevice(roomid, msg);
    }

    //风速按钮实现方法
    public void fanSpeedToDevice(int roomid, int fanSpeed) {
//        commandESP.setRoomId(roomid);
//        commandESP.setDeviceType(Constants.deviceType_phone);//needless
//        commandESP.setSettingFanSpeed(fanSpeed);
//        myRepository.commandToDevice(commandESP);
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject(); //temp object for send temperatureSensorCalibration information.
        jsonObject.addProperty("roomId", roomid);
        jsonObject.addProperty("deviceType", Constants.deviceType_phone);//手机来的命令才接受。DHT，NTC模块接收不在mqttconfig里面？
        jsonObject.addProperty("settingFanSpeed", fanSpeed);
        String msg = gson.toJson(jsonObject);
        myRepository.commandToDevice(roomid, msg);
    }

    //传送设置温度
    public void temperatureToDevice(int roomid, int temp) {
//        commandESP.setRoomId(roomid);
//        commandESP.setDeviceType(Constants.deviceType_phone);
//        commandESP.setSettingTemperature(temp);//TODO 传输的X10 的假浮点？？
//        myRepository.commandToDevice(commandESP);
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject(); //temp object for send temperatureSensorCalibration information.
        jsonObject.addProperty("roomId", roomid);
        jsonObject.addProperty("deviceType", Constants.deviceType_phone);//手机来的命令才接受。DHT，NTC模块接收不在mqttconfig里面？
        jsonObject.addProperty("settingTemperature", temp);
        String msg = gson.toJson(jsonObject);
        myRepository.commandToDevice(roomid, msg);
    }

    //传送设定湿度
    public void humidityToDevice(int roomid, int humidity) {
//        commandESP.setRoomId(roomid);
//        commandESP.setDeviceType(Constants.deviceType_phone);
//        commandESP.setSettingHumidity(humidity);//TODO 传输的X10 的假浮点？？
//        myRepository.commandToDevice(commandESP);
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject(); //temp object for send temperatureSensorCalibration information.
        jsonObject.addProperty("roomId", roomid);
        jsonObject.addProperty("deviceType", Constants.deviceType_phone);//手机来的命令才接受。DHT，NTC模块接收不在mqttconfig里面？
        jsonObject.addProperty("settingHumidity", humidity);
        String msg = gson.toJson(jsonObject);
        myRepository.commandToDevice(roomid, msg);
    }


    /**
     * 包装 myRepository 里的方法：
     */
    public LiveData<List<IndoorDB>> getAllLatestIndoorDBsLive(int devicetypeId) {
        return myRepository.getAllLatestIndoorDBsLive(devicetypeId);
    }

    public LiveData<List<PeriodDB>> getAllLatestPeriodDBsLive() {
        return myRepository.getAllLatestPeriodDBsLive();
    }

    public void insertPeriodDB(int roomId) {
        currentlyPeriodDB.setId(currentlyPeriodDB.getId()+allLatestPeriodDBs.size());
                    //id是从数据库里取出的，加上有几个房间，不会冲掉数据。id不同，数据库认为不是一个数据
        currentlyPeriodDB.setRoomId(roomId);
        currentlyPeriodDB.setTimeStamp(new Date().getTime() / 1000);  //这个方法得到的是毫秒，this method return ms。
        myRepository.insertPeriodDB(currentlyPeriodDB);
    }

    //把周期传递给模块 period[15][3]
    public void periodToDevice(int roomid, List<DayPeriod> dayPeriods){
        currentlyPeriodDB.setRoomId(roomid);
        currentlyPeriodDB.setTimeStamp(new Date().getTime()/1000);//没有必要
        currentlyPeriodDB.setOneRoomWeeklyPeriod(dayPeriods);
        myRepository.periodToDevice(currentlyPeriodDB);
    }

}
