package com.jiuhua.jiuhuacontrol;

import android.content.Context;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {RoomDB.class, RoomNameDB.class, RoomLongTimeDB.class}, version = 1,
        exportSchema = false)
public abstract class RoomsDatabase extends RoomDatabase {
    private static RoomsDatabase INSTANCE;

    static synchronized RoomsDatabase getInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = androidx.room.Room.databaseBuilder(context.getApplicationContext(),
                    RoomsDatabase.class, "rooms_database").build();
        }
        return INSTANCE;
    }

    public static RoomDao getRoomDao() {
        return null;
    }
}
