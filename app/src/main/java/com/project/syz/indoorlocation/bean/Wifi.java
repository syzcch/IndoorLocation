package com.project.syz.indoorlocation.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SYZ on 2016/9/3.
 */
public class Wifi implements Serializable {
    private static final long serialVersionUID = -7060210544600464482L;
    private List<WifiItem> wifiList = new ArrayList<WifiItem>() ;
    private static final String TAG = "Wifi";
    private static Wifi singleWifi = null;

    private Wifi(){}

    public synchronized  static Wifi getInstance() {
        if (singleWifi == null) {
            singleWifi = new Wifi();
        }
        return singleWifi;
    }

    public static boolean isSetWifi(){
        return (null == singleWifi);
    }

    public void add(WifiItem wi){
        wifiList.add(wi);
    }

    public List<WifiItem> get(){
        return wifiList;
    }

    public void setWifiList(List<WifiItem> li){
        wifiList = li;
    }
}
