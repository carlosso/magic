package com.magic.carlosso.magic;

import java.util.Random;
import java.util.Timer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

public class MagicActivity extends Activity {
    /** Called when the activity is first created. */
    private Integer zivotyHrac1;
    private Integer zivotyHrac2;
    private Integer poisonyHrac1;
    private Integer poisonyHrac2;
    private Integer deltaZivoty1;
    private Integer deltaZivoty2;
    private Integer deltaPoisony1;
    private Integer deltaPoisony2;

    Integer randOd;
    Integer randDo;
    String jmeno1;
    String jmeno2;
    Boolean lightOn;
    MojeTimerTask ulohaTimeru;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        if (lightOn) {
            menu.findItem(R.id.lighton).setTitle(getResources().getString(R.string.backoff));
        } else {
            menu.findItem(R.id.lighton).setTitle(getResources().getString(R.string.backon));
        }

        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // ----nacteni ulozeneho stavu--------------
        SharedPreferences settings = getSharedPreferences("CAR_MTGCOUNTER", 0);
        zivotyHrac1 = settings.getInt("zivotyHrac1", 20);
        zivotyHrac2 = settings.getInt("zivotyHrac2", 20);
        poisonyHrac1 = settings.getInt("poisonyHrac1", 0);
        poisonyHrac2 = settings.getInt("poisonyHrac2", 0);
        jmeno1 = settings.getString("jmeno1", "Me");
        jmeno2 = settings.getString("jmeno2", "You");
        randOd = settings.getInt("randOd", 1);
        randDo = settings.getInt("randDo", 6);
        lightOn = settings.getBoolean("light", false);
        if(lightOn)
        {
            ZapniSvetlo();
        }

        NulujDelty();
        VykresliPole();

