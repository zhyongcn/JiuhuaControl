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
    int deviceType_sendperiod = 8;
    int deviceType_mqttconfig = 9;
    int deviceType_toFancoils = 10;

    int Sunday = 0;
    int Monday = 1;
    int Tuesday = 2;
    int Wednesday = 3;
    int Thursday = 4;
    int Friday = 5;
    int Saturday = 6;

    String mqtt_topic_prefix = "86518/yuxiuhuayuan/12-1-101/room";
    //String mqtt_topic_prefix = "86518/xiangyifuyuan/5-1-502/room";
    String upgradeinfo_url = "http://81.68.136.113:8080/upgrade/versioninfo";

    String UPGRADE_API_DOMAIN_DEBUG = "http://rapapi.org/mockjs/21104";
    String UPGRADE_API_DOMAIN_RELEASE = "http://81.68.136.113:8080/upgrade/android.apk";

    int UPGRADE_DELAY_TIME = 1000; //在首页延迟1s启动检测升级

}
