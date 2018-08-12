package com.example.rodrigo.sgame.ScreenSelectMusic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.SoundPool;
import android.os.Build;
import android.support.annotation.RequiresApi;
import com.example.rodrigo.sgame.CommonGame.Common;
import com.example.rodrigo.sgame.CommonGame.SSC;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class SongsGroup {
    public String description="";
    public String name;
    public SoundPool audio;
    public Bitmap banner=null;
    public  ArrayList<SSC> listOfSongs = new ArrayList<>();
    public ArrayList<File> listOfPaths = new ArrayList<>();
    public ArrayList<String> listOfPathsSSC = new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public SongsGroup(File path)   {
       name=path.getName();
       File[] songsPaths=path.listFiles();
        for (File i: songsPaths) {
            if ( i.isDirectory()) {
                File[] songFiles = i.listFiles();
                if (i.getName().contains("info")){
                    try {
                        String x= Common.convertStreamToString(new FileInputStream(i.getPath()+"/data.txt"));
                        description=x;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else {
                    for (File j :songFiles) {
                        if (j.getPath().endsWith(".ssc")) {
                            try {
                                listOfPathsSSC.add(j.getPath());
                                listOfSongs.add(new SSC(Common.convertStreamToString(new FileInputStream(j)),true));
                                listOfPaths.add(i);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            else if ( i.getName().contains("banner")){
                banner= BitmapFactory.decodeFile( i.getPath());
            }
        }


    }




}
