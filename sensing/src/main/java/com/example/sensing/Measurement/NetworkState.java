package com.example.sensing.Measurement;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class NetworkState {
    public Context mContext;
    public ConnectivityManager connectivityManager;

    public String Network_type = null;

    private Network nw;
    private NetworkCapabilities actNw;
    private NetworkInfo nwInfo;
    public NetworkState(Context mContext) {
        this.mContext = mContext;
        this.connectivityManager = (ConnectivityManager)mContext.getSystemService(CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            nw = connectivityManager.getActiveNetwork();
            actNw = connectivityManager.getNetworkCapabilities(nw);
        }
        else {
            nwInfo = connectivityManager.getActiveNetworkInfo();
        }
    }
    public void networkstateTest(){
        Log.d("networkstateTest", String.valueOf(isNetworkAvailable())) ;
    }

    public String isNetworkAvailable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network nw = connectivityManager.getActiveNetwork();
            if (nw == null) return "diconnect";
            NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
            if (actNw != null) {
                if( actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH))){
                    return "connect";
                }
            }
            else {
                if(nwInfo != null && nwInfo.isConnected()){
                    return "connect";
                }
            }
        }
        return "diconnect";
    }
    public String getNetworkType(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network nw = connectivityManager.getActiveNetwork();
            NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
            if (actNw != null) {
                if (actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Network_type = "MOBILE";
                    Log.d("networkstateTest", "mobile") ;
                    return Network_type;
                } else if (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Network_type = "WIFI";
                    Log.d("networkstateTest", "wifi") ;
                    return Network_type;
                }  else if (actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                    Network_type = "ETHERNET";
                    return Network_type;
                }
                else{
                    Network_type = null;
                    return Network_type;
                }

            }
            else {
                if(nwInfo.getTypeName()!=null) {
                    Network_type = nwInfo.getTypeName();
                    return Network_type;
                }
                else{
                    Network_type = "null";
                }

            }
        }
        return null;
    }

}
