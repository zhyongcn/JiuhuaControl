package com.jiuhua.jiuhuacontrol.database;

/*这张表把engineDB表的数据一小时一次加平均，*/

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity
public class EngineLongTimeSheet {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = " timestamp")
    private long timeStamp;   //直接使用UNIX时间
    @ColumnInfo(name = "engine_minute")
    private int engineMinute;  //主机运行时间 0--60分钟

    public EngineLongTimeSheet(long timeStamp, int engineMinute) {
        this.timeStamp = timeStamp;
        this.engineMinute = engineMinute;
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

    public int getEngineMinute() {
        return engineMinute;
    }

    public void setEngineMinute(int engineMinute) {
        this.engineMinute = engineMinute;
    }

}
