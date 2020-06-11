package com.jiuhua.jiuhuacontrol.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {IndoorDB.class, BasicInfoDB.class, IndoorLongTimeDB.class, EngineDB.class,
        EngineLongTimeDB.class}, version = 1, exportSchema = false)
public abstract class MyIndoorsDatabase extends RoomDatabase {//@Database注释的抽象类，在注释中添加了数据库关联的实体列表。
    private static MyIndoorsDatabase INSTANCE;

    public static synchronized MyIndoorsDatabase getDatabase(Context context) {
        if(INSTANCE == null) {
            //Room.databaseBuilder() 或者 Room.inMemoryDatabaseBuilder() 获取数据库的实例。
            INSTANCE = androidx.room.Room.databaseBuilder(context.getApplicationContext(),
                    MyIndoorsDatabase.class, "rooms_database").build();
        }
        return INSTANCE;
    }

    public abstract IndoorDao getRoomDao(); //这个是抽象方法，返回@Dao注释的抽象方法
}
