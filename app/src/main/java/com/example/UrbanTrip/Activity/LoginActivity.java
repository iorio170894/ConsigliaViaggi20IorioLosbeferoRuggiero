package com.example.UrbanTrip.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.UrbanTrip.DAOfactory.DAOFactory;
import com.example.UrbanTrip.DAOfactory.loginDAO;
import com.example.UrbanTrip.R;
import com.example.UrbanTrip.Utility.LoagindDialog;


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