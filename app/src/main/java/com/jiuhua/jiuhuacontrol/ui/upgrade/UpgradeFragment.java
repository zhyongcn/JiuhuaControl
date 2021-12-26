package com.jiuhua.jiuhuacontrol.ui.upgrade;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jiuhua.jiuhuacontrol.BuildConfig;
import com.jiuhua.jiuhuacontrol.Constants;
import com.jiuhua.jiuhuacontrol.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UpgradeFragment extends Fragment implements AppUpgrade {
    private final String TAG = getClass().getName();
    boolean isInit;
    private Context appContext;
    //private DownloadReceiver downloaderReceiver = new DownloadReceiver();
    //private NotificationClickReceiver notificationClickReceiver = new NotificationClickReceiver();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UpgradeViewModel homeViewModel = new ViewModelProvider(this).get(UpgradeViewModel.class);
        homeViewModel.upgrade(Constants.upgradeinfo_url);

        appContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //TODO 检查 myRepository 的versions【0】，if null 显示现在没有更新。
        //TODO 如果有，点击更新。
        DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(
                Context.DOWNLOAD_SERVICE);

        init(appContext);

        return inflater.inflate(R.layout.fragment_upgrade, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unInit();
    }

    @Override
    public void init(Context context) {
        // App更新及增量更新使用示例, 框架不工作？？？
        //Upgrader.with().start(new ApkUpdateBean.Builder()
        //        .newApkUrl("完整apk下载链接")
        //        .newApkVersionCode(2)
        //        // 可选，可增多个，增量更新时使用
        //        .addApkPatchBean(new ApkPatchBean(1, "增量文件链接"))
        //        .build());


        if (isInit) {
            return;
        }

        appContext = context.getApplicationContext();
        isInit = true;
        //appContext.registerReceiver(downloaderReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        //appContext.registerReceiver(notificationClickReceiver, new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
        //这里下载了个app
        DownloadUtils.builder()
                .setContext(this.getContext())
                .setLister(new IDownloadlister() {
                    @Override
                    public void success(Uri uri) {
                        installApk();
                    }
                })
                .download();

    }

    @Override
    public void unInit() {//解除的目的是什么？
        if (!isInit) {
            return;
        }
        //appContext.unregisterReceiver(downloaderReceiver);  //注销广播接收器
        //appContext.unregisterReceiver(notificationClickReceiver);   //注销广播接收器
        isInit = false;
        appContext = null;
    }

    //@Override
    //public void checkLatestVersion(Activity activity) {
    //    //TODO something...
    //}
    //
    //@Override
    //public void checkLatestVersionBackground() {
    //    //TODO something...
    //}
    //
    //@Override
    //public void foundLatestVersion(Activity activity) {
    //    //TODO something...
    //}


    //private void moveApk(String apkName, String path) {
    //
    //    File file = new File(path);
    //
    //    if (file.exists()) {
    //        file.delete();
    //        moveApk(apkName, path);
    //    } else {
    //        try {
    //            file.createNewFile();
    //            //这里不一定是getAssets，下载的目录不同，我们是公共的download文件夹
    //            Path p = Paths.get(path+apkName);
    //            BufferedInputStream bis = new BufferedInputStream(Files.readAllBytes(p));
    //            FileOutputStream fos = new FileOutputStream(file);
    //            int len;
    //            byte[] buff = new byte[1024 * 6];
    //            while ((len = bis.read(buff)) != -1) {
    //                fos.write(buff, 0, len);
    //            }
    //            fos.flush();    //先冲刷，
    //            bis.close();    //再关闭输入
    //            fos.close();    //最后再关闭输出写入的流
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //    }
    //}


    //方法的目的是检查是否已经安装了这个app
    private boolean detectAPP(String packageName) {
        List<String> app = new ArrayList<>();
        List<PackageInfo> installedPackages = appContext.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < installedPackages.size(); i++) {
            app.add(installedPackages.get(i).packageName.toLowerCase());//都需要转化为小写来比较。
        }
        return app.contains(packageName.toLowerCase());
    }


    /**
     * 安装APK文件
     */
    private void installApk() {

        File apkfile = new File(Environment.getExternalStorageDirectory(), "downloadtest.apk");
        //if (!apkfile.exists()) {
        //    return;
        //}

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //兼容 android 6.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                Uri contentUri = FileProvider.getUriForFile(appContext, BuildConfig.APPLICATION_ID + ".ui.upgrade", apkfile);
                //FileProvider.getUriForFile()
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");

                //兼容 android 7.0
                //if (android.os.Build.VERSION.SDK_INT >= 25) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    boolean hasInstallPermission = appContext.getPackageManager().canRequestPackageInstalls();
                    if (!hasInstallPermission) {
                        //请求安装未知应用来源的权限
                        ActivityCompat.requestPermissions((Activity) appContext, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 6665);
                        //TODO 为什么没有操作fileprovder？？
                    }
                }
            }
            startActivity(intent);
        } else {
            // android 6.0 以下的做法。
            //moveApk();
            // 通过Intent安装APK文件
            intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
            startActivity(intent);
        }

        if (appContext.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
            appContext.startActivity(intent);
        }
    }

    private void unInstall(String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 下载完成的广播  TODO:
     */
    //class DownloadReceiver extends BroadcastReceiver {
    //
    //    @Override
    //    public void onReceive(Context context, Intent intent) {
    //        if (downloader == null) {
    //            return;
    //        }
    //        long completeId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
    //        long downloadTaskId = mAppUpgradePersistent.getDownloadTaskId(context);
    //        if (completeId != downloadTaskId) {
    //            return;
    //        }
    //
    //        Query query = new Query();
    //        query.setFilterById(downloadTaskId);
    //        Cursor cur;
    //        cur = downloader.query(query);
    //        if (!cur.moveToFirst()) {
    //            return;
    //        }
    //
    //        int columnIndex = cur.getColumnIndex(DownloadManager.COLUMN_STATUS);
    //        if (DownloadManager.STATUS_SUCCESSFUL == cur.getInt(columnIndex)) {
    //            String uriString = cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
    //            mHandler.obtainMessage(WHAT_ID_INSTALL_APK, uriString).sendToTarget();
    //        } else {
    //            Toast.makeText(context, "xxxApp最新版本失败!", Toast.LENGTH_SHORT).show();
    //        }
    //        // 下载任务已经完成，清除
    //        mAppUpgradePersistent.removeDownloadTaskId(context);
    //        cur.close();
    //    }
    //
    //}

    /**
     * 点击通知栏下载项目，下载完成前点击都会进来，下载完成后点击不会进来。TODO:
     */
    //class NotificationClickReceiver extends BroadcastReceiver {
    //    @Override
    //    public void onReceive(Context context, Intent intent) {
    //        long[] completeIds = intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
    //        //正在下载的任务ID
    //        long downloadTaskId = mAppUpgradePersistent.getDownloadTaskId(context);
    //        if (completeIds == null || completeIds.length <= 0) {
    //            openDownloadsPage(appContext);
    //            return;
    //        }
    //
    //        for (long completeId : completeIds) {
    //            if (completeId == downloadTaskId) {
    //                openDownloadsPage(appContext);
    //                break;
    //            }
    //        }
    //    }
    //}


}