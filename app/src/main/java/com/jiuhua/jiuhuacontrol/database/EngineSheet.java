package com.jiuhua.jiuhuacontrol.database;

/*这张表的数据一分钟一次，存储上限为三个月*/

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
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

    @ColumnInfo
    private boolean isengineRuning;

    public EngineSheet(long timeStamp, boolean isengineRuning) {
        this.timeStamp = timeStamp;
        this.isengineRuning = isengineRuning;
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

    public boolean isIsengineRuning() {
        return isengineRuning;
    }

    public void setIsengineRuning(boolean isengineRuning) {
        this.isengineRuning = isengineRuning;
    }

}
