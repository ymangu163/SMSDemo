package com.sms.code.bean;

/**
 * File description
 *
 * @author gao
 * @date 2018/9/3
 */

public class HomeAd extends BaseBean {
    private int versioncode;
    private String downloadUrl;
    private String iconUrl;
    private String packageName;
    private String name;

    public int getVersioncode() {
        return versioncode;
    }

    public String getDownload() {
        return downloadUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getName() {
        return name;
    }
}
