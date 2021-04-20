package com.example.sensing.Measurement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.example.sensing.Data.DataListenerInterface;

public class ScreenState extends BroadcastReceiver {


    public DataListenerInterface dataListenerInterface;
    public static String screen_state = "on";


    public Context mContext;

    public ScreenState(Context mContext) {
        this.mContext = mContext;
    }
    public void start(DataListenerInterface dataListener) {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mContext.registerReceiver(this, intentFilter);
    }
    public void stop() {
        mContext.unregisterReceiver(this);
    }
    @Override
    public void onReceive(Context context, Intent intent) {


        String action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) {
            screen_state = "on";
        }
        else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            screen_state = "off";
        }
        dataListenerInterface.onDataReceived();
    }

}
