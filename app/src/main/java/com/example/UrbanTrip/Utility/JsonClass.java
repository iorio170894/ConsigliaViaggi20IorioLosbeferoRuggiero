package com.example.UrbanTrip.Utility;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.UrbanTrip.DAOfactory.onResultList;
import com.example.UrbanTrip.Models.RecensioneApprovata;
import com.example.UrbanTrip.Models.Struttura;
import com.example.UrbanTrip.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import androidx.appcompat.app.AlertDialog;

public class JsonClass {

    private RequestQueue mQueue;


    public void getJsonStruttureFromUrl (final onResultList onResultList, final Context context, String url) {

        mQueue = Volley.newRequestQueue(context);
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
                                    onResultList.getResult(strutturaClass);
                                }
                            }
                            onResultList.onFinish();





                        } catch (JSONException e) {
                            e.printStackTrace();
                            if (response.isNull("records")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Errore:");
                builder.setMessage("Attenzione:"+error.getLocalizedMessage());
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.show();
            }
        });
        mQueue.add(request);

    }

    public void getJsonRecensioniByCodStruttura  (final onResultList onResultList, final Context context, String url) {

        mQueue = Volley.newRequestQueue(context);
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
                                if (recensioneApprovata != null) {
                                    onResultList.getResult(recensioneApprovata);
                                }
                            }

                            onResultList.onFinish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Errore:");
                builder.setMessage("Attenzione:"+error.getLocalizedMessage());
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.show();
            }
        });
        mQueue.add(request);

    }

    public void putJsonRecensioniByCodStruttura(final String data, final Context context, String URL, final Dialog myDialog)
    {
        final String savedata= data;
        //String URL="http://consigliaviaggi20.us-east-2.elasticbeanstalk.com/recensione_da_approvare/insert_recensione_da_approvare.php";

        mQueue = Volley.newRequestQueue(context.getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objres=new JSONObject(response);
                    final AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle("Invio Recensione da approvare")
                            .setMessage("Recensione da approvare inviata al BackOffice con successo!")
                            .setPositiveButton("OK", null)
                            .setIcon(R.drawable.ic_review_primary_dark)
                            .show();
                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positiveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            myDialog.dismiss();
                        }
                    });


                } catch (JSONException e) {

                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setTitle("Json Response:");
                    builder.setMessage("Server Error");
                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                    builder.show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Errore:");
                builder.setMessage("Attenzione:"+error.getLocalizedMessage()+"Stringa: \n"+data);
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.show();

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
                    return null;
                }
            }

        };
        mQueue.add(stringRequest);
    }

}