package com.jiuhua.jiuhuacontrol;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.jiuhua.jiuhuacontrol.database.BasicInfoSheet;
import com.jiuhua.jiuhuacontrol.database.SensorSheet;
import com.jiuhua.jiuhuacontrol.repository.MyRepository;
import com.jiuhua.jiuhuacontrol.ui.HomeViewModel;

import java.util.List;

//FIXME: Dev分支目标是基础的架构，软件架构，数据形式，存储方法，基本逻辑等等，不可见的，共性的。
//TODO: 在云端使用数据库存储用户的数据，使用workmanager ?可靠? 管理一个任务，定时15min去获取数据
//TODO: 功能添加：维护&运行的记录。更换配件（下拉列表？），保养（下拉列表？）
//TODO: select * from sensors/fancoils/wateshed/boilers/heatpumps/ where
//              location = "86518/xx/xx/RoomX" and ts > sqlite`s lateset timestamp

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;//首页需要appbar的一个实例，先新建一个句柄。

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar); //顶部的工具条
        setSupportActionBar(toolbar); //appcompatactivity的一个方法，

        // TODO:浮动吧将来应该有用！！
//        FloatingActionButton fab = findViewById(R.id.fab);//浮动的工具园
//        fab.setOnClickListener(view -> {
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);//滑动的侧边栏
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_userinfo, R.id.espTouchActivity,
                R.id.nav_service, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        final MyRepository myRepository = MyRepository.getInstance(this.getApplicationContext());

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
