package com.example.tripnaples;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ActivityStruttureIntornoaMeTest {

    @Test
    public void addOnMarkerSize() {
        ActivityStruttureIntornoaMe activity = new ActivityStruttureIntornoaMe();
        ArrayList<Struttura> arrayStrutture = new ArrayList<>();
        Struttura st1 = new Struttura(1,"via 1", 1, 40.567, 56.789, "Struttura 1", "Napoli", "Ristorante","www.image.it/struttura1");
        Struttura st2 = new Struttura(2,"via 2", 2, 39.567, 45.789, "Struttura 2", "Napoli", "Hotel","www.image.it/struttura2");
        Struttura st3 = new Struttura(3,"via 3", 3, 35.567, 41.789, "Struttura 3", "Pozzuoli", "Museo","www.image.it/struttura3");
        arrayStrutture.add(st1);
        arrayStrutture.add(st2);
        arrayStrutture.add(st3);
        activity.addOnMarker(arrayStrutture);
        assertEquals(3,arrayStrutture.size());
    }
}