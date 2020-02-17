package com.jiuhua.jiuhuacontrol;

import androidx.room.Dao;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RoomDao {

    @Insert
    void insertRoomDB(RoomDB... roomDBS);

    @Insert
    void insertRoomNameDB(RoomNameDB... roomNameDBS);

    @Insert
    void insertRoomLongTimeDB(RoomLongTimeDB...roomLongTimeDBS);

    @Query("SELECT * FROM roomnamedb")
    public List<RoomNameDB> loadAllRoomName();

    //提取最大id的条目
    @Query("SELECT * FROM RoomDB ORDER BY ID DESC LIMIT 8")
    RoomDB getCurrentRoomMessage();

    //TODO 提取一周数据

    //TODO 提取一年数据

    //TODO　提取两年数据

    //TODO  提取三年数据



}
