package com.jiuhua.jiuhuacontrol;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.jiuhua.jiuhuacontrol.database.BasicInfoDB;
import com.jiuhua.jiuhuacontrol.database.IndoorDao;
import com.jiuhua.jiuhuacontrol.database.MyIndoorsDatabase;
import com.jiuhua.mqttsample.IGetMessageCallBack;
import com.jiuhua.mqttsample.MQTTService;
import com.jiuhua.mqttsample.MyServiceConnection;

import java.util.List;

public class MyRepository implements IGetMessageCallBack {

    LiveData<List<BasicInfoDB>> allRoomNameLive;
    private IndoorDao indoorDao;

    //MQTT需要的参数
    private MyServiceConnection serviceConnection;//连接实例
    private MQTTService mqttService;//服务实例

    public MyRepository(Context context) {
        MyIndoorsDatabase myIndoorsDatabase = MyIndoorsDatabase.getDatabase(context.getApplicationContext());
        indoorDao = myIndoorsDatabase.getRoomDao();
        allRoomNameLive = indoorDao.loadAllRoomName();

        serviceConnection = new MyServiceConnection();//新建连接服务的实例
        serviceConnection.setIGetMessageCallBack(MyRepository.this);//把本活动传入
        Intent intent = new Intent(context, getClass());
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        context.startService(intent);
    }

    public LiveData<List<BasicInfoDB>> getAllRoomsNameLive() {return allRoomNameLive;}

    //TODO 实现 Dao 的所有方法
    //插入房间名字
    public void insertRoomName(BasicInfoDB... basicInfoDBS) {
        new InsertRoomNameAsyncTask(indoorDao).execute(basicInfoDBS);
    }
    //删除所有房间名字
    public void deleteAllRoomsName(){
        new DeleteAllRoomsNameAsyncTask(indoorDao).execute();
    }

    @Override
    public void setMessage(String message) {
        //use the mqtt data here.
        //依据message字符串最后一位决定房间号，倒数第二位决定温湿度C为温度，H为湿度。
//        if (message.contains("C1")) room1temperature = message.replace("C1", "C");
//        if (message.contains("RH1")) room1humidity = message.replace("RH1", "RH");
//        if (message.contains("C2")) room2temperature = message.replace("C2", "C");

        //运行状态需要反馈回来
//        if (message.contains("valveonRoom1")) {
//            room1states = "正在运行";
//        }
//        if (message.contains("valveonRoom2")) {
//            room2states = "正在运行";
//        }
//
//        if (message.contains("valveoffRoom1")) {
//            room1states = "停止运行";
//        }
//        if (message.contains("valveoffRoom2")) {
//            room2states = "停止运行";
//        }


        //设置显示的文字
//        buttonA.setText("\n" + room1name + "\n\n" + room1temperature + "\n\n" + room1humidity + "\n\n" + room1states + "\n");
//        buttonB.setText("\n" + room2name + "\n\n" + room2temperature + "\n\n" + room2humidity + "\n\n" + room2states + "\n");
        mqttService = serviceConnection.getMqttService(); //服务连接实例 的 获得服务的方法
//        mqttService.toCreateNotification(message); //服务的发布消息的方法

    }

    //TODO 内部类，辅助线程上执行 Dao 的方法    还有一种线程池的方法（Google文档上的）
    static class InsertRoomNameAsyncTask extends AsyncTask<BasicInfoDB, Void, Void> {
        private IndoorDao indoorDao;   //独立的线程需要独立的 Dao

        InsertRoomNameAsyncTask(IndoorDao indoorDao) {this.indoorDao = indoorDao; }

        @Override
        protected Void doInBackground(BasicInfoDB... basicInfoDBS) {
            indoorDao.insertRoomNameDB(basicInfoDBS);
            return null;
        }
    }
    static class DeleteAllRoomsNameAsyncTask extends AsyncTask<Void, Void, Void> {
        private IndoorDao indoorDao;
        DeleteAllRoomsNameAsyncTask(IndoorDao indoorDao) {this.indoorDao = indoorDao;}

        @Override
        protected Void doInBackground(Void... voids) {
            indoorDao.deleteAllRoomsName();
            return null;
        }
    }
}
