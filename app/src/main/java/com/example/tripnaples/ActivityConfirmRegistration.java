package com.example.tripnaples;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class ActivityConfirmRegistration extends AppCompatActivity {
    Activity activity;
    AlertDialog dialog;

    ActivityConfirmRegistration (Activity myActivity){
        activity=myActivity;
    }

    void  startLoadingDialog () {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.activity_confirm_registration,null));
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.show();
    }

    void  dismissDialog (){
        dialog.dismiss();
    }

}

/*

    LoagindDialog loagindDialog = new LoagindDialog(ActivityConfirmRegistration.this);


    Button conferma = (Button) findViewById(R.id.buttonConfermaConfirm);
        conferma.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        loagindDialog.startLoadingDialog();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
@Override
public void run() {
        loagindDialog.dismissDialog();
        startActivity(new Intent(ActivityConfirmRegistration.this, MainActivity.class));
        }
        }, 5000);
        }
        });
        Button annulla = (Button) findViewById(R.id.buttonAnnullaConfirm);
        annulla.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        startActivity(new Intent(ActivityConfirmRegistration.this, ActivityRegistrazione.class));
        }
        });
*/