package com.jiuhua.jiuhuacontrol.repository;

import com.jiuhua.jiuhuacontrol.database.EngineSheet;
import com.jiuhua.jiuhuacontrol.database.FancoilSheet;
import com.jiuhua.jiuhuacontrol.database.PeriodSheet;
import com.jiuhua.jiuhuacontrol.database.SensorSheet;
import com.jiuhua.jiuhuacontrol.database.WatershedSheet;

public class TDReceptionConverter {

    public static SensorSheet[] toSensorSheet(TDReception tdReception) {
        if (tdReception.getStatus().equals("succ")) {
            int rows = Integer.valueOf(tdReception.getRows());
            SensorSheet[] sensorSheets = new SensorSheet[rows];

            for (int i = 0; i < rows; i++) {
                SensorSheet sensorSheet = new SensorSheet();
                for (int j = 0; j < tdReception.getColumn_meta().length; j++) {
                    switch (tdReception.getColumn_meta()[j][0]) {
                        case "ts":
                            sensorSheet.setTimeStamp(Long.parseLong(tdReception.getData()[i][j]) / 1000);
                            break;
                        case "roomid":
                            sensorSheet.setRoomId(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        case "devicetype":
                            sensorSheet.setDeviceType(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        case "deviceid":
                            sensorSheet.setDeviceId(tdReception.getData()[i][j]);
                            break;
                        case "currenttemperature":
                            sensorSheet.setCurrentTemperature(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        case "currenthumidity":
                            sensorSheet.setCurrentHumidity(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        case "adjustingtemperature":
                            sensorSheet.setAdjustingTemperature(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        case "adjustinghumidity":
                            sensorSheet.setAdjustingHumidity(Integer.valueOf(tdReception.getData()[i][j]));
                            break;

                        //TODO location 如何使用？现在是topic，如何利用数据库在topic上。

                        default:
                            break;
                    }
                }
                sensorSheets[i] = sensorSheet;

            }
            return sensorSheets;
        }
        return null;
    }

    public static FancoilSheet[] toFancoilSheet(TDReception tdReception) {
        if (tdReception.getStatus().equals("succ")) {
            int rows = Integer.valueOf(tdReception.getRows());
            FancoilSheet[] fancoilSheets = new FancoilSheet[rows];

            for (int i = 0; i < rows; i++) {
                FancoilSheet fancoilSheet = new FancoilSheet();
                for (int j = 0; j < tdReception.getColumn_meta().length; j++) {
                    switch (tdReception.getColumn_meta()[j][0]) {
                        case "ts":
                            fancoilSheet.setTimeStamp(Long.parseLong(tdReception.getData()[i][j]) / 1000);
                            break;
                        case "roomid":
                            fancoilSheet.setRoomId(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        case "devicetype":
                            fancoilSheet.setDeviceType(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        case "deviceid":
                            fancoilSheet.setDeviceId(tdReception.getData()[i][j]);
                            break;
                        case "currenttemperature":
                            fancoilSheet.setCurrentTemperature(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        case "currenthumidity":
                            fancoilSheet.setCurrentHumidity(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        case "settingtemperature":
                            fancoilSheet.setSettingTemperature(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        case "settinghumidity":
                            fancoilSheet.setSettingHumidity(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        case "settingfanspeed":
                            fancoilSheet.setSettingFanStatus(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        case "currentfanspeed":
                            fancoilSheet.setCurrentFanStatus(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        case "coilvalve":
                            fancoilSheet.setCoilValveOpen(tdReception.getData()[i][j] == "0" ? false : true);
//                            fancoilSheet.setCoilValveOpen(Integer.valueOf(tdReception.getData()[i][j]) == 0 ? false : true);
                            break;
                        case "roomstate":
                            fancoilSheet.setRoomStatus(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        //TODO location 如何使用？现在是topic，如何利用数据库在topic上。

                        default:
                            break;
                    }
                }
                fancoilSheets[i] = fancoilSheet;

            }
            return fancoilSheets;
        }
        return null;
    }

    public static WatershedSheet[] toWatershedSheet(TDReception tdReception) {
        if (tdReception.getStatus().equals("succ")) {
            int rows = Integer.valueOf(tdReception.getRows());
            WatershedSheet[] watershedSheets = new WatershedSheet[rows];

            for (int i = 0; i < rows; i++) {
                WatershedSheet watershedSheet = new WatershedSheet();
                for (int j = 0; j < tdReception.getColumn_meta().length; j++) {
                    switch (tdReception.getColumn_meta()[j][0]) {
                        case "ts":
                            watershedSheet.setTimeStamp(Long.parseLong(tdReception.getData()[i][j]) / 1000);
                            break;
                        case "roomid":
                            watershedSheet.setRoomId(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        case "devicetype":
                            watershedSheet.setDeviceType(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        case "deviceid":
                            watershedSheet.setDeviceId(tdReception.getData()[i][j]);
                            break;
                        case "currenttemperature":
                            watershedSheet.setCurrentTemperature(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        case "currenthumidity":
                            watershedSheet.setCurrentHumidity(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        case "settingtemperature":
                            watershedSheet.setSettingTemperature(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        case "settinghumidity":
                            watershedSheet.setSettingHumidity(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        case "coilvalve"://TODO  暂时云端没有，也是枚举
                            watershedSheet.setCoilValveOpen(tdReception.getData()[i][j] == "0" ? false : true);
                            break;
                        case "floorvalve":
                            watershedSheet.setFloorValveOpen(tdReception.getData()[i][j] == "0" ? false : true);
                            break;
                        case "roomState":
                            watershedSheet.setRoomStatus(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        //TODO location 如何使用？现在是topic，如何利用数据库在topic上。

                        default:
                            break;
                    }
                }
                watershedSheets[i] = watershedSheet;

            }
            return watershedSheets;
        }
        return null;
    }

    public static EngineSheet[] toEngineSheet(TDReception tdReception) {
        if (tdReception.getStatus().equals("succ")) {
            int rows = Integer.valueOf(tdReception.getRows());
            EngineSheet[] engineSheets = new EngineSheet[rows];

            for (int i = 0; i < rows; i++) {
                EngineSheet engineSheet = new EngineSheet();
                for (int j = 0; j < tdReception.getColumn_meta().length; j++) {
                    switch (tdReception.getColumn_meta()[j][0]) {
                        case "ts":
                            engineSheet.setTimeStamp(Long.parseLong(tdReception.getData()[i][j]) / 1000);
                            break;
                        case "roomid":
                            engineSheet.setRoomId(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        case "devicetype":
                            engineSheet.setDeviceType(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        case "deviceid":
                            engineSheet.setDeviceId(tdReception.getData()[i][j]);
                            break;
                        case "currenttemperature":
                            engineSheet.setCurrentTemperature(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        case "currenthumidity":
                            engineSheet.setCurrentHumidity(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        case "settingtemperature":
                            engineSheet.setSettingTemperature(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        case "settinghumidity":
                            engineSheet.setSettingHumidity(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        case "boilerstate"://枚举量
                            engineSheet.setIsengineRuning(tdReception.getData()[i][j] == "0" ? false : true);
                            break;
                        case "roomstate"://TODO  暂时云端没有，也是枚举
                            engineSheet.setRoomState(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        //TODO location 如何使用？现在是topic，如何利用数据库在topic上。

                        default:
                            break;
                    }
                }
                engineSheets[i] = engineSheet;

            }
            return engineSheets;
        }
        return null;
    }

    /** 周期的接收 */
//    public static PeriodSheet[] toPeriodSheet(TDReception tdReception) {//FIXME
//        if (tdReception.getStatus().equals("succ")) {
//            int rows = Integer.valueOf(tdReception.getRows());
//            PeriodSheet[] periodSheets = new PeriodSheet[rows];
//
//            for (int i = 0; i < rows; i++) {
//                PeriodSheet periodSheet = new PeriodSheet();
//                for (int j = 0; j < tdReception.getColumn_meta().length; j++) {
//                    switch (tdReception.getColumn_meta()[j][0]) {
//                        case "ts":
//                            periodSheet.setTimeStamp(Long.parseLong(tdReception.getData()[i][j]) / 1000);
//                            break;
//                        case "currenttemperature":
//                            periodSheet.setCurrentTemperature(Integer.valueOf(tdReception.getData()[i][j]));
//                            break;
//                        case "currenthumidity":
//                            periodSheet.setCurrentHumidity(Integer.valueOf(tdReception.getData()[i][j]));
//                            break;
//                        case "roomid":
//                            periodSheet.setRoomId(Integer.valueOf(tdReception.getData()[i][j]));
//                            break;
//                        case "devicetype":
//                            periodSheet.setDeviceType(Integer.valueOf(tdReception.getData()[i][j]));
//                            break;
////                        case "settingtemperature":
////                            periodSheet.setSettingTemperature(Integer.valueOf(tdReception.getData()[i][j]));
////                            break;
////                        case "settinghumidity":
////                            periodSheet.setSettingHumidity(Integer.valueOf(tdReception.getData()[i][j]));
////                            break;
//                        case "adjustingtemperature":
//                            periodSheet.setAdjustingTemperature(Integer.valueOf(tdReception.getData()[i][j]));
//                            break;
//                        case "adjustinghumidity":
//                            periodSheet.setAdjustingHumidity(Integer.valueOf(tdReception.getData()[i][j]));
//                            break;
//                        case "deviceid":
//                            periodSheet.setDeviceId(tdReception.getData()[i][j]);
//                            break;
////                        case "coilvalve"://TODO 枚举量，注意如何转换的，传来的字符串是什么？？
////                            engineSheet.setCoilValveOpen(Integer.valueOf(tdReception.getData()[i][j]));
////                            break;
////                        case "floorvalve"://TODO  暂时云端没有，也是枚举
////                            engineSheet.setFloorValveOpen(Integer.valueOf(tdReception.getData()[i][j]));
////                            break;
//                        //TODO 增加 boiler state， deviceID（使用原生REFUS？？的 MAC地址？？）？？
//                        //TODO location 如何使用？现在是topic，如何利用数据库在topic上。
//
//                        default:
//                            break;
//                    }
//                }
//                periodSheets[i] = periodSheet;
//
//            }
//            return periodSheets;
//        }
//        return null;
//    }

    //TODO: basicinfo?? longtimesheet??


}
