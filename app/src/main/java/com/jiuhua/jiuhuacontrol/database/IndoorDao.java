package com.jiuhua.jiuhuacontrol.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface IndoorDao {
    //BasicInfo的相关方法
    @Insert
    void insertBasicInfoDB(BasicInfoDB... basicInfoDBS);

    @Update
    void updateBasicInfoDB(BasicInfoDB... basicInfoDBS);

    @Delete
    void deleteBasicInfoDB(BasicInfoDB... basicInfoDBS);

    @Query("DELETE FROM BasicInfoDB")
    void deleteAllBasicInfo();

    //和下面一条方法不能同时存在，会死机，不知为何！！
//    @Query("SELECT * FROM BasicInfoDB")
//    List<BasicInfoDB> loadAllBasicInfo();

    @Query("SELECT * FROM BasicInfoDB")
    LiveData<List<BasicInfoDB>> loadAllBasicInfoLive();

    @Query("SELECT roomname FROM BasicInfoDB WHERE id = :ID")
    String loadRoomName(int ID);

//IndoorDB的相关方法

    @Insert
    void insertIndoorDB(IndoorDB... indoorDBS);
//    @Insert
//    void insertIndoorDBList(IndoorDB indoorDB, List<IndoorDB> indoorDBList);

    @Query("DELETE FROM IndoorDB")
    void deleteAllIndoorDB();

//    //提取最大id的条目
//    @Query("SELECT * , MAX(id)  FROM IndoorDB WHERE room_name_id = :roomId")
//    LiveData<IndoorDB> getCurrentIndoorDB(int roomId);

     //提取最大id的条目
     @Query("SELECT * , MAX(timeStamp)  FROM IndoorDB GROUP BY room_id")
     LiveData<List<IndoorDB>> loadLatestIndoorDBsLive();

     //IndoorLongtimeDB的相关方法
    @Insert
    void insertRoomLongTimeDB(IndoorLongTimeDB... indoorLongTimeDBS);

    //TODO 提取一周数据

    //TODO 提取一年数据

    //TODO　提取两年数据

    //TODO  提取三年数据


}
