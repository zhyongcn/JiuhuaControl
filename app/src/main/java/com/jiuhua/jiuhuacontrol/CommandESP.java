package com.jiuhua.jiuhuacontrol;

/*
* this class will be translate to json to pass order
* 这个类将转换成json格式通过mqtt传输到模块，去操作模块*/
public class CommandESP {
    private int roomId;
    private String deviceType;//使用枚举？？
    private String roomState;
    private String fanSpeed;
    private String module;
    private int set_temp;
    private int set_humidity;
    private int avg_temp;
    private int avg_humidity;
    private int[] peroid;
    private String upgrade;

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getRoomState() {
        return roomState;
    }

    public void setRoomState(String roomState) {
        this.roomState = roomState;
    }

    public String getFanSpeed() {
        return fanSpeed;
    }

    public void setFanSpeed(String fanSpeed) {
        this.fanSpeed = fanSpeed;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public int getSet_temp() {
        return set_temp;
    }

    public void setSet_temp(int set_temp) {
        this.set_temp = set_temp;
    }

    public int getSet_humidity() {
        return set_humidity;
    }

    public void setSet_humidity(int set_humidity) {
        this.set_humidity = set_humidity;
    }

    public int getAvg_temp() {
        return avg_temp;
    }

    public void setAvg_temp(int avg_temp) {
        this.avg_temp = avg_temp;
    }

    public int getAvg_humidity() {
        return avg_humidity;
    }

    public void setAvg_humidity(int avg_humidity) {
        this.avg_humidity = avg_humidity;
    }

    public int[] getPeroid() {
        return peroid;
    }

    public void setPeroid(int[] peroid) {
        this.peroid = peroid;
    }

    public String getUpgrade() {
        return upgrade;
    }

    public void setUpgrade(String upgrade) {
        this.upgrade = upgrade;
    }
}
