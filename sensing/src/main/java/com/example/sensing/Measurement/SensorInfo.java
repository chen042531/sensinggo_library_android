package com.example.sensing.Measurement;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.example.sensing.Data.DataListenerInterface;
import com.example.sensing.Data.SGData;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.SENSOR_SERVICE;

public class SensorInfo implements SensorEventListener {

    private SensorManager sensorManager;
    public static float[] gSensorValues = new float[3]; // triaxial acceleration
    public static float[] magneticValues = new float[3];
    public static int lightValue;
    private static int sensorInterval;
    public static String proximityValue;
    public static float pressureValue,pvalue;
    private float[] rMatrix = new float[9];    //rotation matrix

    // orientation values, [0]: Azimuth, [1]: Pitch, [2]: Roll
    public static float[] orienValue = new float[3];

    //use for comparison
    private static float[] gSensorValuesTemp = new float[3];
    private static float[] magneticValuesTemp = new float[3];
    private static float[] orienValueTemp = new float[3];

    //    private SGData sg_data;
    public Context mContext;
    public  TextView tt;
    public DataListenerInterface dataListenerInterface;
    public SensorInfo(Context mContext) {
        this.mContext = mContext;
        this.sensorManager = (SensorManager)mContext.getSystemService(SENSOR_SERVICE);
    }


    private Timer mTimer;
    private TimerTask mTimerTask;
    private Handler mHandler;
    private SensorInfo SensorInfo;

    private  void initBfRun() {
        SensorInfo = this;
        gSensorValues[0] = gSensorValues[1] = gSensorValues[2] = 10;
        magneticValues[0] = magneticValues[1] = magneticValues[2] = 0;
        orienValue[0] = orienValue[1] = orienValue[2] = 0;
        lightValue = -1;
        proximityValue = "near";
        pressureValue = 0;

    }

