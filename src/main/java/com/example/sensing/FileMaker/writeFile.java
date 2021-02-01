package com.example.sensing.FileMaker;

import android.content.Context;
import android.util.Log;

import com.example.sensing.Data.SGData;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;

public class writeFile {
    FileOutputStream fop = null;
    File file;
    String content = "123123";
    public Context mContext;
    private String UserID = "user";
    private static FileWriter fileWriter;
//    String filePath = "/data/com.example.sensor/logs/fileName_test";
//    public void writeFile(String fileName,String writestr)
    public void setUserID(String userID){
        this.UserID = userID;
    }
    public writeFile(Context mContext) throws IOException {
        this.mContext = mContext;
//        fileWriter = new FileWriter(UserID+".json",true);
    }
    public boolean write_testTime(String fileName, String test_time) throws IOException {

        try{
//            FileWriter fileWriter = new FileWriter("ff", true);
//            https://blog.csdn.net/diandianxiyu_geek/article/details/78726545
//            IOException: Read-only file system解決
            FileOutputStream fout = new FileOutputStream(mContext.getFilesDir().getPath().toString()+"/"
                    +fileName+".csv", true);
            Log.i("writefile",mContext.getFilesDir().getPath().toString());

//      System.getProperty("line.separator")
            String timeString = test_time;
//      Double test_time_double = test_time;
            Log.i("writefile test_time_gg ", test_time);
            byte [] bytes = timeString.getBytes();
            fout.write(bytes);
            fout.close();
            return true;


        }
        catch(Exception e){
            e.printStackTrace();
            Log.d("writefile", "gg");
            Log.i("writefile", String.valueOf(e));
            return false;
        }

    }
    public boolean write(SGData sgData) throws IOException {

        try{
//            FileWriter fileWriter = new FileWriter("ff", true);
//            https://blog.csdn.net/diandianxiyu_geek/article/details/78726545
//            IOException: Read-only file system解決
            FileOutputStream fout = new FileOutputStream(mContext.getFilesDir().getPath().toString()+"/"
                    +UserID+".json", true);
            Log.i("writefile",mContext.getFilesDir().getPath().toString());
            try {
                Log.i("SGData",sgData.getSGData().toString());
                String sgDataString = sgData.getSGData().toString()+System.getProperty("line.separator");
                byte [] bytes = sgDataString.getBytes();
                fout.write(bytes);
                //            fout.close();
                return true;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                Log.i("writefile", String.valueOf(e));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("writefile", String.valueOf(e));
            }


            Log.d("writefile", "writefile");
//            fileWriter.write(sgData.getSGData().toString());
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            Log.d("writefile", "gg");
            Log.i("writefile", String.valueOf(e));
            return false;
        }

    }


}
