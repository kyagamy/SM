package com.example.rodrigo.sgame.Player;

import com.example.rodrigo.sgame.CommonGame.Common;
import com.example.rodrigo.sgame.CommonGame.SpriteReader;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.rodrigo.sgame.EvaluationActivity;
import com.example.rodrigo.sgame.R;
import com.example.rodrigo.sgame.CommonGame.SSC;
import com.example.rodrigo.sgame.ThreadAudio;
import com.example.rodrigo.sgame.PlayerBga;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

//Este objeto contine todo lo relacionado con el gameplay (Sprites,Gameobjets,evaluador,etc)

public class GamePlay extends SurfaceView implements SurfaceHolder.Callback {
    //Objects variables
    private ThreadAudio musicPlayer;
    private GamePad touchPad;
    public MainThread mainTread;
    private RadarEffectsThread effectsThread;
    private LifeBar life;
    private Steps steps;
    private SSC stepData;
    private Combo ObjectCombo;
    private Bitmap bgaBitmap;
    private PlayerBga BGA;
    private Intent intent;
    private Context context;
    private static byte[] preseedRow = {100, 100, 100, 100, 100, 100, 100, 100, 100, 100};


    //DrawerMarks and Steps information
    //public ArrayList<String[]> bufferSteps;
    public ArrayList<Object[]> bufferSteps;
    private ArrayList<String[]> databga;
    private ArrayList<String[]> ATTACKS;
    protected ArrayList<Float[]> BPMS;
    protected ArrayList<Float[]> DELAYS;
    protected ArrayList<Float[]> STOPS;
    protected ArrayList<Float[]> SPEEDS;
    protected ArrayList<Float[]> WARPS;
    protected ArrayList<Float[]> TICKCOUNTS;
    protected ArrayList<Float[]> SCROLLS;
    protected ArrayList<Float[]> FAKES;
    private int playerSizeX = 400;
    private int playerSizeY = 500;
    protected int currentTickCount = 1;
    protected int posBuffer = 0;
    protected int posBPM = 0;
    private float speed = 2300;
    private int currentSpeedMod = 1300;
    protected int posDelay = 0;
    protected int posSpeed = 1;
    protected int posWarp = 0;
    protected int posTickCount = 0;
    protected int posstop = 0;
    protected int posScroll = 0;
    protected int posFake = 0;
    private int posAttack = 0;
    private int contadorTick = 0;
    private double residuoTick = 0;

    public double currentBeat = 0d, metaBeatSpeed = -100;
    public float BPM, offset, beatsofSpeedMod, currentDurationFake = 0;
    //Generalplayer
    private SoundPool soundPool;
    private String pathMusic, event, tipo;
    public double fps;
    private float currentLife = 50;
    File fileBGA;
    private Paint dibujante;
    private int Combo = 0, soundPullBeat;
    //////
    private Long ttranscurrido;
    private Long currenttiempo;
    public Long curentempobeat;
    private Long Startime;
    private Long ttranscurridobeat;//Time Stamps
    //Score
    byte[] inputs;
    int bad = 0, miss = 0, perfect = 0, great = 0, good = 0;
    public boolean doEvaluate = true, autoplay = false;
    double currentDelay = 0, residuoy = 0, speedMod = 1, lastSpeed, currentSecond = 0;
    SpriteReader items;
    double lostBeats = 0, lostBeatbyWarp = 0;
    long delayTime = 0;


    public void reset() {
        delayTime = 0;
        currentBeat = 0d;
        metaBeatSpeed = -100;
        currentTickCount = 1;
        posBuffer = 0;
        posBPM = 0;
        speed = 2300;
        currentSpeedMod = 1300;
        posDelay = 0;
        posSpeed = 0;
        posWarp = 0;
        posTickCount = 0;
        posstop = 0;
        posScroll = 0;
        posFake = 0;
        bad = 0;
        miss = 0;
        perfect = 0;
        great = 0;
        good = 0;
        currentSecond = 0;
        currentDelay = 0;
        Combo = 0;
        contadorTick = 0;
    }

