package com.example.rodrigo.sgame.CommonGame;
/**
 * @author kyagamy
 */

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SSC {
    private ArrayList<Float[]> SCROLLS = null, BPMS = null, FAKES = null;
    ArrayList<String[]> ATTACKS = null;
    public Map<String, String> songinfo = new HashMap<>();//datos generales para el screen select music
    public Map[] chartsinfo = new Map[30];//datos de cada chart
    public ArrayList[] charts = new ArrayList[30];//stepps como arraylist de arraylist de arraylist
    private short waslong[] = new short[40];

    @TargetApi(Build.VERSION_CODES.KITKAT)


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public SSC(String raw) {
        this.ParseSSCLines(StripComments(raw),false);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public SSC(String raw, boolean option ) {
        this.ParseSSCLines(StripComments(raw),option);
    }

    private static String StripComments(String data) {//Ya quedo uwu
        return data.replaceAll("(\\s+//-([^;]+)\\s)|(//[\\s+]measure\\s[0-9]+\\s)", "");
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)


    public void ParseSSCLines(String data,boolean miniVer) {

        for (int hh = 0; hh < 12; hh++) {
            waslong[hh] = 0;
        }
        Matcher m = Pattern.compile("#([^;]+);").matcher(data);
        int chart = -1;
        String currentmatch;
        while (m.find()) {
            String auxstring = m.group();
            String key = auxstring.substring(1, auxstring.indexOf(":"));
            String valor = auxstring.substring(auxstring.indexOf(":") + 1, auxstring.indexOf(";"));
            if (key.equals("NOTEDATA")) {
                chart += 1;

            } else {
                if (chart > -1) {//es cuando ya se entró al chart
                    switch (key) {
                        case "NOTES":
                            if (!miniVer) {
                                valor = valor.replaceAll(" ", "");
                                this.charts[chart] = this.step2Array(valor);
                            }break;
                        case "BPMS":
                        case "STOPS":
                        case "DELAYS":
                        case "WARPS":
                        case "TIMESIGNATURES":
                        case "TICKCOUNTS":
                        case "COMBOS":
                        case "SPEEDS":
                        case "LABELS":
                        case "SCROLLS":
                        case "STEPSTYPE":
                            if (miniVer){
                                break;
                            }
                        default:
                            if (this.chartsinfo[chart] == null) {
                                Map<String, String> currentChartInfo = new HashMap<>();
                                this.chartsinfo[chart] = currentChartInfo;
                                this.chartsinfo[chart].put(key, valor);
                            } else {
                                this.chartsinfo[chart].put(key, valor);
                            }
                    }
                } else {
                    this.songinfo.put(key, valor);
                }
            }
        }
        int x = 0;
    }


    private ArrayList step2Array(String data) {
        ArrayList<ArrayList<String>> steps = new ArrayList<>();
        ArrayList<String> auxBlock = new ArrayList<>();
        String[] arrayString = data.split("\n");
        for (int x = 1; x < arrayString.length; x++) {
            if (arrayString[x].contains(",")) {
                steps.add(auxBlock);
                auxBlock = new ArrayList<>();
                if (arrayString[x].contains("0") || arrayString[x].contains("1") || arrayString[x].contains("2") || arrayString[x].contains("3")) {
                    auxBlock.add(arrayString[x].replace(",", ""));
                }

            } else {
                auxBlock.add(arrayString[x]);
            }
        }
        steps.add(auxBlock);
        return steps;
    }





    private ArrayList step2Array2(String data) {
        ArrayList<ArrayList<String>> steps = new ArrayList<>();
        ArrayList<String> auxBlock = new ArrayList<>();
        String[] arrayString = data.split("\n");
        for (String  x :arrayString) {
            if (x.contains(",")) {
                steps.add(auxBlock);
                auxBlock = new ArrayList<>();
                if (x.contains("0") || x.contains("1") || x.contains("2") || x.contains("3")) {
                    auxBlock.add(x.replace(",", ""));
                }
            } else {
                auxBlock.add(x);
            }
        }
        //steps.add(auxBlock);
        return steps;
    }


    public ArrayList createBuffer(int nchar) {
        if (this.chartsinfo[nchar].get("SCROLLS") != null && !this.chartsinfo[nchar].get("SCROLLS").equals("")) {
            SCROLLS = this.arrayListTag(this.chartsinfo[nchar].get("SCROLLS").toString());
        }
        double currentBeat = 0;//offset/(60/BPM);
        ArrayList<String[]> buffer = new ArrayList<>();
        ArrayList<ArrayList<String>> aux = this.charts[nchar];
        int numberBlock = 0;
        for (int i = 0; i < aux.size(); i++) {
            for (int j = 0; j < 192; j++) {
                String auxString[] = new String[4];
                if (aux.get(i).size() == 192) {
                    auxString[0] = checkLong(aux.get(i).get(j));
                } else {
                    int div = 192 / aux.get(i).size();
                    if (j % div == 0) {
                        auxString[0] = checkLong(aux.get(i).get(j / div));
                    } else {
                        auxString[0] = checkLong("0000000000");
                    }
                }
                auxString[1] = 192 + "";
                auxString[2] = currentBeat + "";
                auxString[3] = "1";
                buffer.add(auxString);
                currentBeat = (double) numberBlock / 48;
                numberBlock++;
                //currentBeat += (double) 4 / 192;
            }
        }
        for (int i = 0; i < buffer.size() && SCROLLS != null; i++) {
            buffer.get(i)[3] = foundScroll(Float.parseFloat(buffer.get(i)[2]), SCROLLS) + "";
        }

        return buffer;
    }


    public ArrayList <Object[]>createBuffer2(int nchar) {
        if (this.chartsinfo[nchar].get("SCROLLS") != null && !this.chartsinfo[nchar].get("SCROLLS").equals("")) {
            SCROLLS = this.arrayListTag(this.chartsinfo[nchar].get("SCROLLS").toString());
        }
        double currentBeat = 0;//offset/(60/BPM);
        ArrayList<Object[]> buffer = new ArrayList<>();
        ArrayList<ArrayList<String>> aux = this.charts[nchar];
        int numberBlock = 0;


        for (int i = 0; i < aux.size(); i++) {
            for (int j = 0; j < 192; j++) {
                Object auxObject[] = new Object[4];
                if (aux.get(i).size() == 192) {
                    auxObject[0] = stringStep2ByteArary(checkLong(aux.get(i).get(j)));
                } else {
                    int div = 192 / aux.get(i).size();
                    if (j % div == 0) {
                        auxObject[0] =stringStep2ByteArary( checkLong(aux.get(i).get(j / div)));
                    } else {
                        auxObject[0] = stringStep2ByteArary(checkLong("0000000000"));
                    }
                }

                auxObject[1] = currentBeat;
                auxObject[2] = 1f;
                buffer.add(auxObject);
                currentBeat = (double) numberBlock / 48;
                numberBlock++;
                //currentBeat += (double) 4 / 192;
            }
        }
        for (int i = 0; i < buffer.size() && SCROLLS != null; i++) {
            buffer.get(i)[2] = foundScroll((Double) buffer.get(i)[1], SCROLLS) ;
        }

        return buffer;
    }

    private String checkLong(String charts) {
        charts = charts.replace(" ", "");
        charts = charts.replace("{2|v|1|0}", "v");
        charts = charts.replace("{2|v|1|0}", "v");
        charts = charts.replace("{1|v|1|0}", "1");
        if (charts.length() > 10) {
            charts = charts.substring(0, 10);
        }
        for (int x = 0; x < charts.length(); x++) {

            if (charts.charAt(x) == '2') {
                waslong[x] = 1;
            } else if (charts.charAt(x) == '3') {
                waslong[x] = 0;
            } else if (charts.charAt(x) == '1') {
                waslong[x] = 0;
            } else if (charts.charAt(x) == '0' && waslong[x] == 1) {
                charts = changeCharInPosition(x, 'L', charts);
            }

        }

        return charts;
    }

    public String changeCharInPosition(int position, char ch, String str) {
        char[] charArray = str.toCharArray();
        charArray[position] = ch;
        return new String(charArray);
    }


    public ArrayList<Float[]> arrayListTag(String data) {
        ArrayList<Float[]> buffer = new ArrayList<>();
        String[] array = data.replace("\n", "").split(",");
        for (String x :array) {
            Float auxString[] = new Float[2];
            auxString[0] = Float.parseFloat(x.substring(0, x.indexOf("=")));
            auxString[1] = Float.parseFloat(x.substring(x.indexOf("=") + 1, x.length() - 1));
            buffer.add(auxString);
        }

        return buffer;
    }


    public ArrayList<Float[]> arrayListSpeed(String data) {
        ArrayList<Float[]> buffer = new ArrayList<>();
        String[] array = data.replace("\n", "").split(",");
        for (String x :array) {
            Float auxString[] = new Float[6];
            String[] array24 = x.split("=");
            for (int j = 0; j < array24.length; j++) {
                auxString[j] = Float.parseFloat(array24[j]);
            }
            buffer.add(auxString);
        }

        return buffer;
    }


    public ArrayList<String[]> listAttack(String data) {
        ArrayList<String[]> buffer = new ArrayList<>();
        String[] array = data.replace("\n", "").split(":");
        int x = 0;
        /*
        while( x < array.length-1) {
            String auxString[] = new String[3];

            auxString[0]=array[x++];
            auxString[1]=array[x++];
            auxString[2]=array[x++];

            buffer.add(auxString);
        }*/

        return buffer;
    }


    private float foundScroll(double Beat, ArrayList<Float[]> SCROLLS) {
        float f = 1f;
        if (SCROLLS != null || SCROLLS.size() == 1) {
            for (int x = 0; x < SCROLLS.size() && SCROLLS.get(x)[0] <= Beat; x++) {
                f = SCROLLS.get(x)[1];
            }
        } else {

            return 1f;
        }

        return f;
    }

    public static byte[] stringStep2ByteArary(String stepData) {
        /*  0 null char
            1 normal step
            2 start long
            3 end long
            4 body long
            5 fake
            6 hidden
            7 mine
            8 poisson


            255

        */

        byte[] data = new byte[stepData.length()];
        for (int x = 0; x < data.length; x++) {
            char aux = stepData.charAt(x);
            switch (aux) {
                case '1':
                    data[x] = 1;
                    break;
                case '2':
                    data[x] = 2;
                    break;
                case '3':
                    data[x] = 3;
                    break;
                case 'L':
                    data[x] = 4;
                    break;
                case 'M':
                    data[x] = 7;
                    break;
                case 'F':
                    data[x] = 5;
                    break;
                case 'P':
                    data[x] = 127;
                    break;
                default:
                    data[x] = 0;
                    break;

            }

        }

        return data;


    }


}
