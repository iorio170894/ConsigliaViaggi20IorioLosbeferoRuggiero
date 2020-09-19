package com.example.tripnaples;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                //apri activity relativo a continua come ospite
                startActivity(new Intent(MainActivity.this, ActivityBenvenuto.class));
            }
        });

    }
}