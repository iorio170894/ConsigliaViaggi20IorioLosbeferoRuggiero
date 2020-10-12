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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class ActivityStrutturaNonLoggato extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;

    private TextView textNomeStruttura;
    private TextView textIndirizzoStruttura;
    private TextView textCittàStruttura;
    private TextView textRangeStruttura;
    ImageView imageView;
    RecyclerView recyclerView;

    private RequestQueue mQueue;
    ArrayList<RecensioneApprovata> arrayRecensioni=new ArrayList<>();
    TextView textViewMediaRecensioni;
    double mediaRecensioni;


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
        textRangeStruttura= findViewById(R.id.rangeStrutturaNonLoggato);
        imageView=findViewById(R.id.imageViewStruttura2);

        textNomeStruttura.setText(Check.nomeStruttura);
        textIndirizzoStruttura.setText(Check.indirizzoStruttura);
        textCittàStruttura.setText(Check.cittàStruttura);

        if (Check.rangePrezzo==1){
            textRangeStruttura.setText("Range di prezzo basso");
        }
        else if (Check.rangePrezzo==2){
            textRangeStruttura.setText("Range di prezzo medio");
        }
        else if (Check.rangePrezzo==3){
            textRangeStruttura.setText("Range di prezzo alto");
        }

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
        jsonRecensioniApprovate("http://consigliaviaggi20.us-east-2.elasticbeanstalk.com/recensione_approvata/read_for_cod_struttura.php?inputCodStruttura="+Check.codiceStruttura);

    }

    public void jsonRecensioniApprovate (String url) {
        mQueue = Volley.newRequestQueue(this);
        //final ArrayList<Struttura> arrayStrutture = new ArrayList<>();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("records");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject recensione_approvata = jsonArray.getJSONObject(i);

                                int codRecensione = recensione_approvata.getInt("cod_recensione");
                                double numeroStelle = recensione_approvata.getDouble("numero_stelle");
                                String descrizioneTestuale = recensione_approvata.getString("descrizione_testuale");
                                String codiceStruttura = recensione_approvata.getString("codice_struttura");
                                String utente = recensione_approvata.getString("utente");
                                RecensioneApprovata recensioneApprovata = new RecensioneApprovata(codRecensione, numeroStelle, descrizioneTestuale, codiceStruttura, utente);
                                arrayRecensioni.add(recensioneApprovata);
                            }

                            /*AlertDialog.Builder builder = new AlertDialog.Builder(ActivityRicercaPerNome.this);
                            builder.setTitle("Stringa nome struttura:");
                            builder.setMessage(Arrays.toString(strings));
                            builder.show();*/

                            String[] utentiRecensione= new String[jsonArray.length()];
                            String[] descrizioneTestuale= new String [jsonArray.length()];
                            double[] numero_stelle= new double[jsonArray.length()];

                            for (int i=0; i<arrayRecensioni.size(); i++){
                                RecensioneApprovata recensioneApprovataSelected=arrayRecensioni.get(i);
                                utentiRecensione[i]=recensioneApprovataSelected.getUtente();
                                descrizioneTestuale[i]=recensioneApprovataSelected.getDescrizioneTestuale();
                                numero_stelle[i]=recensioneApprovataSelected.getNumeroStelle();
                            }

                            //Media recensioni
                            for (int i=0; i<numero_stelle.length;i++){
                                mediaRecensioni+=numero_stelle[i];
                            }
                            mediaRecensioni=mediaRecensioni/numero_stelle.length;
                            //per avere solo 2 cifre dopo la virgola
                            mediaRecensioni = Math.round(mediaRecensioni * 100);
                            mediaRecensioni = mediaRecensioni/100;

                            textViewMediaRecensioni=findViewById(R.id.mediaRecensioniNonLoggato);
                            textViewMediaRecensioni.setText(Double.toString(mediaRecensioni));


                            recyclerView = findViewById(R.id.recycler_viewNonLoggato);

                            MyAdapter myAdapter = new MyAdapter(ActivityStrutturaNonLoggato.this, utentiRecensione, descrizioneTestuale, numero_stelle);
                            recyclerView.setAdapter(myAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(ActivityStrutturaNonLoggato.this));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                AlertDialog.Builder builder=new AlertDialog.Builder(ActivityStrutturaNonLoggato.this);
                builder.setTitle("Errore:");
                builder.setMessage("Attenzione:"+error.getLocalizedMessage());
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.show();
            }
        });
        mQueue.add(request);


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