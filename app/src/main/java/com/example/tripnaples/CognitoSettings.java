package com.example.tripnaples;

import android.content.Context;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.regions.Regions;

public class CognitoSettings {
    private String userPoolId="us-east-2_mtcGMJi7C";
    private String clientId="552i5pr5krbl9fjvj3a8riud27";
    private String clientSecret="1nor9civ5vbe9pjrhfphjfjodqmi18h0shtn7kon5cbrboi97d1g";
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
