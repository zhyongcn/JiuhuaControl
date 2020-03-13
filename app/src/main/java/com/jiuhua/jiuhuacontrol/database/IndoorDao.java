package com.jiuhua.jiuhuacontrol.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface IndoorDao {

    @Insert
    void insertRoomDB(IndoorDB... indoorDBS);

    @Insert
    void insertRoomNameDB(BasicInfoDB... basicInfoDBS);

    @Insert
    void insertRoomLongTimeDB(IndoorLongTimeDB... indoorLongTimeDBS);

    @Query("DELETE FROM BasicInfoDB")
    void deleteAllRoomsName();

    @Query("SELECT * FROM BasicInfoDB")
    public LiveData<List<BasicInfoDB>> loadAllRoomName();

    //提取最大id的条目
    @Query("SELECT *  FROM IndoorDB ")
    IndoorDB getCurrentRoomMessage();

    //TODO 提取一周数据

    //TODO 提取一年数据

    //TODO　提取两年数据

    //TODO  提取三年数据



}
