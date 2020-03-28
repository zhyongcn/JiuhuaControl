package com.jiuhua.jiuhuacontrol.ui.indoor;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.jiuhua.jiuhuacontrol.MyRepository;
import com.jiuhua.jiuhuacontrol.database.BasicInfoDB;
import com.jiuhua.jiuhuacontrol.database.IndoorDB;

import java.util.List;

public class IndoorViewModel extends AndroidViewModel {

//    enum FanSpeed {STOP, LOW, MEDIUM, HIGH, AUTO}
//    enum RoomState {STOP, MANUAL, AUTO}

    MyRepository myRepository;
    List<IndoorDB> allLatestIndoorDBs;
    IndoorDB latestIndoorDB;

    //变量及其getter & setter 方法
    private int roomNameId = 1;
    private String roomName;

    public int getRoomNameId() {
        return roomNameId;
    }

    public void setRoomNameId(int roomNameId) {
        this.roomNameId = roomNameId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public IndoorDB getLatestIndoorDB() {
        return latestIndoorDB;
    }

    public void setLatestIndoorDB(IndoorDB latestIndoorDB) {
        this.latestIndoorDB = latestIndoorDB;
    }

    public void setAllLatestIndoorDBs(List<IndoorDB> allLatestIndoorDBsLive) {
        this.allLatestIndoorDBs = allLatestIndoorDBsLive;
        if (roomNameId < allLatestIndoorDBsLive.size()) {  //TODO 临时的需要解决超出队列边界的问题
            this.latestIndoorDB = allLatestIndoorDBsLive.get(roomNameId);
        }else {//TODO 单纯非空还是不解决问题
            this.latestIndoorDB = new IndoorDB();
        }
    }

    //下面三个switch用来设定房间的状态
    //下面五个switch用来设定风机状态

    //构造方法
    public IndoorViewModel(@NonNull Application application) {
        super(application);
        this.myRepository = new MyRepository(application);
    }

    //TODO　包装 Repository 里面的 Dao 方法
    //停止按钮要实现方法，停止房间所有设备
    public void stopRoomEquipment(String roomid){
        myRepository.stopRoomEquipment(roomid);
    }
    //TODO 手动方法 自动方法
    //TODO 地暖开关方法  除湿开关方法
    //TODO 设置温度方法  设置湿度方法


    public LiveData<List<IndoorDB>> getAllLatestIndoorDBsLive() {
        return myRepository.getAllLatestIndoorDBsLive();
    }
}
