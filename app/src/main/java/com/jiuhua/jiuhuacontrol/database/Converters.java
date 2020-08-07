package com.jiuhua.jiuhuacontrol.database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {
    @TypeConverter
    public static List<DayPeriod> fromJson(String s) {
        if (s == null) {
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<DayPeriod>>() {}.getType();
        return gson.fromJson(s, type);
    }

    @TypeConverter
    public static String toJson(List<DayPeriod> list) {
        if (list == null) {
            return null;
        }

        Gson gson = new Gson();
        String s = gson.toJson(list);

        return s;
    }
}
