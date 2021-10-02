package com.jiuhua.jiuhuacontrol.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Entity(indices = @Index(value={"timestamp", "room_id"}, unique = true))
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


    //@SerializedName( "deviceType" )
    //@ColumnInfo( name = "device_type" )
    ///** FANCOIL 0, FLOORWATERSHED 1, RADIATOR 2, BOILER 3, HEATPUMP 4, DHTSENSOR 5, NTCSENSOR 6, PHONE 7, sendPeriods 8*/
    //private int deviceType;

    //@ColumnInfo(name = "device_id")
    //@SerializedName("deviceId")
    //private String deviceId; //模块的clipId

    //存储周期数组
    @ColumnInfo(name = "period")
    @SerializedName("period")
    @TypeConverters({Converters.class})
    private List<DayPeriod> oneRoomWeeklyPeriod;//好像是乱序，反正全部使用

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
