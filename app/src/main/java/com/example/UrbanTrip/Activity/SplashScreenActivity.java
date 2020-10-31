package com.example.UrbanTrip.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.example.UrbanTrip.R;
import com.example.UrbanTrip.Utility.CognitoSettings;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT=3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //check connessione a internet
        if (!isConnectionInternetEnabled()) {
            new AlertDialog.Builder(SplashScreenActivity.this)
                    .setMessage("Attenzione, attiva la tua connessione dati")
                    .setCancelable(false)
                    .setPositiveButton("Opzioni", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent i = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            startActivity(i);
                        }
                    })
                    .setNegativeButton("Cancella", null)
                    .show();
        }

        //controlla se l'utente è loggato per reindirizzarlo all'activity corretta
        isLogged();

    }

    public boolean isConnectionInternetEnabled(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void isLogged() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                GetDetailsHandler handler = new GetDetailsHandler() {
                    @Override
                    public void onSuccess(final CognitoUserDetails list) {
                        //utente loggato vai alla dashboard activity benvenuto
                        Intent dashboardIntent = new Intent(SplashScreenActivity.this, ActivityBenvenuto.class);
                        startActivity(dashboardIntent);
                        finish();
                    }
                    @Override
                    public void onFailure(final Exception exception) {
                        //Vai al main per loggarti, registrarti o entrare come ospite*/
                Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
                    }
                };
                CognitoSettings cognitoSettings = new CognitoSettings(SplashScreenActivity.this);
                CognitoUser corrente = cognitoSettings.getUserPool().getCurrentUser();
                cognitoSettings.getUserPool().getUser(corrente.getUserId()).getDetailsInBackground(handler);
            }
        }, SPLASH_TIME_OUT);
    }


}