package com.jiuhua.jiuhuacontrol.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.jiuhua.jiuhuacontrol.MyRepository;
import com.jiuhua.jiuhuacontrol.database.RoomNameDB;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    //    enum FanStatus { turnoff, lowspeed, middlespeed, highspeed, autospeed };
    MyRepository myRepository;

    public HomeViewModel(Application application) {
        super(application);
        myRepository = new MyRepository(application);
    }


//    LiveData<List<RoomLittleMessage>> get

    //TODO　包装 Repository 里面的 Dao 方法
    public void insertRoomName(RoomNameDB... roomNameDBS) {
        myRepository.insertRoomName(roomNameDBS);
    }
    public void deleteAllRoomsName(){
        myRepository.deleteAllRoomsName();
    }

    public LiveData<List<RoomNameDB>> getAllRoomsName(){
        return myRepository.getAllRoomsNameLive();
    }


}
