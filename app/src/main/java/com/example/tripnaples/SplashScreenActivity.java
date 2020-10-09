package com.example.tripnaples;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT=3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        isLogged();

    }

    /*public boolean isLoggedIn() {
        CognitoUser currentUser = CognitoSettings.getUserPool().getCurrentUser();
        SharedPreferences prefs = getSharedPreferences("CognitoIdentityProviderCache", 0);
        String csiIdTokenKey = "CognitoIdentityProvider." + "." + currentUser.getUserId() + ".idToken";
        return prefs.contains(csiIdTokenKey);
    }*/

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