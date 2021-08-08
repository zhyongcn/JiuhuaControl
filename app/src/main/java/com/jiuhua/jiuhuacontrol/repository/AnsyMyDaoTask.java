package com.jiuhua.jiuhuacontrol.repository;

import android.os.AsyncTask;

import com.jiuhua.jiuhuacontrol.database.BasicInfoSheet;
import com.jiuhua.jiuhuacontrol.database.EngineSheet;
import com.jiuhua.jiuhuacontrol.database.FancoilSheet;
import com.jiuhua.jiuhuacontrol.database.MyDao;
import com.jiuhua.jiuhuacontrol.database.PeriodSheet;
import com.jiuhua.jiuhuacontrol.database.SensorSheet;
import com.jiuhua.jiuhuacontrol.database.WatershedSheet;

import java.util.List;

//TODO: Use the standard java.util.concurrent or Kotlin concurrency utilities instead.AsyncTask
public class AnsyMyDaoTask {

    /**
     * 内部类，辅助线程上执行 Dao 的方法。    还有一种线程池的方法（Google文档上的）
     * 读取消耗时间少，没有专门开线程。  read data from SQLite has no new thread.
     */
    //BasicInfo相关
        //这三个参数分别是：传入参数，进度，结果
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

    //Sensor相关 TODO： fancoil，watershed， 暖气片？，boiler，heatpump etc。
    static class InsertSensorSheetAsyncTask extends AsyncTask<SensorSheet, Void, Void> {
        private MyDao myDao;   //独立的线程需要独立的 Dao

        InsertSensorSheetAsyncTask(MyDao myDao) {
            this.myDao = myDao;
        }

        @Override
        protected Void doInBackground(SensorSheet... sensorSheets) {
            myDao.insertSensorSheet(sensorSheets);
            return null;
        }
    }

    static class DeleteAllSensorSheetAsyncTask extends AsyncTask<Void, Void, Void> {
        private MyDao myDao;

        DeleteAllSensorSheetAsyncTask(MyDao myDao) {
            this.myDao = myDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            myDao.deleteAllSensorSheet();
            return null;
        }
    }

    //fancoil相关
    static class InsertFancoilSheetAsyncTask extends AsyncTask<FancoilSheet, Void, Void> {
        private MyDao myDao;   //独立的线程需要独立的 Dao

        InsertFancoilSheetAsyncTask(MyDao myDao) {
            this.myDao = myDao;
        }

        @Override
        protected Void doInBackground(FancoilSheet... fancoilSheets) {
            myDao.insertFancoilSheet(fancoilSheets);
            return null;
        }
    }


    //Period 相关
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

    static class InsertEngineSheetAsyncTask extends AsyncTask<EngineSheet, Void, Void>{
        private MyDao myDao;

        InsertEngineSheetAsyncTask(MyDao myDao) {
            this.myDao = myDao;
        }

        @Override
        protected Void doInBackground(EngineSheet... engineSheets) {
            myDao.insertEngineSheet(engineSheets);
            return null;
        }
    }

    static class InsertWatershedSheetAsyncTask extends AsyncTask<WatershedSheet, Void, Void>{
        private MyDao myDao;

        InsertWatershedSheetAsyncTask(MyDao myDao) {
            this.myDao = myDao;
        }

        @Override
        protected Void doInBackground(WatershedSheet... watershedSheets) {
            myDao.insertWatershedSheet(watershedSheets);
            return null;
        }
    }


}
