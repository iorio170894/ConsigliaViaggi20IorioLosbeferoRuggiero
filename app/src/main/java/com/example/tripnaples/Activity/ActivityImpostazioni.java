package com.example.tripnaples.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.example.tripnaples.DAOfactory.DAOFactory;
import com.example.tripnaples.DAOfactory.dettagliUtenteDao;
import com.example.tripnaples.Models.Utente;
import com.example.tripnaples.R;
import com.example.tripnaples.Utility.Check;
import com.example.tripnaples.Utility.CognitoSettings;

import java.lang.reflect.Array;

public class ActivityImpostazioni extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Utente utenteCorrente;
    private TextView textNomeCognome;
    private TextView textEmail;
    private TextView textNickname;

    static String nomeCognomeSalvato;
    static String emailSalvata;
    static String nicnknameSalvato;

    dettagliUtenteDao dettagliUtenteDao;

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
                        Intent turnMain = new Intent(ActivityImpostazioni.this,MainActivity.class);
                        turnMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        ActivityImpostazioni.this.startActivity(turnMain);
                    }
                });
            }
        });

        //getDettagliUtente

        DAOFactory DF = DAOFactory.getDAOInstance(ActivityImpostazioni.this);
        dettagliUtenteDao = DF.getAuthenticationForGetDettagliUtente();
        dettagliUtenteDao.getDettagliUtente(ActivityImpostazioni.this,textEmail,textNickname,textNomeCognome);


        //Spinner con firma
        Spinner coloredSpinner = findViewById(R.id.Spinner01);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this,
                R.array.spinnerImpostazioni,
                R.layout.color_spinner_layout);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        coloredSpinner.setAdapter(adapter);
        coloredSpinner.setOnItemSelectedListener(this);
        //Check.firma=coloredSpinner.getSelectedItem().toString();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();

        if (text.equals("Nome e Cognome")){
            Check.firma=Check.nomeCognomeSpinner;
        }
        else if (text.equals("Nickname")){
            Check.firma=Check.nicnknameSpinner;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}