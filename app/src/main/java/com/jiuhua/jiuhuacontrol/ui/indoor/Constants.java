package com.jiuhua.jiuhuacontrol.ui.indoor;

//todo: 如果使用 static final const define int roomStateOFF = 0 就像C语言的define 常量是不是更好，少占用空间。速度快。
public interface Constants {
    int roomState_OFF = 0;
    int roomState_MANUAL = 1;
    int roomState_AUTO = 2;
    int roomState_DEHUMIDITY = 3;
    int roomState_FEAST = 4;

    int fanSpeed_STOP = 0;
    int fanSpeed_LOW = 1;
    int fanSpeed_MEDIUM = 2;
    int fanSpeed_HIGH = 3;
    int fanSpeed_AUTO = 4;

    int deviceType_fancoil = 0;
    int deviceType_floorheater = 1;
    int deviceType_radiator = 2;
    int deviceType_boiler = 3;
    int deviceType_heatpump = 4;
    int deviceType_DHTsensor = 5;

    int Monday = 0;
    int Tuesday = 1;
    int Wednesday = 2;
    int Thursday = 3;
    int Friday = 4;
    int Saturday = 5;
    int Sunday = 6;

}
