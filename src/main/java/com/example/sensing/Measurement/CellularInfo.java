package com.example.sensing.Measurement;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.sensing.Data.DataListenerInterface;

import java.lang.ref.WeakReference;
import java.util.List;

public class CellularInfo extends PhoneStateListener {
    private long curTestTime = 0, preTestTime = 0;
    private List<CellInfo> cellInfoList;
    private WeakReference<Context> serviceContext;
    private Context mContext;
    //CellInfoType (LTE or Wcdma)
    public static String cellInfoType;

    public static int PreAtCellID;

    //LTE info
    public static int lteCellRSRP, lteCellID, lteCellMCC, lteCellMNC, lteCellPCI, lteCellTAC, lteCellRSSI, lteCellRSRQ, lteCellRSSNR;
    public static long cellTimeInterval;

    //Wcdma info
    public static int wcdmaAtCellPsc, wcdmaAtCellLac, wcdmaAtCellID, wcdmaAtCellMCC, wcdmaAtCellMNC, wcdmaAtCellSignalStrength;

    public static int passCellNum = 0;
    public static int nowCellID = 0;
    public static long stayCellAt = 0, cellHoldTime = 0;
    public static double avgCellResidenceTime = 0, avgCellHoldTime, ttlCellResidenceTime = 0;

    private TelephonyManager teleManager;

    public DataListenerInterface dataListenerInterface;
    public CellularInfo(Context context) {
        this.serviceContext = new WeakReference<>(context);
        this.mContext = context;
        this.teleManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);;
        initBfRun();
    }
    public void start(DataListenerInterface dataListener) {
        dataListenerInterface = dataListener;
        teleManager.listen(this, CellularInfo.LISTEN_SIGNAL_STRENGTHS);
    }
    public void stop() {
        teleManager.listen(this, CellularInfo.LISTEN_NONE);
    }
    public static void calculateAvg() {
        if (passCellNum != 0) {
            avgCellResidenceTime = ttlCellResidenceTime / passCellNum;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onSignalStrengthsChanged(final android.telephony.SignalStrength signalstrength) {

        curTestTime = System.currentTimeMillis();
        if (ContextCompat.checkSelfPermission(serviceContext.get(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            cellInfoList = teleManager.getAllCellInfo();
        }
        if (cellInfoList != null) {
            for (CellInfo cellInfo : cellInfoList) {
                if (cellInfo.isRegistered()) {
                    /**
                     *  Make sure the cellInfo is from 3g or 4g phone
                     *  We do not consider the 2g case
                     */
                    if (cellInfo instanceof CellInfoLte) {
                        String oneCellInfo = cellInfo.toString();
                        String[] cellInfoParts = oneCellInfo.split(" ");
                        Log.d("cellInfoParts", ": " + cellInfoParts);
                        for (String str : cellInfoParts) {
                            String[] part = str.split("=");
                            if (part.length != 0 && part[0].compareTo("rsrq") == 0) {
                                lteCellRSRQ = Integer.valueOf(part[1]);
                            }
                        }
                        cellInfoType = "LTE";

                        /* cast to CellInfoLte and call all the CellInfoLte methods you need */
                        // gets signal strength as dBm
                        lteCellRSRP = ((CellInfoLte) cellInfo).getCellSignalStrength().getDbm();

                        // Gets the LTE cell identity: (returns 28-bit Cell Identity, Integer.MAX_VALUE if unknown)
                        lteCellID = ((CellInfoLte) cellInfo).getCellIdentity().getCi();
                        // Gets the LTE MCC: (returns 3-digit Mobile Country Code, 0..999, Integer.MAX_VALUE if unknown)
                        lteCellMCC = ((CellInfoLte) cellInfo).getCellIdentity().getMcc();

                        // Gets theLTE MNC: (returns 2 or 3-digit Mobile Network Code, 0..999, Integer.MAX_VALUE if unknown)
                        lteCellMNC = ((CellInfoLte) cellInfo).getCellIdentity().getMnc();

                        // Gets the LTE PCI: (returns Physical Cell Id 0..503, Integer.MAX_VALUE if unknown)
                        lteCellPCI = ((CellInfoLte) cellInfo).getCellIdentity().getPci();

                        // Gets the LTE TAC: (returns 16-bit Tracking Area Code, Integer.MAX_VALUE if unknown)
                        lteCellTAC = ((CellInfoLte) cellInfo).getCellIdentity().getTac();


                    } else if (cellInfo instanceof CellInfoWcdma) {
                        cellInfoType = "Wcdma";

                        wcdmaAtCellSignalStrength = ((CellInfoWcdma) cellInfo).getCellSignalStrength().getDbm();

                        wcdmaAtCellID = ((CellInfoWcdma) cellInfo).getCellIdentity().getCid();

                        wcdmaAtCellMCC = ((CellInfoWcdma) cellInfo).getCellIdentity().getMcc();

                        wcdmaAtCellMNC = ((CellInfoWcdma) cellInfo).getCellIdentity().getMnc();

                        wcdmaAtCellPsc = ((CellInfoWcdma) cellInfo).getCellIdentity().getPsc();

                        wcdmaAtCellLac = ((CellInfoWcdma) cellInfo).getCellIdentity().getLac();


                    } else {
                        // others, e.g. 2g
                        // it's not used in most of the country
                    }
                    cellTimeInterval = curTestTime - preTestTime;
                    preTestTime = curTestTime;

                    if (cellInfoType.equals("LTE") && lteCellID!=nowCellID ) {
                        handoverOccur();
                    } else if (cellInfoType.equals("Wcdma") && wcdmaAtCellID!=nowCellID) {
                        handoverOccur();
                    }
                }

            }
        }
        dataListenerInterface.onDataReceived();
    }

    private void handoverOccur() {
        //Log.d(TagName, "Handover");
        PreAtCellID = nowCellID;
        if (cellInfoType.equals("LTE") && lteCellID!=nowCellID) {
            nowCellID = lteCellID;
        } else if (cellInfoType.equals("Wcdma") && wcdmaAtCellID!=nowCellID) {
            nowCellID = wcdmaAtCellID;
        }

        passCellNum = passCellNum + 1;
        if (stayCellAt == 0) {
            stayCellAt = curTestTime;
        } else {
            cellHoldTime = curTestTime - stayCellAt;
            ttlCellResidenceTime = ttlCellResidenceTime + cellHoldTime;
            stayCellAt = curTestTime;

        }

    }

    public void initBfRun() {
        preTestTime = System.currentTimeMillis();

        lteCellID = 0;
        lteCellMCC = 0;
        lteCellMNC = 0;
        lteCellPCI = 0;
        lteCellTAC = 0;
        lteCellRSRP = 0;
        lteCellRSSI = 0;
        lteCellRSSNR = 0;
        cellTimeInterval = 0;
        cellTimeInterval = 0;

        avgCellHoldTime = 0;
        avgCellResidenceTime = 0;

        cellHoldTime = 0;
        cellInfoType = "LTE";

        wcdmaAtCellID = 0;
        wcdmaAtCellLac = 0;
        wcdmaAtCellMCC = 0;
        wcdmaAtCellMNC = 0;
        wcdmaAtCellPsc = 0;
        wcdmaAtCellSignalStrength = 0;

        nowCellID = 0;
        passCellNum = 0;
        stayCellAt = 0;
        ttlCellResidenceTime = 0;
    }
}
