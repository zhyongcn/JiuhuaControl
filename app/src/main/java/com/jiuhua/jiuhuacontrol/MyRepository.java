package com.jiuhua.jiuhuacontrol;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.jiuhua.jiuhuacontrol.database.BasicInfoDB;
import com.jiuhua.jiuhuacontrol.database.IndoorDB;
import com.jiuhua.jiuhuacontrol.database.IndoorDao;
import com.jiuhua.jiuhuacontrol.database.MyIndoorsDatabase;
import com.jiuhua.mqttsample.IGetMessageCallBack;
import com.jiuhua.mqttsample.MQTTService;
import com.jiuhua.mqttsample.MyServiceConnection;

import java.util.List;

public class MyRepository implements IGetMessageCallBack {

    LiveData<List<BasicInfoDB>> allBasicInfoLive;
    LiveData<List<IndoorDB>> allLatestIndoorDBsLive;
    private IndoorDao indoorDao;

    //MQTT需要的参数
    private MyServiceConnection serviceConnection;//连接实例
    private MQTTService mqttService;//服务实例

    public MyRepository(Context context) {
        MyIndoorsDatabase myIndoorsDatabase = MyIndoorsDatabase.getDatabase(context.getApplicationContext());
        indoorDao = myIndoorsDatabase.getRoomDao();
//        allBasicInfo = indoorDao.loadAllBasicInfo();  //相关于Dao里面的一个有问题的方法。好像不能用。
        allBasicInfoLive = indoorDao.loadAllBasicInfoLive();
        allLatestIndoorDBsLive = indoorDao.loadLatestIndoorDBsLive();

        serviceConnection = new MyServiceConnection();//新建连接服务的实例
        serviceConnection.setIGetMessageCallBack(MyRepository.this);//把本活动传入
        Intent intent = new Intent(context, MQTTService.class);
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    //TODO 实现发送功能
    public void stopRoomEquipment(String roomID) {
//        mqttService = serviceConnection.getMqttService();
        MQTTService.publish("86518/JYCFGC/6-2-3401/Room" + roomID, "Room" + roomID + "turn-offFP", 1, true);
        MQTTService.publish("86518/JYCFGC/6-2-3401/Room" + roomID, "Room" + roomID + "turn-offfloor", 1, true);
    }

    //TODO 实现 Dao 的所有方法  **********************************************************
    //插入基本房间信息
    public void insertBasicInfo(BasicInfoDB... basicInfoDBS) {
        new InsertBasicInfoAsyncTask(indoorDao).execute(basicInfoDBS);
    }

    //修改基本房间信息
    public void updateBasicInfo(BasicInfoDB... basicInfoDBS) {
        new UpdateBasicInfoAsyncTask(indoorDao).execute(basicInfoDBS);
    }

    //删除基本房间信息，依靠主键就可以
    public void deleteBasicInfo(BasicInfoDB... basicInfoDBS) {
        new DeleteBasicInfoAsyncTask(indoorDao).execute(basicInfoDBS);
    }

    //删除所有的基本房间信息
    public void deleteAllBasicInfo() {
        new DeleteAllBasicInfoAsyncTask(indoorDao).execute();
    }

    //    //这个好像有问题，不能与下面的方法同时存在（在Dao里面）需要删除
//    public List<BasicInfoDB> getAllBasicInfo() {
//        return allBasicInfo;
//    }

    //获取所有的基本房间信息，LiveData形式。
    public LiveData<List<BasicInfoDB>> getAllBasicInfoLive() {
        return allBasicInfoLive;
    }

    //获取普通房间的名字
    public String loadRoomName(int id) {
        return indoorDao.loadRoomName(id);
    }

    //插入普通房间的状态信息
    public void insertIndoorDB(IndoorDB... indoorDBS) {
        new InsertIndoorDBAsyncTask(indoorDao).execute(indoorDBS);
    }

    //删除所有普通房间的信息
    public void deleteAllIndoorDB() {
        new DeleteAllIndoorDBAsyncTask(indoorDao).execute();
    }

    //获取普通房间的全部信息
    public LiveData<List<IndoorDB>> getAllLatestIndoorDBsLive() {
        return allLatestIndoorDBsLive;
    }

//    获取单个房间的信息
//    public LiveData<IndoorDB> getSingleLatestIndoorDBLive(int roomNameId) {
//        int Id = roomNameId;
//        lattestIndoorDB = indoorDao.loadSingleLatestIndoorDBLive(Id);
//        return lattestIndoorDB;
//    }


    //TODO 内部类，辅助线程上执行 Dao 的方法。    还有一种线程池的方法（Google文档上的）*****************
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

    static class InsertIndoorDBAsyncTask extends AsyncTask<IndoorDB, Void, Void> {
        private IndoorDao indoorDao;   //独立的线程需要独立的 Dao

        InsertIndoorDBAsyncTask(IndoorDao indoorDao) {
            this.indoorDao = indoorDao;
        }

        @Override
        protected Void doInBackground(IndoorDB... indoorDBS) {
            indoorDao.insertIndoorDB(indoorDBS);
            return null;
        }
    }

    static class DeleteAllIndoorDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private IndoorDao indoorDao;

        DeleteAllIndoorDBAsyncTask(IndoorDao indoorDao) {
            this.indoorDao = indoorDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            indoorDao.deleteAllIndoorDB();
            return null;
        }
    }


