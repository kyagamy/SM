package com.example.rodrigo.sgame.Player;

import com.example.rodrigo.sgame.CommonGame.Common;

import java.util.ArrayList;

public class RadarEffectsThread extends Thread {
    public boolean running = true;
    GamePlay game;


    public RadarEffectsThread(GamePlay game) {
        this.game = game;
    }


    @Override
    public void run() {
        while (running) {
            try {

            if (checkTime(game.BPMS, game.posBPM + 1)) {

                game.BPM = game.BPMS.get(game.posBPM + 1)[1];
                game.posBPM++;

                if (game.BPMS.get(game.posBPM)[1] >= 100000) {
                    if (game.currentBeat>=game.BPMS.get(1+game.posBPM)[0]) {
                        game.posBPM++;
                    }
                    else{
                        game.lostBeatbyWarp = game.currentBeat - game.BPMS.get(game.posBPM + 1)[0];
                        game.posBPM++;
                        game.BPM = game.BPMS.get(game.posBPM)[1];
                        game.currentDurationFake -= (game.BPMS.get(game.posBPM)[0] - game.currentBeat);
                        game.currentBeat = game.BPMS.get(game.posBPM)[0];

                        while ((double)game.bufferSteps.get(game.posBuffer + 1)[1] <= game.currentBeat) {
                            game.posBuffer++;
                        }

                    }

                } /*else {
                BPM = BPMS.get(posBPM + 1)[1];
                posBPM++;
            }*/
            }

            if (checkTime(game.DELAYS, game.posDelay)) {
                double dif = game.currentBeat - game.DELAYS.get(game.posDelay)[0];
                game.currentBeat = game.DELAYS.get(game.posDelay)[0] + 0.00000;

                game.curentempobeat = System.nanoTime();
                game.currentDelay = game.DELAYS.get(game.posDelay)[1] + game.currentSecond - Common.beat2Second(dif * 1.0018, game.BPM);//0.946f
                game.posDelay++;
            }
            //stops
            if (checkTime(game.STOPS, game.posstop)) {
                //                                                                                                                                                                                 delayTime= (long) (STOPS.get(posstop)[1]*1000000000.0);
                double dif = game.currentBeat - game.STOPS.get(game.posstop)[0];
                game.currentBeat = game.STOPS.get(game.posstop)[0];
                game.curentempobeat = System.nanoTime();
                game.currentDelay = game.STOPS.get(game.posstop)[1] + game.currentSecond - Common.beat2Second(dif * 1.018, game.BPM);
                // STOPS.remove(posstop);
                game.posstop++;
            }

            if (checkTime(game.FAKES, game.posFake)) {
                game.currentDurationFake = game.FAKES.get(game.posFake)[1]+game.FAKES.get(game.posFake)[0];
                game.posFake++;
            }
            //spedmods
            if (checkTime(game.SPEEDS, game.posSpeed)) {
                game.metaBeatSpeed = game.currentBeat + game.SPEEDS.get(game.posSpeed)[2];
                game.lastSpeed = game.speedMod;
                game.beatsofSpeedMod = game.SPEEDS.get(game.posSpeed)[2];
                game.speedMod = game.SPEEDS.get(game.posSpeed)[1];
                if (game.speedMod < 0) {
                    game.speedMod = 0;
                }
                game.posSpeed++;
            }

            //tickcount
            if (checkTime(game.TICKCOUNTS, game.posTickCount)) {
                if (game.posTickCount < game.TICKCOUNTS.size()) {
                    game.posTickCount++;
                } else {
                    game.posTickCount = game.TICKCOUNTS.size() - 2;
                }
                game.currentTickCount = game.TICKCOUNTS.get(game.posTickCount)[1].intValue();
            }
            //scrolls
            //
            if (checkTime(game.SCROLLS, game.posScroll)) {
                game.posScroll++;
            }

            //se interpola el speedMod
            if (game.metaBeatSpeed >= game.currentBeat) {
                double div = (game.metaBeatSpeed - game.currentBeat) / game.SPEEDS.get(game.posSpeed - 1)[2];//%
                if (div > 0.01) {
                    if ( (game.posSpeed>1)&&(div < 0.99 || game.SPEEDS.get(game.posSpeed - 1)[2] == 0)) {
                        double difvel = game.SPEEDS.get(game.posSpeed - 2)[1] - game.SPEEDS.get(game.posSpeed - 1)[1];
                        game.speedMod = (game.lastSpeed + (div * difvel));
                    } else if (div > 0.98) {
                        //speedMod=SPEEDS.get(posSpeed-1)[1];
                        game.lastSpeed = game.speedMod;
                    }

                } else {
                    game.speedMod = game.SPEEDS.get(game.posSpeed - 1)[1];
                    game.lastSpeed = game.speedMod;

                }
            } else if (game.posSpeed > 1) {
                game.speedMod = game.SPEEDS.get(game.posSpeed - 1)[1];
                game.lastSpeed = game.speedMod;
            }


            game.doEvaluate = !(game.currentDurationFake >=game.currentBeat);

            //WARP

            if (game.currentDelay <= 0 && checkTime(game.WARPS, game.posWarp)) {
                if (game.currentBeat>=game.WARPS.get(game.posWarp)[1] + game.WARPS.get(game.posWarp)[0]){
                    game.posWarp++;
                }
                else {
                    game.lostBeatbyWarp = game.currentBeat - game.WARPS.get(game.posWarp)[0];
                    game.currentBeat = game.WARPS.get(game.posWarp)[1] + game.WARPS.get(game.posWarp)[0];//perdidad
                    game.posWarp++;

                    while ((double)game.bufferSteps.get(game.posBuffer + 1)[1] <= game.currentBeat) {
                        game.posBuffer++;
                    }
                }
                //curentempobeat=System.nanoTime();
                //
                //  event = lostBeatbyWarp + "";

                //rrentbeat = WARPS.get(posWarp)[1] + WARPS.get(posWarp)[0]+lostBeatbyWarp;//perdidad
            }

            // event = lostBeatbyWarp + "";



                sleep(13);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    public boolean checkTime(ArrayList<Float[]> array, int pos) {
        return  array!=null && (array.size() > 0 && array.size() > pos && game.currentBeat >= array.get(pos)[0]);
    }


}
//bpm
        /*
        if (1 + posBPM == BPMS.size()) {//Set the last BPM of list
            BPM = BPMS.get(BPMS.size() - 1)[1];

        } else if (BPMS.size() > 1 && BPMS.size() > posBPM) {
            if (posBPM < BPMS.size() - 1 && currentBeat >= BPMS.get(posBPM + 1)[0]) {
                if (BPMS.get(posBPM + 1)[1] >= 100000) {
                   // lostBeatbyWarp = currentBeat - BPMS.get(posBPM + 1)[0];
                    posBPM += 2;
                    BPM = BPMS.get(posBPM   )[1];
                    currentDurationFake -= (BPMS.get(posBPM)[0] - currentBeat);
                    currentBeat = BPMS.get(posBPM)[0];

                    while (Double.parseDouble(bufferSteps.get(posBuffer + 1)[2]) <= currentBeat) {
                        posBuffer++;
                    }
                    calculateEffects();

                } else {
                    BPM = BPMS.get(posBPM + 1)[1];
                    posBPM++;
                }
            }
        }
        */
// delays
