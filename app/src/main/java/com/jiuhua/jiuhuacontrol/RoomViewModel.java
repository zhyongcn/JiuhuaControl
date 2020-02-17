package com.jiuhua.jiuhuacontrol;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class RoomViewModel extends ViewModel {
    enum FanStatus { turnoff, lowspeed, middlespeed, highspeed, autospeed };

    LiveData<List<RoomDB>> allRoomLive;

    public RoomViewModel(LiveData<List<RoomDB>> allRoomLive) {
        this.allRoomLive = allRoomLive;
    }

}