    //TODO 接收 MQTT 数据后，依照数据实现相关方法。  *************************************************
    @Override
    public void setMessage(String message) {
        //use mqtt message here.
        //依据message字符串最后一位决定房间号，倒数第二位决定温湿度C为温度，H为湿度。
        //临时方法，目前只有 C2  C3 C5 三个房间有信号
        if (message.contains("C1")) {
            IndoorDB indoorDB1 = new IndoorDB();
            indoorDB1.setTimeStamp(System.currentTimeMillis() / 1000);
            indoorDB1.setRoomNameId(1);//第一个房间
            indoorDB1.setCurrentTemperature(21);
            indoorDB1.setSettingTemperature(20);
            indoorDB1.setCurrentHumidity(Integer.parseInt("40"));
            indoorDB1.setSettingHumidity(50);
            indoorDB1.setFanStatus(0);  //stop 0, low 1, middium 2, high 3, auto 4.
            indoorDB1.setFloorValveOpen(true);
            indoorDB1.setCoilValveOpen(true);
            indoorDB1.setDehumidityStatus(false);
            indoorDB1.setRoomStatus(0);  //stop 0, manual 1, auto 2.
            insertIndoorDB(indoorDB1);
        }
        if (message.contains("C2")) {
            IndoorDB indoorDB1 = new IndoorDB();
            indoorDB1.setTimeStamp(System.currentTimeMillis() / 1000);
            indoorDB1.setRoomNameId(2);
            indoorDB1.setCurrentTemperature(22);
            indoorDB1.setSettingTemperature(20);
            indoorDB1.setCurrentHumidity(Integer.parseInt("40"));
            indoorDB1.setSettingHumidity(50);
            indoorDB1.setFanStatus(1);  //stop 0, low 1, middium 2, high 3, auto 4.
            indoorDB1.setFloorValveOpen(true);
            indoorDB1.setCoilValveOpen(true);
            indoorDB1.setDehumidityStatus(true);
            indoorDB1.setRoomStatus(0);  //stop 0, manual 1, auto 2.
            insertIndoorDB(indoorDB1);
        }
        if (message.contains("C3")) {
            IndoorDB indoorDB = new IndoorDB();
            indoorDB.setTimeStamp(System.currentTimeMillis() / 1000);
            indoorDB.setRoomNameId(3);
            indoorDB.setCurrentTemperature(23);
            indoorDB.setSettingTemperature(33);
            indoorDB.setCurrentHumidity(Integer.parseInt("30"));
            indoorDB.setSettingHumidity(33);
            indoorDB.setFanStatus(2);  //stop 0, low 1, middium 2, high 3, auto 4.
            indoorDB.setFloorValveOpen(false);
            indoorDB.setCoilValveOpen(false);
            indoorDB.setDehumidityStatus(false);
            indoorDB.setRoomStatus(1);  //stop 0, manual 1, auto 2.
            insertIndoorDB(indoorDB);
        }
        if (message.contains("C4")) {
            IndoorDB indoorDB = new IndoorDB();
            indoorDB.setTimeStamp(System.currentTimeMillis() / 1000);
            indoorDB.setRoomNameId(4);//第四个房间
            indoorDB.setCurrentTemperature(24);
            indoorDB.setSettingTemperature(24);
            indoorDB.setCurrentHumidity(Integer.parseInt("30"));
            indoorDB.setSettingHumidity(40);
            indoorDB.setFanStatus(3);  //stop 0, low 1, middium 2, high 3, auto 4.
            indoorDB.setFloorValveOpen(true);
            indoorDB.setCoilValveOpen(false);
            indoorDB.setDehumidityStatus(true);
            indoorDB.setRoomStatus(2);  //stop 0, manual 1, auto 2.
            insertIndoorDB(indoorDB);
        }
        if (message.contains("C5")) {
            IndoorDB indoorDB1 = new IndoorDB();
            indoorDB1.setTimeStamp(System.currentTimeMillis() / 1000);
            indoorDB1.setRoomNameId(5);
            indoorDB1.setCurrentTemperature(35);
            indoorDB1.setSettingTemperature(25);
            indoorDB1.setSettingHumidity(50);
            indoorDB1.setCurrentHumidity(Integer.parseInt("40"));
            indoorDB1.setFanStatus(4);  //stop 0, low 1, middium 2, high 3, auto 4.
            indoorDB1.setFloorValveOpen(false);
            indoorDB1.setCoilValveOpen(true);
            indoorDB1.setDehumidityStatus(false);
            indoorDB1.setRoomStatus(2);  //stop 0, manual 1, auto 2.
            insertIndoorDB(indoorDB1);
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

}
