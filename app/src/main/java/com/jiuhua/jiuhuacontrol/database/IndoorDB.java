package com.jiuhua.jiuhuacontrol.database;

/*这张表的数据一分钟一次，存储上限为三个月*/

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
//fixme 添加关联外键，失败了吧！（是不是在线程的问题上，不能在主线程UI线程）

@Entity
public class IndoorDB {

    @PrimaryKey(autoGenerate = true)
    private long id;
    //
    @ColumnInfo(name = "timestamp")
    @SerializedName("timestamp")
    private long timeStamp;   //直接使用UNIX时间
    //
    @ColumnInfo(name = "room_id")
    @SerializedName("roomId")
    private int roomId;
    //
    @ColumnInfo(name = "device_type")
    @SerializedName("deviceType")
    private int deviceType; //FANCOIL 0, FLOORHEATER 1, RADIATOR 2, BOILER 3, HEATPUMP 4
    //
    @ColumnInfo(name = "setting_temperature")
    @SerializedName("settingTemp")
    private int settingTemperature;//10X 之后的假浮点。
    @ColumnInfo(name = "current_temperature")
    @SerializedName("currentTemp")
    private int currentTemperature;//模块 10X 之后的假浮点。

    @ColumnInfo(name = "setting_humidity")
    @SerializedName("settingHumidity")
    private int settingHumidity;
    @ColumnInfo(name = "current_humidity")
    @SerializedName("currentHumidity")
    private int currentHumidity;
    //
    @ColumnInfo(name = "current_fan_status")//不要使用枚举，占用太多资源，还需要转换器。
    @SerializedName("currentFanSpeed")
    private int currentFanStatus;  //Constants.fanSpeed_XXX
    @ColumnInfo(name = "setting_fan_status")
    @SerializedName("settingFanSpeed")
    private int settingFanStatus;
    //
    @ColumnInfo(name = "floor_valve")
    @SerializedName("floorvalve")
    private boolean floorValveOpen;
    @ColumnInfo(name = "coil_valve")
    @SerializedName("coilvalve")
    private boolean coilValveOpen;
    //
    @ColumnInfo(name = "room_status")//不要使用枚举，占用太多资源，还需要转换器。
    @SerializedName("roomState")
    private int roomStatus; //Constants.roomState_XXXX

    public IndoorDB() {
        //empty public constructor for IndoorViewModel
    }

    @Ignore
    public IndoorDB(long timeStamp, int roomId, int deviceType, int currentTemperature, int settingTemperature,
                    int currentHumidity, int settingHumidity, int settingFanStatus, int currentFanStatus, boolean floorValveOpen,
                    boolean coilValveOpen, int roomStatus) {
        this.timeStamp = timeStamp;
        this.roomId = roomId;
        this.deviceType = deviceType;
        this.currentTemperature = currentTemperature;
        this.settingTemperature = settingTemperature;
        this.currentHumidity = currentHumidity;
        this.settingHumidity = settingHumidity;
        this.settingFanStatus = settingFanStatus;
        this.currentFanStatus = currentFanStatus;
        this.floorValveOpen = floorValveOpen;
        this.coilValveOpen = coilValveOpen;
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

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
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

    public int getCurrentFanStatus() {
        return currentFanStatus;
    }

    public void setCurrentFanStatus(int currentFanStatus) {
        this.currentFanStatus = currentFanStatus;
    }

    public int getSettingFanStatus() {
        return settingFanStatus;
    }

    public void setSettingFanStatus(int settingFanStatus) {
        this.settingFanStatus = settingFanStatus;
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

    public int getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(int roomStatus) {
        this.roomStatus = roomStatus;
    }
}
