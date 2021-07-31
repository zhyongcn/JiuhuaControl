package com.jiuhua.jiuhuacontrol.database;

/*这张表的数据一分钟一次，存储上限为三个月*/

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
//fixme 添加关联外键，失败了吧！（是不是在线程的问题上，不能在主线程UI线程）

@Entity
public class SensorSheet {

    @PrimaryKey( autoGenerate = true )
    private long id;
    //
    @ColumnInfo( name = "timestamp" )
    @SerializedName( "timestamp" )
    private long timeStamp;   //直接使用UNIX时间
    //
    @ColumnInfo( name = "room_id" )
    @SerializedName( "roomId" )
    private int roomId;
    //
    @ColumnInfo( name = "device_type" )
    @SerializedName( "deviceType" )
    private int deviceType; //FANCOIL 0, FLOORWATERSHED 1, RADIATOR 2, BOILER 3, HEATPUMP 4, DHTSENSOR 5, NTCSENSOR 6, PHONE 7

    @ColumnInfo(name = "device_id")
    @SerializedName("deviceId")
    private String deviceId; //模块的MAC，
    //
    @ColumnInfo( name = "current_temperature" )
    @SerializedName( "currentlyTemperature" )
    private int currentTemperature;//模块 10X 之后的假浮点。
    @ColumnInfo( name = "adjusting_temperature" )
    @SerializedName( "adjustingTemperature" )
    private int adjustingTemperature;//模块 10X 之后的假浮点。

    @ColumnInfo( name = "current_humidity" )
    @SerializedName( "currentlyHumidity" )
    private int currentHumidity;
    @ColumnInfo( name = "adjusting_humidity" )
    @SerializedName( "adjustingHumidity" )
    private int adjustingHumidity;

    public SensorSheet() {
        //empty public constructor for IndoorViewModel
    }

    @Ignore
    public SensorSheet(long timeStamp, int roomId, int deviceType, int currentTemperature,
                       int currentHumidity) {
        this.timeStamp = timeStamp;
        this.roomId = roomId;
        this.deviceType = deviceType;
        this.currentTemperature = currentTemperature;
        this.currentHumidity = currentHumidity;
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

    public int getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentTemperature(int currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    public int getCurrentHumidity() {
        return currentHumidity;
    }

    public void setCurrentHumidity(int currentHumidity) {
        this.currentHumidity = currentHumidity;
    }

    public int getAdjustingTemperature() {
        return adjustingTemperature;
    }

    public void setAdjustingTemperature(int adjustingTemperature) {
        this.adjustingTemperature = adjustingTemperature;
    }

    public int getAdjustingHumidity() {
        return adjustingHumidity;
    }

    public void setAdjustingHumidity(int adjustingHumidity) {
        this.adjustingHumidity = adjustingHumidity;
    }
}
