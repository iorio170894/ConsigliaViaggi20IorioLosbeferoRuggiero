package com.example.tripnaples;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
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
    Marker setLocation;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    SupportMapFragment mapFragment;

    //String[] strings = new String[50];
    private RequestQueue mQueue;
    ArrayList<Struttura> arrayStrutture=new ArrayList<>();
    static boolean check_premuto=false;

    //ArrayList<String> strings = new ArrayList<>();

    //String[] prova = {"uno","due","tre"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricerca_per_nome);

        if (!isGPSEnabled()) {
            new AlertDialog.Builder(ActivityRicercaPerNome.this)
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


        //JsonClass jsonStrutture= new JsonClass();
        //Check.arrayStrutture = jsonStrutture.jsonParse("http://consigliaviaggi20.us-east-2.elasticbeanstalk.com/struttura/read.php");


        /*if (Check.arrayStrutture.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityRicercaPerNome.this);
            builder.setTitle("Errore nella ricerca:");
            builder.setMessage("Non sono state trovate strutture!");
            builder.show();
        }*/



       /* for (int i=0; i<Check.arrayStrutture.size(); i++) {
            Struttura strutturaInserita = Check.arrayStrutture.get(i);
            strings[i]=strutturaInserita.getNome();
        }*/

        //genereteString();



        /*AlertDialog.Builder builder = new AlertDialog.Builder(ActivityRicercaPerNome.this);
        builder.setTitle("Stringa nome struttura:");
        builder.setMessage(Arrays.toString(strings));
        builder.show();*/

        //prova=Check.strings;

        autoCompleteTextView=findViewById(R.id.ac_text_view);

        jsonNomeStruttura("http://consigliaviaggi20.us-east-2.elasticbeanstalk.com/struttura/read.php");


        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_per_nome);
        mapFragment.getMapAsync(this);


    }

    public void jsonNomeStruttura (String url) {
        //array=null;
        mQueue = Volley.newRequestQueue(this);
        //final ArrayList<Struttura> arrayStrutture = new ArrayList<>();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("records");

                            String[] strings = new String[jsonArray.length()];

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
                                arrayStrutture.add(strutturaClass);
                                strings[i] = nome;
                            }

                            /*AlertDialog.Builder builder = new AlertDialog.Builder(ActivityRicercaPerNome.this);
                            builder.setTitle("Stringa nome struttura:");
                            builder.setMessage(Arrays.toString(strings));
                            builder.show();*/

                            adapter=new ArrayAdapter<>(ActivityRicercaPerNome.this
                                    ,android.R.layout.simple_list_item_1,strings);
                            autoCompleteTextView.setThreshold(1);
                            autoCompleteTextView.setAdapter(adapter);
                            autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int s, long l) {

                                    for (int i=0; i<arrayStrutture.size(); i++){

                                        final Struttura strutturaCurr=arrayStrutture.get(i);

                                        if (strutturaCurr.getNome()==adapter.getItem(s)){
                                            LatLng latLngSelected= new LatLng(strutturaCurr.getLatitudine(),strutturaCurr.getLongitudine());
                                            Check.coordinateStrutturaPerNome=latLngSelected;
                                            Check.nomeStrutturaPerNome=adapter.getItem(s);

                                            check_premuto=true;
                                            mapFragment.getMapAsync(ActivityRicercaPerNome.this);
                                        }
                                    }
                                }
                            });


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


    }

    @Override
    public void onBackPressed() {
        mMap.clear();
        check_premuto=false;
        Intent turnBenvenuto = new Intent(ActivityRicercaPerNome.this, ActivityBenvenuto.class);
        turnBenvenuto.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ActivityRicercaPerNome.this.startActivity(turnBenvenuto);
    }



    public void genereteString (){

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
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Posizione Corrente");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        if (check_premuto){

            MarkerOptions markerOptionsStrutture = new MarkerOptions();
            markerOptionsStrutture.position(Check.coordinateStrutturaPerNome);
            markerOptionsStrutture.title(Check.nomeStrutturaPerNome);
            //markerOptionsStrutture.snippet(strutturaInserita.getIndirizzo());
            //markerOptionsStrutture.snippet(strutturaInserita.getCittà());

            //calcolo della distanza dalla posizione corrente
            double distancetoCurrentPosition=getDistanceKm(latLng,Check.coordinateStrutturaPerNome);
            markerOptionsStrutture.snippet(String.valueOf("Distanza: "+distancetoCurrentPosition+" km."));
            //markerOptionsStrutture.snippet(String.valueOf(strutturaInserita.getLatitudine())+String.valueOf(strutturaInserita.getLongitudine()));
            markerOptionsStrutture.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            mCurrLocationMarker = mMap.addMarker(markerOptionsStrutture);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Check.coordinateStrutturaPerNome,13));

        }

        //muovi la mappa
        if (!check_premuto) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                final LatLng latLngCurrent=marker.getPosition();

                double latitudineCurr=latLngCurrent.latitude;
                double longitudineCurr=latLngCurrent.longitude;

                for (int i=0; i<arrayStrutture.size(); i++){
                    final Struttura strutturaCurr=arrayStrutture.get(i);
                    if (strutturaCurr.getLatitudine()==latitudineCurr &&
                            strutturaCurr.getLongitudine()==longitudineCurr){

                        double distancetoCurrentPosition=getDistanceKm(latLng,latLngCurrent);

                        final AlertDialog dialog = new AlertDialog.Builder(ActivityRicercaPerNome.this)
                                .setTitle(strutturaCurr.getTipo_struttura()+": \n"+strutturaCurr.getNome())
                                .setMessage(strutturaCurr.getIndirizzo()+", "+strutturaCurr.getCittà()+"\nDistanza: "+distancetoCurrentPosition+" km.")
                                //.setMessage(latitudineCurr+", "+longitudineCurr)
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