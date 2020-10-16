package com.example.tripnaples;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationServices;

import android.location.Location;
import android.Manifest;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.util.Log;
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
    public StrutturaDAO StruttDAO;


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

            //calcolo della distanza della struttura dalla posizione corrente
            double distancetoCurrentPosition = getDistanceKm(Check.latLngCurrent, latLngStrutture);

            markerOptionsStrutture.snippet("Distanza: " + distancetoCurrentPosition + " km.");
            //markerOptionsStrutture.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            //markerOptionsStrutture.icon(bitmapDescriptorFromVector(this, R.drawable.ic_restaurant_marker));
            chooseTypeMarker(markerOptionsStrutture);
            mCurrLocationMarker = mMap.addMarker(markerOptionsStrutture);

        }
    }

    private void chooseTypeMarker(MarkerOptions markerOptionsStrutture) {
        if ((Check.inputTipoStrutturaForSearch).equals("ristorante")){
            markerOptionsStrutture.icon(bitmapDescriptorFromVector(this, R.drawable.ic_restaurant_marker));
        }
        else if ((Check.inputTipoStrutturaForSearch).equals("bar")){
            markerOptionsStrutture.icon(bitmapDescriptorFromVector(this, R.drawable.ic_bar_prova_marker));
        }
        else if ((Check.inputTipoStrutturaForSearch).equals("hotel")){
            markerOptionsStrutture.icon(bitmapDescriptorFromVector(this, R.drawable.ic_hotel_marker));
        }
        else if ((Check.inputTipoStrutturaForSearch).equals("parco")){
            markerOptionsStrutture.icon(bitmapDescriptorFromVector(this, R.drawable.ic_park_marker));
        }
        else if ((Check.inputTipoStrutturaForSearch).equals("teatro")){
            markerOptionsStrutture.icon(bitmapDescriptorFromVector(this, R.drawable.ic_theatre_marker));
        }
        else if ((Check.inputTipoStrutturaForSearch).equals("museo")){
            markerOptionsStrutture.icon(bitmapDescriptorFromVector(this, R.drawable.ic_museo_marker));
        }
    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
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