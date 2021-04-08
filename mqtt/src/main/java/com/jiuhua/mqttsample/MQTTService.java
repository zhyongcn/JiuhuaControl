package com.jiuhua.mqttsample;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTService extends Service {
    public static final String TAG = MQTTService.class.getSimpleName();

    private static MqttAndroidClient client;//在init（）里面新建
    private MqttConnectOptions conOpt;//在init（）里面新建

    private static String mqtt_host = "tcp://180.102.131.255:1883";//ctyun
    private static String mqtt_client_name = "admin";
    private static String mqtt_client_passWord = "password";
    private static String mqtt_sub_familyTopic = "86518/YXHY/12-1-101/phone";      //要订阅的主题  TODO 原始定义采用什么？
    private static String mqtt_publish_topic_prefix = "86518/YXHY/12-1-101/Room";
    private static String mqtt_client_id = "androidId--YXHY12-1-101";//不同的用户需要不同的客户端标识

    private IGetMessageCallBack IGetMessageCallBack;//将在什么地方使用？mqttcallback实例当中改写原来的方法


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        //从存储器中读取数据
        //这个放在开始的部分，利用线程，不耽误其他线程
        //todo  挂几个老化运行的设备，创建一个demo数据，缺省加载。 //TODO 最初始的时候获取的是空null，不是缺省的。
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        mqtt_host = sharedPreferences.getString("mqtt_host", "tcp://180.102.131.255:1883");
        mqtt_client_name = sharedPreferences.getString("mqtt_client_name", "admin");
        mqtt_client_passWord = sharedPreferences.getString("mqtt_client_passwd", "password");
        mqtt_sub_familyTopic = sharedPreferences.getString("mqtt_family_topic", "86518/YXHY/12-1-101/phone");
        mqtt_publish_topic_prefix = sharedPreferences.getString("mqtt_publish_topic_prefix", "86518/YXHY/12-1-101/Room");
        mqtt_client_id = sharedPreferences.getString("mqtt_client_id", "androidId--YXHY12-1-101");
        Log.d(TAG, "onCreate: host is  " + mqtt_host);
        Log.d(TAG, "onCreate: familytopic is  " + mqtt_sub_familyTopic);

        super.onCreate();
        Log.e(getClass().getName(), "onCreate");
        init();//放在onCreate里面，这个service启动的时候调用了。
    }

    public static void publish(String topic, String msg, int qos, boolean retained) {
        // 这里修改一下，原来的一个msg参数，改为三个参数，方便以后调用
//        String topic = myTopic;
//        Integer qos = 0;
//        Boolean retained = false;
        try {
            if (client != null) {
                client.publish(topic, msg.getBytes(), qos, retained);  //msg.getBytes(),发布的字符串转变为字节
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public static void myPublishToDevice(int roomId, String msg, int qos, boolean retained) {
        // 这里修改一下，原来的一个msg参数，改为三个参数，方便以后调用
        String topic = mqtt_publish_topic_prefix + String.valueOf(roomId);
//        Integer qos = 0;
//        Boolean retained = false;
        try {
            if (client != null) {
                client.publish(topic, msg.getBytes(), qos, retained);  //msg.getBytes(),发布的字符串转变为字节
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        // 服务器地址（协议+地址+端口号）
        String uri = mqtt_host;//原来就是string，改个名字，方便使用与理解
        client = new MqttAndroidClient(this, uri, mqtt_client_id);//一个client ID 一个客户端
        //新建一个Android的mqtt客户端，需要三个参数，上下文this，服务器的协议、地址、端口，第三：客户端ID
        // 设置MQTT监听并且接受消息
        client.setCallback(mqttCallback);//设置回调函数，这里是一个mqttcallback实例。

        conOpt = new MqttConnectOptions();//新建一个用于连接操作的实例
        // 清除缓存
        conOpt.setCleanSession(false);//TODO: try false .
        // 设置超时时间，单位：秒
        conOpt.setConnectionTimeout(10);
        // 心跳包发送间隔，单位：秒
        conOpt.setKeepAliveInterval(20);
        // 用户名
        conOpt.setUserName(mqtt_client_name);//注意在这里设置用户名以及密码
        // 密码
        conOpt.setPassword(mqtt_client_passWord.toCharArray());     //将字符串转换为字符串数组

        // last will message   设置遗嘱
        boolean doConnect = true;//这个变量用来标识状态
        String message = "{\"terminal_uid\":\"" + mqtt_client_id + "\"}";//这个就是遗嘱消息，客户端的ID，知道是哪一个客户端。
        Log.e(getClass().getName(), "message是:" + message);
        String topic = mqtt_sub_familyTopic;//***这里又使用了主题***  注意：我们会不会更换主题（应对多个房间）？
        Integer qos = 0;//遗嘱的发送没有质量要求
        Boolean retained = false;//在服务器上不保留
        if ((!message.equals("")) || (!topic.equals(""))) {
            // 最后的遗嘱
            // MQTT本身就是为信号不稳定的网络设计的，所以难免一些客户端会无故的和Broker断开连接。
            //当客户端连接到Broker时，可以指定LWT，Broker会定期检测客户端是否有异常。
            //当客户端异常掉线时，Broker就往连接时指定的topic里推送当时指定的LWT消息。

            try {
                conOpt.setWill(topic, message.getBytes(), qos, retained);//还是没有消息内容？？为什么？？
            } catch (Exception e) {
                Log.i(TAG, "Exception Occured", e);
                doConnect = false;//捕获到异常了，所以连接是否定的。
                iMqttActionListener.onFailure(null, e);//mqtt动作监听器的，失败了的，方法
            }
        }

        if (doConnect) {
            doClientConnection();//连接时存在的时候才执行客户端连接。连接mqtt服务器
        }

    }


    @Override
    public void onDestroy() {
        stopSelf();//停止服务
        try {
            client.disconnect();//关闭客户端的连接
        } catch (MqttException e) {
            e.printStackTrace();//捕获异常，输出异常（控制台？logcat？手机传回？）
        }
        super.onDestroy();//销毁
    }

    /**
     * 连接MQTT服务器
     */
    private void doClientConnection() {
        if (!client.isConnected() && isConnectIsNormal()) {//判断客户端是否连着，网络是否连着
            try {
                client.connect(conOpt, null, iMqttActionListener);
                //连接需要传入 “操作实例” ，内容是空的，mqtt的动作监听者
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

    }

    // MQTT是否连接成功
    private IMqttActionListener iMqttActionListener = new IMqttActionListener() {//新建监听者实例

        @Override
        public void onSuccess(IMqttToken arg0) {
            Log.i(TAG, "连接成功 ");
            try {
                // ***订阅myTopic话题***
                client.subscribe(mqtt_sub_familyTopic, 1);//订阅的主题质量要求必须传到。
                //可以订阅多个主题，消息混在一起，需要注意处理
                //发送信道的topic 没有必要订阅了吧。
//                client.subscribe("86518/JYCFGC/6-2-3401/Room1", 1);
//                client.subscribe("86518/JYCFGC/6-2-3401/Room2", 1);
//                client.subscribe("86518/JYCFGC/6-2-3401/Room3", 1);
//                client.subscribe("86518/JYCFGC/6-2-3401/Room4", 1);
//                client.subscribe("86518/JYCFGC/6-2-3401/Room5", 1);
//                client.subscribe("86518/JYCFGC/6-2-3401/Room6", 1);
//                client.subscribe("86518/JYCFGC/6-2-3401/Room7", 1);
//                client.subscribe("86518/JYCFGC/6-2-3401/Room8", 1);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(IMqttToken arg0, Throwable arg1) {//连接失败如何处理
            arg1.printStackTrace();
            // 连接失败，重连，这里好像没有实现？？
        }
    };

    // MQTT监听并且接受消息
    private MqttCallback mqttCallback = new MqttCallback() {//新建实例

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            //这里的参数调用者会自动赋予
            String str1 = new String(message.getPayload());//获取消息内容  //TODO 是不是可以在这里转到工作线程？？
            if (IGetMessageCallBack != null) {
                IGetMessageCallBack.setMessage(str1);//IGetMessageCallBack的唯一方法
            }
            String str2 = topic + ";qos:" + message.getQos() + ";retained:" + message.isRetained();
            Log.i(TAG, "messageArrived:" + str1);//日志输出，调试方法
            Log.i(TAG, str2);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {//交货完成

        }

        @Override
        public void connectionLost(Throwable arg0) {//连接丢失
            // 失去连接，重连
        }
    };

    /**
     * 判断网络是否连接
     */
    private boolean isConnectIsNormal() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String name = info.getTypeName();
            Log.i(TAG, "MQTT当前网络名称：" + name);
            return true;
        } else {
            Log.i(TAG, "MQTT 没有可用网络");
            return false;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {//绑定什么？？
        Log.e(getClass().getName(), "onBind");
        return new CustomBinder();
    }

    public void setIGetMessageCallBack(IGetMessageCallBack IGetMessageCallBack) {
        this.IGetMessageCallBack = IGetMessageCallBack;//给域值赋值，都是哪个接口
    }

    public class CustomBinder extends Binder {//绑定者子类就是返回当前MQTTservice？？

        public MQTTService getService() {
            return MQTTService.this;
        }
    }

    public void toCreateNotification(String message) {//发布消息的方法？？
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1,
                new Intent(this, MQTTService.class), PendingIntent.FLAG_UPDATE_CURRENT);
        //Pending 未决定的 Intent目的意图 PendingIntent送达服务/单件模式/发送服务
        //再启动一个服务，是本服务的升级方式？？
        //参照底层代码，特别难以理解。

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "chancel1");//新的是不是使用了通道？？
        //3、创建一个通知，属性太多，使用构造器模式

        Notification notification = builder//构造器  不是绑定
                .setTicker("测试标题")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("")
                .setContentText(message)
                .setContentInfo("")
                .setContentIntent(pendingIntent)//点击后才触发的意图，“挂起的”意图
                .setAutoCancel(true)        //设置点击之后notification消失
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        startForeground(0, notification);
        notificationManager.notify(0, notification);

    }
}

