package com.example.tripnaples.DAOfactory;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.example.tripnaples.Activity.LoginActivity;
import com.example.tripnaples.Models.Utente;
import com.example.tripnaples.Utility.Check;
import com.example.tripnaples.Utility.CognitoSettings;

import org.w3c.dom.Text;

public class dettagliUtenteCognitoDao implements dettagliUtenteDao {

    String emailSalvata;
    String nomeCognomeSalvato;
    String nicnknameSalvato;
    Utente utenteCorrente;


    @Override
    public void getDettagliUtente(final Context context, final TextView textEmail, final TextView textNickname, final TextView textNomeCognome) {


        GetDetailsHandler handler = new GetDetailsHandler() {
            @Override
            public void onSuccess(final CognitoUserDetails list) {
                //trovi dettagli utente con successo
                emailSalvata = String.valueOf(list.getAttributes().getAttributes().get("email"));
                nomeCognomeSalvato = String.valueOf(list.getAttributes().getAttributes().get("name"));
                nicnknameSalvato = String.valueOf(list.getAttributes().getAttributes().get("nickname"));
                utenteCorrente = new Utente(nomeCognomeSalvato,emailSalvata,nicnknameSalvato);
                textEmail.setText(utenteCorrente.getEmail());
                textNickname.setText(utenteCorrente.getNickname());
                textNomeCognome.setText(utenteCorrente.getNomeCognome());
                Check.nicnknameSpinner=nicnknameSalvato;
                Check.nomeCognomeSpinner= nomeCognomeSalvato;

            }
            @Override
            public void onFailure(final Exception exception) {
                // Fallimento nel recupero dei dettagli dell'utente
                new android.app.AlertDialog.Builder(context)
                        .setTitle("Errore nel recuper dei dettagli utente")
                        .setMessage("We're sorry but we are experiencing problems with your account. Try to exit and log in again. Error details: " + exception.getLocalizedMessage())
                        .setPositiveButton("Ok!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent turnToLoginPage = new Intent(context, LoginActivity.class);
                                turnToLoginPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                context.startActivity(turnToLoginPage);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        };
        CognitoSettings cognitoSettings = new CognitoSettings(context);
        CognitoUser corrente = cognitoSettings.getUserPool().getCurrentUser();
        cognitoSettings.getUserPool().getUser(corrente.getUserId()).getDetailsInBackground(handler);
    }

    @Override
    public void getNicknameUtente(final Context context, final TextView textFirma) {
        final String[] returnNome = new String[1];
        GetDetailsHandler handler = new GetDetailsHandler() {
            @Override
            public void onSuccess(final CognitoUserDetails list) {
                //trovi dettagli utente con successo
                returnNome[0] = String.valueOf(list.getAttributes().getAttributes().get("nickname"));
                textFirma.setText(returnNome[0]);
            }
            @Override
            public void onFailure(final Exception exception) {
                // Fallimento nel recupero dei dettagli dell'utente
                Log.e("Eccezione dettagli utente:",exception.toString());
                new android.app.AlertDialog.Builder(context)
                        .setTitle("Errore nel recuper dei dettagli utente")
                        .setMessage("We're sorry but we are experiencing problems with your account. Try to exit and log in again. Error details: " + exception.getLocalizedMessage())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        };
        CognitoSettings cognitoSettings = new CognitoSettings(context);
        CognitoUser corrente = cognitoSettings.getUserPool().getCurrentUser();
        cognitoSettings.getUserPool().getUser(corrente.getUserId()).getDetailsInBackground(handler);
    }
}
