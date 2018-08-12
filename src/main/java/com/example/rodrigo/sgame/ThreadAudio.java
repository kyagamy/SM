package com.example.rodrigo.sgame;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;


public class ThreadAudio extends Thread {

    Context c;
    float offset;
    Uri uri;
    MediaPlayer mediaPlayer;

    public ThreadAudio(Context c, float offset, String path) throws IOException {
        this.c = c;
        this.offset = offset;
        uri = Uri.fromFile(new File(path));

        mediaPlayer = MediaPlayer.create(c, uri);
        //mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(1f)); Esto será para el rush
//        mediaPlayer.prepare();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void playmusic()  {


        mediaPlayer.start();
    }

    public void stopmusic() {
        mediaPlayer.stop();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void run() {
        try {
            this.sleep((int) (offset * 1000));
            this.playmusic();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }




    }


} 