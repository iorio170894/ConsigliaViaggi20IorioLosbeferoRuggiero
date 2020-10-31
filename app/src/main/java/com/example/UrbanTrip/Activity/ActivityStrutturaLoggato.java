package com.example.UrbanTrip.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.UrbanTrip.DAOfactory.DAOFactory;
import com.example.UrbanTrip.DAOfactory.RecensioneApprovataDAO;
import com.example.UrbanTrip.DAOfactory.RecensioneDaApprovareDAO;
import com.example.UrbanTrip.DAOfactory.dettagliUtenteDao;
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

public class ActivityStrutturaLoggato extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;

    private TextView textNomeStruttura;
    private TextView textIndirizzoStruttura;
    private TextView textCittàStruttura;
    private TextView textRangeStruttura;
    ImageView imageView;
    private TextView textFirma;
    TextView rateCount;
    EditText review;
    Button submit;
    RatingBar ratingBar;
    double rateValue;
    dettagliUtenteDao dettagliUtenteDao;

    Dialog mydialog;

    RecyclerView recyclerView;

    ArrayList<RecensioneApprovata> arrayRecensioni=new ArrayList<>();
    TextView textViewMediaRecensioni;
    double mediaRecensioni;

    public RecensioneApprovataDAO recDAO;
    public RecensioneDaApprovareDAO recDaApprovareDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_struttura);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_struttura);
        mapFragment.getMapAsync(this);

        //Assegna i valori alle textview

        textNomeStruttura = findViewById(R.id.nomeStruttura);
        textIndirizzoStruttura = findViewById(R.id.indirizzoStruttura);
        textCittàStruttura= findViewById(R.id.cittàStruttura);
        textRangeStruttura= findViewById(R.id.rangeStruttura);
        imageView=findViewById(R.id.imageViewStruttura);

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
        Glide.with(ActivityStrutturaLoggato.this)
                .load(Check.link_immagine)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);

        mydialog=new Dialog(this);

        //Carica le recensioni della struttura
        DAOFactory DF = DAOFactory.getDAOInstance(ActivityStrutturaLoggato.this);
        recDAO = DF.getServerRecensioniDAO();
        recDAO.getRecensioneByCodStruttura(new onResultList() {
            @Override
            public void getResult(Object object) {
                arrayRecensioni.add((RecensioneApprovata)object);
            }

            @Override
            public void onFinish() {
                //aggiungi recensioni alla recyclerView
                addReviewOnRecyclerView(arrayRecensioni);
            }
        }, Check.codiceStruttura, ActivityStrutturaLoggato.this);

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
        textViewMediaRecensioni=findViewById(R.id.mediaRecensioniLoggato);
        textViewMediaRecensioni.setText(Double.toString(mediaRecensioni));

        recyclerView = findViewById(R.id.recycler_viewLoggato);

        MyAdapter myAdapter = new MyAdapter(ActivityStrutturaLoggato.this, utentiRecensione, descrizioneTestuale, numero_stelle);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ActivityStrutturaLoggato.this));

    }

    public double getMediaRecensioni(double[] array) {

        for (int i=0;i<array.length;i++){
            if (array[i] <0 || array[i]>5){
                throw new IllegalArgumentException();
            }
        }

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


    //Popup per l'inserimento della recensione che si attiva quando si preme Aggiungi Recensione
    public void ShowPopup (View v){
        mydialog.setContentView(R.layout.custompopuprecensione);
        textFirma= mydialog.findViewById(R.id.firmaRecensione);
        mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mydialog.show();
        
        if (Check.firma==null) {

            //Se la firma non è stata ancora scelta nelle impostazioni allora di default metti il nickname
            //get Nickname
            DAOFactory DF = DAOFactory.getDAOInstance(ActivityStrutturaLoggato.this);
            dettagliUtenteDao = DF.getAuthenticationForGetDettagliUtente();
            dettagliUtenteDao.getNicknameUtente(ActivityStrutturaLoggato.this,textFirma);

        }
        else
            textFirma.setText(Check.firma);

        rateCount=mydialog.findViewById(R.id.rateCount);
        ratingBar=mydialog.findViewById(R.id.ratingBar);
        review=mydialog.findViewById(R.id.review);
        submit=mydialog.findViewById(R.id.submitBtn);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                rateValue=ratingBar.getRating();

                if (rateValue <=1 && rateValue >0)
                    rateCount.setText("Bad" +rateValue + "/5");
                else if (rateValue <=2 && rateValue >1)
                    rateCount.setText("OK" +rateValue + "/5");
                else if (rateValue <=3 && rateValue >2)
                    rateCount.setText("Good" +rateValue + "/5");
                else if (rateValue <=4 && rateValue >3)
                    rateCount.setText("Very Good" +rateValue + "/5");
                if (rateValue <=5 && rateValue >4)
                    rateCount.setText("Best" +rateValue + "/5");
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((String.valueOf(review.getText()).isEmpty()) || rateValue<0.5 || rateValue >5.0){
                    if (String.valueOf(review.getText()).isEmpty())
                        review.setError("Attenzione campo vuoto");
                    if (rateValue<0.5 || rateValue >5.0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityStrutturaLoggato.this);
                        builder.setTitle("Errore");
                        builder.setMessage("Attenzione seleziona un range di stelle!");
                        builder.setIcon(android.R.drawable.ic_dialog_alert);
                        builder.show();
                    }
                }
                else {
                    if (String.valueOf(review.getText()).length() < 8){
                        review.setError("Inserisci almeno 8 caratteri");
                    }
                    else {
                        String descrizioneTestuale=ActivityStrutturaLoggato.escape(String.valueOf(review.getText()));
                        String data = "{\n" +
                                "\"numero_stelle\":" + "\"" + rateValue + "\",\n" +
                                "\"descrizione_testuale\":" + descrizioneTestuale + ",\n" +
                                //"\"descrizione_testuale\":" + "\"" + String.valueOf(review.getText()) + "\",\n" +
                                "\"codice_struttura\":" + "\"" + Check.codiceStruttura + "\",\n" +
                                "\"utente\":" + "\"" + String.valueOf(textFirma.getText())+ "\"\n" +
                                "}";

                        //Inviare recensione da approvare al database
                        DAOFactory DF = DAOFactory.getDAOInstance(ActivityStrutturaLoggato.this);
                        recDaApprovareDAO = DF. getServerPutRecensioniDAO();
                        recDaApprovareDAO.putRecensioneByCodStruttura(data,ActivityStrutturaLoggato.this,mydialog);
                    }
                }

            }
        });
    }

    public static String escape(String string) {
        if (string == null || string.length() == 0) {
            return "\"\"";
        }

        char         c = 0;
        int          i;
        int          len = string.length();
        StringBuilder sb = new StringBuilder(len + 4);
        String       t;

        sb.append('"');
        for (i = 0; i < len; i += 1) {
            c = string.charAt(i);
            switch (c) {
                case '\\':
                case '"':
                    sb.append('\\');
                    sb.append(c);
                    break;
                case '/':
                    //                if (b == '<') {
                    sb.append('\\');
                    //                }
                    sb.append(c);
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\'':
                    sb.append("\\\\'");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                default:
                    if (c < ' ') {
                        t = "000" + Integer.toHexString(c);
                        sb.append("\\u" + t.substring(t.length() - 4));
                    } else {
                        sb.append(c);
                    }
            }
        }
        sb.append('"');
        return sb.toString();
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
        MapsClass.chooseTypeMarker(markerOptions,Check.tipoStruttura,ActivityStrutturaLoggato.this);

        mMap.addMarker(markerOptions);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Check.coordinateStruttura,13));
    }

}