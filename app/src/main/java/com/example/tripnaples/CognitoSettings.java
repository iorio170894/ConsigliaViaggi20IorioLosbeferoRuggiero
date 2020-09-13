package com.example.tripnaples;

import android.content.Context;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.regions.Regions;

public class CognitoSettings {
    private String userPoolId="us-east-2_02C92CPL8";
    private String clientId="2eig1l17boi1jlf5scuq3gg0no";
    private String clientSecret="1m97rjt42flej3hdqolu6f7qadk8tdck9nlt3joja7qnl9prrmpa";
    private Regions cognitoRegion=Regions.US_EAST_2;

    private Context context;

    public CognitoSettings (Context context){
        this.context=context;
    }

    public String getUserPoolId(){
        return userPoolId;
    }

    public String getClientSecret(){
        return clientSecret;
    }

    public Regions getCognitoRegion(){
        return cognitoRegion;
    }

    public CognitoUserPool getUserPool(){
        return new CognitoUserPool(context,userPoolId,clientId,
                clientSecret,cognitoRegion);
    }
}
