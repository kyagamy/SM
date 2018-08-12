package com.example.rodrigo.sgame.CommonGame;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Point;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Common {

    public final static double[] JudgeSJ={41.6,41.6,41.6,83.3+30};
    public final static double[] JudgeEJ={41.6,41.6,41.6,58.3+30};
    public final static double[] JudgeNJ={41.6,41.6,41.6,41.6+30};
    public final static double[] JudgeHJ={41.6,41.6,41.6,25.5+30};
    public final static double[] JudgeVJ={33.3,33.3,33.3,8.5+30};
    public final static double[] JudgeXJ={41.6,1,16.6,16.6,16.6+30};
    public final static double[] JudgeUJ={41.6,1,8.3,8.3,8.3+30};

    public static File checkDirSongsFolders(){
        File directory = null;

        if (Environment.getExternalStoragePublicDirectory("piu").exists()){
            File folderSongs= new File(Environment.getExternalStoragePublicDirectory("piu").getPath()+"/Songs");
            if (folderSongs.exists()){
                return  folderSongs;
            }

        }
        else if ( Environment.getExternalStoragePublicDirectory("piu").mkdirs()){
            File folderSongs= new File(Environment.getExternalStoragePublicDirectory("piu").getPath()+"/Songs");
            folderSongs.mkdirs();
            if (folderSongs.exists()){
                return  folderSongs;
            }

        }



        if (new File(System.getenv("EXTERNAL_STORAGE") + "/SONGS/").exists()) {
             directory = new File(System.getenv("EXTERNAL_STORAGE") + "/SONGS/");
        } else if (new File(System.getenv("SECONDARY_STORAGE") + "/SONGS/").exists()) {
             directory = new File(System.getenv("SECONDARY_STORAGE") + "/SONGS/");
        } else if (new File(System.getenv("EXTERNAL_SDCARD_STORAGE") + "/SONGS/").exists()) {
             directory = new File(System.getenv("EXTERNAL_SDCARD_STORAGE") + "/SONGS/");
        } else if (new File(Environment.getExternalStorageDirectory().getPath() + "/SONGS/").exists()) {
             directory = new File(Environment.getExternalStorageDirectory().getPath() + "/SONGS/");
        } else if (new File(Environment.DIRECTORY_DOCUMENTS + "/SONGS/").exists()) {
             directory = new File(Environment.DIRECTORY_DOCUMENTS + "/SONGS/");
        } else if (new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath() + "/SONGS/").exists()) {
            directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath()+ "/SONGS/");
        }
        return directory;
    }


    public static String checkBGADir(String currentpath, String bganame){

        String directory = null;
        if (new File(System.getenv("EXTERNAL_STORAGE") + "/SONGMOVIES/"+bganame).exists()) {
            directory = new File(System.getenv("EXTERNAL_STORAGE") + "/SONGMOVIES/"+bganame).getPath();
        } else if (new File(System.getenv("SECONDARY_STORAGE") + "/SONGMOVIES/"+bganame).exists()) {
            directory = new File(System.getenv("SECONDARY_STORAGE") + "/SONGMOVIES/"+bganame).getPath();
        } else if (new File(System.getenv("EXTERNAL_SDCARD_STORAGE") + "/SONGMOVIES/"+bganame).exists()) {
            directory = new File(System.getenv("EXTERNAL_SDCARD_STORAGE") + "/SONGMOVIES/"+bganame).getPath();
        } else if (new File(Environment.getExternalStorageDirectory().getPath() + "/SONGMOVIES/"+bganame).exists()) {
            directory = new File(Environment.getExternalStorageDirectory().getPath() + "/SONGMOVIES/"+bganame).getPath();
        } else if (new File(Environment.getDataDirectory().getPath() + "/SONGMOVIES/"+bganame).exists()) {
            directory = new File(Environment.getDataDirectory() + "/SONGMOVIES/"+bganame).getPath();
        }else if (new File(currentpath+"/"+bganame).exists()){
            directory =  currentpath+"/"+bganame;
        }
        return directory;
    }

    public static String convertStreamToString(FileInputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static double second2Beat(double second,double BPM){
        return second/(60/BPM);
    }
    public static double beat2Second(double beat,double BPM){
        return beat*(60/BPM);
    }
    public static String changeCharInPosition(int position, char ch, String str){
        char[] charArray = str.toCharArray();
        charArray[position] = ch;
        return new String(charArray);
    }



    private static Point getScreenResolution(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);


        return new Point(metrics.widthPixels,metrics.heightPixels);
    }



    public static boolean isAppInLowMemory(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        return memoryInfo.lowMemory;
    }




}
