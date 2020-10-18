package com.example.tripnaples;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;

public interface loginDAO {

    public void login(final Context context, final Button buttonLogin, final EditText editTextPassword, final EditText editTextEmail, final LoagindDialog loagindDialog);

}
