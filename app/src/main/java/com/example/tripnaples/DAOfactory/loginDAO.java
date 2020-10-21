package com.example.tripnaples.DAOfactory;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;

import com.example.tripnaples.Utility.LoagindDialog;

public interface loginDAO {

    public void login(final Context context, final Button buttonLogin, final EditText editTextPassword, final EditText editTextEmail, final LoagindDialog loagindDialog);

}
