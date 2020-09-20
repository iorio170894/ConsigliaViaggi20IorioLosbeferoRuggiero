package com.example.tripnaples;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;

public class ActivityImpostazioni extends AppCompatActivity {

    private Utente utenteCorrente;
    private TextView textNomeCognome;
    private TextView textEmail;
    private TextView textNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impostazioni);

        textNomeCognome = findViewById(R.id.nome_cognome_impostazioni);
        textEmail = findViewById(R.id.email_impostazioni);
        textNickname = findViewById(R.id.nickname_impostazioni);

        //Bottone Logout
        Button logout = (Button) findViewById(R.id.buttonLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialog = new AlertDialog.Builder(ActivityImpostazioni.this)
                        .setTitle("Logout")
                        .setMessage("Sei sicuro di voler effettuare il Logout?")
                        .setPositiveButton("Conferma", null)
                        .setNegativeButton("Annulla", null)
                        .show();
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CognitoSettings.logout();
                        //LoginActivity.logout();
                        startActivity(new Intent(ActivityImpostazioni.this, MainActivity.class));
                    }
                });
            }
        });

        getDettagliUtente();
    }

    public void getDettagliUtente(){
        GetDetailsHandler handler = new GetDetailsHandler() {
            @Override
            public void onSuccess(final CognitoUserDetails list) {
                //trovi dettagli utente con successo
                String emailSalvata = String.valueOf(list.getAttributes().getAttributes().get("email"));
                String nomeCognomeSalvato = String.valueOf(list.getAttributes().getAttributes().get("name"));
                String nicnknameSalvato = String.valueOf(list.getAttributes().getAttributes().get("nickname"));
                utenteCorrente = new Utente(nomeCognomeSalvato,emailSalvata,nicnknameSalvato);
                textEmail.setText(utenteCorrente.getEmail());
                textNickname.setText(utenteCorrente.getNickname());
                textNomeCognome.setText(utenteCorrente.getNomeCognome());

                AlertDialog.Builder builder=new AlertDialog.Builder(ActivityImpostazioni.this);
                builder.setTitle("Campi Utente:");
                builder.setMessage("Email:"+emailSalvata+"\nNome e Cognome:"+nomeCognomeSalvato+"\nNickname:"+nicnknameSalvato);
                builder.show();
            }
            @Override
            public void onFailure(final Exception exception) {
                // Fallimento nel recupero dei dettagli dell'utente
                Log.e("Eccezione dettagli utente:",exception.toString());
            /*    new android.app.AlertDialog.Builder(ActivityImpostazioni.this)
                        .setTitle("Errore nel recuper dei dettagli utente")
                        .setMessage("We're sorry but we are experiencing problems with your account. Try to exit and log in again. Error details: " + exception.getLocalizedMessage())
                        .setPositiveButton("Ok!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent turnToLoginPage = new Intent(ProfileActivity.this,LoginActivity.class);
                                turnToLoginPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                ProfileActivity.this.startActivity(turnToLoginPage);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();*/
            }
        };
        CognitoUser corrente = CognitoSettings.getInstance().getUserPool().getCurrentUser();
        CognitoSettings.getInstance().getUserPool().getUser(corrente.getUserId()).getDetailsInBackground(handler);
    }
}