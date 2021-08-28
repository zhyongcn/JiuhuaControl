package com.jiuhua.jiuhuacontrol.ui;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jiuhua.jiuhuacontrol.CommandFromPhone;
import com.jiuhua.jiuhuacontrol.CommandPeriod;
import com.jiuhua.jiuhuacontrol.Constants;
import com.jiuhua.jiuhuacontrol.database.BasicInfoSheet;
import com.jiuhua.jiuhuacontrol.database.FancoilSheet;
import com.jiuhua.jiuhuacontrol.database.SensorSheet;
import com.jiuhua.jiuhuacontrol.database.PeriodSheet;
import com.jiuhua.jiuhuacontrol.database.WatershedSheet;
import com.jiuhua.jiuhuacontrol.repository.MyRepository;
import com.jiuhua.jiuhuacontrol.database.DayPeriod;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    MyRepository myRepository;
    List<BasicInfoSheet> allLatestBasicInfoSheets = new ArrayList<>();
    List<SensorSheet> allLatestSensorSheets = new ArrayList<>();   //room1,room2...`s currenttempreature .
    List<FancoilSheet> allLatestFancoilSheets = new ArrayList<>();
    List<WatershedSheet> allLatestWatershedSheets = new ArrayList<>();
    List<PeriodSheet> allLatestPeriodSheets = new ArrayList<>();   //room1,room2...`s period.

    SensorSheet currentlySensorSheet = new SensorSheet();
    FancoilSheet currentlyFancoilSheet = new FancoilSheet();
    WatershedSheet currentlyWatershedSheet = new WatershedSheet();
    PeriodSheet currentRoomWeeklyPeriodSheet;  //currently room`s one weekly period.


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

    public void setAllBasicInfo(List<BasicInfoSheet> basicInfoSheets) {
        this.allLatestBasicInfoSheets = basicInfoSheets;
    }

    public List<SensorSheet> getAllLatestSensorSheets() {
        return allLatestSensorSheets;
    }

    public List<FancoilSheet> getAllLatestFancoilSheets() {
        return allLatestFancoilSheets;
    }

    public List<WatershedSheet> getAllLatestWatershedSheets() {
        return allLatestWatershedSheets;
    }

    public List<PeriodSheet> getAllLatestPeriodSheets() {
        return allLatestPeriodSheets;
    }

    public SensorSheet getCurrentlySensorSheet() {
        return currentlySensorSheet;
    }

    public void setCurrentlySensorSheet(SensorSheet currentlySensorSheet) {
        this.currentlySensorSheet = currentlySensorSheet;
    }

    public FancoilSheet getCurrentlyFancoilSheet() {
        return currentlyFancoilSheet;
    }

    public void setCurrentlyFancoilSheet(FancoilSheet currentlyFancoilSheet) {
        this.currentlyFancoilSheet = currentlyFancoilSheet;
    }

    public WatershedSheet getCurrentlyWatershedSheet() {
        return currentlyWatershedSheet;
    }

    public void setCurrentlyWatershedSheet(WatershedSheet currentlyWatershedSheet) {
        this.currentlyWatershedSheet = currentlyWatershedSheet;
    }

    public PeriodSheet getCurrentRoomWeeklyPeriodSheet() {
        return currentRoomWeeklyPeriodSheet;
    }

    public void setCurrentRoomWeeklyPeriodSheet(PeriodSheet currentRoomWeeklyPeriodSheet) {
        this.currentRoomWeeklyPeriodSheet = currentRoomWeeklyPeriodSheet;
    }

    public int getCurrentlyRoomId() {
        return currentlyRoomId;
    }

    //当前所有传感器的状态以及当前房间的传感器状态
    public void setAllLatestSensorSheets(List<SensorSheet> allLatestSensorSheetsLive) {
        this.allLatestSensorSheets = allLatestSensorSheetsLive;
        for (SensorSheet sensorSheet : allLatestSensorSheets) {
            if (sensorSheet.getRoomId() == currentlyRoomId) {
                this.currentlySensorSheet = sensorSheet;
            }
        }
    }

    //设置风机的当前所有状态以及当前房间的风机状态
    public void setAllLatestFancoilSheets(List<FancoilSheet> allLatestFancoilSheets) {
        this.allLatestFancoilSheets = allLatestFancoilSheets;
        if (allLatestFancoilSheets.size() > 0) {
            for (FancoilSheet fancoilSheet : allLatestFancoilSheets) {
                if (fancoilSheet.getRoomId() == currentlyRoomId) {
                    this.currentlyFancoilSheet = fancoilSheet;
                }
            }
        }
    }

    //设置当前分水器的所有状态，以及当前分水器（地暖）的状态
    public void setAllLatestWatershedSheets(List<WatershedSheet> allLatestWatershedSheets) {
        this.allLatestWatershedSheets = allLatestWatershedSheets;
        for (WatershedSheet watershedSheet : allLatestWatershedSheets) {
            if (watershedSheet.getRoomId() == currentlyRoomId) {
                this.currentlyWatershedSheet = watershedSheet;
            }
        }
    }

    //设置当前的所有周期以及当前的房间的周期
    public void setAllLatestPeriodSheets(List<PeriodSheet> allLatestPeriodDBsLive) {
        this.allLatestPeriodSheets = allLatestPeriodDBsLive;
        for (PeriodSheet periodSheet : allLatestPeriodSheets) {
            if (periodSheet.getRoomId() == currentlyRoomId) {
                this.currentRoomWeeklyPeriodSheet = periodSheet;
            }
        }
        if (currentRoomWeeklyPeriodSheet == null) { //如果迭代完成还没有被赋值，说明没有这个房间的数据，新建一个房间的基础数据
            currentRoomWeeklyPeriodSheet = new PeriodSheet();
            currentRoomWeeklyPeriodSheet.setRoomId(currentlyRoomId);
            currentRoomWeeklyPeriodSheet.setOneRoomWeeklyPeriod(new ArrayList<>());
        }
    }

    //构造方法
    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.myRepository = MyRepository.getInstance(application);


    }

    public void firstAskTDengine(){
        //去云端获取数据
        for (BasicInfoSheet basicInfoSheet :allLatestBasicInfoSheets) {
            long ts = 0;
            for (SensorSheet sensorSheet : allLatestSensorSheets) {
                if (sensorSheet.getRoomId() == basicInfoSheet.getRoomId()) {
                    ts = sensorSheet.getTimeStamp();
                }
            }
            if (ts > 16000000) {
                String sql = "select  * from homedevice.sensors where location = '"
                        + Constants.mqtt_topic_prefix
                        + basicInfoSheet.getRoomId()
                        + "' and ts > " + ts + "000";
                myRepository.requestTDengineData(sql);
            }
        }

//sql = "select  * from homedevice.sensors where location = '86518/yuxiuhuayuan/12-1-101/Room2' and ts > now - 1h";
//myRepository.requestTDengineData(sql);
//
//sql = "select  * from homedevice.fancoils where location = '86518/yuxiuhuayuan/12-1-101/room1' and ts > now - 1h";
//myRepository.requestTDengineData(sql);
//
//sql = "select  * from homedevice.watersheds where location = '86518/yuxiuhuayuan/12-1-101/Room1' and ts > now - 1h";
//myRepository.requestTDengineData(sql);

    }

    /**
     * 设备设置调整页面的各种实现方法，供其调用。
     * ***改变了需要改变的参数，其他参数不动。***
     */
    //传送房间设置状态
    public void roomstateToDevice(int roomid, int roomstates) {
        String topic = Constants.mqtt_topic_prefix + roomid;

        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("roomId", roomid);
        jsonObject.addProperty("deviceType", Constants.deviceType_phone);//手机来的命令才接受。DHT，NTC模块接收不在mqttconfig里面？
        jsonObject.addProperty("roomState", roomstates);
        String msg = gson.toJson(jsonObject);

        CommandFromPhone commandFromPhone = new CommandFromPhone(topic, 1, msg, false);
        myRepository.commandToModule(commandFromPhone);
    }

    //传送风速设置按钮
    public void fanSpeedToDevice(int roomid, int fanSpeed) {
        String topic = Constants.mqtt_topic_prefix + roomid;

        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("roomId", roomid);
        jsonObject.addProperty("deviceType", Constants.deviceType_phone);//手机来的命令才接受。DHT，NTC模块接收不在mqttconfig里面？
        jsonObject.addProperty("settingFanSpeed", fanSpeed);
        String msg = gson.toJson(jsonObject);

        CommandFromPhone commandFromPhone = new CommandFromPhone(topic, 1, msg, false);
        myRepository.commandToModule(commandFromPhone);
    }

    //传送设置温度
    public void temperatureToDevice(int roomid, int temp) {
        String topic = Constants.mqtt_topic_prefix + roomid;

        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("roomId", roomid);
        jsonObject.addProperty("deviceType", Constants.deviceType_phone);
        jsonObject.addProperty("settingTemperature", temp);
        String msg = gson.toJson(jsonObject);

        CommandFromPhone commandFromPhone = new CommandFromPhone(topic, 1, msg, false);
        myRepository.commandToModule(commandFromPhone);
    }

    //传送设定湿度
    public void humidityToDevice(int roomid, int humidity) {
        String topic = Constants.mqtt_topic_prefix + roomid;

        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("roomId", roomid);
        jsonObject.addProperty("deviceType", Constants.deviceType_phone);
        jsonObject.addProperty("settingHumidity", humidity);
        String msg = gson.toJson(jsonObject);

        CommandFromPhone commandFromPhone = new CommandFromPhone(topic, 1, msg, false);
        myRepository.commandToModule(commandFromPhone);
    }


    /**
     * 包装 myRepository 里的方法：
     */
    public LiveData<List<SensorSheet>> getAllLatestSensorSheetsLive() {
        return myRepository.getAllLatestSensorSheetsLive(Constants.deviceType_DHTsensor);
    }

    public LiveData<List<FancoilSheet>> getAllLatestFancoilSheetsLive() {
        return myRepository.getAllLatestFancoilSheetsLive();
    }


    public LiveData<List<WatershedSheet>> getAllLatestWatershedSheetsLive() {
        return myRepository.getAllLatestWatershedSheetsLive();
    }

    //***以下 period 周期相关***
    //从本地数据库获取最新的周期信息
    public LiveData<List<PeriodSheet>> getAllLatestPeriodSheetsLive() {
        return myRepository.getAllLatestPeriodSheetsLive();
    }

    //周期插入数据库
    public void insertPeriodSheet(int roomId) {
        currentRoomWeeklyPeriodSheet.setId(currentRoomWeeklyPeriodSheet.getId() + allLatestPeriodSheets.size());
        //id是从数据库里取出的，加上有几个房间，不会冲掉数据。id不同，数据库认为不是一个数据
        currentRoomWeeklyPeriodSheet.setRoomId(roomId);
        currentRoomWeeklyPeriodSheet.setTimeStamp(new Date().getTime() / 1000);  //这个方法得到的是毫秒，this method return ms。
        myRepository.insertPeriodSheet(currentRoomWeeklyPeriodSheet);
    }

    //把周期传递给模块 period[15][3]  一个星期的有必要。
    public void periodToDevice(int roomid, List<DayPeriod> dayPeriods) {
        String topic = Constants.mqtt_topic_prefix + roomid;

        Gson gson = new Gson();

        currentRoomWeeklyPeriodSheet.setRoomId(roomid);
        currentRoomWeeklyPeriodSheet.setTimeStamp(new Date().getTime() / 1000);//没有必要
        currentRoomWeeklyPeriodSheet.setOneRoomWeeklyPeriod(dayPeriods);

        new Thread(new Runnable() {
            @Override
            public void run() {
                int roomid = currentRoomWeeklyPeriodSheet.getRoomId();

                for (int wd = 0; wd < 7; wd++) {
                    CommandPeriod commandPeriod = new CommandPeriod();
                    commandPeriod.setRoomId(roomid);
                    int[][] temperatureArray = new int[15][3];
                    int k = 0;
                    commandPeriod.setWeekday(wd);
                    for (int i = 0; i < currentRoomWeeklyPeriodSheet.getOneRoomWeeklyPeriod().size(); i++) {
                        if (wd == currentRoomWeeklyPeriodSheet.getOneRoomWeeklyPeriod().get(i).getWeekday()) {
                            temperatureArray[k][0] = currentRoomWeeklyPeriodSheet.getOneRoomWeeklyPeriod().get(i).getStartMinuteStamp();
                            temperatureArray[k][1] = currentRoomWeeklyPeriodSheet.getOneRoomWeeklyPeriod().get(i).getEndMinuteStamp();
                            temperatureArray[k][2] = currentRoomWeeklyPeriodSheet.getOneRoomWeeklyPeriod().get(i).getTempreature();
                            k++;
                        }
                    }
                    commandPeriod.setPeriod(temperatureArray);
                    String s = gson.toJson(commandPeriod);
                    Log.d("periodToDevice", s);

                    CommandFromPhone commandFromPhone = new CommandFromPhone(topic, 1, s, false);
                    myRepository.commandToModule(commandFromPhone);

                    try {
                        Thread.sleep(500);//延迟发送，太快模块接受不了。
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();//FIXME：***少写了“.start()”，基本概念不清害死人啊！！***

    }

    //包装 Repository 里面的 Dao 方法
    public void insertRoomName(BasicInfoSheet... basicInfoSheets) {
        myRepository.insertBasicInfo(basicInfoSheets);
    }

    public void deleteAllRoomsName() {
        myRepository.deleteAllBasicInfo();
    }

    public LiveData<List<BasicInfoSheet>> getAllBasicInfoLive() {
        return myRepository.getAllBasicInfoLive();
    }

    public LiveData<List<SensorSheet>> getAllLatestSensorSheetsLive(int devicetypeId) {
        return myRepository.getAllLatestSensorSheetsLive(devicetypeId);
    }

    //获取普通房间的名字
    public String loadRoomName(int roomid) {
        return myRepository.loadRoomName(roomid);
    }




}
