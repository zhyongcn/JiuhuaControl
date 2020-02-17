package com.jiuhua.jiuhuacontrol;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RoomNameDB {

    @PrimaryKey(autoGenerate = true)
    private int Id;
    @ColumnInfo(name = "roomName")
    private String roomName;

    public RoomNameDB() {
    }

    public RoomNameDB(int id, String roomName) {
        this.Id = id;
        this.roomName = roomName;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

}
