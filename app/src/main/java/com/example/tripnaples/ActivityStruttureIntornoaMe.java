package com.example.tripnaples;

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
    private GoogleMap mMapStrutture;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    Marker setLocation;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    //JSON
    //private RequestQueue mQueue;
    ArrayList<Struttura> arrayStrutture=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_struttre_intornoa_me);

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
        //mQueue = Volley.newRequestQueue(this);
        //jsonParse();
        String input = Check.intornoaMe;
        String url = "http://consigliaviaggi20.us-east-2.elasticbeanstalk.com/struttura/search_strutture.php?inputTipo="+input;
        JsonClass jsonStrutturaVicinoaMe= new JsonClass();
        arrayStrutture=jsonStrutturaVicinoaMe.jsonParse(url);

            //Ottieni SupportMapFragment e ricevi una notifica quando la mappa è pronta per essere utilizzata.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.google_map);
            mapFragment.getMapAsync(this);



    }
/*
    public void jsonParse() {
        //final ArrayList<Struttura> array = new ArrayList<>();
        String input = "ristorante";
        String url = "http://consigliaviaggi20.us-east-2.elasticbeanstalk.com/struttura/search_strutture.php?inputTipo="+input;
        //final ArrayList<Struttura> arrayStrutture = new ArrayList<>();
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
                                /*mTextViewResult.append("cod_struttura:"+String.valueOf(cod_struttura)+"\nindirizzo:"+indirizzo+
                                        "\nrange_prezzo:"+String.valueOf(range_prezzo)+"\nlatitudine:"+latitudine+
                                        "\nlongitudine:"+longitudine+ "\ncittà:"+città+"\nnome:"+nome+
                                        "\ntipo_struttura:"+tipo_struttura+"\n\n");*/
                               /* Struttura strutturaClass = new Struttura(cod_struttura,indirizzo,range_prezzo,latitudine,longitudine,
                                        nome,città,tipo_struttura);
                                arrayStrutture.add(strutturaClass);

                                /*mTextViewResult.append("cod_struttura:"+String.valueOf(strutturaClass.getCod_struttura())+"\nindirizzo:"+strutturaClass.getIndirizzo()+
                                        "\nrange_prezzo:"+String.valueOf(strutturaClass.getRange_prezzo())+"\nlatitudine:"+strutturaClass.getLatitudine()+
                                        "\nlongitudine:"+strutturaClass.getLongitudine()+ "\ncittà:"+strutturaClass.getCittà()+"\nnome:"+strutturaClass.getNome()+
                                        "\ntipo_struttura:"+strutturaClass.getTipo_struttura()+"\n\n");*/
                           /* }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
        //return array;
    }*/

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

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Posizione Corrente");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

       /* LatLng latLngStrutture = new LatLng(40.858523, 14.136731);
        MarkerOptions markerOptionsStrutture = new MarkerOptions();
        markerOptionsStrutture.position(latLngStrutture);
        markerOptionsStrutture.title("Tenuta Afrodite");
        markerOptionsStrutture.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mCurrLocationMarker = mMap.addMarker(markerOptionsStrutture);*/

        for (int i=0; i<arrayStrutture.size(); i++){
            Struttura strutturaInserita=arrayStrutture.get(i);
            LatLng latLngStrutture = new LatLng(strutturaInserita.getLatitudine(),strutturaInserita.getLongitudine());
            MarkerOptions markerOptionsStrutture = new MarkerOptions();
            markerOptionsStrutture.position(latLngStrutture);
            markerOptionsStrutture.title(strutturaInserita.getNome());
            //markerOptionsStrutture.snippet(strutturaInserita.getIndirizzo());
            //markerOptionsStrutture.snippet(strutturaInserita.getCittà());
            markerOptionsStrutture.snippet(strutturaInserita.getTipo_struttura());
            markerOptionsStrutture.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mCurrLocationMarker = mMap.addMarker(markerOptionsStrutture);
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngStrutture));
            //mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }


        //muovi la mappa
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //interrompere gli aggiornamenti della posizione
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

}