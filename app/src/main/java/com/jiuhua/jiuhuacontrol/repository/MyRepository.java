package com.jiuhua.jiuhuacontrol.repository;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.jiuhua.jiuhuacontrol.CommandFromPhone;
import com.jiuhua.jiuhuacontrol.CommandPeriod;
import com.jiuhua.jiuhuacontrol.database.BasicInfoSheet;
import com.jiuhua.jiuhuacontrol.database.MyDao;
import com.jiuhua.jiuhuacontrol.database.MyDatabase;
import com.jiuhua.jiuhuacontrol.database.SensorSheet;
import com.jiuhua.jiuhuacontrol.database.PeriodSheet;

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
    private static MyRepository INSTANCE;

    long TimeStamp;
    LiveData<List<BasicInfoSheet>> allBasicInfoLive;
    LiveData<List<SensorSheet>> allLatestIndoorSheetsLive;
    LiveData<List<PeriodSheet>> allLatestPeriodSheetsLive;
    private MyDao myDao;
    Gson gson = new Gson();

    private Retrofit retrofit;
    private CloudServer cloudServer;


    private MyRepository(Context context) {
        //获取数据库实例
        MyDatabase myDatabase = MyDatabase.getDatabase(context.getApplicationContext());
        //获取操作数据库的办法实例
        myDao = myDatabase.getRoomDao();
        //获取数据库里的数据
//        allBasicInfo = myDao.loadAllBasicInfo();  //相关于Dao里面的一个有问题的方法。好像不能用。
        allBasicInfoLive = myDao.loadAllBasicInfoLive();
        allLatestPeriodSheetsLive = myDao.loadLatestPeriodSheetsLive();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://175.24.33.56:6041/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        cloudServer = retrofit.create(CloudServer.class);

    }

    public static synchronized MyRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MyRepository(context);
        }
        return INSTANCE;
    }

    //TODO 添加 sql 参数！！
    public void requestTDengineData() {
        String credentials = "zz" + ":" + "700802";
        final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        String sql = "select  * from homedevice.sensors where location = '86518/yuxiuhuayuan/12-1-101/Room2' and ts > now - 1h";
//        String sql = "select  * from homedevice.sensors where location = '86518/yuxiuhuayuan/12-1-101/Room1' and ts > now - 1h";
//        String sql = "select  * from homedevice.sensors where location = '86518/yuxiuhuayuan/12-1-101/Room2' and ts > now - 1h";
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
                    saveMessageToSQlite(response.body());
                    Log.d(TAG+"Recived TDReception`s status is ", response.body().getStatus());
//                    response.body().show();//检查调试的功能
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

    //TODO 完善它！
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

    //发送指令 command to device TODO: 改成云端转发
    public void commandToDevice(int roomid, String msg) {
//        MQTTService.myPublishToDevice(roomid, msg, 1, false);//这里的retained指令如果为true，会不断发送，摧毁模块。（浪费一天时间）
        Log.d("jsonCommandToDevice", msg);
    }

    //period to device  send currentlyPeriodDB. 发送 currentlyPeriodDB 。
    //TODO 改成云端转发
    public void periodToDevice(PeriodSheet periodSheet) {
        new Thread(new Runnable() {//FIXME: 线程方法不成功！！
            @Override
            public void run() {
                int roomid = periodSheet.getRoomId();

                for (int wd = 0; wd < 7; wd++) {
                    CommandPeriod commandPeriod = new CommandPeriod();
                    commandPeriod.setRoomId(roomid);
                    int[][] temperatureArray = new int[15][3];
                    int k = 0;
                    commandPeriod.setWeekday(wd);
                    for (int i = 0; i < periodSheet.getOneRoomWeeklyPeriod().size(); i++) {
                        if (wd == periodSheet.getOneRoomWeeklyPeriod().get(i).getWeekday()) {
                            temperatureArray[k][0] = periodSheet.getOneRoomWeeklyPeriod().get(i).getStartMinuteStamp();
                            temperatureArray[k][1] = periodSheet.getOneRoomWeeklyPeriod().get(i).getEndMinuteStamp();
                            temperatureArray[k][2] = periodSheet.getOneRoomWeeklyPeriod().get(i).getTempreature();
                            k++;
                        }
                    }
                    commandPeriod.setPeriod(temperatureArray);
                    String s = gson.toJson(commandPeriod);
//                    mqttService = serviceConnection.getMqttService();
//                    MQTTService.myPublishToDevice(roomid, s, 1, false);//这里的retained指令如果为true，会不断发送，摧毁模块。（一天）
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
    public void insertBasicInfo(BasicInfoSheet... basicInfoSheets) {
        new InsertBasicInfoAsyncTask(myDao).execute(basicInfoSheets);
    }

    //修改基本房间信息
    public void updateBasicInfo(BasicInfoSheet... basicInfoSheets) {
        new UpdateBasicInfoAsyncTask(myDao).execute(basicInfoSheets);
    }

    //删除基本房间信息，依靠主键就可以
    public void deleteBasicInfo(BasicInfoSheet... basicInfoSheets) {
        new DeleteBasicInfoAsyncTask(myDao).execute(basicInfoSheets);
    }

    //删除所有的基本房间信息
    public void deleteAllBasicInfo() {
        new DeleteAllBasicInfoAsyncTask(myDao).execute();
    }

    //获取所有的基本房间信息，LiveData形式。
    public LiveData<List<BasicInfoSheet>> getAllBasicInfoLive() {
        return allBasicInfoLive;
    }

    //获取普通房间的名字
    public String loadRoomName(int roomid) {
        return myDao.loadRoomName(roomid);
    }

    /**
     * room information not include period
     */
    //插入普通房间的状态信息
    public void insertIndoorSheet(SensorSheet... sensorSheets) {  // 这个 ... 接收的是数组！！
        new InsertIndoorSheetAsyncTask(myDao).execute(sensorSheets);
    }

    //delete one indoorDB is not necessary

    //删除所有普通房间的信息
    public void deleteAllIndoorSheet() {
        new DeleteAllIndoorSheetAsyncTask(myDao).execute();
    }

    //获取普通房间的全部信息
    public LiveData<List<SensorSheet>> getAllLatestIndoorSheetsLive(int devicetypeId) {
        //获取房间的最新信息指定了参数设备类型，继续包装下去，让调用者决定设备的类型。
        allLatestIndoorSheetsLive = myDao.loadLatestSensorSheetsLive(devicetypeId);
        //一般查询系统会自动安排在非主线程，不需要自己写。其他的需要自己写非主线程。？？right？？
        return allLatestIndoorSheetsLive;
    }

    /**
     * the period information of room
     */
    //插入某个房间一周运行的周期
    public void insertPeriodSheet(PeriodSheet... periodSheets) {//这里的参数需要使用数组，或者单个，多个
        new InsertPeriodSheetAsyncTask(myDao).execute(periodSheets);
        //FIXME: 这里没有写入参数periodDBs ，导致数据库没有写入，耽误了两三天时间。没有报错误。
        //FIXME： 再放弃的时候，才发现。应该是基本的概念不清楚，只知道照抄代码，抄的不仔细。
        //FIXME： 如果概念清楚，会发现灰色的 periodDBs 没有被使用，肯定不能写入数据库。
        //FIXME： 异步执行的函数也不是很了解，不知道 execute 是要执行什么的！
        //FIXME： 难以表述的痛苦，为什么在这里耽误了这么长的时间，甚至怀疑了room的功能。
    }

    //不用删除某个房间一周运行的周期

    //获取所有房间的周期
    public LiveData<List<PeriodSheet>> getAllLatestPeriodSheetsLive() {
        return allLatestPeriodSheetsLive;
    }


    /**
     * 内部类，辅助线程上执行 Dao 的方法。    还有一种线程池的方法（Google文档上的）
     * 读取消耗时间少，没有专门开线程。read data from SQLite has no new thread.
     */
    static class InsertBasicInfoAsyncTask extends AsyncTask<BasicInfoSheet, Void, Void> {
        private MyDao myDao;   //独立的线程需要独立的 Dao

        InsertBasicInfoAsyncTask(MyDao myDao) {
            this.myDao = myDao;
        }

        @Override
        protected Void doInBackground(BasicInfoSheet... basicInfoSheets) {
            myDao.insertBasicInfoSheet(basicInfoSheets);
            return null;
        }
    }

    static class UpdateBasicInfoAsyncTask extends AsyncTask<BasicInfoSheet, Void, Void> {
        private MyDao myDao;

        UpdateBasicInfoAsyncTask(MyDao myDao) {
            this.myDao = myDao;
        }

        @Override
        protected Void doInBackground(BasicInfoSheet... basicInfoSheets) {
            myDao.updateBasicInfoSheet(basicInfoSheets);
            return null;
        }
    }

    static class DeleteBasicInfoAsyncTask extends AsyncTask<BasicInfoSheet, Void, Void> {
        private MyDao myDao;

        DeleteBasicInfoAsyncTask(MyDao myDao) {
            this.myDao = myDao;
        }

        @Override
        protected Void doInBackground(BasicInfoSheet... basicInfoSheets) {
            myDao.deleteBasicInfoSheet(basicInfoSheets);
            return null;
        }
    }

    static class DeleteAllBasicInfoAsyncTask extends AsyncTask<Void, Void, Void> {
        private MyDao myDao;

        DeleteAllBasicInfoAsyncTask(MyDao myDao) {
            this.myDao = myDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            myDao.deleteAllBasicInfo();
            return null;
        }
    }

    static class InsertIndoorSheetAsyncTask extends AsyncTask<SensorSheet, Void, Void> {
        private MyDao myDao;   //独立的线程需要独立的 Dao

        InsertIndoorSheetAsyncTask(MyDao myDao) {
            this.myDao = myDao;
        }

        @Override
        protected Void doInBackground(SensorSheet... sensorSheets) {
            myDao.insertSensorSheet(sensorSheets);
            return null;
        }
    }

    static class DeleteAllIndoorSheetAsyncTask extends AsyncTask<Void, Void, Void> {
        private MyDao myDao;

        DeleteAllIndoorSheetAsyncTask(MyDao myDao) {
            this.myDao = myDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            myDao.deleteAllSensorSheet();
            return null;
        }
    }

    static class InsertPeriodSheetAsyncTask extends AsyncTask<PeriodSheet, Void, Void> {
        private MyDao myDao;

        InsertPeriodSheetAsyncTask(MyDao myDao) {
            this.myDao = myDao;
        }

        @Override
        protected Void doInBackground(PeriodSheet... periodSheets) {
            myDao.insertPeriodSheet(periodSheets);
            return null;
        }
    }

    static class DeletePeriodSheetAsyncTask extends AsyncTask<PeriodSheet, Void, Void> {
        private MyDao myDao;

        DeletePeriodSheetAsyncTask(MyDao myDao) {
            this.myDao = myDao;
        }

        @Override
        protected Void doInBackground(PeriodSheet... periodSheets) {
            myDao.deletePeriodSheet(periodSheets);
            return null;
        }
    }


    /**
     * 请求来的网络数据库数据写入本地数据库！！
     */
    public void saveMessageToSQlite(TDReception tdReception) {
            Log.d("Recived TDReception`s status is ", tdReception.getStatus());
        //转化Retrofit的接收体为IndoorDB的类型和写入数据库，不能在UI线程，是否应该开辟一个线程池来处理??。
        if (tdReception.getStatus().equals("succ")) {
            int rows = Integer.valueOf(tdReception.getRows());

            //ToDO switch devicetype: sensor5/6 , fancoil, boiler/heatpump, watershed.

            SensorSheet[] sensorSheetArray = new SensorSheet[rows];

            for (int i = 0; i < rows; i++) {
                SensorSheet sensorSheet = new SensorSheet();
                for (int j = 0; j < tdReception.getColumn_meta().length; j++) {
                    switch (tdReception.getColumn_meta()[j][0]) {
                        case "ts":
                            sensorSheet.setTimeStamp(Long.parseLong(tdReception.getData()[i][j]) / 1000);
                            break;
                        case "currenttemperature":
                            sensorSheet.setCurrentTemperature(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        case "currenthumidity":
                            sensorSheet.setCurrentHumidity(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        case "roomid":
                            sensorSheet.setRoomId(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        case "devicetype":
                            sensorSheet.setDeviceType(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
//                        case "settingtemperature":
//                            sensorSheet.setSettingTemperature(Integer.valueOf(tdReception.getData()[i][j]));
//                            break;
//                        case "settinghumidity":
//                            sensorSheet.setSettingHumidity(Integer.valueOf(tdReception.getData()[i][j]));
//                            break;
                        case "adjustingtemperature":
                            sensorSheet.setAdjustingTemperature(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        case "adjustinghumidity":
                            sensorSheet.setAdjustingHumidity(Integer.valueOf(tdReception.getData()[i][j]));
                            break;
                        case "deviceid":
                            sensorSheet.setDeviceId(tdReception.getData()[i][j]);
                            break;
//                        case "coilvalve"://TODO 枚举量，注意如何转换的，传来的字符串是什么？？
//                            sensorSheet.setCoilValveOpen(Integer.valueOf(tdReception.getData()[i][j]));
//                            break;
//                        case "floorvalve"://TODO  暂时云端没有，也是枚举
//                            sensorSheet.setFloorValveOpen(Integer.valueOf(tdReception.getData()[i][j]));
//                            break;
                        //TODO 增加 boiler state， deviceID（使用原生REFUS？？的 MAC地址？？）？？
                        //TODO location 如何使用？现在是topic，如何利用数据库在topic上。

                        default:
                            break;
                    }
                }
                sensorSheetArray[i] = sensorSheet;
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (sensorSheetArray != null) {
                        insertIndoorSheet(sensorSheetArray);//这里接收的是数组！！
                        Log.d("insert in sqlite", gson.toJson(sensorSheetArray));
                    }
                }
            }).start();

        }
    }

}
