package com.example.tripnaples;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class ActivityStrutturaNonLoggato extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;

    private TextView textNomeStruttura;
    private TextView textIndirizzoStruttura;
    private TextView textCittàStruttura;
    private TextView textTipoStruttura;
    ImageView imageView;
    RecyclerView recyclerView;

    String utentiRecensione[]={"utente1","utente2","utente3","utente4","utente5","utente6"};
    String descrizioneTestuale[]={"bellissima struttura","esperienza indimenticabile","Non lo consiglio a nessuno!","bellissima struttura","esperienza indimenticabile","Non lo consiglio a nessuno!"};
    double numero_stelle[]={4,3.5,5,1,1.5,3.5};


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_struttura_non_loggato);

        if (!isGPSEnabled()) {
            new AlertDialog.Builder(ActivityStrutturaNonLoggato.this)
                    .setMessage("Attenzione, attiva il GPS!")
                    .setCancelable(false)
                    .setPositiveButton("Opzioni", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(i);
                        }
                    })
                    .setNegativeButton("Cancella", null)
                    .show();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_struttura_non_loggato);
        mapFragment.getMapAsync(this);

        //Assegna i valori alle textview

        textNomeStruttura = findViewById(R.id.nomeStrutturaNonLoggato);
        textIndirizzoStruttura = findViewById(R.id.indirizzoStrutturaNonLoggato);
        textCittàStruttura= findViewById(R.id.cittàStrutturaNonLoggato);
        textTipoStruttura= findViewById(R.id.tipoStrutturaNonLoggato);
        imageView=findViewById(R.id.imageViewStruttura2);

        textNomeStruttura.setText(Check.nomeStruttura);
        textIndirizzoStruttura.setText(Check.indirizzoStruttura);
        textCittàStruttura.setText(Check.cittàStruttura);
        textTipoStruttura.setText(Check.tipoStruttura);

        Glide.with(ActivityStrutturaNonLoggato.this)
                .load(Check.link_immagine)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);

        /*AlertDialog.Builder builder=new AlertDialog.Builder(ActivityStrutturaNonLoggato.this);
        builder.setTitle("Link Immagine");
        builder.setMessage(Check.link_immagine);
        builder.show();*/

        //RecyclerView
        //nestedScrollView = findViewById(R.id.scroll_view);
        //recyclerView = findViewById(R.id.recycler_view);
        //progressBar = findViewById(R.id.progress_bar);

        //utentiRecensione=getResources().getStringArray(R.array.spinnerStrutture);
        //numero_stelle=getResources().getStringArray(R.array.interi);
        //descrizioneTestuale=getResources().getStringArray(R.array.spinnerStrutture);

        recyclerView = findViewById(R.id.recycler_viewNonLoggato);

        MyAdapter myAdapter = new MyAdapter(this, utentiRecensione, descrizioneTestuale, numero_stelle);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions()
                .position(Check.coordinateStruttura)
                .title(Check.nomeStruttura))
                .setSnippet(Check.tipoStruttura);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(Check.coordinateStruttura));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Check.coordinateStruttura,11));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Check.coordinateStruttura,13));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(6));
    }

    private boolean isGPSEnabled() {
        LocationManager cm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return cm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}