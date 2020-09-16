package com.example.tripnaples;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class ActivityRistorantiIntornoaMe extends AppCompatActivity {
    //variabili
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ristoranti_intornoa_me);

        //Assegna variabili
        supportMapFragment=(SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        //inizializza locazione
        client = LocationServices.getFusedLocationProviderClient(this);

        // check permessi

        if (ActivityCompat.checkSelfPermission(ActivityRistorantiIntornoaMe.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            //permesso garantito
            //chiama il metodo
            getCurrentLocation();
        }
        else{
            //permesso negato
            //richiedi permessi
            ActivityCompat.requestPermissions(ActivityRistorantiIntornoaMe.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }
    }

    private void getCurrentLocation() {
        //inizializza task location
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                //quando ha successo
                if (location != null){
                    //sincronizza mappa
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            //inizializza latitudine e longitudine
                            LatLng latLng = new LatLng(location.getLatitude()
                                    ,location.getLongitude());
                            //crea marker options
                            MarkerOptions options = new MarkerOptions().position(latLng)
                                    .title("Io sono qui");
                            //Zoom su mappa
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                            //Aggiungi marker su mappa
                            googleMap.addMarker(options);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44){
            if ( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //quando i permessi sono dati
                //chiama il metodo
                getCurrentLocation();
            }
        }
    }
}