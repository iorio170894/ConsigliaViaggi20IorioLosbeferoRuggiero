package com.example.UrbanTrip.DAOfactory;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;

public interface signUpDAO {

    public void signUp(final Context context, Button iscriviti, final EditText inputPassword, final EditText inputConfermaPassword,
                       final EditText inputEmail, final EditText inputNickname, final EditText inputNome);

}