    public GamePlay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public GamePlay(Context context, String RAWSTEPS, int nchar, String path, byte[] pad, int width, int height) throws IOException {
        super(context);
        build1Object(context, RAWSTEPS, nchar, path, null, pad, width, height);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void build1Object(Context context, String RAWSTEPS, int nchar, String path, PlayerBga bga, byte[] panel, int width, int height) throws IOException {
        this.context = context;
        reset();
        this.setZOrderOnTop(true); //necessary
        getHolder().setFormat(PixelFormat.TRANSPARENT);
        BGA = bga;
        bgaBitmap = null;
        life = new LifeBar(context, "");
        this.inputs = panel;
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPullBeat = soundPool.load(this.getContext(), R.raw.beat, 0);
        getHolder().addCallback(this);
        stepData = new SSC(RAWSTEPS);//se lee el archio ssc y se convierte a array y maps

        bufferSteps = stepData.createBuffer2(nchar);//se crea un array para el player

        steps = new Steps(context);


        if (stepData.chartsinfo[nchar].get("SCROLLS") != null && !stepData.chartsinfo[nchar].get("SCROLLS").equals("")) {
            SCROLLS = stepData.arrayListSpeed(stepData.chartsinfo[nchar].get("SCROLLS").toString());
        }
        BPMS = setMetadata(stepData.chartsinfo[nchar].get("BPMS"), stepData.songinfo.get("BPMS"));
        BPM = BPMS.get(0)[1];
        //ATTACKS= setMetadata(stepData.chartsinfo[nchar].get("ATTACKS"),stepData.songinfo.get("ATTACKS"));
        FAKES = setMetadata(stepData.chartsinfo[nchar].get("FAKES"), stepData.songinfo.get("FAKES"));
        TICKCOUNTS = setMetadata(stepData.chartsinfo[nchar].get("TICKCOUNTS"), stepData.songinfo.get("TICKCOUNTS"));

        STOPS = setMetadata(stepData.chartsinfo[nchar].get("STOPS"), stepData.songinfo.get("STOPS"));
        DELAYS = setMetadata(stepData.chartsinfo[nchar].get("DELAYS"), stepData.songinfo.get("DELAYS"));
        SPEEDS = setMetadata(stepData.chartsinfo[nchar].get("SPEEDS"), stepData.songinfo.get("SPEEDS"));
        WARPS = setMetadata(stepData.chartsinfo[nchar].get("WARPS"), stepData.songinfo.get("WARPS"));

        if (stepData.chartsinfo[nchar].get("OFFSET") != null) {
            offset = Float.parseFloat(stepData.chartsinfo[nchar].get("OFFSET").toString());
        } else {
            offset = Float.parseFloat(stepData.songinfo.get("OFFSET").toString());
        }

        if (stepData.chartsinfo[nchar].get("STEPSTYPE") != null && !stepData.chartsinfo[nchar].get("STEPSTYPE").equals("")) {
            tipo = stepData.chartsinfo[nchar].get("STEPSTYPE").toString();
        } else {
            tipo = "";
        }
        databga = new ArrayList<>();
        if (stepData.songinfo.get("BGCHANGES") != null) {
            String[] infoBgas = stepData.songinfo.get("BGCHANGES").split(",");
            if (infoBgas != null) {

                if (Common.checkBGADir(path, infoBgas[0]) != null) {
                    fileBGA = new File(Common.checkBGADir(path, infoBgas[0]));
                } else {
                    for (int i = 0; i < infoBgas.length; i++) {
                        databga.add(infoBgas[i].split("="));
                        if (Common.checkBGADir(path, databga.get(i)[1]) != null && (databga.get(i)[1].endsWith("mpg") || databga.get(i)[1].endsWith("mp4") || databga.get(i)[1].endsWith("avi"))) {
                            fileBGA = new File(Common.checkBGADir(path, databga.get(i)[1]));

                        }
                    }
                }
            }
        }
        if (fileBGA != null && BGA != null) {
            BGA.setVideoPath(fileBGA.getPath());
        } else if (BGA != null) {
            BGA.setVideoPath(Common.checkBGADir(path, "off.mpg"));
        }


        currentBeat = (double) -offset / (60 / BPM);
        currentSecond = offset;
        pathMusic = path + "/" + stepData.songinfo.get("MUSIC").toString();
        bgaBitmap = BitmapFactory.decodeFile(path + "/" + stepData.songinfo.get("BACKGROUND").toString());
        mainTread = new MainThread(getHolder(), this);
        fps = 0;
        setFocusable(true);
        touchPad = new GamePad(context, tipo, inputs, width, height);
        items = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.objectospiu), 32, 2, 2f);


