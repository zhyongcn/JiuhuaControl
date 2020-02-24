package com.jiuhua.jiuhuacontrol;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.jiuhua.jiuhuacontrol.database.MyRoomsDatabase;
import com.jiuhua.jiuhuacontrol.database.RoomDao;
import com.jiuhua.jiuhuacontrol.database.RoomNameDB;

import java.util.List;

public class MyRepository {

    LiveData<List<RoomNameDB>> allRoomNameLive;
    private RoomDao roomDao;

    public MyRepository(Context context) {
        MyRoomsDatabase myRoomsDatabase = MyRoomsDatabase.getDatabase(context.getApplicationContext());
        roomDao = myRoomsDatabase.getRoomDao();
        allRoomNameLive = roomDao.loadAllRoomName();
    }

    LiveData<List<RoomNameDB>> getAllRoomsNameLive() {return allRoomNameLive;}

    //TODO 实现 Dao 的所有方法
    //插入房间名字
    void insertRoomName(RoomNameDB... roomNameDBS) {
        new InsertRoomNameAsyncTask(roomDao).execute(roomNameDBS);
    }
    //删除所有房间名字
    void deleteAllRoomsName(){
        new DeleteAllRoomsNameAsyncTask(roomDao).execute();
    }

    //TODO 内部类，辅助线程上执行 Dao 的方法    还有一种线程池的方法（Google文档上的）
    static class InsertRoomNameAsyncTask extends AsyncTask<RoomNameDB, Void, Void> {
        private RoomDao roomDao;   //独立的线程需要独立的 Dao

        InsertRoomNameAsyncTask(RoomDao roomDao) {this.roomDao = roomDao; }

        @Override
        protected Void doInBackground(RoomNameDB... roomNameDBS) {
            roomDao.insertRoomNameDB(roomNameDBS);
            return null;
        }
    }
    static class DeleteAllRoomsNameAsyncTask extends AsyncTask<Void, Void, Void> {
        private RoomDao roomDao;
        DeleteAllRoomsNameAsyncTask(RoomDao roomDao) {this.roomDao = roomDao;}

        @Override
        protected Void doInBackground(Void... voids) {
            roomDao.deleteAllRoomsName();
            return null;
        }
    }
}
