package com.jiuhua.jiuhuacontrol;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.jiuhua.jiuhuacontrol.database.BasicInfoDB;
import com.jiuhua.jiuhuacontrol.database.IndoorDB;
import com.jiuhua.jiuhuacontrol.database.IndoorDao;
import com.jiuhua.jiuhuacontrol.database.MyIndoorsDatabase;
import com.jiuhua.mqttsample.IGetMessageCallBack;
import com.jiuhua.mqttsample.MQTTService;
import com.jiuhua.mqttsample.MyServiceConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyRepository implements IGetMessageCallBack {

    LiveData<List<BasicInfoDB>> allBasicInfoLive;
    LiveData<List<IndoorDB>> allLatestIndoorDBsLive;
    List<IndoorDB> writeToDBIndoorDBs;
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

//        timer.schedule(task, 6000, 6000);
    }

//    public void setWriteToDBIndoorDBs(List<IndoorDB> writeToDBIndoorDBs) {
//        this.writeToDBIndoorDBs = writeToDBIndoorDBs;
//    }

    //TODO 一分钟写入一次数据库
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            insertIndoorDB((IndoorDB) writeToDBIndoorDBs);
        }
    };

    //TODO 实现发送功能
    public void stopRoomEquipment(int roomID) {
        mqttService = serviceConnection.getMqttService();  //这句可有可无，有就用小写的，没有就用大写的MQTTService
        mqttService.publish("86518/JYCFGC/6-2-3401/Room" + roomID, "Room" + roomID + "turn-offFP", 1, true);
        MQTTService.publish("86518/JYCFGC/6-2-3401/Room" + roomID, "Room" + roomID + "turn-offfloor", 1, true);
//        Log.d("MQTTtest", "stopRoomEquipment: " + roomID);
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


    int count = 0;
    //TODO 接收 MQTT 数据后，依照数据实现相关方法。  *************************************************
    @Override
    public void setMessage(String message) {   //TODO 活太多了，处理不了，不能有太复杂的逻辑
        //use mqtt message here.
//        if (message.contains("C1")) {
//            IndoorDB indoorDB1 = new IndoorDB();
//            indoorDB1.setTimeStamp(System.currentTimeMillis() / 1000);
//            indoorDB1.setRoomNameId(1);//第一个房间
//            indoorDB1.setCurrentTemperature(21);
//            indoorDB1.setSettingTemperature(20);
//            indoorDB1.setCurrentHumidity(Integer.parseInt("40"));
//            indoorDB1.setSettingHumidity(50);
//            indoorDB1.setFanStatus(0);  //stop 0, low 1, middium 2, high 3, auto 4.
//            indoorDB1.setFloorValveOpen(true);
//            indoorDB1.setCoilValveOpen(true);
//            indoorDB1.setDehumidityStatus(false);
//            indoorDB1.setRoomStatus(0);  //stop 0, manual 1, auto 2.
//            insertIndoorDB(indoorDB1);
//        }
        if (message.contains("C"+1)) {
            writeToDBIndoorDBs.get(0).setCurrentTemperature(
                    Float.parseFloat(message.substring(0, message.indexOf(".") + 2)));
//            insertIndoorDB(writeToDBIndoorDBs.get(0));
        }
        if (message.contains("RH" + 1)) {
            writeToDBIndoorDBs.get(0).setCurrentHumidity(
                    Float.parseFloat(message.substring(0, message.indexOf(".") + 2)));
        }


//        //依据message字符串最后一位决定房间号，倒数第二位决定温湿度C为温度，H为湿度。
//            writeToDBIndoorDBs.get(0).setTimeStamp(System.currentTimeMillis() / 1000);
//            writeToDBIndoorDBs.get(0).setRoomNameId(1);
//            //依据message字符串最后一位决定房间号，倒数第二位决定温湿度C为温度，H为湿度。
//
//            //TODO 从模块回传设置温度和设置湿度
//            writeToDBIndoorDBs.get(0).setSettingTemperature(20);
//            writeToDBIndoorDBs.get(0).setSettingHumidity(30);
//
//            //回传的风机状态
//            if (message.contains("lowRoom" + 1 + "FP"))
//                writeToDBIndoorDBs.get(0).setFanStatus(1);
//            if (message.contains("middleRoom" + 1 + "FP"))
//                writeToDBIndoorDBs.get(0).setFanStatus(2);
//            if (message.contains("highRoom" + 1 + "FP"))
//                writeToDBIndoorDBs.get(0).setFanStatus(4);
//            if (message.contains("stopRoom" + 1 + "FP"))
//                writeToDBIndoorDBs.get(0).setFanStatus(0);
//            //TODO add fanstatus auto
//            //if (message.contains("stopRoom" + i + "FP")) writeToDBIndoorDBs.get(i-1).setFanStatus(0);
//
//            //地暖模块回传的信息
//            if (message.contains("valveonRoom" + 1 + "floor"))
//                writeToDBIndoorDBs.get(0).setFloorValveOpen(true);
//            if (message.contains("valveoffRoom" + 1 + "floor"))
//                writeToDBIndoorDBs.get(0).setFloorValveOpen(false);
//            //两通阀回传的信息
//            if (message.contains("valveonRoom" + 1 + "FP"))
//                writeToDBIndoorDBs.get(0).setCoilValveOpen(true);
//            if (message.contains("valveoffRoom" + 1 + "FP"))
//                writeToDBIndoorDBs.get(0).setCoilValveOpen(false);
//            //回传的除湿状态
//            if (message.contains("dehumidityRoom" + 1 + "FP"))
//                writeToDBIndoorDBs.get(0).setDehumidityStatus(true);
//            //风机模块回传的房间状态信息
//            if (message.contains("turnoffRoom" + 1 + "FP"))
//                writeToDBIndoorDBs.get(0).setRoomStatus(0);
//            if (message.contains("manualRoom" + 1 + "FP"))
//                writeToDBIndoorDBs.get(0).setRoomStatus(1);
//            if (message.contains("automationRoom" + 1 + "FP"))
//                writeToDBIndoorDBs.get(0).setRoomStatus(2);
            count++;
        Log.d("count", String.valueOf(count));
            if (count>50) {
                count = 0;
                insertIndoorDB(writeToDBIndoorDBs.get(0));
            }


        //设置显示的文字
//        buttonA.setText("\n" + room1name + "\n\n" + room1temperature + "\n\n" + room1humidity + "\n\n" + room1states + "\n");
//        buttonB.setText("\n" + room2name + "\n\n" + room2temperature + "\n\n" + room2humidity + "\n\n" + room2states + "\n");
        mqttService = serviceConnection.getMqttService(); //服务连接实例 的 获得服务的方法
//        mqttService.toCreateNotification(message); //服务的发布消息的方法

    }

}
