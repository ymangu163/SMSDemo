package com.sms.code.bean;

/**
 * File description
 *
 * @author gao
 * @date 2018/9/3
 */

public class Upgrade extends BaseBean {
    private int versioncode;
    private String download;
    private String description;
    private boolean force;

    public int getVersioncode() {
        return versioncode;
    }

    public String getDownload() {
        return download;
    }

    public String getDescription() {
        return description;
    }

    public boolean isForce() {
        return force;
    }
}
