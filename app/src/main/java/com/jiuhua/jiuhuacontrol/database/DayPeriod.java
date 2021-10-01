package com.jiuhua.jiuhuacontrol.database;

public class DayPeriod {

    String dayPeriodName = "     ";
    int startMinutes;  //the minutes after 0:00
    int endMinutes;
    int tempreature; //default 240 ℃。
    int weekday;//周日0开始


    public DayPeriod() {
    }

    public int getStartMinutes() {
        return startMinutes;
    }

    public void setStartMinutes(int startMinutes) {
        this.startMinutes = startMinutes;
    }

    public int getEndMinutes() {
        return endMinutes;
    }

    public void setEndMinutes(int endMinutes) {
        this.endMinutes = endMinutes;
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
