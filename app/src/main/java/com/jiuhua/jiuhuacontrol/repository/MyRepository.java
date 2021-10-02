package com.jiuhua.jiuhuacontrol.repository;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.jiuhua.jiuhuacontrol.CommandFromPhone;
import com.jiuhua.jiuhuacontrol.CommandPeriod;
import com.jiuhua.jiuhuacontrol.Constants;
import com.jiuhua.jiuhuacontrol.database.BasicInfoSheet;
import com.jiuhua.jiuhuacontrol.database.EngineSheet;
import com.jiuhua.jiuhuacontrol.database.FancoilSheet;
import com.jiuhua.jiuhuacontrol.database.MyDao;
import com.jiuhua.jiuhuacontrol.database.MyDatabase;
import com.jiuhua.jiuhuacontrol.database.SensorSheet;
import com.jiuhua.jiuhuacontrol.database.PeriodSheet;
import com.jiuhua.jiuhuacontrol.database.WatershedSheet;
import com.jiuhua.jiuhuacontrol.ui.HomeViewModel;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

//单例模式
public class MyRepository {
    private final String TAG = getClass().getName();
    private static MyRepository INSTANCE;

    long TimeStamp;
    LiveData<List<BasicInfoSheet>> allBasicInfoLive;
    LiveData<List<SensorSheet>> allLatestSensorSheetsLive;
    LiveData<List<FancoilSheet>> allLatestFancoilSheetsLive;
    LiveData<List<WatershedSheet>> allLatestWatershedSheetsLive;
    LiveData<List<EngineSheet>> allLatestEngineSheetsLive;
    LiveData<List<PeriodSheet>> allLatestPeriodSheetsLive;
    MyDao myDao;
    Gson gson = new Gson();

    private Retrofit retrofit;
    private CloudServer cloudServer;


    private MyRepository(Context context) {
        //获取数据库实例
        MyDatabase myDatabase = MyDatabase.getDatabase(context.getApplicationContext());
        //获取操作数据库的办法实例
        myDao = myDatabase.getRoomDao();
        //获取数据库里的数据
        //allBasicInfo = myDao.loadAllBasicInfo();  //相关于Dao里面的一个有问题的方法。好像不能用。
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

    //请求云端数据库并写入本机数据库
    public void readTDengine(String sql) {
        String credentials = "zz" + ":" + "700802";
        final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), sql);

        Call<TDReception> call = cloudServer.respoFormTDengine(basic, body);

