package com.jiuhua.jiuhuacontrol.database;

/*这张表把enginerDB表的数据一小时一次加平均，*/

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class EngineLongTimeDB {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = " timestamp")
    private long timeStamp;   //直接使用UNIX时间
    @ColumnInfo(name = "enginer_minute")
    private int enginerMinute;  //主机运行时间 0--60分钟

    public EngineLongTimeDB(long timeStamp, int enginerMinute) {
        this.timeStamp = timeStamp;
        this.enginerMinute = enginerMinute;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getEnginerMinute() {
        return enginerMinute;
    }

    public void setEnginerMinute(int enginerMinute) {
        this.enginerMinute = enginerMinute;
    }

}
