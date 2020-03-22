package com.jiuhua.jiuhuacontrol;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.jiuhua.jiuhuacontrol.database.BasicInfoDB;
import com.jiuhua.jiuhuacontrol.database.IndoorDao;
import com.jiuhua.jiuhuacontrol.database.MyIndoorsDatabase;
import com.jiuhua.jiuhuacontrol.ui.indoor.IndoorViewModel;
import com.jiuhua.mqttsample.IGetMessageCallBack;
import com.jiuhua.mqttsample.MQTTService;
import com.jiuhua.mqttsample.MyServiceConnection;

import java.util.List;

public class MyRepository implements IGetMessageCallBack {

    List<BasicInfoDB> allBasicInfo;
    LiveData<List<BasicInfoDB>> allBasicInfoLive;
    private IndoorDao indoorDao;

    //MQTT需要的参数
    private MyServiceConnection serviceConnection;//连接实例
    private MQTTService mqttService;//服务实例

    public MyRepository(Context context) {
        MyIndoorsDatabase myIndoorsDatabase = MyIndoorsDatabase.getDatabase(context.getApplicationContext());
        indoorDao = myIndoorsDatabase.getRoomDao();
//        allBasicInfo = indoorDao.loadAllBasicInfo();
        allBasicInfoLive = indoorDao.loadAllBasicInfoLive();

        serviceConnection = new MyServiceConnection();//新建连接服务的实例
        serviceConnection.setIGetMessageCallBack(MyRepository.this);//把本活动传入
        Intent intent = new Intent(context, MQTTService.class);
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    //TODO 实现发送功能
    public void stopRoomEquipment(String roomID){
//        mqttService = serviceConnection.getMqttService();
        MQTTService.publish("86518/JYCFGC/6-2-3401/Room"+roomID,"Room"+roomID+"turn-offFP", 1, true);
        MQTTService.publish("86518/JYCFGC/6-2-3401/Room"+roomID,"Room"+roomID+"turn-offfloor", 1, true);
    }


    //TODO 实现 Dao 的所有方法
    //插入房间信息
    public void insertBasicInfo(BasicInfoDB... basicInfoDBS) {
        new InsertBasicInfoAsyncTask(indoorDao).execute(basicInfoDBS);
    }
    //修改房间信息
    public void updateBasicInfo(BasicInfoDB... basicInfoDBS) {
        new UpdateBasicInfoAsyncTask(indoorDao).execute(basicInfoDBS);
    }
    //删除房间信息，依靠主键就可以
    public void deleteBasicInfo(BasicInfoDB... basicInfoDBS) {
        new DeleteBasicInfoAsyncTask(indoorDao).execute(basicInfoDBS);
    }
    //删除所有房间名字
    public void deleteAllBasicInfo() {
        new DeleteAllBasicInfoAsyncTask(indoorDao).execute();
    }
    //这个好像有问题，不能与下面的方法同时存在（早Dao里面）需要删除
    public List<BasicInfoDB> getAllBasicInfo() {
        return allBasicInfo;
    }
    //获取所有房间的信息，livedata形式。
    public LiveData<List<BasicInfoDB>> getAllBasicInfoLive() {
        return allBasicInfoLive;
    }

    @Override
    public void setMessage(String message) {
        //use mqtt message here.
        //依据message字符串最后一位决定房间号，倒数第二位决定温湿度C为温度，H为湿度。
        //目前只有 C2  C3 C5 三个房间有信号
        if (message.contains("C2")) {
            IndoorViewModel.currentTemperature.setValue(message.replace("C2", "C"));
        }

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

    //内部类，辅助线程上执行 Dao 的方法。    还有一种线程池的方法（Google文档上的）
    static class InsertBasicInfoAsyncTask extends AsyncTask<BasicInfoDB, Void, Void> {
        private IndoorDao indoorDao;   //独立的线程需要独立的 Dao

        InsertBasicInfoAsyncTask(IndoorDao indoorDao) {
            this.indoorDao = indoorDao;
        }

        @Override
        protected Void doInBackground(BasicInfoDB... basicInfoDBS) {
            indoorDao.insertBasicInfoDB(basicInfoDBS);
            return null;
        }
    }

    static class UpdateBasicInfoAsyncTask extends AsyncTask<BasicInfoDB, Void, Void> {
        private IndoorDao indoorDao;
        UpdateBasicInfoAsyncTask(IndoorDao indoorDao) {
            this.indoorDao = indoorDao;
        }

        @Override
        protected Void doInBackground(BasicInfoDB... basicInfoDBS) {
            indoorDao.updateBasicInfoDB(basicInfoDBS);
            return null;
        }
    }

    static class DeleteBasicInfoAsyncTask extends AsyncTask<BasicInfoDB, Void, Void> {
        private IndoorDao indoorDao;
        DeleteBasicInfoAsyncTask(IndoorDao indoorDao) {
            this.indoorDao = indoorDao;
        }

        @Override
        protected Void doInBackground(BasicInfoDB... basicInfoDBS) {
            indoorDao.deleteBasicInfoDB(basicInfoDBS);
            return null;
        }
    }

    static class DeleteAllBasicInfoAsyncTask extends AsyncTask<Void, Void, Void> {
        private IndoorDao indoorDao;

        DeleteAllBasicInfoAsyncTask(IndoorDao indoorDao) {
            this.indoorDao = indoorDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            indoorDao.deleteAllBasicInfo();
            return null;
        }
    }
}
