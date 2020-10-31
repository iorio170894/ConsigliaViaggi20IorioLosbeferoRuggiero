package com.example.UrbanTrip.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;


import com.example.UrbanTrip.DAOfactory.DAOFactory;
import com.example.UrbanTrip.DAOfactory.StrutturaDAO;
import com.example.UrbanTrip.DAOfactory.onResultList;
import com.example.UrbanTrip.Models.Struttura;
import com.example.UrbanTrip.R;
import com.example.UrbanTrip.Utility.Check;
import com.example.UrbanTrip.Utility.MapsClass;
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
import android.widget.Button;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;


import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


public class ActivityStruttureIntornoaMe extends AppCompatActivity implements OnMapReadyCallback,
        LocationListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private GoogleMap mMap;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    SupportMapFragment mapFragment;
    static boolean check_premuto;
    ArrayList<Struttura> arrayStrutture=new ArrayList<>();
    public StrutturaDAO StruttDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_struttre_intornoa_me);

        check_premuto=false;
        //Se il GPS non è attivo
        if (!MapsClass.isGPSEnabled(ActivityStruttureIntornoaMe.this)) {
            new AlertDialog.Builder(ActivityStruttureIntornoaMe.this)
                    .setMessage("Attenzione, per visualizzare questa schermata attiva il GPS!")
                    .setCancelable(false)
                    .setPositiveButton("Opzioni", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(i);
                        }
                    })
                    .setNegativeButton("Indietro", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            onBackPressed();
                        }
                    })
                    .show();
        }

        //GET JSON per recuperare le strutture dato come input l'url

        //Ricerca di strutture intorno a me per tipo
        if ((Check.tipoRicerca).equals("intorno a me")) {

            DAOFactory DF = DAOFactory.getDAOInstance(ActivityStruttureIntornoaMe.this);
            StruttDAO = DF.getServerStrutturaDAO();
            StruttDAO.getStrutturaByTipo(new onResultList() {
                @Override
                public void getResult(Object object) {
                    arrayStrutture.add((Struttura) object);
                }

                @Override
                public void onFinish() {
                    //Aggiungi i marker sulla mappa delle strutture trovate
                    addOnMarker(arrayStrutture);
                }
            }, Check.inputTipoStrutturaForSearch, ActivityStruttureIntornoaMe.this);
        }

        //Ricerca di strutture intorno a me per filtri (Ricerca avanzata)
        else if ((Check.tipoRicerca).equals("filtrata")){

            DAOFactory DF = DAOFactory.getDAOInstance(ActivityStruttureIntornoaMe.this);
            StruttDAO = DF.getServerStrutturaDAO();
            StruttDAO.getStrutturaByFilter(new onResultList() {
                @Override
                public void getResult(Object object) {
                    arrayStrutture.add((Struttura) object);
                }

                @Override
                public void onFinish() {
                    //Aggiungi i marker sulla mappa delle strutture trovate
                    addOnMarker(arrayStrutture);
                }
            }, Check.inputTipoStrutturaForSearch,Check.inputCittàStrutturaForSearch,Check.inputRangeStrutturaForSearch, ActivityStruttureIntornoaMe.this);

        }

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

    }

    public void addOnMarker(ArrayList<Struttura> arrayStrutture){

        for (int i = 0; i < arrayStrutture.size(); i++) {
            final Struttura strutturaInserita = arrayStrutture.get(i);
            LatLng latLngStrutture = new LatLng(strutturaInserita.getLatitudine(), strutturaInserita.getLongitudine());

            MarkerOptions markerOptionsStrutture = new MarkerOptions();
            markerOptionsStrutture.position(latLngStrutture);
            markerOptionsStrutture.title(strutturaInserita.getNome());
            markerOptionsStrutture.snippet(strutturaInserita.getTipo_struttura());

            //Scegli il tipo di marker
            markerOptionsStrutture.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            MapsClass.chooseTypeMarker(markerOptionsStrutture,strutturaInserita.getTipo_struttura(),ActivityStruttureIntornoaMe.this);
            mCurrLocationMarker = mMap.addMarker(markerOptionsStrutture);

        }

        if ((Check.tipoRicerca).equals("filtrata")) {
            Struttura strutturaForMarker = arrayStrutture.get(arrayStrutture.size()-1);
            LatLng latLngForMarker= new LatLng(strutturaForMarker.getLatitudine(),strutturaForMarker.getLongitudine());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngForMarker,11));
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        //check permessi
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

    }
    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(50);
        mLocationRequest.setFastestInterval(50);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
       /* if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }*/


        final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        final MarkerOptions markerOptions = new MarkerOptions();
        //Aggiungi marker di colore verde con posizione corrente
        markerOptions.position(latLng);
        markerOptions.title("Posizione Corrente");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //Set current position
        Check.latLngCurrent=latLng;

        if (!(Check.tipoRicerca).equals("filtrata"))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));

        //azione sul click di un marker
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                final LatLng latLngCurrent=marker.getPosition();

                double latitudineCurr=latLngCurrent.latitude;
                double longitudineCurr=latLngCurrent.longitude;

                for (int i=0; i<arrayStrutture.size(); i++){
                    final Struttura strutturaCurr=arrayStrutture.get(i);
                    //se trovo una struttura con la stessa lat e long del marker selezionato allora ho trovato la mia struttura
                    if (strutturaCurr.getLatitudine()==latitudineCurr &&
                        strutturaCurr.getLongitudine()==longitudineCurr){

                        double distancetoCurrentPosition=MapsClass.getDistanceKm(latLng,latLngCurrent);

                        //visualizza alertdialog
                        final AlertDialog dialog = new AlertDialog.Builder(ActivityStruttureIntornoaMe.this)
                                .setTitle(strutturaCurr.getTipo_struttura()+": \n"+strutturaCurr.getNome())
                                .setMessage(strutturaCurr.getIndirizzo()+", "+strutturaCurr.getCittà()+"\nDistanza: "+distancetoCurrentPosition+" km.")
                                .setIcon(R.drawable.ic_location)
                                .setPositiveButton("Visualizza dettagli Struttura", null)
                                .show();

                        dialog.getWindow().setGravity(Gravity.BOTTOM);

                        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        positiveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Check.coordinateStruttura=latLngCurrent;
                                Check.nomeStruttura=strutturaCurr.getNome();
                                Check.indirizzoStruttura=strutturaCurr.getIndirizzo();
                                Check.cittàStruttura=strutturaCurr.getCittà();
                                Check.tipoStruttura=strutturaCurr.getTipo_struttura();
                                Check.codiceStruttura=strutturaCurr.getCod_struttura();
                                Check.link_immagine=strutturaCurr.getLink_immagine();
                                Check.rangePrezzo=strutturaCurr.getRange_prezzo();

                                if (Check.loggato)
                                    startActivity(new Intent(ActivityStruttureIntornoaMe.this, ActivityStrutturaLoggato.class));
                                else
                                    startActivity(new Intent(ActivityStruttureIntornoaMe.this, ActivityStrutturaNonLoggato.class));

                                dialog.dismiss();
                            }
                        });
                    }
                }

                return false;
            }
        });

        //interrompere gli aggiornamenti della posizione
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }



    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

}