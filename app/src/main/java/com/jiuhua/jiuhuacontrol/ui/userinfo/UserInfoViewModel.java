package com.jiuhua.jiuhuacontrol.ui.userinfo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.jiuhua.jiuhuacontrol.MyRepository;
import com.jiuhua.jiuhuacontrol.database.BasicInfoDB;

import java.util.List;

public class UserInfoViewModel extends AndroidViewModel {

    MyRepository myRepository;

    //the data on page

    public UserInfoViewModel(@NonNull Application application) {
        super(application);
        myRepository = new MyRepository(application);
    }

    //TODO　包装 Repository 里面的 Dao 方法
    public void insertRoomName(BasicInfoDB... basicInfoDBS) {
        myRepository.insertRoomName(basicInfoDBS);
    }
    public void deleteAllRoomsName(){
        myRepository.deleteAllRoomsName();
    }

    public LiveData<List<BasicInfoDB>> getAllRoomsName(){
        return myRepository.getAllRoomsNameLive();
    }
}
