package com.jiuhua.jiuhuacontrol.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface IndoorDao {

    @Insert
    void insertRoomDB(IndoorDB... indoorDBS);

    @Insert
    void insertBasicInfoDB(BasicInfoDB... basicInfoDBS);

    @Update
    void updateBasicInfoDB(BasicInfoDB... basicInfoDBS);

    @Delete
    void deleteBasicInfoDB(BasicInfoDB... basicInfoDBS);

    @Insert
    void insertRoomLongTimeDB(IndoorLongTimeDB... indoorLongTimeDBS);

    @Query("DELETE FROM BasicInfoDB")
    void deleteAllBasicInfo();

    //和下面一条方法不能同时存在，会死机，不知为何！！
//    @Query("SELECT * FROM BasicInfoDB")
//    List<BasicInfoDB> loadAllBasicInfo();

    @Query("SELECT * FROM BasicInfoDB")
    LiveData<List<BasicInfoDB>> loadAllBasicInfoLive();

    //提取最大id的条目
    @Query("SELECT *  FROM IndoorDB ")
    IndoorDB getCurrentRoomMessage();

    //TODO 提取一周数据

    //TODO 提取一年数据

    //TODO　提取两年数据

    //TODO  提取三年数据



}
