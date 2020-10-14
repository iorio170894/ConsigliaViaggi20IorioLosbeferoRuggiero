package com.example.tripnaples;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private RequestQueue mQueue;
    SupportMapFragment mapFragment;
    static boolean check_premuto;
    ArrayList<Struttura> arrayStrutture=new ArrayList<>();


    int count=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_struttre_intornoa_me);

        check_premuto=false;
        //Se il GPS non è attivo
        if (!isGPSEnabled()) {
            new AlertDialog.Builder(ActivityStruttureIntornoaMe.this)
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
        //GET JSON per recuperare le strutture dato come input l'url

        jsonParse(Check.inputUrl);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);


    }

    public void jsonParse(String url) {

        mQueue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArray = response.getJSONArray("records");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject struttura = jsonArray.getJSONObject(i);
                                int cod_struttura = struttura.getInt("cod_struttura");
                                String indirizzo = struttura.getString("indirizzo");
                                int range_prezzo = struttura.getInt("range_prezzo");
                                double latitudine = struttura.getDouble("latitudine");
                                double longitudine = struttura.getDouble("longitudine");
                                String nome = struttura.getString("nome");
                                String città = struttura.getString("città");
                                String tipo_struttura = struttura.getString("tipo_struttura");
                                String link_immagine = struttura.getString("link_immagine");
                                Struttura strutturaClass = new Struttura(cod_struttura, indirizzo, range_prezzo, latitudine, longitudine,
                                        nome, città, tipo_struttura,link_immagine);
                                if (strutturaClass != null) {
                                    arrayStrutture.add(strutturaClass);
                                }
                            }

                            //check_premuto=true;//variabile statica, se è true significa che ha trovato qualche struttura
                            //mapFragment.getMapAsync(ActivityStruttureIntornoaMe.this);
                           addOnMarker(arrayStrutture);

                        } catch (JSONException e) {
                            e.printStackTrace();

                            if (arrayStrutture.isEmpty()) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityStruttureIntornoaMe.this);
                                builder.setTitle("Errore nella ricerca:");
                                builder.setMessage("Non sono state trovate strutture!");
                                builder.setIcon(android.R.drawable.ic_dialog_alert);
                                builder.show();
                            }


                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                AlertDialog.Builder builder=new AlertDialog.Builder(ActivityStruttureIntornoaMe.this);
                builder.setTitle("Errore:");
                builder.setMessage("Attenzione:"+error.getLocalizedMessage());
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.show();
            }
        });
        mQueue.add(request);
    }

    public void addOnMarker(ArrayList<Struttura> arrayStrutture){

        for (int i = 0; i < arrayStrutture.size(); i++) {
            final Struttura strutturaInserita = arrayStrutture.get(i);
            LatLng latLngStrutture = new LatLng(strutturaInserita.getLatitudine(), strutturaInserita.getLongitudine());
            MarkerOptions markerOptionsStrutture = new MarkerOptions();
            markerOptionsStrutture.position(latLngStrutture);
            markerOptionsStrutture.title(strutturaInserita.getNome());

            //calcolo della distanza della struttura dalla posizione corrente
            double distancetoCurrentPosition = getDistanceKm(Check.latLngCurrent, latLngStrutture);

            markerOptionsStrutture.snippet("Distanza: " + distancetoCurrentPosition + " km.");
            markerOptionsStrutture.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            mCurrLocationMarker = mMap.addMarker(markerOptionsStrutture);

        }
    }

    private boolean isGPSEnabled() {
        LocationManager cm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return cm.isProviderEnabled(LocationManager.GPS_PROVIDER);
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
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }


        final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        final MarkerOptions markerOptions = new MarkerOptions();
        //Aggiungi marker di colore verde con posizione corrente
        markerOptions.position(latLng);
        markerOptions.title("Posizione Corrente");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //Set current position
        Check.latLngCurrent=latLng;

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
       // mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));

        /*AlertDialog.Builder builder = new AlertDialog.Builder(ActivityStruttureIntornoaMe.this);
        builder.setTitle("Check_premuto:");
        builder.setMessage("Count: "+ count +" , Check_premuto: "+check_premuto);
        count++;
        builder.show();*/



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

                        double distancetoCurrentPosition=getDistanceKm(latLng,latLngCurrent);

                        final AlertDialog dialog = new AlertDialog.Builder(ActivityStruttureIntornoaMe.this)
                                .setTitle(strutturaCurr.getTipo_struttura()+": \n"+strutturaCurr.getNome())
                                .setMessage(strutturaCurr.getIndirizzo()+", "+strutturaCurr.getCittà()+"\nDistanza: "+distancetoCurrentPosition+" km.")
                                //.setMessage(latitudineCurr+", "+longitudineCurr)
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

    public static double getDistanceKm(LatLng pt1, LatLng pt2){
        double distance = 0d;
        try{
            double theta = pt1.longitude - pt2.longitude;
            double dist = Math.sin(Math.toRadians(pt1.latitude)) * Math.sin(Math.toRadians(pt2.latitude))
                    + Math.cos(Math.toRadians(pt1.latitude)) * Math.cos(Math.toRadians(pt2.latitude)) * Math.cos(Math.toRadians(theta));

            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            distance = dist * 60 * 1853.1596;
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        distance=distance/1000;
        distance = Math.round(distance * 100);
        distance = distance/100;
        return distance;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

}