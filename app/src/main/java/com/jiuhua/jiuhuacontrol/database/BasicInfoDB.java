package com.jiuhua.jiuhuacontrol.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class BasicInfoDB {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int Id;
    @ColumnInfo(name = "roomId")
    private int roomId;
    @ColumnInfo(name = "roomname")
    private String roomName;
    @ColumnInfo(name = "sensorCalibration")
    private int sensorCalibration;
    //风机盘管的情况
    @ColumnInfo(name = "fancoiltrademark")
    private String fancoilTrademark;
    @ColumnInfo(name = "fancoiltype")
    private String fanCoilType;
    @ColumnInfo(name = "hascoilvalve")
    private boolean hasCoilValve;
    //地暖的情况
    @ColumnInfo(name = "isfloorHeat")
    private boolean isFloorHeat;
    @ColumnInfo(name = "isfloorauto")
    private boolean isFloorAuto;
    //暖气片的情况
    @ColumnInfo(name = "radiatortrademark")
    private String radiatorTrademark;
    @ColumnInfo(name = "radiatortype")
    private String radiatorType;
    @ColumnInfo(name = "isradiatorauto")
    private boolean isRadiatorAuto;


    public BasicInfoDB() {}

    @Ignore
    public BasicInfoDB(String roomName) {this.roomName = roomName; }

    @Ignore
    public BasicInfoDB(int id, String roomName) {
        this.Id = id;
        this.roomName = roomName;
    }

    @Ignore
    public BasicInfoDB(int roomId, String roomName, int sensorCalibration, String fancoilTrademark, String fanCoilType,
                       boolean hasCoilValve, boolean isFloorHeat, boolean isFloorAuto,
                       String radiatorTrademark, String radiatorType, boolean isRadiatorAuto) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.sensorCalibration = sensorCalibration;
        this.fancoilTrademark = fancoilTrademark;
        this.fanCoilType = fanCoilType;
        this.hasCoilValve = hasCoilValve;
        this.isFloorHeat = isFloorHeat;
        this.isFloorAuto = isFloorAuto;
        this.radiatorTrademark = radiatorTrademark;
        this.radiatorType = radiatorType;
        this.isRadiatorAuto = isRadiatorAuto;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getSensorCalibration() {
        return sensorCalibration;
    }

    public void setSensorCalibration(int sensorCalibration) {
        this.sensorCalibration = sensorCalibration;
    }

    //风机盘管参数相关
    public String getFancoilTrademark() {
        return fancoilTrademark;
    }

    public void setFancoilTrademark(String fancoilTrademark) {
        this.fancoilTrademark = fancoilTrademark;
    }

    public String getFanCoilType() {
        return fanCoilType;
    }

    public void setFanCoilType(String fanCoilType) {
        this.fanCoilType = fanCoilType;
    }

    public boolean isHasCoilValve() {
        return hasCoilValve;
    }

    public void setHasCoilValve(boolean hasCoilValve) {
        this.hasCoilValve = hasCoilValve;
    }

    //地暖参数相关
    public boolean isFloorHeat() {
        return isFloorHeat;
    }

    public void setFloorHeat(boolean floorHeat) {
        isFloorHeat = floorHeat;
    }

    public boolean isFloorAuto() {
        return isFloorAuto;
    }

    public void setFloorAuto(boolean floorAuto) {
        isFloorAuto = floorAuto;
    }

    //暖气片参数相关
    public String getRadiatorTrademark() {
        return radiatorTrademark;
    }

    public void setRadiatorTrademark(String radiatorTrademark) {
        this.radiatorTrademark = radiatorTrademark;
    }

    public String getRadiatorType() {
        return radiatorType;
    }

    public void setRadiatorType(String radiatorType) {
        this.radiatorType = radiatorType;
    }

    public boolean isRadiatorAuto() {
        return isRadiatorAuto;
    }

    public void setRadiatorAuto(boolean radiatorAuto) {
        this.isRadiatorAuto = radiatorAuto;
    }
}
