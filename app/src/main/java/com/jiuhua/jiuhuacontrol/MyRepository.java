package com.jiuhua.jiuhuacontrol;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.jiuhua.jiuhuacontrol.database.BasicInfoDB;
import com.jiuhua.jiuhuacontrol.database.IndoorDB;
import com.jiuhua.jiuhuacontrol.database.IndoorDao;
import com.jiuhua.jiuhuacontrol.database.MyIndoorsDatabase;
import com.jiuhua.mqttsample.IGetMessageCallBack;
import com.jiuhua.mqttsample.MQTTService;
import com.jiuhua.mqttsample.MyServiceConnection;

import java.util.ArrayList;
import java.util.List;

public class MyRepository implements IGetMessageCallBack {

    long TimeStamp;
    LiveData<List<BasicInfoDB>> allBasicInfoLive;
    LiveData<List<IndoorDB>> allLatestIndoorDBsLive;
    private IndoorDao indoorDao;
    Gson gson = new Gson();

    // MQTT 需要的参数
    private MyServiceConnection serviceConnection;//连接实例
    private MQTTService mqttService;//服务实例

//    private static final ThreadPoolExecutor poolexecutor = new ThreadPoolExecutor(
//            4, 4,30, TimeUnit.SECONDS, quene);

    public MyRepository(Context context) {

        //获取数据库实例
        MyIndoorsDatabase myIndoorsDatabase = MyIndoorsDatabase.getDatabase(context.getApplicationContext());
        //获取操作数据库的办法实例
        indoorDao = myIndoorsDatabase.getRoomDao();
        //获取数据库里的数据
//        allBasicInfo = indoorDao.loadAllBasicInfo();  //相关于Dao里面的一个有问题的方法。好像不能用。
        allBasicInfoLive = indoorDao.loadAllBasicInfoLive();
        allLatestIndoorDBsLive = indoorDao.loadLatestIndoorDBsLive();

        serviceConnection = new MyServiceConnection();//新建连接服务的实例
        serviceConnection.setIGetMessageCallBack(MyRepository.this);//把本活动传入
        Intent intent = new Intent(context, MQTTService.class); //bing Service 是需要Intent的。
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);//绑定服务MQTTservice
    }

    //发送指令
    public void jsonToDevice(CommandESP commandESP) {
        int roomID = commandESP.getRoomId();
        String jsonCommandESP = gson.toJson(commandESP);

        mqttService = serviceConnection.getMqttService();  //这句可有可无，有就用小写的，没有就用大写的MQTTService
//        mqttService.publish("86518/JYCFGC/6-2-3401/Room" + roomID, mqttString, 1, true);
        MQTTService.publish("86518/JYCFGC/6-2-3401/Room" + roomID, jsonCommandESP, 1, true);
        Log.d("jsonToDevice", jsonCommandESP);
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

    //TODO 接收 MQTT 数据后，依照数据实现相关方法。（直接去显示 还是只写入数据库）************************
    @Override
    public void setMessage(final String message) {
        //TODO 解析json和写入数据库，且不能在UI线程，是否应该开辟一个线程池来处理。
        //use mqtt message here.
        //回传手机的信息都在 86518/JYCFGC/6-2-3401/HandT 。

        new Thread(new Runnable() {
            IndoorDB indoorDB;
            @Override
            public void run() {
                //TODO 接受执行模块的json字符串 转换为indoorDB的实例，写入数据库。！！
                //先判断一下是不是json数据
                if (message.startsWith("{") && message.endsWith("}")) {
                    Log.d("recivedMQTT", message);
                    indoorDB = gson.fromJson(message, IndoorDB.class);
                    if (indoorDB != null) {
                        insertIndoorDB(indoorDB);
                        Log.d("IndoorDB", gson.toJson(indoorDB));
                    }
                } else {
                    Log.d("the message is not json", message);
                }

            }
        }).start();

        mqttService = serviceConnection.getMqttService(); //服务连接实例 的 获得服务的方法
//        mqttService.toCreateNotification(message); //服务的发布消息的方法

    }

}
