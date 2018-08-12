package com.example.rodrigo.sgame.Player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.rodrigo.sgame.CommonGame.SpriteReader;
import com.example.rodrigo.sgame.CommonGame.TransformBitmap;
import com.example.rodrigo.sgame.R;

import java.util.Stack;

public class Steps {
    public SpriteReader mine, receptor;
    private SpriteReader[] arrows2, longs, tails;
    public SpriteReader[] explotions, explotionTails;
    private int Longinfo[] = {-9999, -9999, -9999, -9999, -9999, -9999, -9999, -9999, -9999, -9999, -9999};
    public boolean efecto = false;


    Steps(Context context) {

        receptor = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.base), 1, 2, 0.5f);
        receptor.play();
        if (arrows2 == null || tails == null || longs == null || explotions == null) {
            arrows2 = new SpriteReader[10];
            arrows2[0] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_down_left_tap), 6, 1, 0.2f);
            arrows2[1] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_up_left_tap), 6, 1, 0.2f);
            arrows2[2] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_center_tap), 6, 1, 0.2f);
            arrows2[3] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_up_right_tap), 6, 1, 0.2f);
            arrows2[4] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_down_right_tap), 6, 1, 0.2f);
            arrows2[5] = arrows2[0];
            arrows2[6] = arrows2[1];
            arrows2[7] = arrows2[2];
            arrows2[8] = arrows2[3];
            arrows2[9] = arrows2[4];
            tails = new SpriteReader[10];
            tails[0] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_down_left_tail), 6, 1, 0.2f);
            tails[1] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_up_left_tail), 6, 1, 0.2f);
            tails[2] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_center_tail), 6, 1, 0.2f);
            tails[3] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_up_right_tail), 6, 1, 0.2f);
            tails[4] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_down_right_tail), 6, 1, 0.2f);
            tails[5] = tails[0];
            tails[6] = tails[1];
            tails[7] = tails[2];
            tails[8] = tails[3];
            tails[9] = tails[4];
            longs = new SpriteReader[10];
            longs[0] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_down_left_body), 6, 1, 0.2f);
            longs[1] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_up_left_body), 6, 1, 0.2f);
            longs[2] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_center_body), 6, 1, 0.2f);
            longs[3] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_up_right_body), 6, 1, 0.2f);
            longs[4] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_down_right_body), 6, 1, 0.2f);
            longs[5] = longs[0];
            longs[6] = longs[1];
            longs[7] = longs[2];
            longs[8] = longs[3];
            longs[9] = longs[4];
            explotionTails = new SpriteReader[10];
            explotionTails[0] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_down_left_tap), 6, 1, 0.2f);
            explotionTails[1] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_up_left_tap), 6, 1, 0.2f);
            explotionTails[2] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_center_tap), 6, 1, 0.2f);
            explotionTails[3] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_up_right_tap), 6, 1, 0.2f);
            explotionTails[4] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_down_right_tap), 6, 1, 0.2f);
            explotionTails[5] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_down_left_tap), 6, 1, 0.2f);
            explotionTails[6] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_up_left_tap), 6, 1, 0.2f);
            explotionTails[7] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_center_tap), 6, 1, 0.2f);
            explotionTails[8] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_up_right_tap), 6, 1, 0.2f);
            explotionTails[9] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_down_right_tap), 6, 1, 0.2f);
            Bitmap r1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.s1);
            Bitmap r2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.s2);
            Bitmap r3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.s3);
            Bitmap r4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.s4);
            Bitmap r5 = BitmapFactory.decodeResource(context.getResources(), R.drawable.s5);
            explotions = new SpriteReader[10];
            for (int cd = 0; cd < 10; cd++) {
                explotions[cd] = new SpriteReader(new Bitmap[]{
                        TransformBitmap.overlay(arrows2[cd].frames[0], r1),
                        TransformBitmap.overlay(arrows2[cd].frames[1], r2),
                        TransformBitmap.overlay(arrows2[cd].frames[2], r3),
                        TransformBitmap.overlay(arrows2[cd].frames[3], r4),
                        TransformBitmap.overlay(arrows2[cd].frames[4], r5),
                        TransformBitmap.overlay(arrows2[cd].frames[4], r4),
                }, 0.15f);

            }

        }//resplandor

        for (int x = 0; x < arrows2.length; x++) {
            arrows2[x].play();
            longs[x].play();
            tails[x].play();
        }
        mine = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.mine), 3, 2, 0.2f);
        mine.play();

    }


    public void draw(Canvas ca, Stack<Object[]> stackSteps, float speed, int posintx, int wa, int playersizex, int playerSizeY) {
        Canvas c;
        Bitmap stepsBitmap = Bitmap.createBitmap(playersizex, playerSizeY, Bitmap.Config.ARGB_8888);
        if (efecto) {
            c = new Canvas(stepsBitmap);
        } else {
            c = ca;
        }

        int posintX2 = posintx;
        int aux2 = (int) (playerSizeY * 0.085);
        int currenty = (int) (playerSizeY * 0.085);
        /*receptor.draw(c, new Rect((int) (playersizex * 0.24), currenty, (int) (playersizex * 0.742
        ), currenty + wa));*/
        while (!stackSteps.isEmpty()) {
            Object[] aux = stackSteps.pop();
            byte[] Steps = (byte[]) aux[0];
            currenty = (int) aux[1];
            int nexty = (int) aux[2];
            for (int j = 0; j < Steps.length && speed != 0; j++) {

                if (efecto) {
                    posintx = (int) (posintX2 + Math.sin((double) (aux2 - currenty) / wa / 1.2) * wa * 0.8);
                }
 /*  0 null char
            1 normal step
            2 start long
            3 end long
            4 body long
            5 fake
            6 hidden
            7 mine
            8 poisson


            255 presed

        */

                switch (Steps[j]) {
                    case (1):
                    case (5):
                        arrows2[j].draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
                        break;
                    case (2):
                        //   longs[j].draw(c, new Rect(posintx + wa * j - 20, currenty + wa / 2, posintx + wa * j + wa, nexty));
                        longs[j].draw(c, new Rect(posintx + wa * j - 20, currenty + wa / 2, posintx + wa * j + wa, Longinfo[j]));
                        arrows2[j].draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
                        Longinfo[j] = -9999;
                        break;
                    case (3):
                        verifyLong(j, currenty);
                        tails[j].draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
                        break;
                    case (4):
                        verifyLong(j, currenty);

                        break;
                    case (7):

                        mine.draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
                        break;
                    case (100):
                        longs[j].draw(c, new Rect(posintx + wa * j - 20, currenty + wa / 2, posintx + wa * j + wa, Longinfo[j]));
                        Longinfo[j] = -9999;
                        break;
                    default:
                }
            }
        }
        for (int j = 0; j < 10; j++) {
            if (Longinfo[j] != -9999) {
                longs[j].draw(c, new Rect(posintx + wa * j - 20, 0, posintx + wa * j + wa, Longinfo[j]));
                Longinfo[j] = -9999;
            }
        }
        currenty = (int) (playerSizeY * 0.085);
        for (int j = 0; j < 10 && speed != 0; j++) {
            explotions[j].draw2(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
            explotionTails[j].draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
            if (Longinfo[j] != -9999) {
                longs[j].draw(c, new Rect(posintx + wa * j - 20, 0, posintx + wa * j + wa, Longinfo[j]));
                Longinfo[j] = -9999;
            }
        }

        Matrix sken = new Matrix();
        if (efecto) {
            Camera camera = new Camera();
            camera.rotateY(-1);
            camera.rotateX(7);
            camera.rotateZ(0);
            camera.getMatrix(sken);
        }
        ca.drawBitmap(stepsBitmap, sken, new Paint());


    }

    public void draw(Canvas ca, Stack<String[]> stackSteps, int currenty, int speed, int posintx, int wa, int playersizex, int playerSizeY) {
        Canvas c;
        Bitmap stepsBitmap = Bitmap.createBitmap(playersizex, playerSizeY, Bitmap.Config.ARGB_8888);
        if (efecto) {
            c = new Canvas(stepsBitmap);
        } else {
            c = ca;
        }

        int posintX2 = posintx;
        int aux2 = (int) (playerSizeY * 0.085);
        currenty = (int) (playerSizeY * 0.085);
        /*receptor.draw(c, new Rect((int) (playersizex * 0.24), currenty, (int) (playersizex * 0.742
        ), currenty + wa));*/
        while (!stackSteps.isEmpty()) {
            String[] aux = stackSteps.pop();
            String Steps = aux[0];
            currenty = Integer.parseInt(aux[1]);
            int nexty = Integer.parseInt(aux[2]);
            for (int j = 0; j < Steps.length() && speed != 0; j++) {

                if (efecto) {
                    posintx = (int) (posintX2 + Math.sin((double) (aux2 - currenty) / wa / 1.2) * wa * 0.8);
                }


                switch (Steps.charAt(j)) {
                    case ('1'):
                        arrows2[j].draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
                        break;
                    case ('F'):
                        arrows2[j].draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
                        break;
                    case ('2'):
                        //   longs[j].draw(c, new Rect(posintx + wa * j - 20, currenty + wa / 2, posintx + wa * j + wa, nexty));
                        longs[j].draw(c, new Rect(posintx + wa * j - 20, currenty + wa / 2, posintx + wa * j + wa, Longinfo[j]));
                        arrows2[j].draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));

                        Longinfo[j] = -9999;
                        break;
                    case ('3'):
                        verifyLong(j, currenty);
                        tails[j].draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
                        break;
                    case ('L'):
                        verifyLong(j, currenty);

                        break;
                    case ('M'):
                        //c.drawBitmap(items.frames[6], null, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa), new Paint());
                        mine.draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
                        break;
                    case ('P'):
                        //c.drawBitmap(items.frames[6], null, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa), new Paint());

                        longs[j].draw(c, new Rect(posintx + wa * j - 20, currenty + wa / 2, posintx + wa * j + wa, Longinfo[j]));
                        Longinfo[j] = -9999;
                        break;
                    default:
                }
            }
        }
        for (int j = 0; j < 10; j++) {
            if (Longinfo[j] != -9999) {
                longs[j].draw(c, new Rect(posintx + wa * j - 20, 0, posintx + wa * j + wa, Longinfo[j]));
                Longinfo[j] = -9999;
            }
        }
        currenty = (int) (playerSizeY * 0.085);
        for (int j = 0; j < 10 && speed != 0; j++) {
            explotions[j].draw2(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
            explotionTails[j].draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
            if (Longinfo[j] != -9999) {
                longs[j].draw(c, new Rect(posintx + wa * j - 20, 0, posintx + wa * j + wa, Longinfo[j]));
                Longinfo[j] = -9999;
            }
        }

        Matrix sken = new Matrix();
        if (efecto) {
            Camera camera = new Camera();
            camera.rotateY(-1);
            camera.rotateX(7);
            camera.rotateZ(0);
            camera.getMatrix(sken);
        }
        ca.drawBitmap(stepsBitmap, sken, new Paint());


    }

    public void update() {
        for (int x = 0; x < 10; x++) {
            arrows2[x].update();
            tails[x].update();
            longs[x].update();
            explotions[x].update();
        }
        receptor.update();
        mine.update();
    }


    private void verifyLong(int pos, int y) {
        if (Longinfo[pos] == -9999 || Longinfo[pos] < y) {
            Longinfo[pos] = y;
        }

    }




    public void makeDDR(Context context){
        Bitmap upOn = BitmapFactory.decodeResource(context.getResources(), R.drawable.dance_pad_up_on);

        explotionTails[0]=explotionTails[2];
        explotionTails[1]=explotionTails[2];
        explotionTails[3]=explotionTails[2];
        tails[0]=tails[2];
        tails[1]=tails[2];
        tails[3]=tails[2];
        arrows2[0]=new SpriteReader(TransformBitmap.RotateBitmap(upOn, 270), 1, 1, 0.2f);
        arrows2[1]=new SpriteReader(TransformBitmap.RotateBitmap(upOn, 180), 1, 1, 0.2f);
        arrows2[2]=new SpriteReader(upOn, 1, 1, 0.2f);
        arrows2[3]=new SpriteReader(TransformBitmap.RotateBitmap(upOn, 90), 1, 1, 0.2f);
        arrows2[0].play();
        arrows2[1].play();
        arrows2[2].play();
        arrows2[3].play();
    }

}

