package com.jiuhua.jiuhuacontrol.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class PeriodDB {

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
                                        //存储房间名。--- 提高了复杂度，舍弃。
                                    //    @ColumnInfo(name = roomName)//提高了复杂度！
                                    //    @SerializedName("roomName")
                                    //    private String roomName;
    //存储周期数组
    @ColumnInfo(name = "period")
    @SerializedName("period")
    private int[][] period;

    public PeriodDB() {
    }

    @Ignore
    public PeriodDB(int roomId, int[][] period) {
        this.roomId = roomId;
        this.period = period;
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

    public int[][] getPeriod() {
        return period;
    }

    public void setPeriod(int[][] period) {
        this.period = period;
    }
}
