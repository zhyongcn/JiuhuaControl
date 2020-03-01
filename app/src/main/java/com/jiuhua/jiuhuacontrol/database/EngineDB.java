package com.jiuhua.jiuhuacontrol.database;

/*这张表的数据一分钟一次，存储上限为三个月*/

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class EngineDB {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = " timestamp")
    private long timeStamp;   //直接使用UNIX时间
    @ColumnInfo
    private boolean isengineRuning;

    public EngineDB(long timeStamp, boolean isengineRuning) {
        this.timeStamp = timeStamp;
        this.isengineRuning = isengineRuning;
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

    public boolean isIsengineRuning() {
        return isengineRuning;
    }

    public void setIsengineRuning(boolean isengineRuning) {
        this.isengineRuning = isengineRuning;
    }

}
