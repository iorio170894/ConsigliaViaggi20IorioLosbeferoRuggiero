package com.example.UrbanTrip.DAOfactory;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;

import com.example.UrbanTrip.Utility.LoagindDialog;

public interface loginDAO {

    public void login(final Context context, final Button buttonLogin, final EditText editTextPassword, final EditText editTextEmail, final LoagindDialog loagindDialog);

}
