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
    @ColumnInfo(name = "roomname")
    private String roomName;
    //主机情况
    @ColumnInfo(name = "enginetrademark")
    private String engineTrademark;
    @ColumnInfo(name = "enginespecification")
    private String engineSpecification;
    //风机盘管的情况
    @ColumnInfo(name = "fancoiltrademark")
    private String fancoilTrademark;
    @ColumnInfo(name = "fancoilspecification ")
    private String fanCoilSpecification;
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
    @ColumnInfo(name = "radiatorspecification")
    private String radiatorSpecification;
    @ColumnInfo(name = "isradiatorauto")
    private boolean isradiatorAuto;


    public BasicInfoDB() {
    }

    @Ignore
    public BasicInfoDB(String roomName) {
        this.roomName = roomName;
    }

    public BasicInfoDB(int id, String roomName) {
        this.Id = id;
        this.roomName = roomName;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    //主机参数相关
    public String getEngineTrademark() {
        return engineTrademark;
    }

    public void setEngineTrademark(String engineTrademark) {
        this.engineTrademark = engineTrademark;
    }

    public String getEngineSpecification() {
        return engineSpecification;
    }

    public void setEngineSpecification(String engineSpecification) {
        this.engineSpecification = engineSpecification;
    }

    //风机盘管参数相关
    public String getFancoilTrademark() {
        return fancoilTrademark;
    }

    public void setFancoilTrademark(String fancoilTrademark) {
        this.fancoilTrademark = fancoilTrademark;
    }

    public String getFanCoilSpecification() {
        return fanCoilSpecification;
    }

    public void setFanCoilSpecification(String fanCoilSpecification) {
        this.fanCoilSpecification = fanCoilSpecification;
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

    public String getRadiatorSpecification() {
        return radiatorSpecification;
    }

    public void setRadiatorSpecification(String radiatorSpecification) {
        this.radiatorSpecification = radiatorSpecification;
    }

    public boolean isIsradiatorAuto() {
        return isradiatorAuto;
    }

    public void setIsradiatorAuto(boolean isradiatorAuto) {
        this.isradiatorAuto = isradiatorAuto;
    }
}
