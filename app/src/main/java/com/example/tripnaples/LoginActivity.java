package com.example.tripnaples;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;


public class LoginActivity extends AppCompatActivity {

    private loginDAO loginDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        final EditText editTextEmail = findViewById(R.id.editTextTextEmailLogin);
        final EditText editTextPassword = findViewById(R.id.editTextTextPasswordLogin);
        Button buttonLogin = findViewById(R.id.buttonAccediLogin);

        //Login
        DAOFactory DF = DAOFactory.getDAOInstance(LoginActivity.this);
        loginDAO = DF.getAuthenticationForLoginDAO();
        loginDAO.login(LoginActivity.this,buttonLogin,editTextPassword,editTextEmail,new LoagindDialog(LoginActivity.this));


    }

}