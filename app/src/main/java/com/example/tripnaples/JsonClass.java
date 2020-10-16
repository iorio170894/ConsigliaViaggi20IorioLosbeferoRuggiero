package com.example.tripnaples;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.view.Display;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

public class JsonClass {

    private RequestQueue mQueue;

    public void getJsonStruttureFromUrl (final onResultList onResultList, final Context context, String url) {

        //array=null;
        mQueue = Volley.newRequestQueue(context);
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
}