package com.jiuhua.jiuhuacontrol.database;

/*这张表的数据一分钟一次，存储上限为三个月*/

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class EnginerDB {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = " timestamp")
    private long timeStamp;   //直接使用UNIX时间
    @ColumnInfo
    private boolean enginerRuning;

    public EnginerDB(long timeStamp, boolean enginerRuning) {
        this.timeStamp = timeStamp;
        this.enginerRuning = enginerRuning;
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

    public boolean isEnginerRuning() {
        return enginerRuning;
    }

    public void setEnginerRuning(boolean enginerRuning) {
        this.enginerRuning = enginerRuning;
    }

}
