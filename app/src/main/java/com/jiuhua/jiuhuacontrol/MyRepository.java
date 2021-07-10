package com.jiuhua.jiuhuacontrol;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.jiuhua.jiuhuacontrol.database.BasicInfoDB;
import com.jiuhua.jiuhuacontrol.database.IndoorDB;
import com.jiuhua.jiuhuacontrol.database.IndoorDao;
import com.jiuhua.jiuhuacontrol.database.MyIndoorsDatabase;
import com.jiuhua.jiuhuacontrol.database.PeriodDB;
import com.jiuhua.mqttsample.IGetMessageCallBack;
import com.jiuhua.mqttsample.MQTTService;
import com.jiuhua.mqttsample.MyServiceConnection;

import java.util.Date;
import java.util.List;

public class MyRepository implements IGetMessageCallBack {
    private final String TAG = getClass().getName();

    long TimeStamp;
    LiveData<List<BasicInfoDB>> allBasicInfoLive;
    LiveData<List<IndoorDB>> allLatestIndoorDBsLive;
    LiveData<List<PeriodDB>> allLatestPeriodDBsLive;
    private IndoorDao indoorDao;
    Gson gson = new Gson();

    // MQTT 需要的参数
    private MyServiceConnection serviceConnection;//连接实例
    private MQTTService mqttService;//服务实例

    public MyRepository(Context context) {

        //获取数据库实例
        MyIndoorsDatabase myIndoorsDatabase = MyIndoorsDatabase.getDatabase(context.getApplicationContext());
        //获取操作数据库的办法实例
        indoorDao = myIndoorsDatabase.getRoomDao();
        //获取数据库里的数据
//        allBasicInfo = indoorDao.loadAllBasicInfo();  //相关于Dao里面的一个有问题的方法。好像不能用。
        allBasicInfoLive = indoorDao.loadAllBasicInfoLive();
        allLatestPeriodDBsLive = indoorDao.loadLatestPeriodDBsLive();

    }

//    //发送指令 command to device TODO:发送命令之前重启mqtt服务！！
//    public void commandToDevice(CommandESP commandESP) {
//        int roomID = commandESP.getRoomId();
//        String jsonCommandESP = gson.toJson(commandESP);
//
//        mqttService = serviceConnection.getMqttService();  //这句可有可无，有就用小写的，没有就用大写的MQTTService
////        mqttService.myPublishToDevice(roomID, jsonCommandESP, 1, true);
//        MQTTService.myPublishToDevice(roomID, jsonCommandESP, 1, false);//这里的retained指令如果为true，会不断发送，摧毁模块。（浪费一天时间）
//        Log.d("jsonCommandToDevice", jsonCommandESP);
//    }

    //发送指令 command to device TODO:发送命令之前重启mqtt服务！！
    public void commandToDevice(int roomid, String msg) {
        MQTTService.myPublishToDevice(roomid, msg, 1, false);//这里的retained指令如果为true，会不断发送，摧毁模块。（浪费一天时间）
        Log.d("jsonCommandToDevice", msg);
    }

