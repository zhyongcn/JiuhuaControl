package com.jiuhua.jiuhuacontrol;

/*
* this class will be translate to json to pass order
* 这个类将转换成json格式通过mqtt传输到模块，去操作模块*/
public class CommandESP {
    private int roomId;// 1, 2, 3, 4, 5, 6, 7, 8, etc
    private int deviceType;//FANCOIL 0, FLOORWATERSHED 1, RADIATOR 2, BOILER 3, HEATPUMP 4, DHTSENSOR 5, NTCSENSOR 6, PHONE 7, sendperiod = 8, mqttconfig = 9;
    private int roomState;//OFF 0, MANUAL 1, AUTOMATION 2, DEHUMIDITY 3, FEAST 4, SLEEP 5, OUTSIDE 6
    private int settingFanSpeed;//STOP 0, LOW 1, MEDIUM 2, HIGH 3, AUTOSPEED 4
    private int settingTemperature;  //X10 假浮点 False float
    private int settingHumidity;  //X10 假浮点 False float
//    private String module;  //reboot, feedback, etc.//TODO：需要实现 feedback。
//    private String upgrade;//升级模块指令集。//TODO: 将来再做吧

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public void setRoomState(int roomState) {
        this.roomState = roomState;
    }

    public void setSettingFanSpeed(int settingFanSpeed) {
        this.settingFanSpeed = settingFanSpeed;
    }

    public void setSettingTemperature(int settingTemperature) {
        this.settingTemperature = settingTemperature;
    }

    public void setSettingHumidity(int settingHumidity) {
        this.settingHumidity = settingHumidity;
    }

}
