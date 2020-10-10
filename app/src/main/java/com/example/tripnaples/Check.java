package com.example.tripnaples;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Check {

    static boolean loggato=true;
    static String firma;
    static String inputUrl;
    static LatLng coordinateStruttura;
    static String nomeStruttura;
    static String indirizzoStruttura;
    static String cittàStruttura;
    static String tipoStruttura;
    static int rangePrezzo;
    static boolean controlloActivityImpostazioni;
    static int codiceStruttura;
    static String link_immagine;
    static String[] strings = new String[1000];
    static ArrayList<Struttura> arrayStrutture=new ArrayList<>();
    static LatLng coordinateStrutturaPerNome;
    static String nomeStrutturaPerNome;
}