        //flecha


        dibujante = new Paint();
        dibujante.setTextSize(20);
        ObjectCombo = new Combo(context);
        ObjectCombo.start();

        if (tipo.equals("dance-single")) {
            steps.makeDDR(context);
        }
        dibujante.setColor(Color.TRANSPARENT);
        this.startGame();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    public void startGame() {
        curentempobeat = currenttiempo = Startime = System.nanoTime();

        try {
            if (!mainTread.running) {
                ttranscurrido = System.nanoTime();


                effectsThread = new RadarEffectsThread(this);
                effectsThread.start();
                mainTread.setRunning(true);
                if (offset > 0) {
                    musicPlayer = new ThreadAudio(this.getContext(), offset, pathMusic);
                    currentBeat = 0;
                    mainTread.start();
                    musicPlayer.start();

                    BGA.startVideo();
                } else {
                    BGA.startVideo();
                    currentBeat = (double) offset / (60 / BPM);
                    musicPlayer = new ThreadAudio(this.getContext(), 0, pathMusic);
                    musicPlayer.start();
                    mainTread.start();
                }
            } else {
                mainTread.sulrfaceHolder = this.getHolder();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        stop();
    }

    public void update() {
        if (currentLife > 100) {
            currentLife = 100;
        } else if (currentLife <= 0) {
            currentLife = 0.01f;
        }
        life.updateLife(currentLife);
        ObjectCombo.update(Combo);
        steps.update();
    }

    private void clearMemory() {
        bgaBitmap = null;
        soundPool = null;
        stepData = null;
        databga = null;
        fileBGA = null;
        BGA = null;
        touchPad = null;
        steps = null;

        System.gc();
    }

    public void draw(Canvas canvas) {

        super.draw(canvas);
        residuoy = 0;
        Paint painty = new Paint();
        painty.setTextSize(20);
        painty.setStyle(Paint.Style.FILL);
        painty.setColor(Color.TRANSPARENT);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        drawStats(canvas);
        if ((double) bufferSteps.get(posBuffer + 1)[1] <= currentBeat) {
            // calculateEffects();
            drawCharts(canvas, posBuffer++, false);
        } else if ((double) bufferSteps.get(posBuffer + 1)[1] > currentBeat) {
            drawCharts(canvas, posBuffer, true);
        }

        ObjectCombo.draw(canvas, playerSizeX, playerSizeY);
        touchPad.draw(canvas);
        life.draw(canvas, playerSizeX, playerSizeY);
    }

    public void stop() {
        clearMemory();
        boolean retry = true;
        mainTread.setRunning(false);
        while (retry) {
            try {
                effectsThread.running = false;
                effectsThread.join();
                mainTread.setRunning(false);
                mainTread.join();
                musicPlayer.stopmusic();
                musicPlayer.join();
                musicPlayer.interrupt();
                retry = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public boolean onTouchEvent(MotionEvent event) {
        try {

            int maskedAction = event.getActionMasked();
            int fingers = event.getPointerCount();
            int[][] inputs = new int[fingers][2];
            for (int i = 0; i < fingers; i++) {
                inputs[i][0] = (int) event.getX(i);
                inputs[i][1] = (int) event.getY(i);
            }
            touchPad.checkInputs(inputs);


            switch (maskedAction) {
                case MotionEvent.ACTION_DOWN:
                    if (event.getX() > playerSizeX / 2 && event.getY() < playerSizeY / 2) {
                        currentSpeedMod += 150;
                    } else if (event.getX() < playerSizeX / 2 && event.getY() < playerSizeY / 2) {
                        if (currentSpeedMod > 99) {
                            currentSpeedMod -= 150;
                        }
                    } else if (event.getX() < playerSizeX / 2 && event.getY() > playerSizeY / 2 && event.getY() < playerSizeY) {
                        autoplay = !autoplay;
                    } else if (event.getX() > playerSizeX / 2 && event.getY() > playerSizeY / 2 && event.getY() < playerSizeY) {

                        steps.efecto = !steps.efecto;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    if (fingers == 1) {
                        touchPad.clearPad();
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return true;
    }


    public void calculateBeat() {

        if (lostBeatbyWarp > 0) {
            currentBeat += lostBeatbyWarp;
            lostBeatbyWarp = 0;
            //calculateEffects();

        }

        ttranscurridobeat = System.nanoTime() - curentempobeat;
        currentBeat += ttranscurridobeat / ((60 / BPM) * 1000 * 1000000);
        currentDurationFake -= ttranscurridobeat / ((60 / BPM) * 1000 * 1000000);//reduce la duración de los fakes
        curentempobeat = System.nanoTime();


    }

    public void drawStats(Canvas c) {
//calculate stats=
        dibujante.setTextSize(20);
        dibujante.setStyle(Paint.Style.FILL);

//        dibujante.setColor(Color.BLACK);
        c.drawPaint(dibujante);
        //dibujante.setColor(Color.TRANSPARENT);
        speed = (int) (currentSpeedMod * speedMod);
        currentSecond += (System.nanoTime() - Startime) / 1000000000.0;//se calcula el segundo
        Startime = System.nanoTime();
        //beat
        if (currentDelay == 0) {
            calculateBeat();
            if ((double) bufferSteps.get(posBuffer + 1)[1] <= currentBeat && posBuffer < bufferSteps.size()) {
                while ((double) bufferSteps.get(posBuffer + 1)[1] <= currentBeat) {
                    posBuffer++;
                    evaluate();
                    calculateBeat();
                    //calculateEffects();
                }
            } else if (posBuffer >= bufferSteps.size()) {
                this.startEvaluation();
                this.stop();

            }
        } else {
            if (currentDelay <= currentSecond) {
                lostBeats = (currentSecond - currentDelay);
                currentBeat += lostBeats / ((60 / BPM));
                //reduce la duración de los fakes
                currentDelay = 0;
                curentempobeat = System.nanoTime();
                currentDurationFake -= (currentSecond - currentDelay) / ((60 / BPM) * 1000 * 1000000);
            }

        }


        dibujante.setTextSize(25);
        dibujante.setColor(Color.WHITE);
        c.drawText("FPS: " + fps, 50, 50, dibujante);
        c.drawText("Log: " + currentTickCount, 50, 100, dibujante);
        c.drawText("event: " + event, 0, playerSizeY - 200, dibujante);
        //iversosn
        c.drawText("C Seg: " + String.format("%.3f", currentSecond), 10, playerSizeY - 100, dibujante);
        c.drawText("C Beat: " + String.format("%.3f", currentBeat), 10, playerSizeY - 50, dibujante);
        c.drawText("C BPM: " + this.BPM, 10, playerSizeY - 150, dibujante);
        c.drawText("C Speed: " + this.speedMod, 10, playerSizeY - 0, dibujante);
        String st = "";
        for (int j = 0; j < 10; j++) {
            st += inputs[j];
        }
        c.drawText("pad: " + st, playerSizeX - 250, playerSizeY - 20, dibujante);
        dibujante.setColor(Color.BLACK);
        c.drawRect(0, playerSizeY - 55, c.getWidth(), c.getHeight(), dibujante);
        dibujante.setColor(Color.TRANSPARENT);
        showSomeArray(SPEEDS, c, posSpeed);
    }

    private void showSomeArray(ArrayList<Float[]> array, Canvas c, int poscosa) {
        if (array != null) {
            int posy = 0;
            for (int x = poscosa; x < array.size(); x++) {
                c.drawText("Speed: " + array.get(x)[0] + "=" + array.get(x)[1] + "=", playerSizeX - 300, playerSizeY - 150 - 50 * posy, dibujante);
                posy++;
            }
        }
    }


    private void drawCharts(Canvas c, int bufferPos, Boolean interpolate) {
        int posIntX = (int) (playerSizeX * 0.05);
        int wa = 0;
        int currentY = (int) (playerSizeY * 0.085);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {//horizonal
            playerSizeX = (int) (c.getWidth() * (1));
            playerSizeY = (int) (c.getHeight() * (1));
            if (tipo.equals("pump-single")) {
                wa = (int) (playerSizeX / 10 * 0.8);
                posIntX = (int) (playerSizeX * 0.2);
            } else if (tipo.equals("pump-double")) {
                wa = (int) (playerSizeX / 10 * 0.8);
                posIntX = (int) (playerSizeX * 0.1);
                steps.receptor.draw(c, new Rect((int) (playerSizeX * 0.04), currentY, (int) (playerSizeX * 0.542
                ), currentY + wa));
                steps.receptor.draw(c, new Rect((int) (playerSizeX * 0.435), currentY, (int) (playerSizeX * 0.94
                ), currentY + wa));
            }
        } else {//portriot
            playerSizeX = (int) (c.getWidth() * (1));
            playerSizeY = (int) (c.getHeight() * (0.5));
            wa = (int) (playerSizeX / 10 * 0.6);
            posIntX = (int) (playerSizeX * 0.35);

            if (tipo.equals("pump-single")) {
                wa = (int) (playerSizeX / 10 * 0.8);
                posIntX = (int) (playerSizeX * 0.3);
                //steps.receptor.draw(c, new Rect((int) (playerSizeX * 0.24), currenty, (int) (playerSizeX * 0.742
                //), currenty + wa));
            } else if (tipo.equals("pump-double")) {
                wa = (int) (playerSizeX / 10 * 0.8);
                posIntX = (int) (playerSizeX * 0.1);
                steps.receptor.draw(c, new Rect((int) (playerSizeX * 0.04), currentY, (int) (playerSizeX * 0.542
                ), currentY + wa));
                steps.receptor.draw(c, new Rect((int) (playerSizeX * 0.435), currentY, (int) (playerSizeX * 0.94
                ), currentY + wa));
            }

        }


        Paint dibujante = new Paint();
        dibujante.setTextSize(25);
        dibujante.setStyle(Paint.Style.FILL);

        if (interpolate) {
            double distanciaentrebeats = (double) bufferSteps.get(posBuffer + 1)[1] - (double) bufferSteps.get(posBuffer)[1];
            double distanciaentrebeactualyela = currentBeat - (double) bufferSteps.get(posBuffer)[1];
            double porcentajeaaumentar = 100 / distanciaentrebeats * distanciaentrebeactualyela / 100;
            double distanciarestar = speed / 192 * porcentajeaaumentar;
            if (!(currentBeat < (double) bufferSteps.get(posBuffer)[1])){
                currentY -= distanciarestar * (double) 1 / 192;
            }
        }

        dibujante.setColor(Color.WHITE);
        Stack<Object[]> stackSteps = new Stack<>();
        for (int i = bufferPos; currentY < playerSizeY && i < bufferSteps.size() && (speedMod > 0.04); i++) {
            float w = (float) bufferSteps.get(i)[2];
            residuoy += (w - (int) w);
            int nextY = (int) (currentY + (w * speed / 192));
            if (containSteps((byte[]) bufferSteps.get(i)[0])) {
                Object[] auxString = new Object[3];
                auxString[0] = bufferSteps.get(i)[0];
                auxString[1] = currentY;
                auxString[2] = nextY;
                stackSteps.push(auxString);

            }
            Double div = ((currentSpeedMod * speedMod / 192));
            int aumentoY = (int) (residuoy + (currentSpeedMod * speedMod / 192));
            residuoy += div - aumentoY;
            currentY += aumentoY * w;
            if (residuoy > 1) {
                residuoy -= (int) residuoy;
            }


        }

        currentY = (int) (playerSizeY * 0.085);

        for (int i = bufferPos; currentY > -50 && (i) >= 0 && (speedMod > 0.04); i--) {
            float w = (float) bufferSteps.get(i)[2];
            int nextY = currentY + (int) (speed / 192 * w);
            if (containSteps((byte[]) bufferSteps.get(i)[0])) {
                Object[] auxString = new Object[3];
                auxString[0] = bufferSteps.get(i)[0];
                auxString[1] = currentY;
                auxString[2] = nextY;
                stackSteps.push(auxString);
            }
            Double div = ((currentSpeedMod * speedMod / 192));
            int aumentY = (int) (residuoy + (currentSpeedMod * speedMod / 192));
            residuoy += div - aumentY;
            currentY -= aumentY * w;

        }
        event = "Nsteps" + stackSteps.size();
        //steps.draw(c, stackSteps, currentY, speed, posIntX, wa, playerSizeX, playerSizeY);
        steps.draw(c, stackSteps, speed, posIntX, wa, playerSizeX, playerSizeY);

        dibujante.setColor(Color.TRANSPARENT);


    }

    public void playTick() {
        if (doEvaluate) {
            soundPool.play(soundPullBeat, 1, 1, 1, 0, 1.1f);
        }
    }
    public void evaluate() {

        if (doEvaluate) {
            double[] currentJudge = Common.JudgeHJ;
            if (autoplay) {
                ObjectCombo.posjudge = 0;
                if (containTaps((byte[]) bufferSteps.get(posBuffer)[0])) {
                    playTick();
                    combopp();
                    currentLife += 0.5;
                    ObjectCombo.show();
                    byte[] auxrow = (byte[]) bufferSteps.get(posBuffer)[0];
                    for (int w = 0; w < auxrow.length; w++) {
                        int aux = auxrow[w];
                        if (aux == 1) {
                            steps.explotions[w].play();
                        } else if (aux == 2) {
                            steps.explotions[w].play();
                            steps.explotionTails[w].play();
                        } else if (aux ==0) {
                            steps.explotionTails[w].stop();
                        }
                    }
                    bufferSteps.get(posBuffer)[0] = preseedRow;


                } else if (containLongs((byte[]) bufferSteps.get(posBuffer)[0])) {

                    residuoTick += ((double) currentTickCount / 48);
                    if (residuoTick >= 1) {
                        residuoTick -= 1;
                        ObjectCombo.show();
                        combopp();
                        currentLife += 0.5;
                    }


                    bufferSteps.get(posBuffer)[0] = preseedRow;

                }

            } else {//juicio normal
                int posBack;
                int posNext;
                int backSteps;
                int rGreat = mil2BackSpaces((float) currentJudge[3]);
                int rGood = rGreat + mil2BackSpaces((float) currentJudge[2]);
                int rBad = rGood + mil2BackSpaces((float) currentJudge[1]);
                backSteps = rBad + 1;

                posBack = -backSteps;
                if (backSteps >= posBuffer) {
                    posBack = -posBuffer;
                }
                posNext = backSteps;

                if (containTaps((byte[]) bufferSteps.get(posBuffer + posBack)[0])) {
                    comboLess();
                    bufferSteps.get(posBuffer + posBack)[0] = preseedRow;
                    posBack++;


                }
                if (containLongs((byte[]) bufferSteps.get(posBuffer + posBack)[0])) {
                    residuoTick += ((double) currentTickCount / 48);
                    if (residuoTick >= 1) {
                        residuoTick -= 1;
                        ObjectCombo.show();
                        comboLess();
                        currentLife += 0.5;
                    }
                }

                int posEvaluate = -1;
                while (posBack <= posNext) {
                    if (containSteps((byte[]) bufferSteps.get(posBuffer + posBack)[0])) {
                        boolean checkLong = true;
                        byte[] auxRow = (byte[]) bufferSteps.get(posBuffer + posBack)[0];
                        for (int w = 0; w < auxRow.length; w++) {
                            byte currentChar = auxRow[w];


                            if (posBack < 1 && (inputs[w] != 0) && (containLongs(currentChar))) {
                                steps.explotionTails[w].play();
                                auxRow[w] = 100;
                                residuoTick += ((double) currentTickCount / 48);
                                if (checkLong && residuoTick >= 1) {
                                    residuoTick -= 1;
                                    ObjectCombo.posjudge = 0;
                                    ObjectCombo.show();
                                    currentLife += 0.5;
                                    combopp();
                                    checkLong = false;
                                }
                                inputs[w] = 2;

                            }

                            if (inputs[w] == 1 && containTaps(currentChar)) {

                                steps.explotions[w].play();
                                auxRow[w] = 0;
                                inputs[w] = 2;
                                posEvaluate = posBuffer + posBack;

                            }
                            if (inputs[w] == 0) {
                                steps.explotionTails[w].stop();
                            }

                            bufferSteps.get(posBuffer + posBack)[0] = auxRow;

                        }
                    }
                    if (posEvaluate != -1) {
                        if (!containSteps((byte[]) bufferSteps.get(posEvaluate)[0])) {

                            int auxRetro = Math.abs(posBack);

                            if (auxRetro < rGreat) {
                                ObjectCombo.posjudge = 0;
                                combopp();
                                currentLife += 0.5;
                            } else if (auxRetro < rGood) {
                                ObjectCombo.posjudge = 1;
                                combopp();

                            } else if (auxRetro < rBad) {
                                ObjectCombo.posjudge = 2;

                            } else {
                                ObjectCombo.posjudge = 3;
                                Combo = 0;
                                currentLife -= 0.5;
                            }
                            // AQUI SE VERA SI ES GREAT O QUE ONDA
                            ObjectCombo.show();
                        }
                        posEvaluate = -1;

                    }
                    posBack++;
                }
            }
        }
    }

    private void combopp() {
        if (Combo < 0) {
            Combo = 0;
        }
        Combo++;
    }

    private void comboLess() {
        miss++;
        ObjectCombo.posjudge = 4;
        if (Combo > 0) {
            Combo = 0;
        } else {
            Combo -= 1;
        }
        ObjectCombo.show();
        currentLife -= 1 - Combo;
    }


    private void addContadorTick() {
        contadorTick++;
        if (contadorTick > 48) {
            contadorTick = 0;
        }
    }

    private int mil2BackSpaces(float judgeTime) {
        int backs = 0;
        float auxJudge = 0;
        while ((posBuffer - backs) >= 0) {
            auxJudge += Common.beat2Second((double) 4 / 192, BPM) * 1000;
            backs++;
            if (auxJudge >= judgeTime + 23) {
                break;
            }
        }
        return backs;
    }

    private boolean containTaps(byte... row) {
        for (int x:row) {
            if (x == 1 || x == 5) {
                return true;
            }
        }
        return false;
    }

    private boolean containLongs(byte... row) {
        for (int x:row) {
            if (x== 2 ||x == 3 || x == 4) {
                return true;
            }
        }
        return false;
    }
    private boolean containSteps(byte... row) {
        for (byte x:row){
            if (x != 0 && x != 127) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    private ArrayList setMetadata(Object data, String s) {
        if (data != null && !data.equals("")) {
            return stepData.arrayListSpeed(data.toString());
        } else if (s != null && !s.equals("")) {
            return setMetadata(s, null);
        } else {
            return null;
        }
    }

    private void startEvaluation() {
        context.startActivity(intent);
    }

    public void setIntent(Intent i) {
        intent = i;
    }


}