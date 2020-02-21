package com.jiuhua.jiuhuacontrol;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.jiuhua.jiuhuacontrol.database.RoomNameDB;

import java.util.List;

public class RoomViewModel extends AndroidViewModel {

    //    enum FanStatus { turnoff, lowspeed, middlespeed, highspeed, autospeed };
    RoomsRepository roomsRepository;

    public RoomViewModel(Application application) {
        super(application);
        roomsRepository = new RoomsRepository(application);
    }


//    LiveData<List<RoomLittleMessage>> get

    //TODO　包装 Repository 里面的 Dao 方法
    void insertRoomName(RoomNameDB... roomNameDBS) {
        roomsRepository.insertRoomName(roomNameDBS);
    }
    void deleteAllRoomsName(){
        roomsRepository.deleteAllRoomsName();
    }

    LiveData<List<RoomNameDB>> getAllRoomsName(){
        return roomsRepository.getAllRoomsNameLive();
    }


}
