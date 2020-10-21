package com.example.tripnaples.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.example.tripnaples.R;
import com.example.tripnaples.Utility.Check;

public class ActivityBenvenuto extends AppCompatActivity {

    //variabili
    DrawerLayout drawerLayout;
    Dialog mydialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_benvenuto);


        drawerLayout=findViewById(R.id.drawer_layout);


        //Bottone ristoranti intorno a me
        Button ristoranti = (Button) findViewById(R.id.buttonSearchRistoranti);
        ristoranti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check.tipoRicerca="intorno a me";
                Check.inputTipoStrutturaForSearch="Ristorante";
                startActivity(new Intent(ActivityBenvenuto.this, ActivityStruttureIntornoaMe.class));
            }
        });

        //Bottone parchi intorno a me
        Button parchi = (Button) findViewById(R.id.buttonSearchParchi);
        parchi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check.tipoRicerca="intorno a me";
                Check.inputTipoStrutturaForSearch="Parco";
                startActivity(new Intent(ActivityBenvenuto.this, ActivityStruttureIntornoaMe.class));
            }
        });

        //Bottone hotel intorno a me
        Button hotel = (Button) findViewById(R.id.buttonSearchHotel);
        hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check.tipoRicerca="intorno a me";
                Check.inputTipoStrutturaForSearch="Hotel";
                startActivity(new Intent(ActivityBenvenuto.this, ActivityStruttureIntornoaMe.class));
            }
        });

        //Bottone bar intorno a me
        Button bar = (Button) findViewById(R.id.buttonSearchBar);
        bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check.tipoRicerca="intorno a me";
                Check.inputTipoStrutturaForSearch="Bar";
                startActivity(new Intent(ActivityBenvenuto.this, ActivityStruttureIntornoaMe.class));
            }
        });

        //Bottone teatri intorno a me
        Button teatri = (Button) findViewById(R.id.buttonSearchTeatri);
        teatri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check.tipoRicerca="intorno a me";
                Check.inputTipoStrutturaForSearch="Teatro";
                startActivity(new Intent(ActivityBenvenuto.this, ActivityStruttureIntornoaMe.class));
            }
        });

        //Bottone musei intorno a me
        Button musei = (Button) findViewById(R.id.buttonSearchMusei);
        musei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check.tipoRicerca="intorno a me";
                Check.inputTipoStrutturaForSearch="Museo";
                startActivity(new Intent(ActivityBenvenuto.this, ActivityStruttureIntornoaMe.class));
            }
        });

        mydialog=new Dialog(this);



    }

    //Popup on click su Ricerca Struttura
    public void ShowPopupRicerca (View v){
        mydialog.setContentView(R.layout.custompopupricerca);
        mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mydialog.show();

        Button ricercaAvanzata = (Button) mydialog.findViewById(R.id.buttonRicercaAvanzata);
        ricercaAvanzata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityBenvenuto.this, ActivityRicerca.class));
            }
        });

        Button ricercaPerNome = (Button) mydialog.findViewById(R.id.buttonRicercaPerNome);
        ricercaPerNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityBenvenuto.this, ActivityRicercaPerNome.class));
            }
        });

    }



        @Override
        public void onBackPressed() {
            if (Check.loggato) {
                final androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(ActivityBenvenuto.this)
                        .setTitle("Indietro")
                        .setMessage("Confermi di tornare Indietro? verrà chiusa l'app")
                        .setPositiveButton("Conferma", null)
                        .setNegativeButton("Annulla", null)
                        .show();
                Button positiveButton = dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                        int pid = android.os.Process.myPid();
                        android.os.Process.killProcess(pid);


                    }
                });
            }
            else {
                Intent turnMain = new Intent(ActivityBenvenuto.this, MainActivity.class);
                turnMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                ActivityBenvenuto.this.startActivity(turnMain);
            }

        }

    public void ClickMenu (View view){
        //Apri drawer

        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        //apri drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }


    public static void closeDrawer(DrawerLayout drawerLayout) {
        //chiudi drawer layout
        //check condizioni
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            //quando il drawer è aperto
            //chiudi il drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome (View view){
        //Ricrea Activity
        recreate();
        //redirectActivity(this,ActivityBenvenuto.class);
    }

    public void ClickRicerca (View view){

        redirectActivity(this,ActivityRicerca.class);
    }

    public void ClickImpostazioni (View view){

        if(Check.loggato)
            redirectActivity(this,ActivityImpostazioni.class);
        else
            redirectActivity(this,ActivityImpostazioniOspite.class);
    }


    public static void redirectActivity(Activity activity, Class aClass) {
        //inizializza Intent
        Intent intent = new Intent(activity,aClass);
        //set Flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //start activity
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //close drawer
        closeDrawer(drawerLayout);
    }
}