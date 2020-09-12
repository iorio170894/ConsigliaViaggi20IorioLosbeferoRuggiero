package com.example.tripnaples;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityRegistrazione2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione2);

        //Tasto torna al menu torna a MainActivity

        Button tornaMenu = (Button) findViewById(R.id.buttonTornaMenu);
        tornaMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //apri activity relativo al mainActivity
                startActivity(new Intent(ActivityRegistrazione2.this, MainActivity.class));
            }
        });

        //Tasto indietro torna a ActivityRegistrazione

        Button indietro = (Button) findViewById(R.id.buttonIndietro);
        indietro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //apri activity relativo al mainActivity
                startActivity(new Intent(ActivityRegistrazione2.this, ActivityRegistrazione.class));
            }
        });

    }
}