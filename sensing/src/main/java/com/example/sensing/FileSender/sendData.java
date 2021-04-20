package com.example.sensing.FileSender;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.util.Log;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
//import org.apache.http.util.EncodingUtils;
import static android.content.Context.SENSOR_SERVICE;

public class sendData extends AsyncTask<Void, String, Integer> {
    private Session mSession;

    private String logPath;
    public Context mContext;
    String filePath = "/data/data/com.example.sensor/files";
    private String UserID_path = "user";

    // String filePath = "/data/com.example.sensor/logs/fileName_test";
    // public void writeFile(String fileName,String writestr)
    public void setUserID(String userID_path) {
        this.UserID_path = userID_path;
    }

    public sendData(Context mContext) {
        this.mContext = mContext;

    }

    public boolean connectSFTPServer() {
        try {
            JSch jsch = new JSch();
            mSession = null;

            Log.d("hostohst", "");
            mSession = jsch.getSession("", "", ); // port 22
            mSession.setPassword("");

            Properties properties = new Properties();
            properties.setProperty("StrictHostKeyChecking", "no");
            mSession.setConfig(properties);
            mSession.connect();
            return true;
        } catch (JSchException e) {
            e.printStackTrace();
            Log.d("hostohst", String.valueOf(e));
            return false;
        }

    }

    // try{
    // FileInputStream fin = mContext.openFileInput("user1.json");
    // int length = fin.available();
    // byte [] buffer = new byte[length];
    // fin.read(buffer);
    // String str = new String(buffer, StandardCharsets.UTF_8);
    // Log.d("connectSFTPServer", str);
    //// res = EncodingUtils.getString(buffer, "UTF-8");
    // fin.close();
    // }
    // catch(Exception e){
    // e.printStackTrace();
    // }

    @Override
    protected Integer doInBackground(Void... params) {
        if (connectSFTPServer()) {
            ChannelSftp sftpChannel = null;
            try {
                sftpChannel = (ChannelSftp) mSession.openChannel("sftp");
                sftpChannel.connect();
                Log.i("sender", "/data/data/com.example.sensor/files/" + UserID_path + ".json");
                Log.i("sender", "/home/yicchen/Desktop/" + UserID_path + ".json");
                String cTime = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
                // Log.i("BackgroundTime", String.valueOf(cTime));
                sftpChannel.put("/data/data/com.example.sensor/files/" + UserID_path + ".json",
                        "/home/yicchen/Desktop/" + UserID_path + cTime + ".json");
            } catch (JSchException | SftpException e) {
                e.printStackTrace();
                Log.i("sender", String.valueOf(e));
            }

        } else {
            Log.d("connectSFTPServer", "no");
        }
        return null;
    }
}
