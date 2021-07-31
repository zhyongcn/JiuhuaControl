package com.jiuhua.jiuhuacontrol.ui.userinfo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.jiuhua.jiuhuacontrol.repository.MyRepository;
import com.jiuhua.jiuhuacontrol.database.BasicInfoSheet;

import java.util.List;

public class UserInfoViewModel extends AndroidViewModel {

    MyRepository myRepository;

    //the data on page

    public UserInfoViewModel(@NonNull Application application) {
        super(application);
        myRepository = MyRepository.getInstance(application);
    }

    //包装 Repository 里面的 Dao 方法
    public void insertBasicInfo(BasicInfoSheet... basicInfoSheets) {
        myRepository.insertBasicInfo(basicInfoSheets);
    }

    public void updateBasicInfo(BasicInfoSheet... basicInfoSheets) {
        myRepository.updateBasicInfo(basicInfoSheets);
    }

    public void deleteBasicInfo(BasicInfoSheet... basicInfoSheets) {
        myRepository.deleteBasicInfo(basicInfoSheets);
    }

//    public List<BasicInfoSheet> getAllBasicInfo(){
//        return myRepository.getAllBasicInfo();
//    }

    public LiveData<List<BasicInfoSheet>> getAllBasicInfoLive(){
        return myRepository.getAllBasicInfoLive();
    }
}
