package com.jiuhua.jiuhuacontrol.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Entity
public class PeriodSheet {

    @PrimaryKey(autoGenerate = true)
    private int id;
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
    @TypeConverters({Converters.class})
    private List<DayPeriod> oneRoomWeeklyPeriod;

    public PeriodSheet() {
    }

    @Ignore
    public PeriodSheet(int roomId, List<DayPeriod> oneRoomWeeklyPeriod) {
        this.roomId = roomId;
        this.oneRoomWeeklyPeriod = oneRoomWeeklyPeriod;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public List<DayPeriod> getOneRoomWeeklyPeriod() {
        return oneRoomWeeklyPeriod;
    }

    public void setOneRoomWeeklyPeriod(List<DayPeriod> oneRoomWeeklyPeriod) {
        this.oneRoomWeeklyPeriod = oneRoomWeeklyPeriod;
    }
}
