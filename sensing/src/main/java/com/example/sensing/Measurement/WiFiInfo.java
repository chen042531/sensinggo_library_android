package com.example.sensing.Measurement;

import android.content.Context;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import static android.content.Context.SENSOR_SERVICE;

public class WiFiInfo {
    WifiManager wifi;
    public Context mContext;
    private static ConnectivityManager connManager;
    private static NetworkInfo mWifi;
    public static String servingBSSID = "null";
    public static String servingChan = "null";
    public static String servingFreq = "null";
    public static String servingIP = "null";
    public static String servingLevel = "null";
    public static String servingMAC = "null";
    public static String servingSSID = "null";
    public static int servingSpeed = 0;

    public static boolean wifiState = false;

    public WiFiInfo(Context mContext) {
        this.mContext = mContext;
        this.wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void getWiFiInfo() {
        WifiInfo wifiInfo = wifi.getConnectionInfo();
        servingSSID = wifiInfo.getSSID(); //wifi id
        servingBSSID = wifiInfo.getBSSID(); // wifi mac ip
        servingSpeed = wifiInfo.getLinkSpeed();
        servingMAC = wifiInfo.getMacAddress();
        int version = Build.VERSION.SDK_INT;
        if (version >= 20) {
            servingFreq = String.valueOf(wifiInfo.getFrequency());
            servingChan = String.valueOf(convertFrequencyToChannel(wifiInfo.getFrequency()));
        } else {
            servingFreq = "Null";
            servingChan = "Null";
        }
        servingSpeed = wifiInfo.getLinkSpeed();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = String.format("%d.%d.%d.%d",
                (ipAddress & 0xff),
                (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff),
                (ipAddress >> 24 & 0xff));
        servingIP = ip;
        servingSSID = wifiInfo.getSSID();
        servingBSSID = wifiInfo.getBSSID();
        servingMAC = wifiInfo.getMacAddress();

        servingLevel = String.valueOf(wifiInfo.getRssi()); // Returns the received signal strength indicator of the current 802.11 network, in dBm.
    }

    // For wifi frequency to channel
    public static int convertFrequencyToChannel(int freq) {
        if (freq >= 2412 && freq <= 2484) {
            return (freq - 2412) / 5 + 1;
        } else if (freq >= 5170 && freq <= 5825) {
            return (freq - 5170) / 5 + 34;
        } else {
            return -1;
        }
    }

}
