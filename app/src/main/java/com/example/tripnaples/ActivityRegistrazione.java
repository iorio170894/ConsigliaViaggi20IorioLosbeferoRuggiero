package com.example.tripnaples;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;

import static android.widget.Toast.*;

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
            final EditText inputEmail = findViewById(R.id.editTextMail);
            final EditText inputPassword = findViewById(R.id.editTextTextPassword);
            final EditText inputConfermaPassword = findViewById(R.id.editTextTextConfirmPassword);
            final EditText inputNickname = findViewById(R.id.editTextNickname);


            //Crea un CognitoUserAttributes e un user Attributes

            final CognitoUserAttributes userAttributes = new CognitoUserAttributes();

            userAttributes.addAttribute("email",String.valueOf(inputEmail.getText()));
            userAttributes.addAttribute("nickname",String.valueOf(inputNickname.getText()));

            final SignUpHandler signupCallback = new SignUpHandler() {
                @Override
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
                  //  if (inputPassword.equals(inputConfermaPassword)) {
                        loagindDialog.startLoadingDialog();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loagindDialog.dismissDialog();
                                startActivity(new Intent(ActivityRegistrazione.this, ActivityBenvenuto.class));
                            }
                        }, 5000);
                  //  }
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

            Button avanti = (Button) findViewById(R.id.buttonAvanti);
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
                       CognitoSettings cognitoSettings = new CognitoSettings(ActivityRegistrazione.this);
                       cognitoSettings.getUserPool().signUpInBackground(String.valueOf(inputNickname.getText())
                               , String.valueOf(inputPassword.getText()), userAttributes, null, signupCallback
                       );
                        }
                }
            });
        }

        LoagindDialog loagindDialog = new LoagindDialog(ActivityRegistrazione.this);

}