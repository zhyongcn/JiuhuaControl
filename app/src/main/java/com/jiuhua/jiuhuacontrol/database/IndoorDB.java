package com.jiuhua.jiuhuacontrol.database;

/*这张表的数据一分钟一次，存储上限为三个月*/

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
//不要自以为牛逼，添加关联外键，失败了吧！（是不是在线程的问题上，不能在主线程UI线程）

@Entity
public class IndoorDB {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = " timestamp")
    private long timeStamp;   //直接使用UNIX时间
    @ColumnInfo(name = "room_name_id")
    private int roomNameId;
    @ColumnInfo(name = "current_temperature")
    private int currentTemperature;
    @ColumnInfo(name = "setting_temperature")
    private int settingTemperature;
    @ColumnInfo(name = "current_humidity")
    private int currentHumidity;
    @ColumnInfo(name = "setting_humidity")
    private int settingHumidity;
    @ColumnInfo(name = "fan_status")
    private int fanStatus;//stop 0, low 1, middium 2, high 3, auto 4.
    @ColumnInfo(name = "floor_valve")
    private boolean floorValveOpen;
    @ColumnInfo(name = "coil_valve")
    private boolean coilValveOpen;
    @ColumnInfo(name = "dehumidity_status")
    private boolean dehumidityStatus;
    @ColumnInfo(name = "room_status")
    private int roomStatus;//stop 0, manual 1, auto 2.

    public IndoorDB() {
        //empty public constructor for IndoorViewModel
    }

    @Ignore
    public IndoorDB(long timeStamp, int roomNameId, int currentTemperature, int settingTemperature,
                    int currentHumidity, int settingHumidity, int fanStatus, boolean floorValveOpen,
                    boolean coilValveOpen, boolean dehumidityStatus, int roomStatus) {
        this.timeStamp = timeStamp;
        this.roomNameId = roomNameId;
        this.currentTemperature = currentTemperature;
        this.settingTemperature = settingTemperature;
        this.currentHumidity = currentHumidity;
        this.settingHumidity = settingHumidity;
        this.fanStatus = fanStatus;
        this.floorValveOpen = floorValveOpen;
        this.coilValveOpen = coilValveOpen;
        this.dehumidityStatus = dehumidityStatus;
        this.roomStatus = roomStatus;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getRoomNameId() {
        return roomNameId;
    }

    public void setRoomNameId(int roomNameId) {
        this.roomNameId = roomNameId;
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

    public int getFanStatus() {
        return fanStatus;
    }

    public void setFanStatus(int fanStatus) {
        this.fanStatus = fanStatus;
    }

    public boolean isFloorValveOpen() {
        return floorValveOpen;
    }

    public void setFloorValveOpen(boolean floorValveOpen) {
        this.floorValveOpen = floorValveOpen;
    }

    public boolean isCoilValveOpen() {
        return coilValveOpen;
    }

    public void setCoilValveOpen(boolean coilValveOpen) {
        this.coilValveOpen = coilValveOpen;
    }

    public boolean isDehumidityStatus() {
        return dehumidityStatus;
    }

    public void setDehumidityStatus(boolean dehumidityStatus) {
        this.dehumidityStatus = dehumidityStatus;
    }

    public int getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(int roomStatus) {
        this.roomStatus = roomStatus;
    }
}
