package com.jiuhua.jiuhuacontrol.room;

public class RoomLotMessage {

        private String roomName;
        private String currentTemperature;
        private String settingTemperature;
        private String currentHumidity;
        private String settingHumidity;
        private String fanStatus;
        private String floorValveOpen;
        private String coilValveOpen;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentTemperature(String currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    public String getSettingTemperature() {
        return settingTemperature;
    }

    public void setSettingTemperature(String settingTemperature) {
        this.settingTemperature = settingTemperature;
    }

    public String getCurrentHumidity() {
        return currentHumidity;
    }

    public void setCurrentHumidity(String currentHumidity) {
        this.currentHumidity = currentHumidity;
    }

    public String getSettingHumidity() {
        return settingHumidity;
    }

    public void setSettingHumidity(String settingHumidity) {
        this.settingHumidity = settingHumidity;
    }

    public String getFanStatus() {
        return fanStatus;
    }

    public void setFanStatus(String fanStatus) {
        this.fanStatus = fanStatus;
    }

    public String getFloorValveOpen() {
        return floorValveOpen;
    }

    public void setFloorValveOpen(String floorValveOpen) {
        this.floorValveOpen = floorValveOpen;
    }

    public String getCoilValveOpen() {
        return coilValveOpen;
    }

    public void setCoilValveOpen(String coilValveOpen) {
        this.coilValveOpen = coilValveOpen;
    }
}
