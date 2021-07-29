package com.jiuhua.jiuhuacontrol.repository;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.jiuhua.jiuhuacontrol.CommandFromPhone;
import com.jiuhua.jiuhuacontrol.CommandPeriod;
import com.jiuhua.jiuhuacontrol.database.BasicInfoDB;
import com.jiuhua.jiuhuacontrol.database.IndoorDB;
import com.jiuhua.jiuhuacontrol.database.IndoorDao;
import com.jiuhua.jiuhuacontrol.database.MyIndoorsDatabase;
import com.jiuhua.jiuhuacontrol.database.PeriodDB;
import com.jiuhua.mqttsample.IGetMessageCallBack;
import com.jiuhua.mqttsample.MQTTService;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

//TODO: *** 是不是单例，应该采用单例模式！！***
public class MyRepository {
    private final String TAG = getClass().getName();

    long TimeStamp;
    LiveData<List<BasicInfoDB>> allBasicInfoLive;
    LiveData<List<IndoorDB>> allLatestIndoorDBsLive;
    LiveData<List<PeriodDB>> allLatestPeriodDBsLive;
    private IndoorDao indoorDao;
    Gson gson = new Gson();

    private Retrofit retrofit;
    private CloudServer cloudServer;


    public MyRepository(Context context) {

        //获取数据库实例
        MyIndoorsDatabase myIndoorsDatabase = MyIndoorsDatabase.getDatabase(context.getApplicationContext());
        //获取操作数据库的办法实例
        indoorDao = myIndoorsDatabase.getRoomDao();
        //获取数据库里的数据
//        allBasicInfo = indoorDao.loadAllBasicInfo();  //相关于Dao里面的一个有问题的方法。好像不能用。
        allBasicInfoLive = indoorDao.loadAllBasicInfoLive();
        allLatestPeriodDBsLive = indoorDao.loadLatestPeriodDBsLive();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://175.24.33.56:6041/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        cloudServer = retrofit.create(CloudServer.class);

        requestTDengineData();

    }

    public void requestTDengineData() {
        String credentials = "zz" + ":" + "700802";
        final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        String sql = "select  * from homedevice.sensors where location = '86518/yuxiuhuayuan/12-1-101/Room10' and ts > now - 1h";
//        String sql = "select  * from homedevice.sensors where location = '86518/yuxiuhuayuan/12-1-101/Room1' and ts > now - 1h";
//        String sql2 = "select  * from homedevice.sensors where location = '86518/yuxiuhuayuan/12-1-101/Room2' and ts > now - 1h";
//        String sql3 = "select  * from homedevice.sensors where location = '86518/yuxiuhuayuan/12-1-101/Room3' and ts > now - 1h";
//        String sql4 = "select  * from homedevice.sensors where location = '86518/yuxiuhuayuan/12-1-101/Room4' and ts > now - 1h";
//        String sql5 = "select  * from homedevice.sensors where location = '86518/yuxiuhuayuan/12-1-101/Room5' and ts > now - 1h";
//        String sql6 = "select  * from homedevice.sensors where location = '86518/yuxiuhuayuan/12-1-101/Room6' and ts > now - 1h";
//        String sql7 = "select  * from homedevice.sensors where location = '86518/yuxiuhuayuan/12-1-101/Room7' and ts > now - 1h";
//        String sql8 = "select  * from homedevice.sensors where location = '86518/yuxiuhuayuan/12-1-101/Room8' and ts > now - 1h";
//        String sql9 = "select  * from homedevice.sensors where location = '86518/yuxiuhuayuan/12-1-101/Room9' and ts > now - 1h";
//        String sql10 = "select  * from homedevice.sensors where location = '86518/yuxiuhuayuan/12-1-101/Room10' and ts > now - 1h";
//        String sql11 = "select  * from homedevice.sensors where location = '86518/yuxiuhuayuan/12-1-101/Room11' and ts > now - 1h";
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), sql);

        Call<TDReception> call = cloudServer.respoFormTDengine(basic, body);

        call.enqueue(new Callback<TDReception>() {
            @Override
            public void onResponse(Call<TDReception> call, Response<TDReception> response) {
                try {//回来的数据不稳定，保护一下。
                    Log.d("TAG", "onResponse: " + response.body().toString());

                    saveMessageToSQlite(response.body());

                    response.body().show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<TDReception> call, Throwable t) {
                System.out.println("连接失败！");

            }
        });
    }

    public void commandToModule() {
        //接口需要的参数。
        CommandFromPhone commandFromPhone = new CommandFromPhone();
        commandFromPhone.setTopic("TopicTestTest2");
        commandFromPhone.setQos(1);
        commandFromPhone.setMessage("I am real from phone!2");
        commandFromPhone.setRetained(false);

        //定义去联网的call 使用实例的哪一个方法&传入需要的参数。
        Call<String> call = cloudServer.reposForCommand("http://175.24.33.56:8080/command", commandFromPhone);

        //异步执行该call，如果是同步会使用excute方法，enqueue排队的意思。
        call.enqueue(new Callback<String>() {
            //** retrofit的封装，下面两个override都回到了主线程 UI线程来执行。**
            @Override
            public void onResponse(retrofit2.Call<String> call, retrofit2.Response<String> response) {
                System.out.println("success :) ok!\n" + response.body());
            }

            @Override
            public void onFailure(retrofit2.Call<String> call, Throwable t) {
                Log.d(this.getClass().getSimpleName(), "onFailure: " + t.toString());
            }

        });
    }

//    //发送指令 command to device
//    public void commandToDevice(CommandESP commandESP) {
//        int roomID = commandESP.getRoomId();
//        String jsonCommandESP = gson.toJson(commandESP);

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


    /**
     * 请求来的网络数据库数据写入本地数据库！！
     */
    public void saveMessageToSQlite(TDReception tdReception) {
        //转化Retrofit的接收体为IndoorDB的类型和写入数据库，不能在UI线程，是否应该开辟一个线程池来处理??。

        new Thread(new Runnable() {

            IndoorDB indoorDB;

            @Override
            public void run() {
                indoorDB = new IndoorDB();
                Log.d("转化TDReception", tdReception.toString());
                //TODO 获取时间戳，并转换为unix时间
                indoorDB.setRoomId(Integer.valueOf(tdReception.getData()[0][6]));//roomid 本机指令 不来自网络
                indoorDB.setDeviceType(Integer.valueOf(tdReception.getData()[0][7]));
                indoorDB.setTimeStamp(Long.parseLong(tdReception.getData()[0][0])/1000);
                indoorDB.setCurrentTemperature(Integer.valueOf(tdReception.getData()[0][1]));
                indoorDB.setCurrentHumidity(Integer.valueOf(tdReception.getData()[0][2]));
                if (indoorDB != null) {
                    insertIndoorDB(indoorDB);//TODO 这里会不会效率太低，应该组装成list再插入？？
                    Log.d("insert in sqlite", gson.toJson(indoorDB));
                }

            }
        }).start();

    }

}
