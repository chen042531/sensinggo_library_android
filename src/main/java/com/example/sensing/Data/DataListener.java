package com.example.sensing.Data;

import android.view.View;

import com.example.sensing.Measurement.SensorInfo;

public class DataListener implements DataListenerInterface {
    Object observerable;
    View tv1, tv2, tv3;
    public DataListener(Object observerable){
        this.observerable = observerable;
    }
    public DataListener(Object observerable, View tv1){
        this.observerable = observerable;
        this.tv1 = tv1;
    }
    public DataListener(SensorInfo observerable, View tv1, View tv2, View tv3){
        this.observerable = observerable;
        this.tv1 = tv1;
        this.tv1 = tv2;
        this.tv1 = tv3;
    }
    @Override
    public void onDataReceived() {


    }
}
