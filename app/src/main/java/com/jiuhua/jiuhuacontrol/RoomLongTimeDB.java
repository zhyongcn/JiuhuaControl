package com.jiuhua.jiuhuacontrol;


/*这张表把room表的数据一小时一次加平均，*/
//TODO 计算时间匹配，风量，开阀时间，平均温度湿度，需要在其他地方。
//TODO 一小时填表一次，还是三个月填表一次？？

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = RoomDB.class, parentColumns = "id",childColumns = "room_name_id"),
 indices = { @Index(value = {"room_name_id"})})
public class RoomLongTimeDB {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = " timestamp")
    private long timeStamp;   //小时的时间戳
    @ColumnInfo(name = "room_name_id")
    private int RoomNameId;
    @ColumnInfo(name = "current_temperature")
    private int currentTemperature;    //一小时的温度平均值
    @ColumnInfo(name = "setting_temperature")
    private int settingTemperature;    //设置温度值
    @ColumnInfo(name = "current_humidity")
    private int currentHumidity;   //一小时的湿度平均值
    @ColumnInfo(name = "setting_humidity")
    private int settingHumidity;
    @ColumnInfo(name = "air_volume")
    private int airVolume;   //风量，立方米 (国标风机参数表，实际需要校准[安装后风速*截面积] )
    @ColumnInfo(name = "floor_minute")
    private int floorMinute;   //地暖运行时间  0--60分钟
    @ColumnInfo(name = "coil_valve_minute")
    private boolean coilValveMinute;  //两通阀打开时间  0--60分钟

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

    public int getRoomNameId() {
        return RoomNameId;
    }

    public void setRoomNameId(int roomNameId) {
        RoomNameId = roomNameId;
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

    public int getAirVolume() {
        return airVolume;
    }

    public void setAirVolume(int airVolume) {
        this.airVolume = airVolume;
    }

    public int getFloorMinute() {
        return floorMinute;
    }

    public void setFloorMinute(int floorMinute) {
        this.floorMinute = floorMinute;
    }

    public boolean isCoilValveMinute() {
        return coilValveMinute;
    }

    public void setCoilValveMinute(boolean coilValveMinute) {
        this.coilValveMinute = coilValveMinute;
    }

    public RoomLongTimeDB() {
    }

    public RoomLongTimeDB(long timeStamp, int roomNameId, int currentTemperature, int settingTemperature,
                          int currentHumidity, int settingHumidity, int airVolume, int floorMinute,
                          boolean coilValveMinute) {

        this.timeStamp = timeStamp;
        RoomNameId = roomNameId;
        this.currentTemperature = currentTemperature;
        this.settingTemperature = settingTemperature;
        this.currentHumidity = currentHumidity;
        this.settingHumidity = settingHumidity;
        this.airVolume = airVolume;
        this.floorMinute = floorMinute;
        this.coilValveMinute = coilValveMinute;

    }

}
