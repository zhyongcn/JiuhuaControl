package com.jiuhua.jiuhuacontrol.room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.jiuhua.jiuhuacontrol.MyRepository;

public class RoomViewModel extends AndroidViewModel {

    MyRepository myRepository;

    public RoomViewModel(@NonNull Application application) {
        super(application);
        myRepository = new MyRepository(application);
    }

}
