package com.example.tripnaples;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityRegistrazione extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);

        //Tasto annulla torna a MainActivity

        Button annulla = (Button) findViewById(R.id.buttonAnnulla);
        annulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //apri activity relativo al mainActivity
                startActivity(new Intent(ActivityRegistrazione.this, MainActivity.class));
            }
        });

        //Tasto avanti va avanti nella registrazione

        Button avanti = (Button) findViewById(R.id.buttonAvanti);
        avanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //apri activity relativo alla seconda registrazione
                startActivity(new Intent(ActivityRegistrazione.this, ActivityRegistrazione2.class));
            }
        });
    }
}