package com.jiuhua.jiuhuacontrol.database;

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
public interface MyDao {
    /**
     * BasicInfoSheet的相关方法
     * 配合索引使用 onConflict = OnConflictStrategy.IGNORE，保证不重复插入。
     */
    @Insert( onConflict = OnConflictStrategy.IGNORE )
    void insertBasicInfoSheet(BasicInfoSheet... basicInfoSheets);

    @Update
    void updateBasicInfoSheet(BasicInfoSheet... basicInfoSheets);

    @Delete
    void deleteBasicInfoSheet(BasicInfoSheet... basicInfoSheets);

    @Query( "DELETE FROM BasicInfoSheet" )
    void deleteAllBasicInfo();

    @Query( "SELECT * FROM BasicInfoSheet" )
    LiveData<List<BasicInfoSheet>> loadAllBasicInfoLive();   //only can use LiveData<> !

    @Query( "SELECT roomname FROM BasicInfoSheet WHERE roomId = :roomid" )
    String loadRoomName(int roomid);

    /**
     * SensorSheet的相关方法
     * 配合索引使用 onConflict = OnConflictStrategy.IGNORE，保证不重复插入。
     */
    @Insert( onConflict = OnConflictStrategy.IGNORE )
    void insertSensorSheet(SensorSheet... sensorSheets); // ... 任意个该类型的参数，可以数组？？

    @Query( "DELETE FROM SensorSheet" )
    void deleteAllSensorSheet();

    @SuppressWarnings( RoomWarnings.CURSOR_MISMATCH )  //TODO  每次编译都出错，能正常运行，太烦了，才加上的。
            //@Query( "SELECT * , MAX(timeStamp)  FROM SensorSheet WHERE device_type = :devicetypeId GROUP BY room_id" )
            //only can use LiveData<> !   这里添加了Max（timestamp）一列，但是IndoorDB里面没有。
            //LiveData<List<SensorSheet>> loadLatestSensorSheetsLive(int devicetypeId);
    //**** Try 不使用deviceID ****
    @Query( "SELECT * , MAX(timeStamp)  FROM SensorSheet  GROUP BY room_id" )
    LiveData<List<SensorSheet>> loadLatestSensorSheetsLive();

    /**
     * FancoilSheet的相关方法
     */
    @Insert( onConflict = OnConflictStrategy.IGNORE )
    void insertFancoilSheet(FancoilSheet... fancoilSheets); // ... 任意个该类型的参数，可以数组？？

    @Query( "DELETE FROM FancoilSheet" )
    void deleteAllFancoilSheet();

    @SuppressWarnings( RoomWarnings.CURSOR_MISMATCH )  //TODO  每次编译都出错，能正常运行，太烦了，才加上的。
    @Query( "SELECT * , MAX(timeStamp)  FROM FancoilSheet GROUP BY room_id" )
        //only can use LiveData<> !   这里添加了Max（timestamp）一列，但是IndoorDB里面没有。
    LiveData<List<FancoilSheet>> loadLatestFancoilSheetsLive();

    /**
     * WatershedSheet的相关方法
     */
    @Insert( onConflict = OnConflictStrategy.IGNORE )
    void insertWatershedSheet(WatershedSheet... watershedSheets); // ... 任意个该类型的参数，可以数组？？

    @Query( "DELETE FROM WatershedSheet" )
    void deleteAllWatershedSheet();

    @SuppressWarnings( RoomWarnings.CURSOR_MISMATCH )  //TODO  每次编译都出错，能正常运行，太烦了，才加上的。
    @Query( "SELECT * , MAX(timeStamp)  FROM WatershedSheet  GROUP BY room_id" )
        //only can use LiveData<> !   这里添加了Max（timestamp）一列，但是IndoorDB里面没有。
    LiveData<List<WatershedSheet>> loadLatestWatershedSheetsLive();

    /**
     * EngineSheet的相关方法
     */
    @Insert( onConflict = OnConflictStrategy.IGNORE )
    void insertEngineSheet(EngineSheet... engineSheets); // ... 任意个该类型的参数，可以数组？？

    @Query( "DELETE FROM EngineSheet" )
    void deleteAllEngineSheet();

    @SuppressWarnings( RoomWarnings.CURSOR_MISMATCH )  //TODO  每次编译都出错，能正常运行，太烦了，才加上的。
    @Query( "SELECT * , MAX(timeStamp)  FROM EngineSheet WHERE device_type = :devicetypeId GROUP BY room_id" )
        //only can use LiveData<> !   这里添加了Max（timestamp）一列，但是IndoorDB里面没有。
    LiveData<List<EngineSheet>> loadLatestEngineSheetsLive(int devicetypeId);

//    /**TODO：其他表的操作方法需要使用。
//     * IndoorSheet的相关方法
//     */
//    @Insert
//    void insertSensorSheet(SensorSheet... sensorSheets); // ... 任意个该类型的参数，可以数组？？
//
//    @Query( "DELETE FROM SensorSheet" )
//    void deleteAllSensorSheet();
//
//    @SuppressWarnings( RoomWarnings.CURSOR_MISMATCH )  //TODO  每次编译都出错，能正常运行，太烦了，才加上的。
//    @Query( "SELECT * , MAX(timeStamp)  FROM SensorSheet WHERE device_type = :devicetypeId GROUP BY room_id" )
//    LiveData<List<SensorSheet>> loadLatestSensorSheetsLive(int devicetypeId);   //only can use LiveData<> !   这里添加了Max（timestamp）一列，但是IndoorDB里面没有。

    /**
     * PeroidSheet的相关方法
     * TODO: 没有索引，不知道为什么重复？？ 使用上可以。
     */
    @Insert( onConflict = OnConflictStrategy.REPLACE )
    void insertPeriodSheet(PeriodSheet... periodSheets);

    @Delete
    void deletePeriodSheet(PeriodSheet... periodSheets);

    //每次编译都出错，能正常运行，太烦了，才加上的。 //其实多出来一项，entity里面没有的。
    @SuppressWarnings( RoomWarnings.CURSOR_MISMATCH )
    @Query( "SELECT * , MAX(timestamp) FROM PeriodSheet GROUP BY room_id" )
    //only can use LiveData<> ! 加了转换器居然编译通过了。
    LiveData<List<PeriodSheet>> loadLatestPeriodSheetsLive();

    /**
     * IndoorLongtimeSheet的相关方法
     */
    @Insert( onConflict = OnConflictStrategy.IGNORE )
    void insertSensorLongTimeSheet(SensorLongTimeSheet... sensorLongTimeSheets);

    //TODO 提取一周数据

    //TODO 提取一年数据

    //TODO　提取两年数据

    //TODO  提取三年数据


}
