package com.example.sensing.Measurement;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

import com.example.sensing.Data.DataListenerInterface;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Objects;

import static android.content.Context.ACTIVITY_SERVICE;

public class PhoneInfo {
    public static String cpuInfo() throws IOException {
        String[] DATA = {"/system/bin/cat", "/proc/cpuinfo"};
        ProcessBuilder processBuilder = new ProcessBuilder(DATA);
        Process process = processBuilder.start();
        InputStream inputStream = process.getInputStream();
        byte[] byteArry = new byte[1024];
        String output = "";
        while (inputStream.read(byteArry) != -1) {
            output = output + new String(byteArry);
        }
        inputStream.close();
        return output;
    }
}