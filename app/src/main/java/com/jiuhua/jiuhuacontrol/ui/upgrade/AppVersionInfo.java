package com.jiuhua.jiuhuacontrol.ui.upgrade;

/** 需要解析的json文本
       {
        "data": {
        "downloadUrl": "http://a5.pc6.com/cx3/weixin.pc6.apk",
        "version": "1.0.1",
        "versionCode": 2,
        "versionDesc": "主要修改:\n1.增加多项新功能;\n2.修复已知bug。"
        },
        "errCode": 0,
        "errMsg": "",
        "success": true
       }

 */

public class AppVersionInfo {
    private int versionCode;
    private String appVersion;
    private String downloadUrl;
    private String versionDesc;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }
}
