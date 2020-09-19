package com.example.tripnaples;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityBenvenuto extends AppCompatActivity {

    //variabili
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_benvenuto);

        //Assegna variabili

        drawerLayout=findViewById(R.id.drawer_layout);


        //Bottone Ristoranti intorno a me
        Button ristoranti = (Button) findViewById(R.id.buttonSearchRistoranti);
        ristoranti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //apri activity relativo al Login
                startActivity(new Intent(ActivityBenvenuto.this, ActivityRistorantiIntornoaMe.class));
            }
        });


    }

    public void ClickMenu (View view){
        //Apri drawer

        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        //apri drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo (View view){
        //chiudi drawer
    //    closeDrawer(drawerLayout);
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
        redirectActivity(this,ActivityBenvenuto.class);
    }

    public void ClickRicerca (View view){
        //reindirizza activity alla dashboard
        redirectActivity(this,ActivityRicerca.class);
    }

    public void ClickImpostazioni (View view){

        redirectActivity(this,ActivityImpostazioni.class);
    }

    public void ClickLogout (View view){
        //chiudi app
      //  logout(this);
    }

    public static void logout(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Logout");
        builder.setMessage("Sei sicuro di voler far eil Logout?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Finish activity
                activity.finishAffinity();
                //exit app
                System.exit(0);
            }
        });
        //Negative no button
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Dismiss dialog
                dialog.dismiss();
            }
        });
        builder.show();
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