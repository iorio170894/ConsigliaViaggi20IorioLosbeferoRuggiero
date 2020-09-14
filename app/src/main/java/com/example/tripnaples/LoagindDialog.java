package com.example.tripnaples;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;

import java.security.KeyStore;

public class LoagindDialog {
    Activity activity;
    AlertDialog dialog;

    LoagindDialog (Activity myActivity){
        activity=myActivity;
    }

    void  startLoadingDialog () {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog,null));
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.show();
    }

    void  dismissDialog (){
        dialog.dismiss();
    }
}
