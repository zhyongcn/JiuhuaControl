package com.jiuhua.jiuhuacontrol.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.jiuhua.jiuhuacontrol.MyRepository;
import com.jiuhua.jiuhuacontrol.database.BasicInfoDB;
import com.jiuhua.jiuhuacontrol.database.IndoorDB;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    MyRepository myRepository;

    public HomeViewModel(Application application) {
        super(application);
        myRepository = new MyRepository(application);
    }


    //包装 Repository 里面的 Dao 方法
    public void insertRoomName(BasicInfoDB... basicInfoDBS) {
        myRepository.insertBasicInfo(basicInfoDBS);
    }
    public void deleteAllRoomsName(){
        myRepository.deleteAllBasicInfo();
    }

    public LiveData<List<BasicInfoDB>> getAllBasicInfoLive(){
        return myRepository.getAllBasicInfoLive();
    }

    public LiveData<List<IndoorDB>> getAllLatestIndoorDBsLive() {
        return myRepository.getAllLatestIndoorDBsLive();
    }


}
