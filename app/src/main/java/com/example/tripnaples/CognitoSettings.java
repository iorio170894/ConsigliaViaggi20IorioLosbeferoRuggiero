package com.example.tripnaples;

import android.content.Context;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.regions.Regions;

public class CognitoSettings {
    private static CognitoSettings INSTANCE = new CognitoSettings(null);
    private static String userPoolId="us-east-2_mtcGMJi7C";
    private static String clientId="552i5pr5krbl9fjvj3a8riud27";
    private static String clientSecret="1nor9civ5vbe9pjrhfphjfjodqmi18h0shtn7kon5cbrboi97d1g";
    private static Regions cognitoRegion=Regions.US_EAST_2;

    private static Context context;

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

    public static CognitoUserPool getUserPool(){
        return new CognitoUserPool(context,userPoolId,clientId,
                clientSecret,cognitoRegion);
    }

    public static void logout(){
        CognitoUserPool pool=CognitoSettings.getUserPool();
        if (pool != null) {
            CognitoUser user = pool.getCurrentUser();
            if (user != null) {
                GenericHandler handler = new GenericHandler() {

                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onFailure(Exception e) {
                    }
                };
                user.globalSignOutInBackground(handler);
            }

        }
    }
    public static CognitoSettings getInstance() {
        return(INSTANCE);
    }
}
