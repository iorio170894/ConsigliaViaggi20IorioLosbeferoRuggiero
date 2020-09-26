package com.example.tripnaples;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);

        registerUser();

        Button annulla = (Button) findViewById(R.id.buttonAnnulla);
        annulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //apri activity relativo al mainActivity
                startActivity(new Intent(ActivityRegistrazione.this, MainActivity.class));
            }
        });
    }

    private void registerUser() {
        final EditText inputEmail = findViewById(R.id.editTextMailRegistration);
        final EditText inputPassword = findViewById(R.id.editTextTextPasswordRegistration);
        final EditText inputConfermaPassword = findViewById(R.id.editTextTextConfirmPasswordRegistration);
        final EditText inputNickname = findViewById(R.id.editTextNicknameRegistration);
        final EditText inputNome = findViewById(R.id.editTextNomeCognomeRegistration);
        //  final EditText inputCognome = findViewById(R.id.editTextCognome);


        //Crea un CognitoUserAttributes e un user Attributes

        final CognitoUserAttributes userAttributes = new CognitoUserAttributes();

        //userAttributes.addAttribute("family name",String.valueOf(inputCognome.getText()));

        final SignUpHandler signupCallback = new SignUpHandler() {

            public void onSuccess(CognitoUser user, boolean signUpConfirmationState, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
                //Registrazione riuscita
                Log.i("Tag", "Registrazione avvenuta con successo" + signUpConfirmationState);
                //Controlla se user (cognitoUser) deve essere confermato
                if (!signUpConfirmationState) {
                    Log.i("Tag", "Registrazione avvenuta con successo, codice di verifica inviato a" + cognitoUserCodeDeliveryDetails.getDestination());
                } else {
                    //user è stato confermato
                    Log.i("Tag", "Registrazione avvenuta con successo... confermata");
                }

                //custom dialog
                final AlertDialog dialog = new AlertDialog.Builder(ActivityRegistrazione.this)
                        .setTitle("Registrazione")
                        .setMessage("Registrazione avvenuta con successo")
                        .setPositiveButton("OK", null)
                        .show();
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ActivityRegistrazione.this, MainActivity.class));
                        dialog.dismiss();
                    }
                });
            }


            @Override
            public void onFailure(Exception exception) {
                //registrazione fallita controlla l'eccezione
                Log.i( "Tag","Registrazione fallita" + exception.getLocalizedMessage());
                AlertDialog.Builder builder=new AlertDialog.Builder(ActivityRegistrazione.this);
                builder.setTitle("Errore nella Registrazione:");
                builder.setMessage("Attenzione:"+exception.getLocalizedMessage());
                builder.show();
            }
        };

        //Tasto avanti va avanti nella registrazione

        Button avanti = (Button) findViewById(R.id.buttonIscritivi);
        avanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!inputPassword.getText().toString().equals(inputConfermaPassword.getText().toString())) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(ActivityRegistrazione.this);
                    builder.setTitle("Errore nella Registrazione:");
                    builder.setMessage("Attenzione: Password e Conferma Password non coincidono");
                    builder.show();
                }
                else {
                    //custom dialog
                    final AlertDialog dialog = new AlertDialog.Builder(ActivityRegistrazione.this)
                            .setTitle("Registrazione")
                            .setMessage("Conferma la Registrazione?")
                            .setPositiveButton("Conferma", null)
                            .setNegativeButton("Annulla", null)
                            .show();

                    //dialog.getWindow().setGravity(Gravity.BOTTOM);

                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positiveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            userAttributes.addAttribute("email",String.valueOf(inputEmail.getText()));
                            userAttributes.addAttribute("nickname",String.valueOf(inputNickname.getText()));
                            userAttributes.addAttribute("name",String.valueOf(inputNome.getText()));

                            CognitoSettings cognitoSettings = new CognitoSettings(ActivityRegistrazione.this);
                            cognitoSettings.getUserPool().signUpInBackground(String.valueOf(inputEmail.getText())
                                    , String.valueOf(inputPassword.getText()), userAttributes, null, signupCallback
                            );
                            dialog.dismiss();
                        }
                    });
                }

            }
        });
    }




}