        Timer casovac = new Timer();
        ulohaTimeru = new MojeTimerTask(this);
        casovac.schedule(ulohaTimeru, Constants.SMAZAT_KOSTKU_ZA_MS,
                Constants.SMAZAT_KOSTKU_ZA_MS);

    }

    protected static final int REQUEST_ADD_BOOK = 0;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nova_hra:
                NewGame();
                return true;
            case R.id.lighton:
                LightOnOff();
                if (lightOn) {
                    item.setTitle(getResources().getString(R.string.backoff));
                } else {
                    item.setTitle(getResources().getString(R.string.backon));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onStop() {
        super.onStop();
        // ----ulozeni hodnot-----------------
        SharedPreferences settings = getSharedPreferences("CAR_MTGCOUNTER", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("zivotyHrac1", zivotyHrac1);
        editor.putInt("zivotyHrac2", zivotyHrac2);
        editor.putInt("poisonyHrac1", poisonyHrac1);
        editor.putInt("poisonyHrac2", poisonyHrac2);
        jmeno1 = ((EditText) findViewById(R.id.jmeno1)).getText().toString();
        jmeno2 = ((EditText) findViewById(R.id.jmeno2)).getText().toString();
        editor.putString("jmeno1", jmeno1);
        editor.putString("jmeno2", jmeno2);
        editor.putBoolean("light", lightOn);

        // Commit the edits!
        editor.commit();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public void buttonPlusLife1(View button) {
        zivotyHrac1++;
        deltaZivoty1++;
        ((TextView) findViewById(R.id.zivoty1)).setText(zivotyHrac1.toString());
        VykresliDelty();
        ulohaTimeru.setCasPosledniho();

    }

    public void buttonMinusLife1(View button) {
        zivotyHrac1--;
        deltaZivoty1--;
        ((TextView) findViewById(R.id.zivoty1)).setText(zivotyHrac1.toString());
        VykresliDelty();
        ulohaTimeru.setCasPosledniho();

    }

    public void buttonPlusLife2(View button) {
        zivotyHrac2++;
        deltaZivoty2++;
        ((TextView) findViewById(R.id.zivoty2)).setText(zivotyHrac2.toString());
        VykresliDelty();
        ulohaTimeru.setCasPosledniho();

    }

    public void buttonMinusLife2(View button) {
        zivotyHrac2--;
        deltaZivoty2--;
        ((TextView) findViewById(R.id.zivoty2)).setText(zivotyHrac2.toString());
        VykresliDelty();
        ulohaTimeru.setCasPosledniho();

    }

    // -----------------poisony-------------------------

    public void buttonPlusPoison1(View button) {
        poisonyHrac1++;
        deltaPoisony1++;
        ((TextView) findViewById(R.id.poisony1)).setText(poisonyHrac1
                .toString());
        VykresliDelty();
        ulohaTimeru.setCasPosledniho();

    }

    public void buttonMinusPoison1(View button) {
        poisonyHrac1--;
        deltaPoisony1--;
        ((TextView) findViewById(R.id.poisony1)).setText(poisonyHrac1
                .toString());
        VykresliDelty();
        ulohaTimeru.setCasPosledniho();

    }

    public void buttonPlusPoison2(View button) {
        poisonyHrac2++;
        deltaPoisony2++;
        ((TextView) findViewById(R.id.poisony2)).setText(poisonyHrac2
                .toString());
        VykresliDelty();
        ulohaTimeru.setCasPosledniho();

    }

    public void buttonMinusPoison2(View button) {
        poisonyHrac2--;
        deltaPoisony2--;
        ((TextView) findViewById(R.id.poisony2)).setText(poisonyHrac2
                .toString());
        VykresliDelty();
        ulohaTimeru.setCasPosledniho();

    }

    public void buttonRand(View button) {
        double nah;
        Integer max;
        Integer min;
        Integer vysledek;
        Random generator = new Random();
        generator.setSeed(System.currentTimeMillis());
        nah = Math.random();
        max = Integer.parseInt(((EditText) findViewById(R.id.random_do))
                .getText().toString());
        min = Integer.parseInt(((EditText) findViewById(R.id.random_od))
                .getText().toString());
        vysledek = (int) Math.round(nah * (max - min)) + min;
        if (vysledek > max) {
            // ---vyjimecny pripad
            vysledek = max;
        }
        ((EditText) findViewById(R.id.random_vysledek)).setText(vysledek
                .toString());
        ulohaTimeru.setCasPoslednihoZapisuKostky();
    }

    private void NewGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    NulujHru();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    // No button clicked
                    break;
            }
        }
    };

    private void NulujHru() {
        zivotyHrac1 = 20;
        zivotyHrac2 = 20;
        poisonyHrac1 = 0;
        poisonyHrac2 = 0;
        ((TextView) this.findViewById(R.id.zivoty1)).setText(zivotyHrac1
                .toString());
        ((TextView) findViewById(R.id.zivoty2)).setText(zivotyHrac2.toString());
        ((TextView) findViewById(R.id.poisony1)).setText(poisonyHrac1
                .toString());
        ((TextView) findViewById(R.id.poisony2)).setText(poisonyHrac2
                .toString());

    }

    private void LightOnOff() {
        if (lightOn == false) {
            ZapniSvetlo();
            lightOn=true;
        } else {
            VypniSvetlo();
            lightOn=false;

        }
    }
    private void ZapniSvetlo()
    {
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ((View) findViewById(R.id.zarovka1))
                .setBackgroundColor(getResources().getColor(
                        R.color.zarovkaon));

    }
    private void VypniSvetlo()
    {
        getWindow().setFlags(0,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ((View) findViewById(R.id.zarovka1))
                .setBackgroundColor(getResources().getColor(
                        R.color.zarovkaoff));
    }

    private void Zobraz(String retezec) {
        Context context = getApplicationContext();
        CharSequence text = retezec;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void VykresliDelty() {
        // -----zapise do kontrolu delty------------
        String pom;
        if (deltaZivoty1 != 0) {
            pom = deltaZivoty1.toString();
        } else {
            pom = "";
        }
        ((TextView) this.findViewById(R.id.zivoty_delta1)).setText(pom);

        if (deltaZivoty2 != 0) {
            pom = deltaZivoty2.toString();
        } else {
            pom = "";
        }
        ((TextView) this.findViewById(R.id.zivoty_delta2)).setText(pom);

        if (deltaPoisony1 != 0) {
            pom = deltaPoisony1.toString();
        } else {
            pom = "";
        }
        ((TextView) this.findViewById(R.id.poisony_delta1)).setText(pom);

        if (deltaPoisony2 != 0) {
            pom = deltaPoisony2.toString();
        } else {
            pom = "";
        }
        ((TextView) this.findViewById(R.id.poisony_delta2)).setText(pom);

    }

    private void VykresliPole() {
        ((TextView) findViewById(R.id.zivoty1)).setText(zivotyHrac1.toString());
        ((TextView) findViewById(R.id.zivoty2)).setText(zivotyHrac2.toString());
        ((TextView) findViewById(R.id.poisony1)).setText(poisonyHrac1
                .toString());
        ((TextView) findViewById(R.id.poisony2)).setText(poisonyHrac2
                .toString());
        ((EditText) findViewById(R.id.jmeno1)).setText(jmeno1);
        ((EditText) findViewById(R.id.jmeno2)).setText(jmeno2);
        ((EditText) findViewById(R.id.random_od)).setText(randOd.toString());
        ((EditText) findViewById(R.id.random_do)).setText(randDo.toString());

    }

    public void NulujDelty() {
        deltaZivoty1 = 0;
        deltaZivoty2 = 0;
        deltaPoisony1 = 0;
        deltaPoisony2 = 0;
    }
    public void NulujKostku() {
        ((EditText) findViewById(R.id.random_vysledek)).setText("");    }

    }