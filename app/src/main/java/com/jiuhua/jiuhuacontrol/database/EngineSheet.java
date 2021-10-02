package com.jiuhua.jiuhuacontrol.database;

/*这张表的数据一分钟一次，存储上限为三个月*/

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(indices = @Index(value={"timestamp", "room_id"}, unique = true))
public class EngineSheet {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "timestamp")
    private long timeStamp;   //直接使用UNIX时间
    //
    @ColumnInfo(name = "room_id")
    @SerializedName("roomId")
    private int roomId;

    //
    @ColumnInfo(name = "device_type")
    @SerializedName("deviceType")
    private int deviceType;

    @ColumnInfo(name = "device_id")
    @SerializedName("deviceId")
    private String deviceId; //模块的MAC，

    //
    @ColumnInfo(name = "setting_temperature")
    @SerializedName("settingTemperature")
    private int settingTemperature;//10X 之后的假浮点。
    @ColumnInfo(name = "current_temperature")
    @SerializedName("currentlyTemperature")
    private int currentTemperature;//模块 10X 之后的假浮点。
    //
    @ColumnInfo(name = "setting_humidity")
    @SerializedName("settingHumidity")
    private int settingHumidity;
    @ColumnInfo(name = "current_humidity")
    @SerializedName("currentlyHumidity")
    private int currentHumidity;

    @ColumnInfo
    private boolean isengineRuning;

    @ColumnInfo
    private int roomState;

    public EngineSheet() {
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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getRoomState() {
        return roomState;
    }

    public int getSettingTemperature() {
        return settingTemperature;
    }

    public void setSettingTemperature(int settingTemperature) {
        this.settingTemperature = settingTemperature;
    }

    public int getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentTemperature(int currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    public int getSettingHumidity() {
        return settingHumidity;
    }

    public void setSettingHumidity(int settingHumidity) {
        this.settingHumidity = settingHumidity;
    }

    public int getCurrentHumidity() {
        return currentHumidity;
    }

    public void setCurrentHumidity(int currentHumidity) {
        this.currentHumidity = currentHumidity;
    }

    public void setRoomState(int roomState) {
        this.roomState = roomState;
    }

    public boolean isIsengineRuning() {
        return isengineRuning;
    }

    public void setIsengineRuning(boolean isengineRuning) {
        this.isengineRuning = isengineRuning;
    }

}
