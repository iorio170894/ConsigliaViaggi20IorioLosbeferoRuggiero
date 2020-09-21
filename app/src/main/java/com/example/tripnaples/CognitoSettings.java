package com.example.tripnaples;

import android.content.Context;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.regions.Regions;

public class CognitoSettings {
    private static String userPoolId="us-east-2_lsiqRfFjT";
    private static String clientId="44958qqvjntk6kr1k533jetpeg";
    private static String clientSecret="8v3rdc8q822itbfdqmrts127pg0huqs44uhb4psq1oimdqivs91";
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
            /*if (user != null) {
                GenericHandler handler = new GenericHandler() {

                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onFailure(Exception e) {
                    }
                };*/
                //user.globalSignOutInBackground(handler);
                user.signOut();
            }

        }
}
