package com.example.tripnaples.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tripnaples.DAOfactory.DAOFactory;
import com.example.tripnaples.DAOfactory.StrutturaDAO;
import com.example.tripnaples.DAOfactory.onResultList;
import com.example.tripnaples.Models.Struttura;
import com.example.tripnaples.R;
import com.example.tripnaples.Utility.Check;
import com.example.tripnaples.Utility.MapsClass;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.util.Strings;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class ActivityRicercaPerNome extends AppCompatActivity implements OnMapReadyCallback,
        LocationListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapter;
    GoogleMap mMap;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    Marker marketToRemove;
    Marker setLocation;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    SupportMapFragment mapFragment;
    public StrutturaDAO StruttDAO;
    private RequestQueue mQueue;
    ArrayList<Struttura> arrayStrutture=new ArrayList<>();
    static boolean check_premuto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricerca_per_nome);

        check_premuto=false;

        //controllo gps
        if (!MapsClass.isGPSEnabled(ActivityRicercaPerNome.this)) {
            new AlertDialog.Builder(ActivityRicercaPerNome.this)
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

        autoCompleteTextView=findViewById(R.id.ac_text_view);

        //Ricevi un Arraylist con tutte le strutture
        DAOFactory DF = DAOFactory.getDAOInstance(ActivityRicercaPerNome.this);
        StruttDAO = DF.getServerStrutturaDAO();
        StruttDAO.getAllStrutture(new onResultList() {
            @Override
            public void getResult(Object object) {
                arrayStrutture.add((Struttura) object);
            }

            @Override
            public void onFinish() {
                String[] stringNomi = new String[arrayStrutture.size()];
                for (int i=0; i<arrayStrutture.size(); i++){
                    stringNomi[i]=arrayStrutture.get(i).getNome();
                }

                //Setta l'array di Stringhe contenente i nomi delle strutture nell'adapter
                setAdapter(stringNomi);

            }
        },ActivityRicercaPerNome.this);

        //Cancellare il contenuto scritto cliccando sul drawable X a destra
        autoCompleteTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int DRAWABLE_RIGHT = 2;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (autoCompleteTextView.getRight() - autoCompleteTextView.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        autoCompleteTextView.setText("");
                        return true;
                    }
                }
                return false;
            }
        });

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_per_nome);
        mapFragment.getMapAsync(this);


    }

    public void setAdapter(String[] strings){

        adapter=new ArrayAdapter<>(ActivityRicercaPerNome.this
                ,android.R.layout.simple_list_item_1,strings);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int s, long l) {

                //Aggiungi martker alla mappa
                addMarkerSelected(arrayStrutture,adapter.getItem(s));

            }
        });

    }


    public void addMarkerSelected(ArrayList<Struttura> arrayStrutture, String nomeStruttura ){

        for (int i=0; i<arrayStrutture.size(); i++){

            final Struttura strutturaCurr=arrayStrutture.get(i);

            if (strutturaCurr.getNome().equals(nomeStruttura)){
                LatLng latLngSelected= new LatLng(strutturaCurr.getLatitudine(),strutturaCurr.getLongitudine());
                Check.tipoStruttura=strutturaCurr.getTipo_struttura();

                if (marketToRemove!=null){
                    marketToRemove.remove();
                }

                MarkerOptions markerOptionsStrutture = new MarkerOptions();
                markerOptionsStrutture.position(latLngSelected);
                markerOptionsStrutture.title(nomeStruttura);

                markerOptionsStrutture.snippet(Check.tipoStruttura);

                //Scegli il tipo di marker
                MapsClass.chooseTypeMarker(markerOptionsStrutture,Check.tipoStruttura,ActivityRicercaPerNome.this);
                mCurrLocationMarker = mMap.addMarker(markerOptionsStrutture);
                marketToRemove = mCurrLocationMarker;
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngSelected, 13));
            }
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        //check permessi
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
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

        mMap.clear();

        mLastLocation = location;
        /*if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }*/


        final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Posizione Corrente");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //Set current position
        Check.latLngCurrent=latLng;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,13));

        //azione sul click del marker
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
                        final AlertDialog dialog = new AlertDialog.Builder(ActivityRicercaPerNome.this)
                                .setTitle(strutturaCurr.getTipo_struttura()+": \n"+strutturaCurr.getNome())
                                .setMessage(strutturaCurr.getIndirizzo()+", "+strutturaCurr.getCittà()+"\nDistanza: "+distancetoCurrentPosition+" km.")
                                .setPositiveButton("Visualizza dettagli Struttura", null)
                                .setIcon(R.drawable.ic_location)
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
                                    startActivity(new Intent(ActivityRicercaPerNome.this, ActivityStrutturaLoggato.class));
                                else
                                    startActivity(new Intent(ActivityRicercaPerNome.this, ActivityStrutturaNonLoggato.class));

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