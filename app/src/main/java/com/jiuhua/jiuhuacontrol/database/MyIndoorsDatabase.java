package com.jiuhua.jiuhuacontrol.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {IndoorDB.class, BasicInfoDB.class, IndoorLongTimeDB.class, EngineDB.class,
        EngineLongTimeDB.class}, version = 1, exportSchema = false)
public abstract class MyIndoorsDatabase extends RoomDatabase {
    private static MyIndoorsDatabase INSTANCE;

    public static synchronized MyIndoorsDatabase getDatabase(Context context) {
        if(INSTANCE == null) {
            INSTANCE = androidx.room.Room.databaseBuilder(context.getApplicationContext(),
                    MyIndoorsDatabase.class, "rooms_database").build();
        }
        return INSTANCE;
    }

    public abstract IndoorDao getRoomDao(); //这个是抽象方法
}
