package com.example.tripnaples;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityStrutturaLoggato extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;

    private TextView textNomeStruttura;
    private TextView textIndirizzoStruttura;
    private TextView textCittàStruttura;
    private TextView textTipoStruttura;
    ImageView imageView;
    private TextView textFirma;
    String nicnknameSalvato;
    TextView rateCount, showRating;
    EditText review;
    Button submit;
    RatingBar ratingBar;
    float rateValue;
    String temp;

    private RequestQueue requestQueue;

    Dialog mydialog;

    RecyclerView recyclerView;

    private RequestQueue mQueue;
    ArrayList<RecensioneApprovata> arrayRecensioni=new ArrayList<>();
    TextView textViewMediaRecensioni;
    double mediaRecensioni;

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
        imageView=findViewById(R.id.imageViewStruttura);

        textNomeStruttura.setText(Check.nomeStruttura);
        textIndirizzoStruttura.setText(Check.indirizzoStruttura);
        textCittàStruttura.setText(Check.cittàStruttura);
        textTipoStruttura.setText(Check.tipoStruttura);

        Glide.with(ActivityStrutturaLoggato.this)
                .load(Check.link_immagine)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);

        mydialog=new Dialog(this);

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

                            textViewMediaRecensioni=findViewById(R.id.mediaRecensioniLoggato);
                            textViewMediaRecensioni.setText(Double.toString(mediaRecensioni));


                            recyclerView = findViewById(R.id.recycler_viewLoggato);

                            MyAdapter myAdapter = new MyAdapter(ActivityStrutturaLoggato.this, utentiRecensione, descrizioneTestuale, numero_stelle);
                            recyclerView.setAdapter(myAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(ActivityStrutturaLoggato.this));


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
                /*
            * {
                    "numero_stelle": "5",
                    "descrizione_testuale": "Ottima struttura, da consigliare. Ci tornerò!",
                    "codice_struttura": "1",
                    "utente": "iorio170894"
            }
            *
            * */
                String data = "{\n"+
                        "\"numero_stelle\":" + "\"" + rateValue + "\",\n"+
                        "\"descrizione_testuale\":" + "\"" + String.valueOf(review.getText()) + "\",\n"+
                        "\"codice_struttura\":" + "\"" + Check.codiceStruttura + "\",\n"+
                        "\"utente\":" + "\"" + Check.firma + "\"\n"+
                        "}";
                JsonPost(data);

            }
        });
    }

    private void JsonPost(String data)
    {
        final String savedata= data;
        String URL="http://consigliaviaggi20.us-east-2.elasticbeanstalk.com/recensione_da_approvare/insert_recensione_da_approvare.php";

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objres=new JSONObject(response);
                    //Toast.makeText(getApplicationContext(),objres.toString(),Toast.LENGTH_LONG).show();
                    /*AlertDialog.Builder builder=new AlertDialog.Builder(ActivityStrutturaLoggato.this);
                    builder.setTitle("Json Response:");
                    builder.setMessage(objres.toString());
                    builder.show();*/
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


                } catch (JSONException e) {
                    //Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_LONG).show();
                    AlertDialog.Builder builder=new AlertDialog.Builder(ActivityStrutturaLoggato.this);
                    builder.setTitle("Json Response:");
                    builder.setMessage("Server Error");
                    builder.show();

                }
                //Log.i("VOLLEY", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder=new AlertDialog.Builder(ActivityStrutturaLoggato.this);
                builder.setTitle("Json Response Error: "+error.getMessage());
                builder.setMessage(error.getMessage());
                builder.show();

                //Log.v("VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return savedata == null ? null : savedata.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    //Log.v("Unsupported Encoding while trying to get the bytes", data);
                    return null;
                }
            }

        };
        requestQueue.add(stringRequest);
    }

/*
    private void postRequest() {
        RequestQueue requestQueue= Volley.newRequestQueue(ActivityStrutturaLoggato.this);
        String url="http://consigliaviaggi20.us-east-2.elasticbeanstalk.com/recensione_da_approvare/insert_recensione_da_approvare.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //let's parse json data
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //post_response_text.setText("Data 1 : " + jsonObject.getString("data_1_post")+"\n");
                    //post_response_text.append("Data 2 : " + jsonObject.getString("data_2_post")+"\n");
                    //post_response_text.append("Data 3 : " + jsonObject.getString("data_3_post")+"\n");
                    //post_response_text.append("Data 4 : " + jsonObject.getString("data_4_post")+"\n");
                }
                catch (Exception e){
                    e.printStackTrace();
                    //post_response_text.setText("POST DATA : unable to Parse Json");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //post_response_text.setText("Post Data : Response Failed");
            }
        }){

            /*
            * {
                    "numero_stelle": "5",
                    "descrizione_testuale": "Ottima struttura, da consigliare. Ci tornerò!",
                    "codice_struttura": "1",
                    "utente": "iorio170894"
            }
            *
            * *//*
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params=new HashMap<String, String>();
                params.put("numero_stelle",String.valueOf(rateValue));
                params.put("descrizione_testuale",String.valueOf(review.getText()));
                params.put("codice_struttura",String.valueOf(Check.codiceStruttura));
                params.put("utente",Check.firma);
                return params;
            }

            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String,String> params=new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        requestQueue.add(stringRequest);

    }*/

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
        CognitoSettings cognitoSettings = new CognitoSettings(ActivityStrutturaLoggato.this);
        CognitoUser corrente = cognitoSettings.getUserPool().getCurrentUser();
        cognitoSettings.getUserPool().getUser(corrente.getUserId()).getDetailsInBackground(handler);
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
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(6));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Check.coordinateStruttura,13));
    }

    private boolean isGPSEnabled() {
        LocationManager cm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return cm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}