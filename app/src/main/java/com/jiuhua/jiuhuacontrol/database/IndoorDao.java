package com.jiuhua.jiuhuacontrol.database;

import android.icu.text.Replaceable;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Update;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

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

    @Query( "DELETE FROM BasicInfoDB" )
    void deleteAllBasicInfo();

    @Query( "SELECT * FROM BasicInfoDB" )
    LiveData<List<BasicInfoDB>> loadAllBasicInfoLive();   //only can use LiveData<> !

    @Query( "SELECT roomname FROM BasicInfoDB WHERE roomId = :roomid" )
    String loadRoomName(int roomid);

    /**
     * IndoorDB的相关方法
     */
    @Insert
    void insertIndoorDB(IndoorDB... indoorDBS);

    @Query( "DELETE FROM IndoorDB" )
    void deleteAllIndoorDB();

    //    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)  //TODO  每次编译都出错，能正常运行，太烦了，才加上的。
    @Query( "SELECT * , MAX(timeStamp)  FROM IndoorDB WHERE device_type = :devicetypeId GROUP BY room_id" )
    LiveData<List<IndoorDB>> loadLatestIndoorDBsLive(int devicetypeId);   //only can use LiveData<> !   这里添加了Max（timestamp）一列，但是IndoorDB里面没有。

    /**
     * PeroidDB的相关方法
     */
    @Insert( onConflict = REPLACE )
    void insertPeriodDB(PeriodDB... periodDBS);

    @Delete
    void deletePeriodDB(PeriodDB... periodDBS);

    @SuppressWarnings( RoomWarnings.CURSOR_MISMATCH )  //TODO  每次编译都出错，能正常运行，太烦了，才加上的。
    @Query( "SELECT * , MAX(timestamp) FROM PeriodDB GROUP BY room_id" )
    LiveData<List<PeriodDB>> loadLatestPeriodDBsLive();  //only can use LiveData<> ! 加了转换器居然编译通过了。//FIXME ??其实多出来一项，entity里面没有的。

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
