package com.jiuhua.jiuhuacontrol.database;

import android.view.View;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

public class Converters {
    @TypeConverter
    public static int[][] fromJson(String s){
        Gson gson = new Gson();
        //TODO: pass the compiler ,
        //TODO  Type type = new TypeToken<int[][]>() {}.getType(); //I don`t know , this method is ok??
        int[][] peroid = gson.fromJson(s, int[][].class);

        return peroid;
    }

    @TypeConverter
    public static String toJson(int[][] ints){
        Gson gson = new Gson();
        String s = gson.toJson(ints);
        return s;
    }
}
