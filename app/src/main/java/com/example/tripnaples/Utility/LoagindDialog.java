package com.example.tripnaples.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;

import com.example.tripnaples.R;

import java.security.KeyStore;

public class LoagindDialog {
    Activity activity;
    AlertDialog dialog;

    public LoagindDialog(Activity myActivity){
        activity=myActivity;
    }

    public void  startLoadingDialog () {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog,null));
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.show();
    }

    public void  dismissDialog (){
        dialog.dismiss();
    }
}