        call.enqueue(new Callback<TDReception>() {
            @Override
            public void onResponse(Call<TDReception> call, Response<TDReception> response) {
                try {//回来的数据不稳定，保护一下。
                    //请求传感器的数据
                    if (sql.contains("homedevice.sensors")) {
                        SensorSheet[] sensorSheetArray = TDReceptionConverter.toSensorSheet(response.body());
                        if (sensorSheetArray != null) {
                            new Thread(() -> {
                                try {
                                    insertSensorSheet(sensorSheetArray);//这里接收的是数组！！
                                    Log.d("insert in sqlite", gson.toJson(sensorSheetArray));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }).start();
                        }
                    }
                    //请求风机盘管的数据
                    if (sql.contains("homedevice.fancoils")) {
                        FancoilSheet[] fancoilSheets = TDReceptionConverter.toFancoilSheet(response.body());
                        if (fancoilSheets != null) {
                            new Thread(() -> {
                                try {
                                    insertFancoilSheet(fancoilSheets);//这里接收的是数组！！
                                    Log.d("insert in sqlite", gson.toJson(fancoilSheets));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }).start();
                        }
                    }
                    //请求分水器的数据
                    if (sql.contains("homedevice.watersheds")) {
                        WatershedSheet[] watershedSheets = TDReceptionConverter.toWatershedSheet(response.body());
                        if (watershedSheets != null) {
                            new Thread(() -> {
                                try {
                                    insertWatershedSheet(watershedSheets);//这里接收的是数组！！
                                    Log.d("insert in sqlite", gson.toJson(watershedSheets));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }).start();
                        }
                    }

                    //请求周期的数据  TODO：暂时不搞
                    if (sql.contains("homedevice.periods")) {
                        PeriodSheet[] periodSheets = TDReceptionConverter.toPeriodSheet(response.body());
                        if (periodSheets != null) {
                            new Thread(() -> {
                                try {
                                    insertPeriodSheet(periodSheets);//这里接收的是数组！！
                                    Log.d("insert in sqlite", gson.toJson(periodSheets));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }).start();
                        }
                    }

                    //请求锅炉的数据
                    if (sql.contains("homedevice.boilers")) {
                        EngineSheet[] engineSheets = TDReceptionConverter.toEngineSheet(response.body());
                        if (engineSheets != null) {
                            new Thread(() -> {
                                try {
                                    insertEngineSheet(engineSheets);//这里接收的是数组！！
                                    Log.d("insert in sqlite", gson.toJson(engineSheets));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }).start();
                        }
                    }

                    //TODO: heatpump

                    response.body().show();//检查调试的功能
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

    //上传数据到云端数据库
    public void updateTDengine(String sql) {
        String credentials = "zz" + ":" + "700802";
        final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), sql);

        Call<TDReception> call = cloudServer.respoFormTDengine(basic, body);

        call.enqueue(new Callback<TDReception>() {
            @Override
            public void onResponse(Call<TDReception> call, Response<TDReception> response) {
                System.out.println("上传成功！");
            }

            @Override
            public void onFailure(Call<TDReception> call, Throwable t) {
                System.out.println("连接失败！");

            }
        });
    }

    //这是一个云端转发的方法，也可以传送周期到模块
    public void commandToModule(CommandFromPhone commandFromPhone) {

        //定义去联网的call 使用实例的哪一个方法&传入需要的参数。
        Call<String> call = cloudServer.reposForCommand("http://175.24.33.56:8080/command", commandFromPhone);

        //异步执行该call，如果是同步会使用excute方法，enqueue排队的意思。
        call.enqueue(new Callback<String>() {
            //** retrofit的封装，下面两个override都回到了主线程 UI线程来执行。**
            @Override
            public void onResponse(retrofit2.Call<String> call, retrofit2.Response<String> response) {
                System.out.println("send command to 云端转发器 success :) ok!\n" + response.body());
            }

            @Override
            public void onFailure(retrofit2.Call<String> call, Throwable t) {
                Log.d(this.getClass().getSimpleName(), "send to 云端转发器 onFailure: " + t.toString());
            }

        });
    }


    /**  ******实现 Dao 的所有方法******   */
    /**
     * basic room information
     */
    //插入基本房间信息
    public void insertBasicInfo(BasicInfoSheet... basicInfoSheets) {
        new AnsyMyDaoTask.InsertBasicInfoAsyncTask(myDao).execute(basicInfoSheets);
    }

    //修改基本房间信息
    public void updateBasicInfo(BasicInfoSheet... basicInfoSheets) {
        new AnsyMyDaoTask.UpdateBasicInfoAsyncTask(myDao).execute(basicInfoSheets);
    }

    //删除基本房间信息，依靠主键就可以
    public void deleteBasicInfo(BasicInfoSheet... basicInfoSheets) {
        new AnsyMyDaoTask.DeleteBasicInfoAsyncTask(myDao).execute(basicInfoSheets);
    }

    //删除所有的基本房间信息
    public void deleteAllBasicInfo() {
        new AnsyMyDaoTask.DeleteAllBasicInfoAsyncTask(myDao).execute();
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
    //SENSOR 插入房间传感器的状态信息
    public void insertSensorSheet(SensorSheet... sensorSheets) {  // 这个 ... 接收的是数组！！
        new AnsyMyDaoTask.InsertSensorSheetAsyncTask(myDao).execute(sensorSheets);
    }

    //SENSOR 获取房间传感器的全部信息
    public LiveData<List<SensorSheet>> getAllLatestSensorSheetsLive(int devicetypeId) {
        //获取房间的最新信息指定了参数设备类型，继续包装下去，让调用者决定设备的类型。
        allLatestSensorSheetsLive = myDao.loadLatestSensorSheetsLive(devicetypeId);
        //一般查询系统会自动安排在非主线程，不需要自己写。其他的需要自己写非主线程。？？right？？
        return allLatestSensorSheetsLive;
    }

    //SENSOR 删除所有房间传感器的信息
    public void deleteAllSensorSheet() {
        new AnsyMyDaoTask.DeleteAllSensorSheetAsyncTask(myDao).execute();
    }

    //FANCOIL 插入房间风机的状态信息
    public void insertFancoilSheet(FancoilSheet... fancoilSheets) {  // 这个 ... 接收的是数组！！
        new AnsyMyDaoTask.InsertFancoilSheetAsyncTask(myDao).execute(fancoilSheets);
    }

    //FANCOIL 获取房间风机的全部信息
    public LiveData<List<FancoilSheet>> getAllLatestFancoilSheetsLive() {
        //获取房间的最新信息指定了参数设备类型，继续包装下去，让调用者决定设备的类型。
        allLatestFancoilSheetsLive = myDao.loadLatestFancoilSheetsLive();
        //一般查询系统会自动安排在非主线程，不需要自己写。其他的需要自己写非主线程。？？right？？
        return allLatestFancoilSheetsLive;
    }

    //ENGINE 插入锅炉热泵等的状态信息
    public void insertEngineSheet(EngineSheet... engineSheets) {  // 这个 ... 接收的是数组！！
        new AnsyMyDaoTask.InsertEngineSheetAsyncTask(myDao).execute(engineSheets);
    }

    //WATERSHED 插入房间风机的状态信息
    public void insertWatershedSheet(WatershedSheet... watershedSheets) {  // 这个 ... 接收的是数组！！
        new AnsyMyDaoTask.InsertWatershedSheetAsyncTask(myDao).execute(watershedSheets);
    }

    //WATERSHED 获取房间分水器的全部信息
    public LiveData<List<WatershedSheet>> getAllLatestWatershedSheetsLive() {
        //获取房间的最新信息指定了参数设备类型，继续包装下去，让调用者决定设备的类型。
        allLatestWatershedSheetsLive = myDao.loadLatestWatershedSheetsLive();
        //一般查询系统会自动安排在非主线程，不需要自己写。其他的需要自己写非主线程。？？right？？
        return allLatestWatershedSheetsLive;
    }

    //TODO 存储的数据优化降维的时候，还是需要删除数据的。


    /**
     * the period information of room
     */
    //插入某个房间一周运行的周期
    public void insertPeriodSheet(PeriodSheet... periodSheets) {//这里的参数需要使用数组，或者单个，多个
        new AnsyMyDaoTask.InsertPeriodSheetAsyncTask(myDao).execute(periodSheets);
        //FIXME: 这里没有写入参数periodSheets ，导致数据库没有写入，耽误了两三天时间。没有报错误。
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




}
