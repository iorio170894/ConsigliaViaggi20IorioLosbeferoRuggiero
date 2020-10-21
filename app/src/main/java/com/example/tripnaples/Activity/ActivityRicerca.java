package com.example.tripnaples.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;


import com.example.tripnaples.Models.Struttura;
import com.example.tripnaples.R;
import com.example.tripnaples.Utility.Check;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationServices;

import android.location.Location;
import android.Manifest;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


public class ActivityRicerca extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

   // private GoogleMap mMap;
   // Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    SearchView searchView;
    SupportMapFragment mapFragment;

    ArrayList<Struttura> arrayStrutture= new ArrayList<>();
    String strutturaSelected;
    String cittàSelected;
    int range;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricerca);


        //Spinner Categoria Struttura
        final Spinner spinnerStruttura = findViewById(R.id.SpinnerStruttura);
        final ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this,
                R.array.spinnerStrutture,
                R.layout.color_spinner_layout2);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spinnerStruttura.setAdapter(adapter);
        spinnerStruttura.setOnItemSelectedListener(this);
        //final String strutturaSelected = adapter.getItem(positionSpinner).toString();

        //Sinner Range di prezzo
        final Spinner spinnerRangePrezzo = findViewById(R.id.SpinnerRangePrezzo);
        final ArrayAdapter adapter2 = ArrayAdapter.createFromResource(
                this,
                R.array.spinnerRangePrezzo,
                R.layout.color_spinner_layout2);
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spinnerRangePrezzo.setAdapter(adapter2);
        spinnerRangePrezzo.setOnItemSelectedListener(this);


        //Azione sul bottone
        Button cercaStruttureConFiltri;
        cercaStruttureConFiltri = findViewById(R.id.buttonCercaRicerca);
        cercaStruttureConFiltri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strutturaSelected=spinnerStruttura.getSelectedItem().toString();
                String rangePrezzoSelected=spinnerRangePrezzo.getSelectedItem().toString();
                EditText inputcittàSelected= findViewById(R.id.editTextCittàStruttura);
                cittàSelected = String.valueOf(inputcittàSelected.getText());
                range=0;

                if (rangePrezzoSelected.equals("basso")){
                    rangePrezzoSelected="1";
                    range=Integer.parseInt(rangePrezzoSelected);
                }
                else if (rangePrezzoSelected.equals("medio")){
                    rangePrezzoSelected="2";
                    range=Integer.parseInt(rangePrezzoSelected);
                }
                else if (rangePrezzoSelected.equals("alto")){
                    rangePrezzoSelected="3";
                    range=Integer.parseInt(rangePrezzoSelected);
                }

                if (cittàSelected.isEmpty()){
                    inputcittàSelected.setError("Attenzione campo vuoto!");
                }
                else {

                    Check.tipoRicerca="filtrata";
                    Check.inputTipoStrutturaForSearch=strutturaSelected;
                    Check.inputCittàStrutturaForSearch=cittàSelected;
                    Check.inputRangeStrutturaForSearch=range;

                    //mostra le strutture intorno a me che soddisfano i requisiti nell'activity ActivityStruttureIntornoaMe
                    startActivity(new Intent(ActivityRicerca.this, ActivityStruttureIntornoaMe.class));
                }


            }

        });


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //positionSpinner=i;
        String text = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}