    protected void setSensorInfo() {
        Sensor mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (mAccelerometer == null){
            //No Accelerometer Sensor!
            Log.i("ssssss","No Accelerometer Sensor!");
        } else{
            sensorManager.registerListener(this, mAccelerometer, sensorInterval,sensorInterval);
        }

        Sensor mProximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (mProximity == null){
            //No Proximity Sensor!
            Log.i("ssssss","No Proximity Sensor!");
        } else{
            sensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
        }

        // Measures the ambient light level (illumination) in lx.
        Sensor mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (mLight == null){
            //No mLight Sensor!
            Log.i("ssssss","No mLight Sensor!");
        } else{
            sensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
        }

        // Measures the ambient air pressure in hPa or mbar.
        Sensor mPressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if (mPressure == null){
            //No mPressure Sensor!
            Log.i("ssssss","No mPressure Sensor!");
        } else{
            sensorManager.registerListener(this, mPressure, SensorManager.SENSOR_DELAY_NORMAL);
        }

        // Measures the ambient geomagnetic field for all three physical axes (x, y, z) in 弮T.
        Sensor mMagnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (mMagnetic == null){
            //No mMagnetic Sensor!
            Log.i("ssssss","No mMagnetic Sensor!");
        } else{
            sensorManager.registerListener(this, mMagnetic, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void startService(DataListenerInterface dataListener,
                             int sInterval) {
        sensorInterval=sInterval;
        initBfRun();
        setSensorInfo();

        dataListenerInterface = dataListener;
    }
    public void startService(DataListenerInterface dataListener) {

        initBfRun();
        setSensorInfo();
        Log.i("ttgg", String.valueOf(dataListener));
        dataListenerInterface = dataListener;
        Log.i("ttgg", String.valueOf(dataListenerInterface));
    }
    public void startService_acc(DataListenerInterface dataListener) {
        initBfRun();
        Sensor mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (mAccelerometer == null){
            //No Accelerometer Sensor!
            Log.i("ssssss","No Accelerometer Sensor!");
        } else{
            sensorManager.registerListener(this, mAccelerometer, sensorInterval,sensorInterval);
        }
        dataListenerInterface = dataListener;
    }
    public void startService_Proximity(DataListenerInterface dataListener) {
        initBfRun();
        Sensor mProximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (mProximity == null){
            //No Proximity Sensor!
            Log.i("ssssss","No Proximity Sensor!");
        } else{
            sensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_FASTEST);
        }
        dataListenerInterface = dataListener;
    }
    public void startService_LIGHT(DataListenerInterface dataListener) {
        initBfRun();
        Sensor mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (mLight == null){
            //No mLight Sensor!
            Log.i("ssssss","No mLight Sensor!");
        } else{
            sensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_FASTEST);
        }
        dataListenerInterface = dataListener;
    }
    public void startService_PRESSURE(DataListenerInterface dataListener) {
        initBfRun();
        Sensor mPressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if (mPressure == null){
            //No mPressure Sensor!
            Log.i("ssssss","No mPressure Sensor!");
        } else{
            sensorManager.registerListener(this, mPressure, SensorManager.SENSOR_DELAY_FASTEST);
        }
        dataListenerInterface = dataListener;
    }
    public void startService_MAG(DataListenerInterface dataListener) {
        initBfRun();
        Sensor mMagnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (mMagnetic == null){
            //No mMagnetic Sensor!
            Log.i("ssssss","No mMagnetic Sensor!");
        } else{
            sensorManager.registerListener(this, mMagnetic, SensorManager.SENSOR_DELAY_FASTEST);
        }
        dataListenerInterface = dataListener;
    }
    public void stopService() {
        // Accelerometer, Light, Proximity, Barometer, Magnetometer
        sensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(final SensorEvent event) {
        // TODO Auto-generated method stub
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                float[] values = event.values;
                // Update the value of triaxial acceleration when an axis changed exceed 1
                if(Math.abs(gSensorValuesTemp[0]-values[0])>=1 || Math.abs(gSensorValuesTemp[1]-values[1])>=1 || Math.abs(gSensorValuesTemp[2]-values[2])>=1){
                    gSensorValuesTemp[0] = values[0];
                    gSensorValuesTemp[1] = values[1];
                    gSensorValuesTemp[2] = values[2];
                }
                gSensorValues[0] = values[0];
                gSensorValues[1] = values[1];
                gSensorValues[2] = values[2];
                dataListenerInterface.onDataReceived();
                break;

            case Sensor.TYPE_PROXIMITY:
                String str;
                if (event.values[0] == 0) {
                    str = "near";
                } else {
                    str = "far";
                }
                proximityValue = str;
                pvalue = event.values[0];
                Log.i("ssssss", "TYPE_PROXIMITY: "+event.values[0]);
//                        FileMaker.write(JsonParser.sensorInfoToJson("PROXIMITY", str));
//                sg_data.pvalue = event.values[0];
                dataListenerInterface.onDataReceived();
                break;

            case Sensor.TYPE_LIGHT: //lux
                if(Math.abs(lightValue-event.values[0])>=10){
                    lightValue = (int)event.values[0];

                    Log.i("ssssss", "TYPE_LIGHT: "+lightValue);
//                            FileMaker.write(JsonParser.sensorInfoToJson("LIGHT", ""+event.values[0]));
//                    sg_data.lightValue = (int)event.values[0];
//                    dataListenerInterface.onDataReceived();
                }
                dataListenerInterface.onDataReceived();
                break;

            case Sensor.TYPE_PRESSURE: //hPa
                pressureValue = event.values[0];
                Log.i("ssssss", "pressureValue: "+pressureValue);
//                    sg_data.pressureValue = event.values[0];
                dataListenerInterface.onDataReceived();
                 break;

            case Sensor.TYPE_MAGNETIC_FIELD: // Measures the ambient geomagnetic field for all three physical axes (x, y, z) in 弮T.
                float[] mValues = event.values;
                if(Math.abs(magneticValuesTemp[0]-mValues[0])>=10 || Math.abs(magneticValuesTemp[1]-mValues[1])>=10 || Math.abs(magneticValuesTemp[2]-mValues[2])>=10){
                    magneticValuesTemp[0] = mValues[0];
                    magneticValuesTemp[1] = mValues[1];
                    magneticValuesTemp[2] = mValues[2];
                    Log.i("ssssss", "magneticValuesTemp: "+magneticValuesTemp[0]+", "+magneticValuesTemp[1]+", "+magneticValuesTemp[2]);
//                            FileMaker.write(JsonParser.sensorInfoToJson("MAGNETIC_FIELD", magneticValuesTemp[0]+","+magneticValuesTemp[1]+","+magneticValuesTemp[2]));
                }
                magneticValues[0] = mValues[0];
                magneticValues[1] = mValues[1];
                magneticValues[2] = mValues[2];

                dataListenerInterface.onDataReceived();
                break;
        }

//        float[] tempValues = new float[3];
//        SensorManager.getRotationMatrix(rMatrix, null, gSensorValues, magneticValues);
//        SensorManager.getOrientation(rMatrix, tempValues);
//        if(Math.abs(orienValueTemp[0]-(float) Math.toDegrees(tempValues[0]))>=15 ||
//                Math.abs(orienValueTemp[1]-(float) Math.toDegrees(tempValues[1]))>=15 ||
//                Math.abs(orienValueTemp[2]-(float) Math.toDegrees(tempValues[2]))>=15
//        ){
//            orienValueTemp[0] = (float) Math.toDegrees(tempValues[0]);
//            orienValueTemp[1] = (float) Math.toDegrees(tempValues[1]);
//            orienValueTemp[2] = (float) Math.toDegrees(tempValues[2]);
//            Log.i("ssssss", "orienValueTemp: "+orienValueTemp[0]+", "+orienValueTemp[1]+", "+orienValueTemp[2]);
////                    FileMaker.write(JsonParser.sensorInfoToJson("ROTATION",orienValueTemp[0]+","+orienValueTemp[1]+","+orienValueTemp[2]));
//
//        }
//        orienValue[0] = (float) Math.toDegrees(tempValues[0]);
//        orienValue[1] = (float) Math.toDegrees(tempValues[1]);
//        orienValue[2] = (float) Math.toDegrees(tempValues[2]);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

//    public float getData() {
////        Log.i("ssss", String.valueOf(gSensorValuesTemp[0]));
//        return pvalue;
//
//    }
}
//http://berniechenopenvpn.blogspot.com/2016/08/sensormanagerregisterlitener.html