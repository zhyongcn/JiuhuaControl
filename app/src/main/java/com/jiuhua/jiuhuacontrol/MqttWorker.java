package com.jiuhua.jiuhuacontrol;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.TimeUnit;

public class MqttWorker extends Worker {
    public MqttWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        WorkManager.getInstance(context).enqueue(commandworkRequest);// 任务入队，把任务提交给系统

//        WorkManager.getInstance(context).cancelAllWork(); //取消任务，也可以按照ID 或者 Tag 来取消
        //应该写在观察者的类里面。
//        WorkManager.getInstance(context).getWorkInfoByIdLiveData(oneTimeWorkRequest.getId()).observe(this, new Observer<WorkInfo>() {
//            @Override  //使用livedata 来观察任务，获取数据。
//            public void onChanged(WorkInfo workInfo) {
//
//            }
//        });
    }

    Data inputdata = new Data.Builder().putString("key", "value").build();//workmanager向work传参，

    Constraints constraints = new Constraints.Builder()//约束条件---执行的条件
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .build();

    OneTimeWorkRequest commandworkRequest = new OneTimeWorkRequest.Builder(MqttWorker.class) //需求---配置任务
            .setConstraints(constraints)
            .setInitialDelay(250, TimeUnit.MICROSECONDS)
            .setBackoffCriteria(BackoffPolicy.LINEAR,// 线性退让还是指数退让？？
                    OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.SECONDS)
            .addTag("mqtt")
            .setInputData(inputdata)
            .build();

    PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest//配置周期性的任务
            .Builder(MqttWorker.class, 30, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .addTag("periodMQTT")
            .build();



    @NonNull
    @Override
    public Result doWork() {  //在这里完成耗时的任务
        String inputdata = getInputData().getString("key");//从任务管理器workmanager来获取参数

        Data outputData = new Data.Builder().putString("kays", "I am the result").build();

        return Result.success(outputData);//return 方式传出结果/参数等。
    }
}
