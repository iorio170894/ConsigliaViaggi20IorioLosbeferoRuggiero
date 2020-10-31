package com.example.UrbanTrip.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.UrbanTrip.DAOfactory.DAOFactory;
import com.example.UrbanTrip.DAOfactory.signUpDAO;
import com.example.UrbanTrip.R;

public class ActivityRegistrazione extends AppCompatActivity {

    EditText inputEmail;
    EditText inputPassword;
    EditText inputConfermaPassword;
    EditText inputNickname;
    EditText inputNome;

    signUpDAO signUpDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);

        inputEmail = findViewById(R.id.editTextMailRegistration);
        inputPassword = findViewById(R.id.editTextTextPasswordRegistration);
        inputConfermaPassword = findViewById(R.id.editTextTextConfirmPasswordRegistration);
        inputNickname = findViewById(R.id.editTextNicknameRegistration);
        inputNome = findViewById(R.id.editTextNomeCognomeRegistration);

        Button iscriviti = (Button) findViewById(R.id.buttonIscritivi);

        //register user;
        DAOFactory DF = DAOFactory.getDAOInstance(ActivityRegistrazione.this);
        signUpDAO = DF.getAuthenticationForSignUpDAO();
        signUpDAO.signUp(ActivityRegistrazione.this,iscriviti,inputPassword,inputConfermaPassword,
                            inputEmail,inputNickname,inputNome);



        Button annulla = (Button) findViewById(R.id.buttonAnnulla);
        annulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //apri activity relativo al mainActivity
                startActivity(new Intent(ActivityRegistrazione.this, MainActivity.class));
            }
        });
    }




}