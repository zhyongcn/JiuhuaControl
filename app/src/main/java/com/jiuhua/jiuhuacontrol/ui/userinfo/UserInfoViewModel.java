package com.jiuhua.jiuhuacontrol.ui.userinfo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.jiuhua.jiuhuacontrol.repository.MyRepository;
import com.jiuhua.jiuhuacontrol.database.BasicInfoDB;

import java.util.List;

public class UserInfoViewModel extends AndroidViewModel {

    MyRepository myRepository;

    //the data on page

    public UserInfoViewModel(@NonNull Application application) {
        super(application);
        myRepository = new MyRepository(application);
    }

    //包装 Repository 里面的 Dao 方法
    public void insertBasicInfo(BasicInfoDB... basicInfoDBS) {
        myRepository.insertBasicInfo(basicInfoDBS);
    }

    public void updateBasicInfo(BasicInfoDB... basicInfoDBS) {
        myRepository.updateBasicInfo(basicInfoDBS);
    }

    public void deleteBasicInfo(BasicInfoDB... basicInfoDBS) {
        myRepository.deleteBasicInfo(basicInfoDBS);
    }

//    public List<BasicInfoDB> getAllBasicInfo(){
//        return myRepository.getAllBasicInfo();
//    }

    public LiveData<List<BasicInfoDB>> getAllBasicInfoLive(){
        return myRepository.getAllBasicInfoLive();
    }
}
