package com.example.tripnaples;

import org.junit.Test;

import static org.junit.Assert.*;

public class ActivityStrutturaLoggatoTest {

    @Test
    public void getMediaRecensioni() {
        ActivityStrutturaLoggato activity = new ActivityStrutturaLoggato();
        double[] input={2.0,5.0};
        double ritorno;
        ritorno=activity.getMediaRecensioni(input);
        assertTrue(ritorno==3.5);
    }

    @Test (expected = IllegalArgumentException.class)
    public void getMediaRecensioniOnEmptyArray() {
        ActivityStrutturaLoggato activity = new ActivityStrutturaLoggato();
        double[] input = {};
        double ritorno;
        ritorno = activity.getMediaRecensioni(input);
    }
}