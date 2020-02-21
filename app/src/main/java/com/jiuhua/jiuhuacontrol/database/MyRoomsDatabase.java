package com.jiuhua.jiuhuacontrol.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {RoomDB.class, RoomNameDB.class, RoomLongTimeDB.class}, version = 1,
        exportSchema = false)
public abstract class MyRoomsDatabase extends RoomDatabase {
    private static MyRoomsDatabase INSTANCE;

    public static synchronized MyRoomsDatabase getDatabase(Context context) {
        if(INSTANCE == null) {
            INSTANCE = androidx.room.Room.databaseBuilder(context.getApplicationContext(),
                    MyRoomsDatabase.class, "rooms_database").build();
        }
        return INSTANCE;
    }

    public abstract  RoomDao getRoomDao(); //这个是抽象方法
}
