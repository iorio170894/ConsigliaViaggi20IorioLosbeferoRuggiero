package com.example.tripnaples;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;



public class ActivityRicerca extends AppCompatActivity implements OnMapReadyCallback,
        LocationListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, AdapterView.OnItemSelectedListener{

    private GoogleMap mMap;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    //JSON
    private RequestQueue mQueue;
    TextView mTextViewResult;
    ArrayList<Struttura> arrayStrutture=new ArrayList<>();

    //Spinner
    //static String strutturaSelected="default";
    //static String rangeSelected="default";
    //static int positionSpinner;

    static boolean check_premuto=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricerca);

        //Se il GPS non è attivo
        if (!isGPSEnabled()) {
            new AlertDialog.Builder(ActivityRicerca.this)
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
        //final String rangeSelected = adapter2.getItem(0).toString();

        //Bottone cerca con filtri
        //mQueue = Volley.newRequestQueue(this);
       // mTextViewResult = findViewById(R.id.text_view_result);

        Button cercaStruttureConFiltri;
        cercaStruttureConFiltri = findViewById(R.id.buttonCercaRicerca);
        cercaStruttureConFiltri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strutturaSelected=spinnerStruttura.getSelectedItem().toString();
                String rangePrezzoSelected=spinnerRangePrezzo.getSelectedItem().toString();
                EditText inputcittàSelected= findViewById(R.id.editTextCittàStruttura);
                String cittàSelected = String.valueOf(inputcittàSelected.getText());
                int range=0;

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

                //String url = "http://consigliaviaggi20.us-east-2.elasticbeanstalk.com/struttura/search_filter_strutture.php?inputTipo="+strutturaSelected+"&inputCitt%C3%A0="+cittàSelected+"&inputRangePrezzo="+range;
                  String url = "http://consigliaviaggi20.us-east-2.elasticbeanstalk.com/struttura/search_filter_strutture.php?inputTipo="+strutturaSelected+"&inputCitt%C3%A0="+cittàSelected+"&inputRangePrezzo="+range;

                JsonClass jsonRicerca= new JsonClass();
                arrayStrutture=jsonRicerca.jsonParse(url);

                if (arrayStrutture.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityRicerca.this);
                    builder.setTitle("Errore nella ricerca:");
                    builder.setMessage("Non sono state trovate strutture!");
                    builder.show();
                }

                //check_premuto=true;

                //onLocationChanged(mLastLocation);

                //Ottieni SupportMapFragment e ricevi una notifica quando la mappa è pronta per essere utilizzata.
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(ActivityRicerca.this);

            }

        });


    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //positionSpinner=i;
        String text = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();

        /*if(text == "hotel" || text == "parco" || text == "ristorante" || text == "bar"){
            strutturaSelected=text;
        }
        else if (text == "basso" || text == "medio" || text == "alto"){
            rangeSelected=text;
        }*/

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
        //Posiziona l'indicatore della posizione corrente
       // LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        //MarkerOptions markerOptions = new MarkerOptions();
        //markerOptions.position(latLng);
        //markerOptions.title("Posizione Corrente");
        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        //mCurrLocationMarker = mMap.addMarker(markerOptions);

        //if (check_premuto) {
            for (int i=0; i<arrayStrutture.size(); i++){
                final Struttura strutturaInserita=arrayStrutture.get(i);
                LatLng latLngStrutture = new LatLng(strutturaInserita.getLatitudine(),strutturaInserita.getLongitudine());
                MarkerOptions markerOptionsStrutture = new MarkerOptions();
                markerOptionsStrutture.position(latLngStrutture);
                markerOptionsStrutture.title(strutturaInserita.getNome());
                //markerOptionsStrutture.snippet(strutturaInserita.getIndirizzo());
                //markerOptionsStrutture.snippet(strutturaInserita.getCittà());

                //calcolo della distanza dalla posizione corrente
                //markerOptionsStrutture.snippet(String.valueOf(strutturaInserita.getLatitudine())+String.valueOf(strutturaInserita.getLongitudine()));
                markerOptionsStrutture.snippet(strutturaInserita.getTipo_struttura());
                markerOptionsStrutture.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                mCurrLocationMarker = mMap.addMarker(markerOptionsStrutture);
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngStrutture));
                //mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

            }
      //  }

        //muovi la mappa
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(40.8283157,14.2454157)));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

        //interrompere gli aggiornamenti della posizione
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

}