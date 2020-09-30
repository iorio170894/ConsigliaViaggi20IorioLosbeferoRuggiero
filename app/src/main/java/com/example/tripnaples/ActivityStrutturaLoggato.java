package com.example.tripnaples;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
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
    private TextView textTipoStruttura;
    private TextView textFirma;
    String nicnknameSalvato;
    TextView rateCount, showRating;
    EditText review;
    Button submit;
    RatingBar ratingBar;
    float rateValue;
    String temp;

    Dialog mydialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_struttura);

        getNicknameUtente();

        if (!isGPSEnabled()) {
            new AlertDialog.Builder(ActivityStrutturaLoggato.this)
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
                .findFragmentById(R.id.map_struttura);
        mapFragment.getMapAsync(this);

        //Assegna i valori alle textview

        textNomeStruttura = findViewById(R.id.nomeStruttura);
        textIndirizzoStruttura = findViewById(R.id.indirizzoStruttura);
        textCittàStruttura= findViewById(R.id.cittàStruttura);
        textTipoStruttura= findViewById(R.id.tipoStruttura);

        textNomeStruttura.setText(Check.nomeStruttura);
        textIndirizzoStruttura.setText(Check.indirizzoStruttura);
        textCittàStruttura.setText(Check.cittàStruttura);
        textTipoStruttura.setText(Check.tipoStruttura);


        ListView listView;
        listView=(ListView)findViewById(R.id.listView);

        ArrayList<String> arrayList= new ArrayList<>();
        arrayList.add("Pippo\n" +
                      "numero Stelle: 4\n" +
                      "Bellissimo posto si mangia bene,personale cordiale");
        arrayList.add("Pippo\n" +
                "numero Stelle: 4\n" +
                "Bellissimo posto si mangia bene,personale cordiale");
        arrayList.add("Pippo\n" +
                "numero Stelle: 4\n" +
                "Bellissimo posto si mangia bene,personale cordiale");
        arrayList.add("Pippo\n" +
                "numero Stelle: 4\n" +
                "Bellissimo posto si mangia bene,personale cordiale");
        arrayList.add("Pippo\n" +
                "numero Stelle: 4\n" +
                "Bellissimo posto si mangia bene,personale cordiale");
        arrayList.add("Pippo\n" +
                "numero Stelle: 4\n" +
                "Bellissimo posto si mangia bene,personale cordiale");
        arrayList.add("Pippo\n" +
                "numero Stelle: 4\n" +
                "Bellissimo posto si mangia bene,personale cordiale");
        arrayList.add("Pippo\n" +
                "numero Stelle: 4\n" +
                "Bellissimo posto si mangia bene,personale cordiale");
        arrayList.add("Pippo\n" +
                "numero Stelle: 4\n" +
                "Bellissimo posto si mangia bene,personale cordiale");
        arrayList.add("Pippo\n" +
                "numero Stelle: 4\n" +
                "Bellissimo posto si mangia bene,personale cordiale");
        arrayList.add("Pippo\n" +
                "numero Stelle: 4\n" +
                "Bellissimo posto si mangia bene,personale cordiale");
        arrayList.add("Pippo\n" +
                "numero Stelle: 4\n" +
                "Bellissimo posto si mangia bene,personale cordiale");
        arrayList.add("Pippo\n" +
                "numero Stelle: 4\n" +
                "Bellissimo posto si mangia bene,personale cordiale");
        arrayList.add("Pippo\n" +
                "numero Stelle: 4\n" +
                "Bellissimo posto si mangia bene,personale cordiale");
        arrayList.add("Pippo\n" +
                "numero Stelle: 4\n" +
                "Bellissimo posto si mangia bene,personale cordiale");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);

        listView.setAdapter(arrayAdapter);

        mydialog=new Dialog(this);
    }

    public void ShowPopup (View v){
        TextView textFirma;
        mydialog.setContentView(R.layout.custompopuprecensione);
        textFirma= mydialog.findViewById(R.id.firmaRecensione);
        mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mydialog.show();
        /*AlertDialog.Builder builder=new AlertDialog.Builder(ActivityStrutturaLoggato.this);
        builder.setTitle("Firma:");
        builder.setMessage("Utente: "+nicnknameSalvato);
        builder.show();*/
        if (!Check.controlloActivityImpostazioni) {
            Check.firma = nicnknameSalvato;
        }
        textFirma.setText(Check.firma);
        rateCount=mydialog.findViewById(R.id.rateCount);
        ratingBar=mydialog.findViewById(R.id.ratingBar);
        review=mydialog.findViewById(R.id.review);
        submit=mydialog.findViewById(R.id.submitBtn);
        //showRating=mydialog.findViewById(R.id.showRating);

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
                final AlertDialog dialog = new AlertDialog.Builder(ActivityStrutturaLoggato.this)
                        .setTitle("Invio Recensione da approvare")
                        .setMessage("Recensione da approvare inviata al BackOffice con successo!")
                        .setPositiveButton("OK", null)
                        .show();
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        mydialog.dismiss();
                    }
                });
            }
        });
    }

    public void getNicknameUtente(){
        GetDetailsHandler handler = new GetDetailsHandler() {
            @Override
            public void onSuccess(final CognitoUserDetails list) {
                //trovi dettagli utente con successo
                nicnknameSalvato = String.valueOf(list.getAttributes().getAttributes().get("nickname"));

                /*AlertDialog.Builder builder=new AlertDialog.Builder(ActivityImpostazioni.this);
                builder.setTitle("Campi Utente:");
                builder.setMessage("Email:"+emailSalvata+"\nNome e Cognome:"+nomeCognomeSalvato+"\nNickname:"+nicnknameSalvato);
                builder.show();*/
            }
            @Override
            public void onFailure(final Exception exception) {
                // Fallimento nel recupero dei dettagli dell'utente
                Log.e("Eccezione dettagli utente:",exception.toString());
                new android.app.AlertDialog.Builder(ActivityStrutturaLoggato.this)
                        .setTitle("Errore nel recuper dei dettagli utente")
                        .setMessage("We're sorry but we are experiencing problems with your account. Try to exit and log in again. Error details: " + exception.getLocalizedMessage())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        };
        CognitoUser corrente = CognitoSettings.getUserPool().getCurrentUser();
        CognitoSettings.getUserPool().getUser(corrente.getUserId()).getDetailsInBackground(handler);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions()
                .position(Check.coordinateStruttura)
                .title(Check.nomeStruttura))
                .setSnippet(Check.tipoStruttura);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Check.coordinateStruttura));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(6));
    }

    private boolean isGPSEnabled() {
        LocationManager cm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return cm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}