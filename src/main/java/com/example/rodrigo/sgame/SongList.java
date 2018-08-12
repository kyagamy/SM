package com.example.rodrigo.sgame;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.rodrigo.sgame.CommonGame.Common;
import com.example.rodrigo.sgame.CommonGame.SSC;
import com.example.rodrigo.sgame.CommonGame.TransformBitmap;
import com.example.rodrigo.sgame.ScreenSelectMusic.AdapterSSC;
import com.example.rodrigo.sgame.ScreenSelectMusic.MusicThread;
import com.example.rodrigo.sgame.ScreenSelectMusic.RecyclerItemClickListener;
import com.example.rodrigo.sgame.ScreenSelectMusic.SongsGroup;
import com.example.rodrigo.sgame.ScreenSelectMusic.ThemeElements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class SongList extends AppCompatActivity {
    File[] files;
    Spinner s2;
    String currentSSC;
    String paths;
    String path = "";
    int nchar, spCode;
    MediaPlayer mediaPlayer;
    MusicThread musicTimer;
    VideoView bg;
    SoundPool changeMusic;
    final List<String> songs = new ArrayList<>();
    final List<String> lvl = new ArrayList<>();
    final List<SongsGroup> groups = new ArrayList<>();
    int currentSongIndex = 0;
    ThemeElements themeElements;
    ArrayAdapter<String> adp2;
    Intent i;
    VideoView preview;
    ImageView backgroundBluour;

    SongsGroup songsGroup;
    RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Windows Decorator
        try {
            this.getSupportActionBar().hide();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);


        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_songlist);

        //------Se llenan los valosres de las lineas guia----//

        Guideline guideLine =  findViewById(R.id.guideStartList);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) guideLine.getLayoutParams();
        params.guidePercent = 0.5f; // 45% // range: 0 <-> 1
        guideLine.setLayoutParams(params);
        guideLine = findViewById(R.id.guideEndLIst);
        params = (ConstraintLayout.LayoutParams) guideLine.getLayoutParams();
        params.guidePercent = 0.93f; // 45% // range: 0 <-> 1
        guideLine.setLayoutParams(params);
        guideLine = findViewById(R.id.guidePreviewH1);
        params = (ConstraintLayout.LayoutParams) guideLine.getLayoutParams();
        params.guidePercent = 0.23f; // 45% // range: 0 <-> 1
        guideLine.setLayoutParams(params);
        guideLine = findViewById(R.id.guidePreviewH2);
        params = (ConstraintLayout.LayoutParams) guideLine.getLayoutParams();
        params.guidePercent = 0.97f; // 45% // range: 0 <-> 1
        guideLine.setLayoutParams(params);
        guideLine = findViewById(R.id.guideStartPreviewV);
        params = (ConstraintLayout.LayoutParams) guideLine.getLayoutParams();
        params.guidePercent = 0.07f; // 45% // range: 0 <-> 1
        guideLine.setLayoutParams(params);
        guideLine = findViewById(R.id.guideEndPreviewV);
        params = (ConstraintLayout.LayoutParams) guideLine.getLayoutParams();
        params.guidePercent = 0.32f; // 45% // range: 0 <-> 1
        guideLine.setLayoutParams(params);


        //------Fin de las lineas guia-----//
        //------get Elemets----------------//

        themeElements = findViewById(R.id.songElements);
        backgroundBluour = findViewById(R.id.bgBlur);
        recyclerView = findViewById(R.id.recyclerSongs);
        s2 = findViewById(R.id.spinner2);
        bg = findViewById(R.id.bgVideoView);
        preview = findViewById(R.id.preview);
        ImageView b = findViewById(R.id.startButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                nchar = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View v) {
                releaseMediaPlayer();
                releaseMediaPlayer();

                preview.suspend();
                ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
                ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                activityManager.getMemoryInfo(mi);
                double availableMegs = mi.availMem / 0x100000L;
                double percentAvail = mi.availMem / (double) mi.totalMem * 100.0;

                if (percentAvail < 5) {
                    songsGroup = null;
                    System.gc();
                }

                i.putExtra("ssc", currentSSC);
                i.putExtra("nchar", nchar);
                i.putExtra("path", paths);
                startActivity(i);
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                changeSong(position
                );
            }
            @Override
            public void onItemLongClick(View view, int position) {
                // ...
            }
        }));

        try {
            changeMusic = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
            spCode = changeMusic.load(this, R.raw.change_song, 0);
            i = new Intent(this, MainActivity.class);
            i = new Intent(this, PlayerBga.class);
            mediaPlayer = new MediaPlayer();


            Uri uriVideoBg = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bg);
            bg.setVideoURI(uriVideoBg);
            bg.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setLooping(true);
                    mediaPlayer.setVolume(0, 0);
                }
            });
            preview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setLooping(true);
                    mediaPlayer.setVolume(0, 0);
                }
            });

            adp2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lvl);
            s2.setAdapter(adp2);
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CONTACTS)) {
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            69);
                }
            }

            File aux = Common.checkDirSongsFolders();
            path = aux.getPath();
            files = Common.checkDirSongsFolders().listFiles();
            if (aux == null) {
                File archivo = new File(this.getBaseContext().getFilesDir().getPath() + "/piu/Songs/");
                if (archivo.exists()) {
                    files = archivo.listFiles();
                    path = archivo.getPath();
                } else if (archivo.mkdirs()) {
                    files = archivo.listFiles();
                    path = archivo.getPath();
                } else {
                    Toast.makeText(getBaseContext(), "No se podra por hoy :C ", Toast.LENGTH_LONG).show();
                }
            }
            if (files.length <= 0) {
                Toast.makeText(getBaseContext(), "No hay archivos :c en:" + path, Toast.LENGTH_LONG).show();
            } else {
                songsGroup = new SongsGroup(Common.checkDirSongsFolders());
                if (songsGroup.listOfSongs.size() <= 0) {
                    Toast.makeText(getBaseContext(), ":c No hay archivos que pueda leer" +
                            " en:" + path, Toast.LENGTH_LONG).show();
                } else {
                    AdapterSSC adapterSSC = new AdapterSSC(songsGroup, currentSongIndex);
                    recyclerView.setAdapter(adapterSSC);
                    themeElements.biuldObject(this, songsGroup);
                    changeSong(0);
                }
            }



           // preview.setZOrderOnTop(true);
            //themeElements.setZOrderOnTop(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                bg.setZ(0);
                backgroundBluour.setZ(10000000);
                preview.setZ(19000000);
                themeElements.setZ(21000000);
                b.setZ(21000000);
                s2.setZ(21000000);
                recyclerView.setZ(21000000);
            }


            bg.start();

        } catch (Exception e) {

            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void changeSong(int position) {

        changeMusic.play(spCode, 1, 1, 1, 0, 1.0f);
        releaseMediaPlayer();

        paths = songsGroup.listOfPaths.get(position).getPath().toString();
        try {
            themeElements.flash.play();
            SSC auxStep = songsGroup.listOfSongs.get(position);
            File audio = new File(paths + "/" + auxStep.songinfo.get("MUSIC").toString());
            File video = new File(paths + "/" + auxStep.songinfo.get("PREVIEWVID").toString());
            File bg = new File(paths + "/" + auxStep.songinfo.get("BACKGROUND").toString());

            Float x = Float.parseFloat(auxStep.songinfo.get("SAMPLESTART").toString());
            int offset = (int) (x * 1000);
            currentSSC = songsGroup.listOfPathsSSC.get(position);
            lvl.clear();
            if (video.exists() && (video.getPath().endsWith(".mpg") || video.getPath().endsWith(".mp4") || video.getPath().endsWith(".avi"))) {
                preview.setVideoPath(video.getPath());
                preview.start();

            }

            if (bg.exists()) {
                Bitmap ww = TransformBitmap.makeTransparent(TransformBitmap.myblur(BitmapFactory.decodeFile(bg.getPath()), getApplicationContext()), 130);
                backgroundBluour.setImageBitmap(ww);

            }
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setVolume(1f, 1f);
            mediaPlayer.setDataSource(audio.getPath());
            mediaPlayer.prepare();
            mediaPlayer.seekTo(offset);

            musicTimer = new MusicThread();
            musicTimer.player = mediaPlayer;
            musicTimer.time = (long) (Double.parseDouble(auxStep.songinfo.get("SAMPLELENGTH").toString()) * 1000);
            musicTimer.start();


            mediaPlayer.start();


            for (int j = 0; j < auxStep.chartsinfo.length; j++) {
                String metter = "";
                String lvl = "";
                if (auxStep.chartsinfo[j].get("METER") != null) {
                    metter = auxStep.chartsinfo[j].get("METER").toString();
                }
                if (auxStep.chartsinfo[j].get("DESCRIPTION") != null) {
                    lvl = auxStep.chartsinfo[j].get("DESCRIPTION").toString();
                }
                SongList.this.lvl.add(lvl + "-" + metter);
                adp2.notifyDataSetChanged();
            }
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 69: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Peform your task here if any
                } else {

                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {//Can add more as per requirement
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                123);
                    } else {

                    }
                }
                return;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.bg.start();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.musicTimer.isRunning = false;
        releaseMediaPlayer();
    }

    public void onPause() {
        super.onPause();
        this.musicTimer.isRunning = false;
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        try {
            if (mediaPlayer != null) {
                preview.suspend();
                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;

            }
            musicTimer.suspend();
            musicTimer.isRunning=false;
            musicTimer.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
