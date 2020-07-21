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
    /**
     * BasicInfo的相关方法
     */
    @Insert
    void insertBasicInfoDB(BasicInfoDB... basicInfoDBS);

    @Update
    void updateBasicInfoDB(BasicInfoDB... basicInfoDBS);

    @Delete
    void deleteBasicInfoDB(BasicInfoDB... basicInfoDBS);

    @Query("DELETE FROM BasicInfoDB")
    void deleteAllBasicInfo();

    @Query("SELECT * FROM BasicInfoDB")
    LiveData<List<BasicInfoDB>> loadAllBasicInfoLive();   //only can use LiveData<> !

    @Query("SELECT roomname FROM BasicInfoDB WHERE id = :ID")
    String loadRoomName(int ID);

    /**
     * IndoorDB的相关方法
     */
    @Insert
    void insertIndoorDB(IndoorDB... indoorDBS);

    @Query("DELETE FROM IndoorDB")
    void deleteAllIndoorDB();

    @Query("SELECT * , MAX(timeStamp)  FROM IndoorDB GROUP BY room_id")
    LiveData<List<IndoorDB>> loadLatestIndoorDBsLive();   //only can use LiveData<> !

    /**
     * PeroidDB的相关方法
     */
    @Insert
    void insertPeroidDB(PeriodDB... periodDBS);

    @Delete
    void deletePeroidDB(PeriodDB... periodDBS);

    @Query("SELECT * , MAX(timestamp) FROM PeriodDB GROUP BY room_id")
    LiveData<List<PeriodDB>> loadLatestPeriodDBsLive();  //only can use LiveData<> ! 加了转换器居然编译通过了。

    /**
     * IndoorLongtimeDB的相关方法
     */
    @Insert
    void insertRoomLongTimeDB(IndoorLongTimeDB... indoorLongTimeDBS);

    //TODO 提取一周数据

    //TODO 提取一年数据

    //TODO　提取两年数据

    //TODO  提取三年数据


}