        //period to device  send currentlyPeriodDB. 发送 currentlyPeriodDB 。
    public void periodToDevice(PeriodDB periodDB) {
        new Thread(new Runnable() {//FIXME: 线程方法不成功！！
            @Override
            public void run() {
                int roomid = periodDB.getRoomId();

                for (int wd = 0; wd < 7; wd++) {
                    CommandPeriod commandPeriod = new CommandPeriod();
                    commandPeriod.setRoomId(roomid);
                    int[][] temperatureArray = new int[15][3];
                    int k = 0;
                    commandPeriod.setWeekday(wd);
                    for (int i = 0; i < periodDB.getOneRoomWeeklyPeriod().size(); i++) {
                        if (wd == periodDB.getOneRoomWeeklyPeriod().get(i).getWeekday()) {
                            temperatureArray[k][0] = periodDB.getOneRoomWeeklyPeriod().get(i).getStartMinuteStamp();
                            temperatureArray[k][1] = periodDB.getOneRoomWeeklyPeriod().get(i).getEndMinuteStamp();
                            temperatureArray[k][2] = periodDB.getOneRoomWeeklyPeriod().get(i).getTempreature();
                            k++;
                        }
                    }
                    commandPeriod.setPeriod(temperatureArray);
                    String s = gson.toJson(commandPeriod);
//                    mqttService = serviceConnection.getMqttService();
                    MQTTService.myPublishToDevice(roomid, s, 1, false);//这里的retained指令如果为true，会不断发送，摧毁模块。（一天）
                    Log.d("periodToDevice", s);
                    try {
                        Thread.sleep(500);//延迟发送，太快模块接受不了。
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();//FIXME：***少写了“.start()”，基本概念不清害死人啊！！***


    }

    /**  ******实现 Dao 的所有方法*********************************   */
    /**
     * basic room information
     */
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

    //获取所有的基本房间信息，LiveData形式。
    public LiveData<List<BasicInfoDB>> getAllBasicInfoLive() {
        return allBasicInfoLive;
    }

    //获取普通房间的名字
    public String loadRoomName(int roomid) {
        return indoorDao.loadRoomName(roomid);
    }

    /**
     * room information not include period
     */
    //插入普通房间的状态信息
    public void insertIndoorDB(IndoorDB... indoorDBS) {
        new InsertIndoorDBAsyncTask(indoorDao).execute(indoorDBS);
    }

    //delete one indoorDB is not necessary

    //删除所有普通房间的信息
    public void deleteAllIndoorDB() {
        new DeleteAllIndoorDBAsyncTask(indoorDao).execute();
    }

    //获取普通房间的全部信息
    public LiveData<List<IndoorDB>> getAllLatestIndoorDBsLive(int devicetypeId) {
        //获取房间的最新信息指定了参数设备类型，继续包装下去，让调用者决定设备的类型。
        allLatestIndoorDBsLive = indoorDao.loadLatestIndoorDBsLive(devicetypeId);
        //一般查询系统会自动安排在非主线程，不需要自己写。其他的需要自己写非主线程。？？right？？
        return allLatestIndoorDBsLive;
    }

    /**
     * the period information of room
     */
    //插入某个房间一周运行的周期
    public void insertPeriodDB(PeriodDB... periodDBS) {
        new InsertPeriodDBAsyncTask(indoorDao).execute(periodDBS);
        //FIXME: 这里没有写入参数periodDBs ，导致数据库没有写入，耽误了两三天时间。没有报错误。
        //FIXME： 再放弃的时候，才发现。应该是基本的概念不清楚，只知道照抄代码，抄的不仔细。
        //FIXME： 如果概念清楚，会发现灰色的 periodDBs 没有被使用，肯定不能写入数据库。
        //FIXME： 异步执行的函数也不是很了解，不知道 execute 是要执行什么的！
        //FIXME： 难以表述的痛苦，为什么在这里耽误了这么长的时间，甚至怀疑了room的功能。
    }

    //不用删除某个房间一周运行的周期

    //获取所有房间的周期
    public LiveData<List<PeriodDB>> getAllLatestPeriodDBsLive() {
        return allLatestPeriodDBsLive;
    }


    /**
     * 内部类，辅助线程上执行 Dao 的方法。    还有一种线程池的方法（Google文档上的）
     * 读取消耗时间少，没有专门开线程。read data from SQLite has no new thread.
     */
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

    static class InsertPeriodDBAsyncTask extends AsyncTask<PeriodDB, Void, Void> {
        private IndoorDao indoorDao;

        InsertPeriodDBAsyncTask(IndoorDao indoorDao) {
            this.indoorDao = indoorDao;
        }

        @Override
        protected Void doInBackground(PeriodDB... periodDBS) {
            indoorDao.insertPeriodDB(periodDBS);
            return null;
        }
    }

    static class DeletePeriodDBAsyncTask extends AsyncTask<PeriodDB, Void, Void> {
        private IndoorDao indoorDao;

        DeletePeriodDBAsyncTask(IndoorDao indoorDao) {
            this.indoorDao = indoorDao;
        }

        @Override
        protected Void doInBackground(PeriodDB... periodDBS) {
            indoorDao.deletePeriodDB(periodDBS);
            return null;
        }
    }


    /** FIXME: 此方法废弃，修改为请求网络数据库数据写入本地数据库！！ */
    @Override
    public void setMessage(final String message) {
        //解析json和写入数据库，不能在UI线程，是否应该开辟一个线程池来处理??。
        //use mqtt message here.
        //回传手机的信息都在 86518/JYCFGC/6-2-3401/phone

        new Thread(new Runnable() {
            IndoorDB indoorDB;

            @Override
            public void run() {
                //接受执行模块的json字符串 转换为indoorDB的实例，写入数据库。！！
                //先判断一下是不是json数据
                if (message.startsWith("{") && message.endsWith("}")) {
                    Log.d("recivedMQTT", message);
                    indoorDB = gson.fromJson(message, IndoorDB.class);
                    if (indoorDB != null) {
                        indoorDB.setTimeStamp(new Date().getTime() / 1000);
                        insertIndoorDB(indoorDB);
                        Log.d("insert in sqlite", gson.toJson(indoorDB));
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
