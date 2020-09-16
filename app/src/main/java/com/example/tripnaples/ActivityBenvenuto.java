package com.example.tripnaples;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityBenvenuto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_benvenuto);

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
}