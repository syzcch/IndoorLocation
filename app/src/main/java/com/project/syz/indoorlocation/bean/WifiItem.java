package com.project.syz.indoorlocation.bean;

import java.io.Serializable;

/**
 * Created by SYZ on 2016/9/3.
 */
public class WifiItem  implements Serializable {
    private static final long serialVersionUID = -7060210544600464481L;
    private String SSID;
    private int level;

    public WifiItem(String SSID, int level){
        this.SSID = SSID;
        this.level = level;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
