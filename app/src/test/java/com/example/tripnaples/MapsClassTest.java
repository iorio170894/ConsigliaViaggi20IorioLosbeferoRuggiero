package com.example.tripnaples;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class MapsClassTest {

    @Test
    public void getDistanceKmSamePositionTest() {
        LatLng latLng1 = new LatLng(40.858294,14.262128);
        LatLng latLng2 = new LatLng(40.858294,14.262128);
        double ritorno;
        ritorno=MapsClass.getDistanceKm(latLng1,latLng2);
        assertTrue(ritorno==0.0);
    }



}