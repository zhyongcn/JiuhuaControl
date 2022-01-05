package com.jiuhua.jiuhuacontrol;

public class CommandPeriod {
    private int roomId;// 1, 2, 3, 4, 5, 6, 7, 8, etc
    private int deviceType = Constants.deviceType_sendperiod;
    private int weekday;
    private int[][] period = new int[15][3];

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    public int[][] getPeriod() {
        return period;
    }

    public void setPeriod(int[][] period) {
        this.period = period;
    }
}
