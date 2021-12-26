package com.jiuhua.jiuhuacontrol.ui.upgrade;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.jiuhua.jiuhuacontrol.repository.MyRepository;

public class UpgradeViewModel extends AndroidViewModel {
    MyRepository myRepository;

    public UpgradeViewModel(@NonNull Application application) {
        super(application);
        this.myRepository = MyRepository.getInstance(application);
    }

    public void upgrade(String fullurl) {
        myRepository.upgrade(fullurl);
    }
}
