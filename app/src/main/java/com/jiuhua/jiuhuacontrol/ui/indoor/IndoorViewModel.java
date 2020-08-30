package com.jiuhua.jiuhuacontrol.ui.indoor;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.jiuhua.jiuhuacontrol.CommandESP;
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
    CommandESP commandESP = new CommandESP();
    PeriodDB currentlyPeriodDB;  //currently room`s one weekly period.


    private int currentlyRoomId;
    private String currentlyRoomName;

    Gson gson = new Gson();

    //变量getter & setter 方法
    public int getCurrentlyRoomId() {
        return currentlyRoomId;
    }

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

                    commandESP.setRoomId(currentlyIndoorDB.getRoomId());
                    commandESP.setDeviceType(currentlyIndoorDB.getDeviceType());
                    commandESP.setRoomState(currentlyIndoorDB.getRoomStatus());
                    commandESP.setSetting_temp(currentlyIndoorDB.getSettingTemperature());
                    commandESP.setSetting_humidity(currentlyIndoorDB.getSettingHumidity());
                    commandESP.setSettingfanSpeed(currentlyIndoorDB.getSettingFanStatus());
                }
            }
        }
        if (currentlyIndoorDB.getRoomId() == 0) {
            currentlyIndoorDB.setRoomId(currentlyRoomId);
            commandESP.setRoomId(currentlyRoomId);
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
     * 设备设置调整页面的各种实现方法，供其调用
     */
    //停止按钮实现方法，停止房间所有设备
    public void stopRoomDevice(int roomid) {
        commandESP.setRoomId(roomid);
        commandESP.setDeviceType(Constants.deviceType_fancoil);
        commandESP.setRoomState(Constants.roomState_OFF);
        commandESP.setSettingfanSpeed(Constants.fanSpeed_STOP);
        myRepository.commandToDevice(commandESP);
        commandESP.setRoomId(roomid);
        commandESP.setDeviceType(Constants.deviceType_floorheater);
        commandESP.setRoomState(Constants.roomState_OFF);
        myRepository.commandToDevice(commandESP);
    }

    //手动按钮实现方法
    public void manualRoomDevice(int roomid) {
        commandESP.setRoomId(roomid);
        commandESP.setRoomState(Constants.roomState_MANUAL);
        myRepository.commandToDevice(commandESP);
    }

    //周期自动按钮实现方法
    public void autoRoomDevice(int roomid) {
        commandESP.setRoomId(roomid);
        commandESP.setRoomState(Constants.roomState_AUTO);
        commandESP.setSettingfanSpeed(Constants.fanSpeed_AUTO);
        myRepository.commandToDevice(commandESP);
    }

    //宴会按钮实现方法，这个好像没有用到。
    public void feastRoomDevice(int roomid) {
        commandESP.setRoomId(roomid);
        commandESP.setDeviceType(Constants.deviceType_floorheater);
        //判断一下是否在宴会状态
        if (commandESP.getRoomState() == Constants.roomState_FEAST) {
            commandESP.setRoomState(Constants.roomState_MANUAL);
        } else {
            commandESP.setRoomState(Constants.roomState_FEAST);
        }
        myRepository.commandToDevice(commandESP);
    }

    //除湿按钮实现方法
    public void dehumidityRoomDevice(int roomid) {
        commandESP.setRoomId(roomid);
        commandESP.setDeviceType(Constants.deviceType_fancoil);
        if (commandESP.getRoomState() == Constants.roomState_DEHUMIDITY) {
            commandESP.setSettingfanSpeed(Constants.roomState_MANUAL);
        } else {
            commandESP.setSettingfanSpeed(Constants.roomState_DEHUMIDITY);
            commandESP.setSettingfanSpeed(Constants.fanSpeed_LOW);
        }
        myRepository.commandToDevice(commandESP);
    }

    //地暖按钮实现方法
    public void floorRoomDevice(int roomid) {
        commandESP.setRoomId(roomid);
        commandESP.setDeviceType(Constants.deviceType_floorheater);
        myRepository.commandToDevice(commandESP);
    }

    //空调按钮实现方法
    public void fancoilRoomDevice(int roomid) {
        commandESP.setRoomId(roomid);
        commandESP.setDeviceType(Constants.deviceType_fancoil);
        myRepository.commandToDevice(commandESP);
    }

    //风速按钮实现方法
    public void fanSpeedRoomDevice(int roomid, int fanSpeed) {
        commandESP.setRoomId(roomid);
        commandESP.setDeviceType(Constants.deviceType_fancoil);
        commandESP.setRoomState(Constants.roomState_MANUAL);//yes 改成手动运行
        commandESP.setSettingfanSpeed(fanSpeed);
        myRepository.commandToDevice(commandESP);
    }

    //传送温度
    public void temperatureToRoomDevice(int roomid, int temp) {
        commandESP.setRoomId(roomid);
        commandESP.setDeviceType(Constants.deviceType_fancoil);
        commandESP.setSetting_temp(temp);//传输的X10 的假浮点
        myRepository.commandToDevice(commandESP);
        commandESP.setRoomId(roomid);
        commandESP.setDeviceType(Constants.deviceType_floorheater);
        commandESP.setSetting_temp(temp);//传输的X10 的假浮点
        myRepository.commandToDevice(commandESP);
    }

    //传送湿度
    public void humidityToRoomDevice(int roomid, int temp) {
        commandESP.setRoomId(roomid);
        commandESP.setDeviceType(Constants.deviceType_fancoil);
        commandESP.setSetting_humidity(temp);//传输的X10 的假浮点
        myRepository.commandToDevice(commandESP);
        commandESP.setRoomId(roomid);
        commandESP.setDeviceType(Constants.deviceType_floorheater);
        commandESP.setSetting_humidity(temp);//传输的X10 的假浮点
        myRepository.commandToDevice(commandESP);
    }


    /**
     * 包装 myRepository 里的方法：
     */
    public LiveData<List<IndoorDB>> getAllLatestIndoorDBsLive() {
        return myRepository.getAllLatestIndoorDBsLive();
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
