package com.example.tripnaples.DAOfactory;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.example.tripnaples.Activity.MainActivity;
import com.example.tripnaples.Utility.CognitoSettings;

import androidx.appcompat.app.AlertDialog;

public class signUpCognitoDAO implements signUpDAO {

    @Override
    public void signUp(final Context context, Button iscriviti, final EditText inputPassword, final EditText inputConfermaPassword,
                       final EditText inputEmail, final EditText inputNickname, final EditText inputNome) {

        final CognitoUserAttributes userAttributes = new CognitoUserAttributes();

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
                final AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("Registrazione")
                        .setMessage("Registrazione avvenuta con successo")
                        .setPositiveButton("OK", null)
                        .show();
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Check.firma=String.valueOf(inputNickname.getText());
                        context.startActivity(new Intent(context, MainActivity.class));
                        dialog.dismiss();
                    }
                });
            }


            @Override
            public void onFailure(Exception exception) {
                //registrazione fallita controlla l'eccezione
                Log.i( "Tag","Registrazione fallita" + exception.getLocalizedMessage());
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Errore nella Registrazione:");
                builder.setMessage("Attenzione:"+exception.getLocalizedMessage());
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.show();


            }
        };

        //Tasto avanti va avanti nella registrazione

        iscriviti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!inputPassword.getText().toString().equals(inputConfermaPassword.getText().toString())) {

                    inputConfermaPassword.setError("Attenzione: Password e Conferma Password non coincidono!");
                }
                else {
                    if (controlCampiVuoti(inputEmail, inputNickname, inputNome) ){
                        //custom dialog
                        final AlertDialog dialog = new AlertDialog.Builder(context)
                                .setTitle("Registrazione")
                                .setMessage("Conferma la Registrazione?")
                                .setPositiveButton("Conferma", null)
                                .setNegativeButton("Annulla", null)
                                .show();

                        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        positiveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                userAttributes.addAttribute("email", String.valueOf(inputEmail.getText()));
                                userAttributes.addAttribute("nickname", String.valueOf(inputNickname.getText()));
                                userAttributes.addAttribute("name", String.valueOf(inputNome.getText()));

                                CognitoSettings cognitoSettings = new CognitoSettings(context);
                                cognitoSettings.getUserPool().signUpInBackground(String.valueOf(inputEmail.getText())
                                        , String.valueOf(inputPassword.getText()), userAttributes, null, signupCallback
                                );
                                dialog.dismiss();
                            }
                        });
                    }


                }

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
