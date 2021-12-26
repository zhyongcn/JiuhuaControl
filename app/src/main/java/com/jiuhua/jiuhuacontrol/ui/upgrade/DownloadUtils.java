package com.jiuhua.jiuhuacontrol.ui.upgrade;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;

public class DownloadUtils {

    //测试url uniform resource locator，下载链接
    private String url = "http://81.68.136.113:8080/upgrade/android" +
            ".apk";

    //加.好处是默认隐藏路径
    //private final String FILE_URI = "/storage/emulated/0/Download";
    //为什么只有这个目录可以使用，其他的都闪退了。
    private final String FILE_URI = Environment.DIRECTORY_DOWNLOADS;

    private IDownloadlister lister = null;
    //文件名
    private String fileName = "downloadtest.apk";
    //Context
    private Context context;


    public static DownloadUtils builder() {
        return new DownloadUtils();
    }

    public void download() {
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        //创建下载任务，url即任务链接
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        // **指定下载路径及文件名**
        request.setDestinationInExternalPublicDir(FILE_URI, fileName);
        //获取下载管理器
        final DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        //一些配置
        //允许移动网络与WIFI下载
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        //是否在通知栏显示下载进度
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);


        //设置可见及可管理
        /*注意，Android Q之后不推荐使用*/
        //request.setVisibleInDownloadsUi(true);


        //将任务加入下载队列
        assert downloadManager != null;
        final long id = downloadManager.enqueue(request);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //获取下载id
                long myDwonloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (myDwonloadID == id) {
                    //获取下载uri
                    Uri uri = downloadManager.getUriForDownloadedFile(myDwonloadID);
                    lister.success(uri);
                }
            }
        };
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.registerReceiver(receiver, filter);
        }
    }




    public DownloadUtils setUrl(String url) {
        this.url = url;
        return this;
    }

    public DownloadUtils setLister(IDownloadlister lister) {
        this.lister = lister;
        return this;
    }

    public DownloadUtils setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public DownloadUtils setContext(Context context) {
        this.context = context;
        return this;
    }


}


