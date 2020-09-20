package com.example.tripnaples;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;

public class ActivityImpostazioniOspite extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impostazioni_ospite);

        //Bottone Login
        Button login = (Button) findViewById(R.id.buttonLoginOspite);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //apri activity relativo al Login
                startActivity(new Intent(ActivityImpostazioniOspite.this, LoginActivity.class));
            }
        });
    }

}