package com.example.tripnaples;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult;

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

    public static boolean controlCampiVuoti(EditText inputEmail, EditText inputNickname, EditText inputNomeCognome) {
        boolean ritorno=true;
        if (String.valueOf(inputEmail.getText()).isEmpty()){
            inputEmail.setError("Attenzione campo vuoto!");
            ritorno=false;
        }
        if (String.valueOf(inputNickname.getText()).isEmpty()){
            inputNickname.setError("Attenzione campo vuoto!");
            ritorno=false;
        }
        if (String.valueOf(inputNomeCognome.getText()).isEmpty()){
            inputNomeCognome.setError("Attenzione campo vuoto!");
            ritorno=false;
        }
        return ritorno;
    }


}