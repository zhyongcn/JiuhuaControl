package com.jiuhua.jiuhuacontrol;

public class RoomLittleMessage {
    private String roomName;
    private int RoomId;
    private String currentTemperation;
    private String currentHumidity;
    private String runningStatus;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getRoomId() {
        return RoomId;
    }

    public void setRoomId(int roomId) {
        RoomId = roomId;
    }

    public String getCurrentTemperation() {
        return currentTemperation;
    }

    public void setCurrentTemperation(String currentTemperation) {
        this.currentTemperation = currentTemperation;
    }

    public String getCurrentHumidity() {
        return currentHumidity;
    }

    public void setCurrentHumidity(String currentHumidity) {
        this.currentHumidity = currentHumidity;
    }

    public String getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(String runningStatus) {
        this.runningStatus = runningStatus;
    }
}
