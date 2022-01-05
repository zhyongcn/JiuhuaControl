package com.jiuhua.jiuhuacontrol.ui.upgrade;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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

        if (isInit) {
            return;
        }

        appContext = context.getApplicationContext();
        isInit = true;
        //这里下载了个app
        DownloadUtils.builder()
                .setContext(this.getContext())
                .setLister(uri -> installApk())
                .download();

    }

    @Override
    public void unInit() {//解除的目的是什么？
        if (!isInit) {
            return;
        }
        isInit = false;
        appContext = null;
    }


    /**
     * 安装APK文件
     */
    private void installApk() {

        File apkfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "downloadtest.apk");
        if (!apkfile.exists()) {  //预防没有安装包也跳转??
            return;
        }
        //if (!apkfile.isFile()) {   //和上面的方法相同吗？？
        //    return;
        //}

        /**
         * Oreo 巧克力饼干 　 API　２７　　Android８.１
         * 　　　　　　　　　　API　２６　　Android８.０
         * Nougat　牛轧糖　API２５　Android７.１
         * 　　　　　　　　 API２４　　Android７.０
         * Marshmallow　   棉花糖　　 API２３　  Android６.０
         * Lollipop　　　棒棒糖　　API２２　Android５.１
         * 　　　　　　　　　　　　　API２１　Android５.０
         */

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //Android的各版本判断：
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //大于&等于 Android6.0 都需要权限
            String[] permissions = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //大于&等于 Android7.0做法：都需要FileProvider，
                Uri contentUri = FileProvider.getUriForFile(appContext,
                        "com.jiuhua.jiuhuacontrol.ui.upgrade.MyFileProvider", apkfile);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //Android8.0：做法  需要每次授权允许未知来源，& 唤起安装界面。
                    boolean hasInstallPermission = appContext.getPackageManager().canRequestPackageInstalls();
                    if (!hasInstallPermission) {
                        //请求安装未知应用来源的权限
                        Uri packageURI = Uri.parse("package:" + appContext.getPackageName());
                        Intent intentForPermission = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
                        startActivityForResult(intentForPermission, 10001);
                    } else {
                        startActivity(intent);
                    }

                }else {
                    //Android7.0做法
                    startActivity(intent);
                }

            } else {
                //Android6.0的做法：需要动态申请权限，不需要FileProvider。
                if (appContext.checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(appContext, permissions[1]) == PackageManager.PERMISSION_GRANTED) {
                    intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
                    startActivity(intent);
                } else {
                    this.requestPermissions(permissions, 6665);
                }

            }

        } else {
            // android 6.0 以下的做法。  下载的文件在外部公共区域，没有必要再移动了。
            // 通过Intent安装APK文件
            intent.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                    "application/vnd.android.package-archive");
            startActivity(intent);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 6665:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    installApk();
                }
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10001) {
            if (resultCode == RESULT_OK) {
                installApk();
            } else {
                Toast.makeText(appContext, "未打开'安装未知来源'开关,无法安装,请打开后重试", Toast.LENGTH_SHORT).show();
            }
        }
    }
}