package com.jiuhua.jiuhuacontrol;

/*
* this class will be translate to json to pass order
* 这个类将转换成json格式通过mqtt传输到模块，去操作模块*/
public class CommandESP {
    private int roomId;// 1, 2, 3, 4, 5, 6, 7, 8, etc
    private int deviceType;//FANCOIL 0, FLLORHEATER 1, RADIATOR 2, BOILER 3, HEATPUMP 4, DHTSENSOR 5
    private int roomState;//OFF 0, MANUAL 1, AUTOMATION 2, DEHUMIDITY 3, FEAST 4
    private int settingfanSpeed;//STOP 0, LOW 1, MEDIUM 2, HIGH 3, AUTOSPEED 4
    private String module;  //reboot, feedback, etc.
    private int setting_temp;  //X10 假浮点 False float
    private int setting_humidity;  //X10 假浮点 False float
    private int[] peroid; //
    private String upgrade;//升级模块指令集。

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

    public int getRoomState() {
        return roomState;
    }

    public void setRoomState(int roomState) {
        this.roomState = roomState;
    }

    public int getSettingfanSpeed() {
        return settingfanSpeed;
    }

    public void setSettingfanSpeed(int settingfanSpeed) {
        this.settingfanSpeed = settingfanSpeed;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public int getSetting_temp() {
        return setting_temp;
    }

    public void setSetting_temp(int setting_temp) {
        this.setting_temp = setting_temp;
    }

    public int getSetting_humidity() {
        return setting_humidity;
    }

    public void setSetting_humidity(int setting_humidity) {
        this.setting_humidity = setting_humidity;
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
