package com.jiuhua.jiuhuacontrol.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.jiuhua.jiuhuacontrol.database.BasicInfoSheet;
import com.jiuhua.jiuhuacontrol.database.SensorSheet;
import com.jiuhua.jiuhuacontrol.repository.MyRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    MyRepository myRepository;

    public HomeViewModel(Application application) {
        super(application);
        myRepository = MyRepository.getInstance(application);
    }


    //包装 Repository 里面的 Dao 方法
    public void insertRoomName(BasicInfoSheet... basicInfoSheets) {
        myRepository.insertBasicInfo(basicInfoSheets);
    }
    public void deleteAllRoomsName(){
        myRepository.deleteAllBasicInfo();
    }

    public LiveData<List<BasicInfoSheet>> getAllBasicInfoLive(){
        return myRepository.getAllBasicInfoLive();
    }

    public LiveData<List<SensorSheet>> getAllLatestIndoorDBsLive(int devicetypeId) {
        return myRepository.getAllLatestIndoorSheetsLive(devicetypeId);
    }

    //获取普通房间的名字
    public String loadRoomName(int roomid) {
        return myRepository.loadRoomName(roomid);
    }


}
