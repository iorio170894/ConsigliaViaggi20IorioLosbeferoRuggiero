package com.example.UrbanTrip;

import com.example.UrbanTrip.Activity.ActivityStrutturaLoggato;

import org.junit.Test;

import static org.junit.Assert.*;

public class ActivityStrutturaLoggatoTest {

    /*TESTING BLACK BOX*/

    /*CLASSI DI EQUIVALENZA:
    * • CE1 = {0.5...5} VALIDO 1
      • CE2 = {5.5...MaxInt} NON VALIDO 2
      • CE3 = {MinInt...0}  NON VALIDO 3

    /*TEST CASE:
      • TC1: getMediaRecensioni ({2.0,5.0}) copre CE1,         VALIDO 1
      • TC2: getMediaRecensioni ({3.5,4,6.5}) copre CE2,       NON VALIDO 2
      • TC3: getMediaRecensioni ({3.5,4,-2.5}) copre CE3       NON VALIDO 3
*/
    @Test
    public void getMediaRecensioniTC1() {
        ActivityStrutturaLoggato activity = new ActivityStrutturaLoggato();
        double[] input={2.0,5.0};
        double ritorno;
        ritorno=activity.getMediaRecensioni(input);
        assertTrue(ritorno==3.5);
    }

    @Test (expected = IllegalArgumentException.class)
    public void getMediaRecensioniTC2() {
        ActivityStrutturaLoggato activity = new ActivityStrutturaLoggato();
        double[] input={3.5,4,6.5};
        double ritorno;
        ritorno=activity.getMediaRecensioni(input);
    }

    @Test (expected = IllegalArgumentException.class)
    public void getMediaRecensioniTC3() {
        ActivityStrutturaLoggato activity = new ActivityStrutturaLoggato();
        double[] input={3.5,4,-2.5};
        double ritorno;
        ritorno=activity.getMediaRecensioni(input);
    }

    //Test con array vuoto in input
    @Test (expected = IllegalArgumentException.class)
    public void getMediaRecensioniOnEmptyArray() {
        ActivityStrutturaLoggato activity = new ActivityStrutturaLoggato();
        double[] input = {};
        double ritorno;
        ritorno = activity.getMediaRecensioni(input);
    }
}