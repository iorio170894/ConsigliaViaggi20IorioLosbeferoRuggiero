package com.example.tripnaples;

import com.example.tripnaples.Utility.MapsClass;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class MapsClassTest {
    /* Il valore della latitudine varia da 0 a 90, mentre il valore della longitudine varia da 0 a 180 */

    //TESTING WHITE-BOX
    /*ci sono 3 branch in questo GFC di conseguenza scriviamo 3 test*/

    @Test (expected = IllegalArgumentException.class)
    public void getDistanceKmTest59_60() {
        LatLng latLng1 = new LatLng(90.0875,20.09876);
        LatLng latLng2 = new LatLng(30.0874,220.09875);
        double ritorno;
        ritorno= MapsClass.getDistanceKm(latLng1,latLng2);
    }

    @Test (expected = IllegalArgumentException.class)
    public void getDistanceKmTest59_62_63() {
        LatLng latLng1 = new LatLng(-30.0875,20.09876);
        LatLng latLng2 = new LatLng(30.0874,20.09875);
        double ritorno;
        ritorno= MapsClass.getDistanceKm(latLng1,latLng2);
    }

    @Test
    public void getDistanceKmTest59_62_66() {
        LatLng latLng1 = new LatLng(30.00000,20.00000);
        LatLng latLng2 = new LatLng(20.00000,20.00000);
        double ritorno;
        ritorno= MapsClass.getDistanceKm(latLng1,latLng2);
        assertTrue(ritorno==1111.9);
    }

    //Test che mi torna distanza 0 se do come input due coordinate uguali
    @Test
    public void getDistanceKmSamePositionTest() {
        LatLng latLng1 = new LatLng(40.858294,14.262128);
        LatLng latLng2 = new LatLng(40.858294,14.262128);
        double ritorno;
        ritorno= MapsClass.getDistanceKm(latLng1,latLng2);
        assertTrue(ritorno==0.0);
    }



}