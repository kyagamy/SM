package com.example.rodrigo.sgame.CommonGame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.rodrigo.sgame.Player.Attack;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class SpriteReader {
    public Bitmap[] frames;
    private int frameIndex;
    private float frameTime;
    private long lastFrame;
    private boolean isPlaying = false;
    private float lapsedtime;
    private int interpolateIndex;
    boolean rotate=false;

    double seconds;
    Paint paint;
    ArrayList<String[]> attacksList= new ArrayList<String[]>();


    public SpriteReader(Bitmap[] frames, float timeFrame) {
        this.frameIndex = 0;
        this.frames = frames;
        frameTime = timeFrame / frames.length;
    }

    public SpriteReader(Bitmap sprite, int sizeX, int sizeY, float timeFrame) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);

        frames = new Bitmap[sizeX * sizeY];
        int frameWhith = (sprite.getWidth() / sizeX);
        int frameHeight = (sprite.getHeight() / sizeY);
        int count = 0;
        try {
            for (int y = 0; y < sizeY; y++) {
                for (int x = 0; x < sizeX; x++) {
                    frames[count] = Bitmap.createBitmap(sprite, x * frameWhith, y * frameHeight, frameWhith, frameHeight);
                    count++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.frameIndex = 0;
        frameTime = timeFrame / frames.length;
    }

    public void play() {
        isPlaying = true;
        frameIndex = 0;
        lastFrame = System.currentTimeMillis();
    }

    public void stop() {
        isPlaying = false;
    }

    public void update() {
        lapsedtime = System.currentTimeMillis() - lastFrame;
        seconds += lapsedtime;
        if (lapsedtime > frameTime * 1000) {
            frameIndex++;
            if (frameIndex == frames.length) {
                frameIndex = 0;

            }
            lastFrame = System.currentTimeMillis();
        }
    }

    public void draw(Canvas canvas, Rect destino) {
        if (!isPlaying) {
            return;
        }
        if(rotate){
            canvas.drawBitmap(TransformBitmap.RotateBitmap(frames[frameIndex], (float) (45)) , null, destino, paint);
        }
        else if(attacksList.size()<1){
            canvas.drawBitmap(frames[frameIndex], null, destino, paint);
        }


    }

    public void draw2(Canvas canvas, Rect destiny) {
        if (!isPlaying) {
            return;
        }
        if ((1 + frameIndex) == frames.length) {
            isPlaying = false;
        } else {
            canvas.drawBitmap(frames[frameIndex], null, destiny, paint);

        }


    }
}

