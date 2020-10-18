package com.example.tripnaples;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();

        //Bottone Login
        Button login = (Button) findViewById(R.id.buttonLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //apri activity relativo al Login
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        //Bottone Registrati

        Button registrati = (Button) findViewById(R.id.ButtonReg);
        registrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //apri activity relativo alla registrazione
                startActivity(new Intent(MainActivity.this, ActivityRegistrazione.class));
            }
        });

        //Bottone continua come ospite

        Button ospite = (Button) findViewById(R.id.buttonOspite);
        ospite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Check.loggato=false;
                startActivity(new Intent(MainActivity.this, ActivityBenvenuto.class));

            }
        });


    }

    public void checkPermission(){

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this,"Permessi concessi",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(MainActivity.this,"Permessi non dati",Toast.LENGTH_SHORT).show();

            }
        };

        TedPermission.with(MainActivity.this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE)
                .check();
    }
}