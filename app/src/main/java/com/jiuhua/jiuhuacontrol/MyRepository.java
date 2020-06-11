package com.jiuhua.jiuhuacontrol;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.jiuhua.jiuhuacontrol.database.BasicInfoDB;
import com.jiuhua.jiuhuacontrol.database.IndoorDB;
import com.jiuhua.jiuhuacontrol.database.IndoorDao;
import com.jiuhua.jiuhuacontrol.database.MyIndoorsDatabase;
import com.jiuhua.mqttsample.IGetMessageCallBack;
import com.jiuhua.mqttsample.MQTTService;
import com.jiuhua.mqttsample.MyServiceConnection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MyRepository implements IGetMessageCallBack {

    long TimeStamp;
    LiveData<List<BasicInfoDB>> allBasicInfoLive;
    LiveData<List<IndoorDB>> allLatestIndoorDBsLive;
    private IndoorDao indoorDao;
    ArrayList<IndoorDB> indoorDBS = new ArrayList<>();

    // MQTT 需要的参数
    private MyServiceConnection serviceConnection;//连接实例
    private MQTTService mqttService;//服务实例

//    private static final ThreadPoolExecutor poolexecutor = new ThreadPoolExecutor(
//            4, 4,30, TimeUnit.SECONDS, quene);

    public MyRepository(Context context) {
        //初始化用来写入数据库的arraylist。
        TimeStamp = System.currentTimeMillis()/1000;
        indoorDBS.add(new IndoorDB());//必须要初始化，否则没有东西无法写入数据，无法写入数据库。
        indoorDBS.add(new IndoorDB());//五句，使用了5个元素。
        indoorDBS.add(new IndoorDB());
        indoorDBS.add(new IndoorDB());
        indoorDBS.add(new IndoorDB());
        indoorDBS.get(0).setRoomNameId(1);
        indoorDBS.get(1).setRoomNameId(2);
        indoorDBS.get(2).setRoomNameId(3);
        indoorDBS.get(3).setRoomNameId(4);
        indoorDBS.get(4).setRoomNameId(5);
//        indoorDBS.get(0).setDehumidityStatus(false);
//        indoorDBS.get(1).setDehumidityStatus(false);
//        indoorDBS.get(2).setDehumidityStatus(false);
//        indoorDBS.get(3).setDehumidityStatus(false);
//        indoorDBS.get(4).setDehumidityStatus(false);

        //获取数据库实例
        MyIndoorsDatabase myIndoorsDatabase = MyIndoorsDatabase.getDatabase(context.getApplicationContext());
        //获取操作数据库的办法实例
        indoorDao = myIndoorsDatabase.getRoomDao();
//        allBasicInfo = indoorDao.loadAllBasicInfo();  //相关于Dao里面的一个有问题的方法。好像不能用。
        allBasicInfoLive = indoorDao.loadAllBasicInfoLive();
        allLatestIndoorDBsLive = indoorDao.loadLatestIndoorDBsLive();

        serviceConnection = new MyServiceConnection();//新建连接服务的实例
        serviceConnection.setIGetMessageCallBack(MyRepository.this);//把本活动传入
        Intent intent = new Intent(context, MQTTService.class); //bing Service 是需要Intent的。
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);//绑定服务MQTTservice
    }

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
                                //一般查询系统会自动安排在非主线程，不需要自己写。其他的需要自己写非主线程。
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
    //TODO 这个太长了，能不能单独做一个类（or 文件）
    @Override
    public void setMessage(final String message) {
        //TODO 活太多了，处理不了，不能有太复杂的逻辑。主要是不能在UI线程，应该开辟一个线程池来处理。
        //use mqtt message here.
        //TODO 要订阅 86518/JYCFGC/6-2-3401/RoomX 的八个 topic，接受 avg_temp, avg_humidity 数据，写入数据库。

//        indoorDB.setTimeStamp(System.currentTimeMillis() / 1000);
//        indoorDB.setSettingTemperature(20);
//        indoorDB.setSettingHumidity(50);
//        indoorDB.setFanStatus(0);  //stop 0, low 1, middium 2, high 3, auto 4.
//        indoorDB.setFloorValveOpen(true);
//        indoorDB.setCoilValveOpen(true);
//        indoorDB.setDehumidityStatus(false);
//        indoorDB.setRoomStatus(0);  //stop 0, manual 1, auto 2.

        new Thread(new Runnable() {
            @Override
            public void run() {
                //回传温度
                //TODO 这个即时传来的温度和湿度最好去显示数据。写入数据库的最好使用平均的温湿度
                if (message.contains("C1")) {//第一个房间的温度
                    indoorDBS.get(0).setTimeStamp(System.currentTimeMillis()/1000);
                    indoorDBS.get(0).setCurrentTemperature(
                            Float.parseFloat(message.substring(0, message.indexOf(".") + 2)));
                }
                if (message.contains("C2")) {
                    indoorDBS.get(1).setTimeStamp(System.currentTimeMillis()/1000);
                    indoorDBS.get(1).setCurrentTemperature(
                            Float.parseFloat(message.substring(0, message.indexOf(".") + 2)));
                }
                if (message.contains("C3")) {
                    indoorDBS.get(2).setTimeStamp(System.currentTimeMillis()/1000);
                    indoorDBS.get(2).setCurrentTemperature(
                            Float.parseFloat(message.substring(0, message.indexOf(".") + 2)));
                }
                if (message.contains("C4")) {
                    indoorDBS.get(3).setTimeStamp(System.currentTimeMillis()/1000);
                    indoorDBS.get(3).setCurrentTemperature(
                            Float.parseFloat(message.substring(0, message.indexOf(".") + 2)));
                }
                if (message.contains("C5")) {
                    indoorDBS.get(4).setTimeStamp(System.currentTimeMillis()/1000);
                    indoorDBS.get(4).setCurrentTemperature(
                            Float.parseFloat(message.substring(0, message.indexOf(".") + 2)));
                }
                //回传湿度
                if (message.contains("RH1")) {
//                    indoorDBS.get(0).setTimeStamp(System.currentTimeMillis()/1000);//温度湿度同时来的，不需要重复时间戳
                    indoorDBS.get(0).setCurrentHumidity(
                            Float.parseFloat(message.substring(0, message.indexOf(".") + 2)));
                }
                if (message.contains("RH2")) {
                    indoorDBS.get(1).setCurrentHumidity(
                            Float.parseFloat(message.substring(0, message.indexOf(".") + 2)));
                }
                if (message.contains("RH3")) {
                    indoorDBS.get(2).setCurrentHumidity(
                            Float.parseFloat(message.substring(0, message.indexOf(".") + 2)));
                }
                if (message.contains("RH4")) {
                indoorDBS.get(3).setCurrentHumidity(
                        Float.parseFloat(message.substring(0, message.indexOf(".") + 2)));
                }
                if (message.contains("RH5")) {
                    indoorDBS.get(4).setCurrentHumidity(
                            Float.parseFloat(message.substring(0, message.indexOf(".") + 2)));
                }
                //回传风机状态
                //第1个房间
                if (message.contains("stopRoom" + 1 + "FP")) {
                    indoorDBS.get(0).setFanStatus(0);
                }
                if (message.contains("lowRoom" + 1 + "FP")) {
                    indoorDBS.get(0).setFanStatus(1);
                }
                if (message.contains("middleRoom" + 1 + "FP")) {
                    indoorDBS.get(0).setFanStatus(2);
                }
                if (message.contains("highRoom" + 1 + "FP")) {
                    indoorDBS.get(0).setFanStatus(3);
                }
                if (message.contains("autoFanspeed???" + 1 + "FP")) { //TODO 此处待定回传的信息
                    indoorDBS.get(0).setFanStatus(4); //自动风的状态
                }
                //第2个房间
                if (message.contains("stopRoom" + 2 + "FP")) {
                    indoorDBS.get(1).setFanStatus(0);
                }
                if (message.contains("lowRoom" + 2 + "FP")) {
                    indoorDBS.get(1).setFanStatus(1);
                }
                if (message.contains("middleRoom" + 2 + "FP")) {
                    indoorDBS.get(1).setFanStatus(2);
                }
                if (message.contains("highRoom" + 2 + "FP")) {
                    indoorDBS.get(1).setFanStatus(3);
                }
                if (message.contains("autoFanspeed???" + 2 + "FP")) { //TODO 此处待定回传的信息
                    indoorDBS.get(1).setFanStatus(4); //自动风的状态
                }
                //第3个房间
                if (message.contains("stopRoom" + 3 + "FP")) {
                    indoorDBS.get(2).setFanStatus(0);
                }
                if (message.contains("lowRoom" + 3 + "FP")) {
                    indoorDBS.get(2).setFanStatus(1);
                }
                if (message.contains("middleRoom" + 3 + "FP")) {
                    indoorDBS.get(2).setFanStatus(2);
                }
                if (message.contains("highRoom" + 3 + "FP")) {
                    indoorDBS.get(2).setFanStatus(3);
                }
                if (message.contains("autoFanspeed???" + 3 + "FP")) { //TODO 此处待定回传的信息
                    indoorDBS.get(2).setFanStatus(4); //自动风的状态
                }
                //第4个房间
                if (message.contains("stopRoom" + 4 + "FP")) {
                    indoorDBS.get(3).setFanStatus(0);
                }
                if (message.contains("lowRoom" + 4 + "FP")) {
                    indoorDBS.get(3).setFanStatus(1);
                }
                if (message.contains("middleRoom" + 4 + "FP")) {
                    indoorDBS.get(3).setFanStatus(2);
                }
                if (message.contains("highRoom" + 4 + "FP")) {
                    indoorDBS.get(3).setFanStatus(3);
                }
                if (message.contains("autoFanspeed???" + 4 + "FP")) { //TODO 此处待定回传的信息
                    indoorDBS.get(3).setFanStatus(4); //自动风的状态
                }
                //第5个房间
                if (message.contains("stopRoom" + 5 + "FP")) {
                    indoorDBS.get(4).setFanStatus(0);
                }
                if (message.contains("lowRoom" + 5 + "FP")) {
                    indoorDBS.get(4).setFanStatus(1);
                }
                if (message.contains("middleRoom" + 5 + "FP")) {
                    indoorDBS.get(4).setFanStatus(2);
                }
                if (message.contains("highRoom" + 5 + "FP")) {
                    indoorDBS.get(4).setFanStatus(3);
                }
                if (message.contains("autoFanspeed???" + 5 + "FP")) { //TODO 此处待定回传的信息
                    indoorDBS.get(4).setFanStatus(4); //自动风的状态
                }
                //地暖模块回传信息
                //第1个房间
                if (message.contains("valveonRoom" + 1 + "floor")) {
                    indoorDBS.get(0).setFloorValveOpen(true);
                }
                if (message.contains("valveoffRoom" + 1 + "floor")) {
                    indoorDBS.get(0).setFloorValveOpen(false);
                }
                //第2个房间
                if (message.contains("valveonRoom" + 2 + "floor")) {
                    indoorDBS.get(1).setFloorValveOpen(true);
                }
                if (message.contains("valveoffRoom" + 2 + "floor")) {
                    indoorDBS.get(1).setFloorValveOpen(false);
                }
                //第3个房间
                if (message.contains("valveonRoom" + 3 + "floor")) {
                    indoorDBS.get(2).setFloorValveOpen(true);
                }
                if (message.contains("valveoffRoom" + 3 + "floor")) {
                    indoorDBS.get(2).setFloorValveOpen(false);
                }
                //第4个房间
                if (message.contains("valveonRoom" + 4 + "floor")) {
                    indoorDBS.get(3).setFloorValveOpen(true);
                }
                if (message.contains("valveoffRoom" + 4 + "floor")) {
                    indoorDBS.get(3).setFloorValveOpen(false);
                }
                //第5个房间
                if (message.contains("valveonRoom" + 5 + "floor")) {
                    indoorDBS.get(4).setFloorValveOpen(true);
                }
                if (message.contains("valveoffRoom" + 5 + "floor")) {
                    indoorDBS.get(4).setFloorValveOpen(false);
                }
                //两通阀回传信息
                //第1个房间
                if (message.contains("valveonRoom" + 1 + "FP")) {
                    indoorDBS.get(0).setCoilValveOpen(true);
                }
                if (message.contains("valveoffRoom" + 1 + "FP")) {
                    indoorDBS.get(0).setCoilValveOpen(false);
                }
                //第2个房间
                if (message.contains("valveonRoom" + 2 + "FP")) {
                    indoorDBS.get(1).setCoilValveOpen(true);
                }
                if (message.contains("valveoffRoom" + 2 + "FP")) {
                    indoorDBS.get(1).setCoilValveOpen(false);
                }
                //第3个房间
                if (message.contains("valveonRoom" + 3 + "FP")) {
                    indoorDBS.get(2).setCoilValveOpen(true);
                }
                if (message.contains("valveoffRoom" + 3 + "FP")) {
                    indoorDBS.get(2).setCoilValveOpen(false);
                }
                //第4个房间
                if (message.contains("valveonRoom" + 4 + "FP")) {
                    indoorDBS.get(3).setCoilValveOpen(true);
                }
                if (message.contains("valveoffRoom" + 4 + "FP")) {
                    indoorDBS.get(3).setCoilValveOpen(false);
                }
                //第5个房间
                if (message.contains("valveonRoom" + 5 + "FP")) {
                    indoorDBS.get(4).setCoilValveOpen(true);
                }
                if (message.contains("valveoffRoom" + 5 + "FP")) {
                    indoorDBS.get(4).setCoilValveOpen(false);
                }
                //回传的除湿状态
                //第1个房间
                if (message.contains("dehumidityRoom" + 1 + "FP")) {
                    indoorDBS.get(0).setDehumidityStatus(true);
                }
                //第2个房间
                if (message.contains("dehumidityRoom" + 2 + "FP")) {
                    indoorDBS.get(1).setDehumidityStatus(true);
                }
                //第3个房间
                if (message.contains("dehumidityRoom" + 3 + "FP")) {
                    indoorDBS.get(2).setDehumidityStatus(true);
                }
                //第4个房间
                if (message.contains("dehumidityRoom" + 4 + "FP")) {
                    indoorDBS.get(3).setDehumidityStatus(true);
                }
                //第5个房间
                if (message.contains("dehumidityRoom" + 5 + "FP")) {
                    indoorDBS.get(4).setDehumidityStatus(true);
                }
                //风机模块回传的房间状态信息
                //第1个房间
                if (message.contains("turn-offRoom" + 1 + "FP")) {
                    indoorDBS.get(0).setRoomStatus(0);
                }
                if (message.contains("manualRoom" + 1 + "FP")) {
                    indoorDBS.get(0).setRoomStatus(1);
                }
                if (message.contains("automationRoom" + 1 + "FP")) {
                    indoorDBS.get(0).setRoomStatus(2);
                }
                //第2个房间
                if (message.contains("turn-offRoom" + 2 + "FP")) {
                    indoorDBS.get(1).setRoomStatus(0);
                }
                if (message.contains("manualRoom" + 2 + "FP")) {
                    indoorDBS.get(1).setRoomStatus(1);
                }
                if (message.contains("automationRoom" + 2 + "FP")) {
                    indoorDBS.get(1).setRoomStatus(2);
                }
                //第3个房间
                if (message.contains("turn-offRoom" + 3 + "FP")) {
                    indoorDBS.get(2).setRoomStatus(0);
                }
                if (message.contains("manualRoom" + 3 + "FP")) {
                    indoorDBS.get(2).setRoomStatus(1);
                }
                if (message.contains("automationRoom" + 3 + "FP")) {
                    indoorDBS.get(2).setRoomStatus(2);
                }
                //第4个房间
                if (message.contains("turn-offRoom" + 4 + "FP")) {
                    indoorDBS.get(3).setRoomStatus(0);
                }
                if (message.contains("manualRoom" + 4 + "FP")) {
                    indoorDBS.get(3).setRoomStatus(1);
                }
                if (message.contains("automationRoom" + 4 + "FP")) {
                    indoorDBS.get(3).setRoomStatus(2);
                }
                //第5个房间
                if (message.contains("turnoff-Room" + 5 + "FP")) {
                    indoorDBS.get(4).setRoomStatus(0);
                }
                if (message.contains("manualRoom" + 5 + "FP")) {
                    indoorDBS.get(4).setRoomStatus(1);
                }
                if (message.contains("automationRoom" + 5 + "FP")) {
                    indoorDBS.get(4).setRoomStatus(2);
                }

                //TODO 如果写入的是平均的温湿度，是不是考虑其他的判断方式，不仅仅是一分钟的时间？？
                if (System.currentTimeMillis()/1000 - TimeStamp >= 60) {
                    for (int i=0;i<5;i++) {
                        insertIndoorDB(indoorDBS.get(i));
                    }
                    TimeStamp = System.currentTimeMillis()/1000;
                }
                Log.d("recivedMQTT", message);

            }
        }).start();

//            //TODO 从模块回传设置温度和设置湿度
//            IndoorDBs.get(0).setSettingTemperature(20);
//            IndoorDBs.get(0).setSettingHumidity(30);
//
//            //从模块回传的风机状态
//            if (message.contains("lowRoom" + 1 + "FP"))
//                IndoorDBs.get(0).setFanStatus(1);
//            if (message.contains("middleRoom" + 1 + "FP"))
//                IndoorDBs.get(0).setFanStatus(2);
//            if (message.contains("highRoom" + 1 + "FP"))
//                IndoorDBs.get(0).setFanStatus(4);
//            if (message.contains("stopRoom" + 1 + "FP"))
//                IndoorDBs.get(0).setFanStatus(0);
//            //TODO add fanstatus auto
//            //if (message.contains("stopRoom" + i + "FP")) IndoorDBs.get(i-1).setFanStatus(0);
//
//            //地暖模块回传的信息
//            if (message.contains("valveonRoom" + 1 + "floor"))
//                IndoorDBs.get(0).setFloorValveOpen(true);
//            if (message.contains("valveoffRoom" + 1 + "floor"))
//                IndoorDBs.get(0).setFloorValveOpen(false);
//            //两通阀回传的信息
//            if (message.contains("valveonRoom" + 1 + "FP"))
//                IndoorDBs.get(0).setCoilValveOpen(true);
//            if (message.contains("valveoffRoom" + 1 + "FP"))
//                IndoorDBs.get(0).setCoilValveOpen(false);
//            //回传的除湿状态
//            if (message.contains("dehumidityRoom" + 1 + "FP"))
//                writeToIndoorDBsDBIndoorDBs.get(0).setDehumidityStatus(true);
//            //风机模块回传的房间状态信息
//            if (message.contains("turnoffRoom" + 1 + "FP"))
//                IndoorDBs.get(0).setRoomStatus(0);
//            if (message.contains("manualRoom" + 1 + "FP"))
//                IndoorDBs.get(0).setRoomStatus(1);
//            if (message.contains("automationRoom" + 1 + "FP"))
//                IndoorDBs.get(0).setRoomStatus(2);

        mqttService = serviceConnection.getMqttService(); //服务连接实例 的 获得服务的方法
//        mqttService.toCreateNotification(message); //服务的发布消息的方法

    }

}
