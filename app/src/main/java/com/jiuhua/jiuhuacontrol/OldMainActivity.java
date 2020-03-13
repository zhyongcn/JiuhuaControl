//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.espressif.iot.esptouch.demo_activity.EsptouchDemoActivity;
//import com.jiuhua.jiuhuacontrol.R;
//import com.jiuhua.jiuhuacontrol.ui.indoor.OldRoomActivity;
//import com.jiuhua.mqttsample.IGetMessageCallBack;
//import com.jiuhua.mqttsample.MQTTService;
//import com.jiuhua.mqttsample.MyServiceConnection;
//
//public class OldMainActivity extends AppCompatActivity implements IGetMessageCallBack {
//
//    //MQTT需要的参数
//    private MyServiceConnection serviceConnection;//连接实例
//    private MQTTService mqttService;//服务实例
//
//    @Override
//    protected void onCreate(final Bundle savedInstanceState) {
//        //TODO 要通过全局对象来传递数据，getApplication()
//        //从存储器中读取数据：各个房间的名字
//        //这个放在开始的部分，利用线程，不耽误其他线程
//        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
//        String room1name = sharedPreferences.getString("room1name", "");
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//
//        //如果房间名称是空的，让这个按钮不可见
//        Button buttonA;
//        if (room1name.equals("")) buttonA.setVisibility(View.INVISIBLE);
//
//        //点击各个房间的按钮，跳转到各个房间的详情
//        buttonA.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(OldMainActivity.this, OldRoomActivity.class);
//                intent.putExtra("roomname", room1name);//传递了房间名字的参数
//                intent.putExtra("roomid", "1");//传递了房间的ID
//                startActivity(intent);
//            }
//        });
//        Button buttonB;
//        buttonB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(OldMainActivity.this, OldRoomActivity.class);
//                intent.putExtra("roomname", room2name);//传递了房间名字的参数
//                intent.putExtra("roomid", "2");
//                startActivity(intent);
//            }
//        });
//
//        serviceConnection = new MyServiceConnection();//新建连接服务的实例
//        serviceConnection.setIGetMessageCallBack(OldMainActivity.this);//把本活动传入
//        Intent intent = new Intent(this, MQTTService.class);
//        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
//        startService(intent);
//    }
//
//    //右上角的目录的设置
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.userinfo:
//                //这里的intent必须不同，因为他们在一个作用域范围内
//                Intent intent1 = new Intent(OldMainActivity.this, UserinfoActivity.class);
//                startActivity(intent1);
//                break;
//            case R.id.set_up_net:
//                Intent intent2 = new Intent(OldMainActivity.this, EsptouchDemoActivity.class);
//                startActivity(intent2);
//                break;
//            case R.id.set_up_timer:
//                Intent intent3 = new Intent(OldMainActivity.this, timerSettingActivity.class);
//                startActivity(intent3);
//                break;
//            case R.id.check_version:
//                //Intent intent4 = new Intent(OldMainActivity.this, );
//                Toast.makeText(this, "开发中，敬请期待。。。", Toast.LENGTH_SHORT).show();
//                break;
//        }
//        return true;
//    }
//
//    String room1temperature = "";
//    String room1humidity = "";
//    //定义运行状态
//    String room1states = "状态未知";
//    String room2states = "状态未知";
//
//    @SuppressLint("SetTextI18n")
//    @Override
//    public void setMessage(String message) {
//        //依据message字符串最后一位决定房间号，倒数第二位决定温湿度C为温度，H为湿度。
//        if (message.contains("C1")) room1temperature = message.replace("C1", "C");
//        if (message.contains("RH1")) room1humidity = message.replace("RH1", "RH");
//        if (message.contains("C2")) room2temperature = message.replace("C2", "C");
//
//        //运行状态需要反馈回来
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
//
//
//        //设置显示的文字
//        buttonA.setText("\n" + room1name + "\n\n" + room1temperature + "\n\n" + room1humidity + "\n\n" + room1states + "\n");
//        buttonB.setText("\n" + room2name + "\n\n" + room2temperature + "\n\n" + room2humidity + "\n\n" + room2states + "\n");
//        mqttService = serviceConnection.getMqttService();//服务连接实例 的 获得服务的方法
////        mqttService.toCreateNotification(message);//服务的发布消息的方法
//    }
//
//    @Override
//    protected void onDestroy() {
//        unbindService(serviceConnection);
//        super.onDestroy();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        MQTTService.publish("86518/JYCFGC/6-2-3401/IndoorDB", "feedback", 1, true);
//        MQTTService.publish("86518/JYCFGC/6-2-3401/Room2", "feedback", 1, true);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        //在这里onPause 存储数据
//    }
//}
