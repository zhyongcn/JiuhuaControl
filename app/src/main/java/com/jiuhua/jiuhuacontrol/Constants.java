package com.jiuhua.jiuhuacontrol;

//通过接口使用常量，比象C语言那样define定义，比枚举更好，少占用空间。速度快。
public interface Constants {
    int roomState_OFF = 0;
    int roomState_MANUAL = 1;
    int roomState_AUTO = 2;
    int roomState_DEHUMIDITY = 3;
    int roomState_FEAST = 4;
    int roomState_SLEEP = 5;
    int roomState_OUTSIDE =6;

    int fanSpeed_STOP = 0;
    int fanSpeed_LOW = 1;
    int fanSpeed_MEDIUM = 2;
    int fanSpeed_HIGH = 3;
    int fanSpeed_AUTO = 4;

    int deviceType_fancoil = 0;
    int deviceType_floorwatershed = 1;
    int deviceType_radiator = 2;
    int deviceType_boiler = 3;
    int deviceType_heatpump = 4;
    int deviceType_DHTsensor = 5;
    int deviceType_NTCsensor = 6;
    int deviceType_phone = 7;

    int Monday = 0;
    int Tuesday = 1;
    int Wednesday = 2;
    int Thursday = 3;
    int Friday = 4;
    int Saturday = 5;
    int Sunday = 6;

    //字符串常量 接受的topic 发送的Topic等等 ^=^
//    String MQTT_publish_topic_prefix = "86518/YXHY/12-1-101/Room";

}
