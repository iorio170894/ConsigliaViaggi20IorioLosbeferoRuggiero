package com.example.tripnaples;


import android.content.Context;
import android.widget.ProgressBar;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DAOFactory {

    String server;

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

}
