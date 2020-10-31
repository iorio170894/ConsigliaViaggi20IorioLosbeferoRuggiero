package com.example.UrbanTrip.DAOfactory;


import android.content.Context;

import java.io.InputStream;
import java.util.Properties;

public class DAOFactory {

    String server;
    String authentication;

    private static DAOFactory theDAO;


    public static synchronized DAOFactory getDAOInstance(Context context) {
        if (theDAO==null)
            theDAO= new DAOFactory(context);

        return theDAO;

    }


    //Prende dal file di configurazione il server
    private DAOFactory(Context context) {
        try {
            InputStream is = context.getAssets().open("config.properties");
            Properties props = new Properties();
            props.load(is);

           server =  props.getProperty("server");
           authentication = props.getProperty("authentication");

            is.close();
        } catch (Exception e) {
        }
}


    public StrutturaDAO getServerStrutturaDAO() {

        /*if (server.equals("postgres"))
            return new SrutturaPostgresDAO();*/

        if (server.equals("AWS Elastic Beanstalk"))
            return new StrutturaAWSElasticBeanstalkDAO();


        return null;
    }

    public RecensioneApprovataDAO getServerRecensioniDAO() {

        /*if (server.equals("postgres"))
            return new SrutturaPostgresDAO();*/

        if (server.equals("AWS Elastic Beanstalk"))
            return new RecensioneApprovataAWSElasticBeanstalkDAO();


        return null;
    }

    public RecensioneDaApprovareDAO getServerPutRecensioniDAO() {

        /*if (server.equals("postgres"))
            return new SrutturaPostgresDAO();*/

        if (server.equals("AWS Elastic Beanstalk"))
            return new RecensioneDaApprovareAWSElasticBeanstalkDAO();


        return null;
    }

    public signUpDAO getAuthenticationForSignUpDAO() {

        /*if (server.equals("postgres"))
            return new SrutturaPostgresDAO();*/

        if (authentication.equals("Cognito"))
            return new signUpCognitoDAO();


        return null;
    }

    public loginDAO getAuthenticationForLoginDAO() {

        /*if (server.equals("postgres"))
            return new SrutturaPostgresDAO();*/

        if (authentication.equals("Cognito"))
            return new loginCognitoDAO();


        return null;
    }

    public dettagliUtenteDao getAuthenticationForGetDettagliUtente(){

        if (authentication.equals("Cognito"))
            return new dettagliUtenteCognitoDao();

        return null;
    }

}
