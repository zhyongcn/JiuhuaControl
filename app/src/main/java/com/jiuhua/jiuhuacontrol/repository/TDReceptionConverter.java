package com.jiuhua.jiuhuacontrol.repository;

import com.jiuhua.jiuhuacontrol.database.DayPeriod;
import com.jiuhua.jiuhuacontrol.database.EngineSheet;
import com.jiuhua.jiuhuacontrol.database.FancoilSheet;
import com.jiuhua.jiuhuacontrol.database.PeriodSheet;
import com.jiuhua.jiuhuacontrol.database.SensorSheet;
import com.jiuhua.jiuhuacontrol.database.WatershedSheet;

import java.util.ArrayList;
import java.util.List;

public class TDReceptionConverter {

    public static SensorSheet[] toSensorSheet(TDReception tdReception) {
        if (tdReception.getStatus().equals("succ")) {
            int rows = tdReception.getRows();
            SensorSheet[] sensorSheets = new SensorSheet[rows];

            for (int i = 0; i < rows; i++) {
                SensorSheet sensorSheet = new SensorSheet();
                for (int j = 0; j < tdReception.getColumn_meta().length; j++) {
                    switch (tdReception.getColumn_meta()[j][0]) {
                        case "ts":
                            sensorSheet.setTimeStamp(Long.parseLong(tdReception.getData()[i][j]) / 1000);
                            break;
                        case "roomid":
                            sensorSheet.setRoomId(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "devicetype":
                            sensorSheet.setDeviceType(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "deviceid":
                            sensorSheet.setDeviceId(tdReception.getData()[i][j]);
                            break;
                        case "currenttemperature":
                            sensorSheet.setCurrentTemperature(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "currenthumidity":
                            sensorSheet.setCurrentHumidity(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "adjustingtemperature":
                            sensorSheet.setAdjustingTemperature(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "adjustinghumidity":
                            sensorSheet.setAdjustingHumidity(Integer.parseInt(tdReception.getData()[i][j]));
                            break;

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
            int rows = tdReception.getRows();
            FancoilSheet[] fancoilSheets = new FancoilSheet[rows];

            for (int i = 0; i < rows; i++) {
                FancoilSheet fancoilSheet = new FancoilSheet();
                for (int j = 0; j < tdReception.getColumn_meta().length; j++) {
                    switch (tdReception.getColumn_meta()[j][0]) {
                        case "ts":
                            fancoilSheet.setTimeStamp(Long.parseLong(tdReception.getData()[i][j]) / 1000);
                            break;
                        case "roomid":
                            fancoilSheet.setRoomId(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "devicetype":
                            fancoilSheet.setDeviceType(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "deviceid":
                            fancoilSheet.setDeviceId(tdReception.getData()[i][j]);
                            break;
                        case "currenttemperature":
                            fancoilSheet.setCurrentTemperature(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "currenthumidity":
                            fancoilSheet.setCurrentHumidity(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "settingtemperature":
                            fancoilSheet.setSettingTemperature(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "settinghumidity":
                            fancoilSheet.setSettingHumidity(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "settingfanspeed":
                            fancoilSheet.setSettingFanStatus(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "currentfanspeed":
                            fancoilSheet.setCurrentFanStatus(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "coilvalve":
                            fancoilSheet.setCoilValveOpen(Integer.parseInt(tdReception.getData()[i][j]) != 0);
                            break;
                        case "roomstate":
                            fancoilSheet.setRoomStatus(Integer.parseInt(tdReception.getData()[i][j]));
                            break;

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
            int rows = tdReception.getRows();
            WatershedSheet[] watershedSheets = new WatershedSheet[rows];

            for (int i = 0; i < rows; i++) {
                WatershedSheet watershedSheet = new WatershedSheet();
                for (int j = 0; j < tdReception.getColumn_meta().length; j++) {
                    switch (tdReception.getColumn_meta()[j][0]) {
                        case "ts":
                            watershedSheet.setTimeStamp(Long.parseLong(tdReception.getData()[i][j]) / 1000);
                            break;
                        case "roomid":
                            watershedSheet.setRoomId(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "devicetype":
                            watershedSheet.setDeviceType(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "deviceid":
                            watershedSheet.setDeviceId(tdReception.getData()[i][j]);
                            break;
                        case "currenttemperature":
                            watershedSheet.setCurrentTemperature(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "currenthumidity":
                            watershedSheet.setCurrentHumidity(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "settingtemperature":
                            watershedSheet.setSettingTemperature(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "settinghumidity":
                            watershedSheet.setSettingHumidity(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        //*** 传来0代表阀关，不懂字符非串的比较，转化为Integer比较，有点绕 ***
                        case "coilvalve"://TODO  暂时云端没有，也是枚举
                            watershedSheet.setCoilValveOpen(Integer.parseInt(tdReception.getData()[i][j]) != 0);
                            break;
                        //*** 传来0代表阀关，不懂字符非串的比较，转化为Integer比较，有点绕 ***
                        case "floorvalve":
                            watershedSheet.setFloorValveOpen(Integer.parseInt(tdReception.getData()[i][j]) != 0);
                            break;
                        case "roomstate":
                            watershedSheet.setRoomStatus(Integer.parseInt(tdReception.getData()[i][j]));
                            break;

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
            int rows = tdReception.getRows();
            EngineSheet[] engineSheets = new EngineSheet[rows];

            for (int i = 0; i < rows; i++) {
                EngineSheet engineSheet = new EngineSheet();
                for (int j = 0; j < tdReception.getColumn_meta().length; j++) {
                    switch (tdReception.getColumn_meta()[j][0]) {
                        case "ts":
                            engineSheet.setTimeStamp(Long.parseLong(tdReception.getData()[i][j]) / 1000);
                            break;
                        case "roomid":
                            engineSheet.setRoomId(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "devicetype":
                            engineSheet.setDeviceType(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "deviceid":
                            engineSheet.setDeviceId(tdReception.getData()[i][j]);
                            break;
                        case "currenttemperature":
                            engineSheet.setCurrentTemperature(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "currenthumidity":
                            engineSheet.setCurrentHumidity(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "settingtemperature":
                            engineSheet.setSettingTemperature(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "settinghumidity":
                            engineSheet.setSettingHumidity(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "boilerstate"://枚举量
                            engineSheet.setIsengineRuning(Integer.parseInt(tdReception.getData()[i][j]) != 0);
                            break;
                        case "roomstate"://TODO  暂时云端没有，也是枚举
                            engineSheet.setRoomState(Integer.parseInt(tdReception.getData()[i][j]));
                            break;

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


    /**
     * 周期的接收 TODO：暂时不搞
     */
    public static PeriodSheet[] toPeriodSheet(TDReception tdReception) {
        if (tdReception.getStatus().equals("succ")) {
            int rows = tdReception.getRows();
            PeriodSheet[] periodSheets = new PeriodSheet[rows];

            for (int i = 0; i < rows; i++) {
                PeriodSheet periodSheet = new PeriodSheet();
                List<DayPeriod> oneRoomWeeklyPeriod = new ArrayList<>();//好像是乱序，反正全部使用
                for (int l = 0; l < 15; l++) {
                    oneRoomWeeklyPeriod.add(new DayPeriod());
                }
                for (int j = 0; j < tdReception.getColumn_meta().length; j++) {
                    switch (tdReception.getColumn_meta()[j][0]) {
                        case "ts":
                            periodSheet.setTimeStamp(Long.parseLong(tdReception.getData()[i][j]) / 1000);
                            break;
                        case "roomid":
                            periodSheet.setRoomId(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        //case "devicetype":
                        //    periodSheet.setDeviceType(Integer.parseInt(tdReception.getData()[i][j]));
                        //    break;
                        //case "deviceid":
                        //    periodSheet.setDeviceId(tdReception.getData()[i][j]);
                        //    break;
                        case "period01_s":
                            oneRoomWeeklyPeriod.get(0).setStartMinutes(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period01_e":
                            oneRoomWeeklyPeriod.get(0).setEndMinutes(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period01_t":
                            oneRoomWeeklyPeriod.get(0).setTempreature(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period02_s":
                            oneRoomWeeklyPeriod.get(1).setStartMinutes(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period02_e":
                            oneRoomWeeklyPeriod.get(1).setEndMinutes(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period02_t":
                            oneRoomWeeklyPeriod.get(1).setTempreature(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period03_s":
                            oneRoomWeeklyPeriod.get(2).setStartMinutes(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period03_e":
                            oneRoomWeeklyPeriod.get(2).setEndMinutes(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period03_t":
                            oneRoomWeeklyPeriod.get(2).setTempreature(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period04_s":
                            oneRoomWeeklyPeriod.get(3).setStartMinutes(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period04_e":
                            oneRoomWeeklyPeriod.get(3).setEndMinutes(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period04_t":
                            oneRoomWeeklyPeriod.get(3).setTempreature(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period05_s":
                            oneRoomWeeklyPeriod.get(4).setStartMinutes(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period05_e":
                            oneRoomWeeklyPeriod.get(4).setEndMinutes(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period05_t":
                            oneRoomWeeklyPeriod.get(4).setTempreature(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period06_s":
                            oneRoomWeeklyPeriod.get(5).setStartMinutes(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period06_e":
                            oneRoomWeeklyPeriod.get(5).setEndMinutes(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period06_t":
                            oneRoomWeeklyPeriod.get(5).setTempreature(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period07_s":
                            oneRoomWeeklyPeriod.get(6).setStartMinutes(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period07_e":
                            oneRoomWeeklyPeriod.get(6).setEndMinutes(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period07_t":
                            oneRoomWeeklyPeriod.get(6).setTempreature(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period08_s":
                            oneRoomWeeklyPeriod.get(7).setStartMinutes(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period08_e":
                            oneRoomWeeklyPeriod.get(7).setEndMinutes(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period08_t":
                            oneRoomWeeklyPeriod.get(7).setTempreature(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period09_s":
                            oneRoomWeeklyPeriod.get(8).setStartMinutes(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period09_e":
                            oneRoomWeeklyPeriod.get(8).setEndMinutes(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period09_t":
                            oneRoomWeeklyPeriod.get(8).setTempreature(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period10_s":
                            oneRoomWeeklyPeriod.get(9).setStartMinutes(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period10_e":
                            oneRoomWeeklyPeriod.get(9).setEndMinutes(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period10_t":
                            oneRoomWeeklyPeriod.get(9).setTempreature(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period11_s":
                            oneRoomWeeklyPeriod.get(10).setStartMinutes(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period11_e":
                            oneRoomWeeklyPeriod.get(10).setEndMinutes(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period11_t":
                            oneRoomWeeklyPeriod.get(10).setTempreature(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period12_s":
                            oneRoomWeeklyPeriod.get(11).setStartMinutes(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period12_e":
                            oneRoomWeeklyPeriod.get(11).setEndMinutes(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period12_t":
                            oneRoomWeeklyPeriod.get(11).setTempreature(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period13_s":
                            oneRoomWeeklyPeriod.get(12).setStartMinutes(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period13_e":
                            oneRoomWeeklyPeriod.get(12).setEndMinutes(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period13_t":
                            oneRoomWeeklyPeriod.get(12).setTempreature(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period14_s":
                            oneRoomWeeklyPeriod.get(13).setStartMinutes(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period14_e":
                            oneRoomWeeklyPeriod.get(13).setEndMinutes(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period14_t":
                            oneRoomWeeklyPeriod.get(13).setTempreature(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period15_s":
                            oneRoomWeeklyPeriod.get(14).setStartMinutes(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period15_e":
                            oneRoomWeeklyPeriod.get(14).setEndMinutes(Integer.parseInt(tdReception.getData()[i][j]));
                            break;
                        case "period15_t":
                            oneRoomWeeklyPeriod.get(14).setTempreature(Integer.parseInt(tdReception.getData()[i][j]));
                            break;

                        default:
                            break;
                    }
                }
                periodSheet.setOneRoomWeeklyPeriod(oneRoomWeeklyPeriod);
                periodSheets[i] = periodSheet;

            }
            return periodSheets;
        }
        return null;
    }

    //TODO: basicinfo?? longtimesheet??


}
