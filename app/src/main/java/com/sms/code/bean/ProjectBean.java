package com.sms.code.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * File description
 *
 * @author gao
 * @date 2018/8/29
 */

public class ProjectBean extends BaseBean implements Serializable {
    private int id;
    @SerializedName("entry_name")
    private String name;
    @SerializedName("unit_price")
    private float price;
    private String description;
    private int xiangmuleix;
    private String fasongshoujihao;
    private float kashangjiage;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getXiangmuleix() {
        return xiangmuleix;
    }

    public String getFasongshoujihao() {
        return fasongshoujihao;
    }

    public float getKashangjiage() {
        return kashangjiage;
    }
}
