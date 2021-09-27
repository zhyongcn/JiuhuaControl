package com.jiuhua.jiuhuacontrol.database;

public class DayPeriod {

    String dayPeriodName = "     ";
    int startMinuteStamp;  //the minutes after 0:00
    int endMinuteStamp;
    int tempreature; //default 240 ℃。
    int weekday;//周日0开始


    public DayPeriod() {
    }

    public int getStartMinuteStamp() {
        return startMinuteStamp;
    }

    public void setStartMinuteStamp(int startMinuteStamp) {
        this.startMinuteStamp = startMinuteStamp;
    }

    public int getEndMinuteStamp() {
        return endMinuteStamp;
    }

    public void setEndMinuteStamp(int endMinuteStamp) {
        this.endMinuteStamp = endMinuteStamp;
    }

    public int getTempreature() {
        return tempreature;
    }

    public void setTempreature(int tempreature) {
        this.tempreature = tempreature;
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    public String getDayPeriodName() {
        return dayPeriodName;
    }

    public void setDayPeriodName(String dayPeriodName) {
        this.dayPeriodName = dayPeriodName;
    }
}
