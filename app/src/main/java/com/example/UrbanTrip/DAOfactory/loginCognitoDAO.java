package com.example.UrbanTrip.DAOfactory;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.example.UrbanTrip.Activity.ActivityBenvenuto;
import com.example.UrbanTrip.Utility.Check;
import com.example.UrbanTrip.Utility.CognitoSettings;
import com.example.UrbanTrip.Utility.LoagindDialog;

import androidx.appcompat.app.AlertDialog;

public class loginCognitoDAO implements loginDAO {

    @Override
    public void login(final Context context, final Button buttonLogin, final EditText editTextPassword, final EditText editTextEmail, final LoagindDialog loagindDialog) {
        final AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
            @Override
            public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                // L'accesso è riuscito, cognitoUserSession conterrà i token per l'utente
                Log.i("Cognito", "Login succesfull");
                loagindDialog.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loagindDialog.dismissDialog();
                        Check.loggato=true;
                        context.startActivity(new Intent(context, ActivityBenvenuto.class));
                    }
                }, 2000);
            }

            @Override
            public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
                Log.i("Cognito", "in getAuthenticationDetails()...");
                // L'API necessita delle credenziali di accesso dell'utente per continuare
                // bisogna aver password e userId per continuare
                AuthenticationDetails authenticationDetails = new AuthenticationDetails(userId
                        , String.valueOf(editTextPassword.getText()), null);

                // Passa le credenziali di accesso dell'utente alla continuazione
                authenticationContinuation.setAuthenticationDetails(authenticationDetails);

                // Consenti all'accesso per continuare
                authenticationContinuation.continueTask();
            }

            @Override
            public void getMFACode(MultiFactorAuthenticationContinuation continuation) {

            }

            @Override
            public void authenticationChallenge(ChallengeContinuation continuation) {
                Log.i("Cognito", "in AuthenticationChallenge.");
            }

            @Override
            public void onFailure(Exception exception) {
                // Accesso non riuscito, controlla l'eccezione per la causa
                Log.i("Cognito", "Login faildes:"+exception.getLocalizedMessage());

                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Errore nel Login:");
                builder.setMessage(" Attenzione:"+exception.getLocalizedMessage());
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.show();


            }
        };

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CognitoSettings cognitoSettings = new CognitoSettings(context);

                if (String.valueOf(editTextEmail.getText()).isEmpty() || String.valueOf(editTextPassword.getText()).isEmpty()){
                    if (String.valueOf(editTextEmail.getText()).isEmpty() )
                        editTextEmail.setError("Attenzione campo vuoto!");
                    if (String.valueOf(editTextPassword.getText()).isEmpty() )
                        editTextPassword.setError("Attenzione campo vuoto!");
                }
                else {

                    CognitoUser thisUser = cognitoSettings.getUserPool().
                            getUser(String.valueOf(editTextEmail.getText()));

                    //Sign in the user
                    Log.i("Cognito", "in button clicked..");

                    thisUser.getSessionInBackground(authenticationHandler);
                }
            }
        });
    }
}
