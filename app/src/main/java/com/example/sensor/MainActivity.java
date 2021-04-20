package com.example.sensor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HardwarePropertiesManager;
import android.util.Log;
import android.widget.TextView;

import com.example.sensing.Data.SGData;
import com.example.sensing.FileMaker.writeFile;
import com.example.sensing.FileSender.sendData;
import com.example.sensing.Measurement.BatteryState;
import com.example.sensing.Measurement.CellularInfo;
import com.example.sensing.Data.DataListener;
import com.example.sensing.Measurement.LocationInfo;
import com.example.sensing.Measurement.NetworkState;
import com.example.sensing.Measurement.PhoneInfo;
import com.example.sensing.Measurement.PhoneState;
import com.example.sensing.Measurement.WiFiInfo;
import com.example.sensing.Measurement.SensorInfo;
import com.example.sensing.SensingGO;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static android.os.BatteryManager.EXTRA_PLUGGED;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private SensorInfo sr ;
    private LocationInfo location;
    private WiFiInfo wifiinfo;
    private CellularInfo cell;
    private NetworkState networkstate;
    private PhoneState phoneState;
    private PhoneInfo phoneInfo;
    private BatteryState batteryState;
    private CellularInfo cellularInfo;
    public  TextView text;

    public HardwarePropertiesManager hardwarePropertiesManager;

    public static long start,end,test_time;
    public static boolean first_time_flag=true;

    public ConnectivityManager connectivityManager;

    public String Network_type = null;


    private Network nw;
    private NetworkCapabilities actNw;
    private NetworkInfo nwInfo;
    private Handler taskHandler;
    private Runnable repeatativeTaskRunnable;
    private Context mContext;

    private int type = 0;
    private int count = 0;
    private int ROUND = 10 ;
    private boolean first_ = false;
    private writeFile write_file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        try {
            write_file = new writeFile(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        sr =  new SensorInfo(this);
//        final TextView tv1 = (TextView)findViewById(R.id.textView2);
//
//        sr.startService(new DataListener(sr,tv1){
//            @Override
//            public void onDataReceived() {
//
//                Log.i("mainsensor", "sensor_dddd: "+sr.magneticValues[0]);
//                String s = String.valueOf(sr.magneticValues[0]);
//                tv1.setText(s);
//            }
//        },1000);




//
//        SGData sdata = new SGData();
//        try {
//            Log.i("SGData",sdata.getSGData().toString());
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        try {
//            writeFile write_file = new writeFile(this);
//            write_file.setUserID("user6");
//            write_file.write(sdata);
//            write_file.write(sdata);
//            write_file.write(sdata);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        sendData s = new sendData(this);
//        s.setUserID("user6");
//        s.execute();
        ///////////test

        taskHandler = new Handler();


        repeatativeTaskRunnable = new Runnable() {
            public void run() {
                //DO YOUR THINGS
                if(type == 0){
                    first_=true;
                    start = System.currentTimeMillis();
                    sr =  new SensorInfo(mContext);
                    sr.startService_acc(new DataListener(sr){
                        @Override
                        public void onDataReceived() {
                            end = System.currentTimeMillis();

                            if(count == ROUND){

                                try {
                                    write_file.write_testTime("time_lib","\n");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                type = type + 1;
                                count = 0;
                            }
                            if(first_ && type==0){
                                first_=false;
                                Log.i("test_time", String.valueOf(test_time));
                                try {
                                    write_file.write_testTime("time_lib",(end-start)+"");
                                    write_file.write_testTime("time_lib",",");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                sr.stopService();
                                count = count+1;
                            }
                        }
                    });
                }
                if(type == 1){
                    first_=true;
                    start = System.currentTimeMillis();
                    sr =  new SensorInfo(mContext);
                    sr.startService_MAG(new DataListener(sr){
                        @Override
                        public void onDataReceived() {
                            end = System.currentTimeMillis();

                            if(count == ROUND){

                                try {
                                    write_file.write_testTime("time_lib","\n");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                type = type + 1;
                                count = 0;
                            }
                            if(first_ && type==1){
                                first_=false;
//                                Log.i("test_time", String.valueOf(test_time));
                                try {
                                    write_file.write_testTime("time_lib",(end-start)+"");
                                    write_file.write_testTime("time_lib",",");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                sr.stopService();
                                count = count+1;
                            }
                        }
                    });
                }
                if(type == 2){
                    first_=true;
                    start = System.currentTimeMillis();
                    sr =  new SensorInfo(mContext);
                    sr.startService_LIGHT(new DataListener(sr){
                        @Override
                        public void onDataReceived() {
                            end = System.currentTimeMillis();

                            if(count == ROUND){

                                try {
                                    write_file.write_testTime("time_lib","\n");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                type = type + 1;
                                count = 0;
                            }
                            if(first_ && type==2){
                                first_=false;
                                Log.i("test_time", String.valueOf(test_time));
                                try {
                                    write_file.write_testTime("time_lib",(end-start)+"");
                                    write_file.write_testTime("time_lib",",");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                sr.stopService();
                                count = count+1;
                            }
                        }
                    });
                }
                if(type == 3){
                    first_=true;
                    start = System.currentTimeMillis();
                    sr =  new SensorInfo(mContext);
                    sr.startService_Proximity(new DataListener(sr){
                        @Override
                        public void onDataReceived() {
                            end = System.currentTimeMillis();

                            if(count == ROUND){

                                try {
                                    write_file.write_testTime("time_lib","\n");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                type = type + 1;
                                count = 0;
                            }
                            if(first_ && type==3){
                                first_=false;
                                Log.i("test_time", String.valueOf(test_time));
                                try {
                                    write_file.write_testTime("time_lib",(end-start)+"");
                                    write_file.write_testTime("time_lib",",");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                sr.stopService();
                                count = count+1;
                            }
                        }
                    });
                }
                if(type == 4){
                    first_=true;
                    start = System.currentTimeMillis();
                    location =  new LocationInfo(mContext);
                    Log.i("gggggggg","gggg");
                    location.startNetwork_location(new DataListener(location){
                        @Override
                        public void onDataReceived() {
                            end = System.currentTimeMillis();

                            if(count == ROUND){

                                try {
                                    write_file.write_testTime("time_lib","\n");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                type = type + 1;
                                count = 0;
                            }
                            if(first_ && type==4){
                                first_=false;
                                Log.i("test_time", String.valueOf(test_time));
                                try {
                                    write_file.write_testTime("time_lib",(end-start)+"");
                                    write_file.write_testTime("time_lib",",");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                location.stopNetwork_location();
                                count = count+1;
                            }
                        }
                    });
                }
                if(type == 5){
                    first_=true;
                    start = System.currentTimeMillis();
                    cell =  new CellularInfo(mContext);
                    cell.start(new DataListener(cell){
                        @Override
                        public void onDataReceived() {
                            end = System.currentTimeMillis();

                            if(count == ROUND){

                                try {
                                    write_file.write_testTime("time_lib","\n");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                type = type + 1;
                                count = 0;
                            }
                            if(first_ && type==5){
                                first_=false;
                                Log.i("test_time", String.valueOf(test_time));
                                try {
                                    write_file.write_testTime("time_lib",(end-start)+"");
                                    write_file.write_testTime("time_lib",",");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                cell.stop();
                                count = count+1;
                            }
                        }
                    });
                }
                if(type == 6){
                    first_=true;
                    start = System.currentTimeMillis();
                    batteryState =  new BatteryState(mContext);
                    batteryState.start(new DataListener(batteryState){
                        @Override
                        public void onDataReceived() {
                            end = System.currentTimeMillis();

                            if(count == ROUND){

                                try {
                                    write_file.write_testTime("time_lib","\n");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                type = type + 1;
                                count = 0;
                            }
                            if(first_ && type==6){
                                first_=false;
                                Log.i("test_time", String.valueOf(test_time));
                                try {
                                    write_file.write_testTime("time_lib",(end-start)+"");
                                    write_file.write_testTime("time_lib",",");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                batteryState.stop();
                                count = count+1;
                            }
                        }
                    });
                }
                if(type == 7){
                    first_=true;
                    start = System.currentTimeMillis();
                    phoneState =  new PhoneState(mContext);
                    phoneState.start(new DataListener(phoneState){
                        @Override
                        public void onDataReceived() {
                            end = System.currentTimeMillis();

                            if(count == ROUND){

                                try {
                                    write_file.write_testTime("time_lib","\n");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                type = type + 1;
                                count = 0;
                            }
                            if(first_ && type==7){
                                first_=false;
                                Log.i("test_time", String.valueOf(test_time));
                                try {
                                    write_file.write_testTime("time_lib",(end-start)+"");
                                    write_file.write_testTime("time_lib",",");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                phoneState.stop();
                                count = count+1;
                            }
                        }
                    });
                }

                taskHandler.postDelayed(repeatativeTaskRunnable, 1500);
            }

        };
        startHandler();

//        test_wifi,
        long sum = 0;
        int count = 10;
        for(int i = 0; i<count; i++){

//            wifi
//            start = System.nanoTime();
//            WiFiInfo wifiInfo;
//            wifiInfo = new WiFiInfo(this);
//            wifiInfo.getWiFiInfo();
//            end = System.nanoTime();
//            test_time = end-start;
//            Log.i("test_time", String.valueOf(test_time));
//            sensor
//            start = System.nanoTime();
//            sr =  new SensorInfo(this);
//            sr.startService(new DataListener(sr){
//                @Override
//                public void onDataReceived() {
//                    end = System.nanoTime();
//                    if(first_time_flag){
//                        test_time = end-start;
//                        Log.i("test_time", String.valueOf(test_time));
//                        first_time_flag=false;
//                        sr.stopService();
//                    }
//                }
//            });
//            sr.stopService();

//
//            gps = new LocationInfo(this);
//            gps.startService(new DataListener(gps){
//                @Override
//                public void onDataReceived() {
////                gps_endTime = System.nanoTime();
////                Log.i("test_gps", String.valueOf((gps_endTime - gps_startTime)));
////
//                    Log.i("test_gps", String.valueOf((LocationInfo.userlocationN)));
//                }
//            });
//            start = System.nanoTime();
//            networkstate = new NetworkState(this);
//            networkstate.getNetworkType();
//            end = System.nanoTime();
//            test_time = end-start;
//            Log.i("test_time", String.valueOf(test_time));
//            start = System.nanoTime();
//            phoneInfo = new PhoneInfo(this);
//            phoneInfo.startService(new DataListener(phoneInfo){
//                @Override
//                public void onDataReceived() {
//                    end = System.nanoTime();
//                    if(first_time_flag){
//                        test_time = end-start;
//                        Log.i("test_time", String.valueOf(test_time));
//                        first_time_flag=false;
//
//                    }
//
//                }
//            });
//                        start = System.nanoTime();
//            phoneState = new PhoneState(this);
//            phoneState.startService(new DataListener(phoneState){
//                @Override
//                public void onDataReceived() {
//                    end = System.nanoTime();
//                    if(first_time_flag){
//                        test_time = end-start;
//                        Log.i("test_time", String.valueOf(test_time));
//                        first_time_flag=false;
//
//                    }
//
//                }
//            });
//            cellularInfo = new CellularInfo(this);
//            cellularInfo.startService(new DataListener(cellularInfo){
//                @Override
//                public void onDataReceived() {
//                    end = System.nanoTime();
//                    if(first_time_flag){
//                        test_time = end-start;
//                        Log.i("test_time", String.valueOf(test_time));
//                        first_time_flag=false;
//
//                    }
//
//                }
//            });
        }
        /////////////////////

        //////////////////////
        sum = 0;
        count = 10;
        for(int i = 0; i<count; i++){



            //wifi
//            start = System.nanoTime();
//            WifiManager wifi = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
//            WifiInfo wifiInfo = wifi.getConnectionInfo();
//            wifiInfo.getSSID();
//            end = System.nanoTime();
//            test_time = end - start;
//            Log.i("test_time", String.valueOf(test_time));
            //sensor

            SensorManager sm;
            start = System.nanoTime();
            sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
            final int sensorType = Sensor.TYPE_LIGHT;
            SensorEventListener myAccelerometerListener = new SensorEventListener(){


                public void onSensorChanged(SensorEvent sensorEvent){
                    if(sensorEvent.sensor.getType() == sensorType){
                        end = System.nanoTime();
                        if(first_time_flag) {
                            test_time = end - start;
                            Log.i("test_time", String.valueOf(test_time));
                        }
                        first_time_flag = false;

                    }
                }

                public void onAccuracyChanged(Sensor sensor , int accuracy){

                }
            };
            sm.registerListener(myAccelerometerListener,sm.getDefaultSensor(sensorType),SensorManager.SENSOR_DELAY_FASTEST);


//            networkstate
//            start = System.nanoTime();
//            connectivityManager = (ConnectivityManager)this.getSystemService(CONNECTIVITY_SERVICE);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                nw = connectivityManager.getActiveNetwork();
//                actNw = connectivityManager.getNetworkCapabilities(nw);
//            }
//            else {
//                nwInfo = connectivityManager.getActiveNetworkInfo();
//            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                Network nw = connectivityManager.getActiveNetwork();
//                NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
//                if (actNw != null) {
//                    if (actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
//                        Network_type = "MOBILE";
//                        Log.d("networkstateTest", "mobile") ;
//
//                    } else if (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
//                        Network_type = "WIFI";
//                        Log.d("networkstateTest", "wifi") ;
//
//                    }  else if (actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
//                        Network_type = "ETHERNET";
//
//                    }
//                    else{
//                        Network_type = null;
//
//                    }
//
//                }
//                else {
//                    if(nwInfo.getTypeName()!=null) {
//                        Network_type = nwInfo.getTypeName();
//
//                    }
//                    else{
//                        Network_type = "null";
//                    }
//
//                }
//            }
//            end = System.nanoTime();
//            test_time = end - start;
//            Log.i("test_time", String.valueOf(test_time));

//              CellularInfo
//                SensorManager sm;
//            start = System.nanoTime();
//            sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
//            int sensorType = Sensor.TYPE_ACCELEROMETER;
//            final SensorEventListener myAccelerometerListener = new SensorEventListener(){
//
//
//                public void onSensorChanged(SensorEvent sensorEvent){
//                    if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
//                        end = System.nanoTime();
//                        if(first_time_flag) {
//                            test_time = end - start;
//                            Log.i("test_time", String.valueOf(test_time));
//                        }
//                        first_time_flag = false;
//
//                    }
//                }
//
//                public void onAccuracyChanged(Sensor sensor , int accuracy){
//
//                }
//            };
//            sm.registerListener(myAccelerometerListener,sm.getDefaultSensor(sensorType),SensorManager.SENSOR_DELAY_FASTEST);

        }
        /////////////////////

        //////////////////////





        /////////
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    //test
    void startHandler() {
        taskHandler.postDelayed(repeatativeTaskRunnable, 5 * 100);
    }

    void stopHandler() {
        taskHandler.removeCallbacks(repeatativeTaskRunnable);
    }


}
