package com.magic.carlosso.magic;

/**
 * Created by stein on 13.1.2015.
 */
import java.util.TimerTask;

public class MojeTimerTask extends TimerTask {

    MagicActivity nadrA;
    private long casPoslednihoZapisuPromenne = 0;
    private long casPoslednihoZapisuKostky = 0;

    public MojeTimerTask(MagicActivity nadr) {
        nadrA = nadr;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        long aktCas = System.currentTimeMillis();

        if ((aktCas - casPoslednihoZapisuPromenne) > Constants.SMAZAT_ZA_MS) {
            // ---smaze delty
            if(!nadrA.isButtonPlusLife1LongPressed) {
                nadrA.NulujDelty();
                NulujTexty();
            }
        }
        if ((aktCas - casPoslednihoZapisuKostky) > Constants.SMAZAT_KOSTKU_ZA_MS) {
            // ---smaze delty
            NulujKostku();
        }
    }

    public void setCasPosledniho() {
        casPoslednihoZapisuPromenne = System.currentTimeMillis();
    }
    public void setCasPoslednihoZapisuKostky() {
        casPoslednihoZapisuKostky = System.currentTimeMillis();
    }

    protected void NulujTexty() {
        nadrA.runOnUiThread(new Runnable() {
            public void run() {
                nadrA.VykresliDelty();

            }
        });
    }

    protected void NulujKostku() {
        nadrA.runOnUiThread(new Runnable() {
            public void run() {
                nadrA.NulujKostku();

            }
        });
    }

}
