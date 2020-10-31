package com.example.UrbanTrip.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.UrbanTrip.DAOfactory.DAOFactory;
import com.example.UrbanTrip.DAOfactory.RecensioneApprovataDAO;
import com.example.UrbanTrip.DAOfactory.onResultList;
import com.example.UrbanTrip.Models.RecensioneApprovata;
import com.example.UrbanTrip.R;
import com.example.UrbanTrip.Utility.Check;
import com.example.UrbanTrip.Utility.MapsClass;
import com.example.UrbanTrip.Utility.MyAdapter;
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
    private TextView textRangeStruttura;
    ImageView imageView;
    RecyclerView recyclerView;

    ArrayList<RecensioneApprovata> arrayRecensioni=new ArrayList<>();
    TextView textViewMediaRecensioni;
    double mediaRecensioni;
    public RecensioneApprovataDAO recDAO;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_struttura_non_loggato);

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

        //Carica immagine Struttura
        Glide.with(ActivityStrutturaNonLoggato.this)
                .load(Check.link_immagine)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);

        //Carica le recensioni della struttura
        DAOFactory DF = DAOFactory.getDAOInstance(ActivityStrutturaNonLoggato.this);
        recDAO = DF.getServerRecensioniDAO();
        recDAO.getRecensioneByCodStruttura(new onResultList() {
            @Override
            public void getResult(Object object) {
                arrayRecensioni.add((RecensioneApprovata)object);
            }

            @Override
            public void onFinish() {
                //Aggiungi le recensione sulla RecyclerView
                addReviewOnRecyclerView(arrayRecensioni);
            }
        }, Check.codiceStruttura, ActivityStrutturaNonLoggato.this);

    }

    private void addReviewOnRecyclerView(ArrayList<RecensioneApprovata> arrayRecensioni) {

        String[] utentiRecensione= new String[arrayRecensioni.size()];
        String[] descrizioneTestuale= new String [arrayRecensioni.size()];
        double[] numero_stelle= new double[arrayRecensioni.size()];

        for (int i=0; i<arrayRecensioni.size(); i++){
            RecensioneApprovata recensioneApprovataSelected=arrayRecensioni.get(i);
            utentiRecensione[i]=recensioneApprovataSelected.getUtente();
            descrizioneTestuale[i]=recensioneApprovataSelected.getDescrizioneTestuale();
            numero_stelle[i]=recensioneApprovataSelected.getNumeroStelle();
        }



        //Calcola la media delle recensioni e aggiungila alla textView
        mediaRecensioni=getMediaRecensioni(numero_stelle);
        textViewMediaRecensioni=findViewById(R.id.mediaRecensioniNonLoggato);
        textViewMediaRecensioni.setText(Double.toString(mediaRecensioni));

        recyclerView = findViewById(R.id.recycler_viewNonLoggato);

        MyAdapter myAdapter = new MyAdapter(ActivityStrutturaNonLoggato.this, utentiRecensione, descrizioneTestuale, numero_stelle);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ActivityStrutturaNonLoggato.this));

    }

    public double getMediaRecensioni(double[] array) {

        double media = 0;

        if (array.length>0) {
            //Media recensioni
            for (int i = 0; i < array.length; i++) {
                media += array[i];
            }
            media = media / array.length;
            //per avere solo 2 cifre dopo la virgola
            media = Math.round(media * 100);
            media = media / 100;
        }
        else
            throw new IllegalArgumentException();

        return media;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        final MarkerOptions markerOptions = new MarkerOptions();
        //Aggiungi marker di colore verde con posizione corrente
        markerOptions.position(Check.coordinateStruttura);
        markerOptions.title(Check.nomeStruttura);
        markerOptions.snippet(Check.tipoStruttura);
        //Scegli il tipo di marker
        MapsClass.chooseTypeMarker(markerOptions,Check.tipoStruttura,ActivityStrutturaNonLoggato.this);

        mMap.addMarker(markerOptions);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Check.coordinateStruttura,13));
    }

}