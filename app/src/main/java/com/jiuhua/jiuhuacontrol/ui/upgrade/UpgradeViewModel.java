package com.jiuhua.jiuhuacontrol.ui.upgrade;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.gson.Gson;
import com.jiuhua.jiuhuacontrol.repository.MyRepository;

public class UpgradeViewModel extends AndroidViewModel {
    private final String TAG = getClass().getSimpleName();
    MyRepository myRepository;

    AppVersionInfo appVersionInfo = new AppVersionInfo();

    public UpgradeViewModel(@NonNull Application application) {
        super(application);
        this.myRepository = MyRepository.getInstance(application);
    }

    public void upgrade(String fullurl) {
        myRepository.upgrade(fullurl);
    }

    public AppVersionInfo getAppVersionInfo() {
        appVersionInfo = myRepository.getAppVersionInfos()[0];
        Log.d(TAG, new Gson().toJson(appVersionInfo));
        return appVersionInfo;
    }
}


