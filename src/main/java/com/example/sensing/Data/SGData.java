package com.example.sensing.Data;

import android.location.GpsStatus;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import com.example.sensing.Measurement.CellularInfo;
import com.example.sensing.Measurement.LocationInfo;
import com.example.sensing.Measurement.NetworkState;
import com.example.sensing.Measurement.PhoneInfo;
import com.example.sensing.Measurement.PhoneState;
import com.example.sensing.Measurement.WiFiInfo;
import com.example.sensing.Measurement.SensorInfo;
public class SGData {
//    AppicationType
//            ApplicationVersion
//    APPVersion
//            Account
//    SHANSERVER
//            equipmentId
//    Model
//            BatteryLevel
//    BatteryTemp
//            Time
//    TimeStamp
//    TimeZone(GMT)
//    DataVersion
//            TestType
//    Screen state
//    Cpu
//            Ram
//    CallID
//            NetworkType
//    ConnectionState
//            LatG
//    LngG
//            SpeedG
//    LatN
//            LngN
//    SpeedN
//            GPSTimeG
//    GPSTimeStampG
//    Event
//    ACCELEROMETER
//    LIGHT
//    ROTATION
//    PROXIMITY
//    MAGNETIC_FIELD

//    ServingCellInfo/NeighborCellInfo
        //CellInfoType
        //        CellID
        //    CellMCC
        //            CellMNC
        //    CellPCI
        //            CellTAC
        //    RSSI
        //            SINR
        //    RSRQ
        //            RSRP

//    handover
    //    FromCellID
    //        ToCellID
    //    CellResidenceTime
//    ServingWifi/NeighborWiFi
//SSID
//        level
//    Freq
//            Chan
//    cap


//    Event":"AppsInfo","LastTime":174145,"AppsInfo":[{"AppName":"edu.nctu.wirelab.sensinggo","TotalTx":204940383,"TotalRx":79512950,"DeltaTx":336,"DeltaRx":0}],"AllAppsTxBytes(MB)":"2870.36","AllAppsRxBytes(MB)":"24990.77"}
//    PhoneState
    public  String phoneState = "IDLE"; //IDLE, OFFHOOK, RINGING
    public  String callState = "IDLE"; //Callout, ""Callin"", RINGING
    public  String callID = "null";
    public  String startCallTime, endCallTime;

    public  int callNum = 0, callExcessNum = 0;
    public  long callStartAt = 0, callHoldingTime = 0, excessLife = 0;
    public  double avgCallHoldTime = 0, avgExcessLife = 0;
    public  double ttlCallHoldTime = 0, ttlExcessLife = 0;
    public boolean FirstCallCell = false;

    // phoneInfo
    public  double electricity;
    public int health = 0;
    public int icon_small = 0;
    public  int level = 0;
    public  int lastLevel = -1;
    public  double lastElect = -1;
    public int plugged = 0;
    public  boolean present = false;
    public  int scale = 0;
    public  int status = 0;
    public  String technology = "0";
    public  String tmp;
    public  double temperature = 0.0;
    public  int voltage = 0;
    public  String screen_state = "on";

    public static double ram_uti;

    //    LocationInfo
    public  LocationManager locationManager;
    public LocationListener locationListener;
    //G: GPS, N: Network, S: Satellite
    public android.location.Location userlocationG = null, userlocationN = null;
    public static String speedG = null,speedN = null;
    public static Long updateTimeG=0L,updateTimeN=0L,updateTimeS;
    public static String updateTimeStampG ="unknown",updateTimeStampN ="unknown";
    public static GpsStatus gpsStatus = null;

    //    cellularinfo
    public  String cellInfoType;

    public int PreAtCellID;

    //LTE info
    public  int lteCellRSRP, lteCellID, lteCellMCC, lteCellMNC, lteCellPCI, lteCellTAC, lteCellRSSI, lteCellRSRQ, lteCellRSSNR;
    public long cellTimeInterval;

    //Wcdma info
    public int wcdmaAtCellPsc, wcdmaAtCellLac, wcdmaAtCellID, wcdmaAtCellMCC, wcdmaAtCellMNC, wcdmaAtCellSignalStrength;

    public  int passCellNum = 0;
    public  int nowCellID = 0;
    public  long stayCellAt = 0, cellHoldTime = 0;
    public  double avgCellResidenceTime = 0, avgCellHoldTime, ttlCellResidenceTime = 0;


    //    sensorInfo
    public float[] gSensorValues = new float[3]; // triaxial acceleration
    public  float[] magneticValues = new float[3];
    public  int lightValue;
//    public  int sensorInterval;
    public String proximityValue;
    public float pressureValue,pvalue;
    // orientation values, [0]: Azimuth, [1]: Pitch, [2]: Roll
    public float[] orienValue = new float[3];


    // networkstate
    public String Network_type;


    //wifi
    public String servingBSSID = "null";
    public String servingChan = "null";
    public String servingFreq = "null";
    public String servingIP = "null";
    public String servingLevel = "null";
    public String servingMAC = "null";
    public String servingSSID = "null";
    public int servingSpeed = 0;

    private Class cls = this.getClass();


    public Object getSGData() throws IllegalAccessException, JSONException {
        JSONObject dataInfo = new JSONObject();
        for (Field field : cls.getDeclaredFields()){
            String field_name = field.getName();
            Log.i("SGData", field_name+": "+String.valueOf(field.get(this)));
            dataInfo.put(field_name,field.get(this));
        }
        return dataInfo;
    }
}
