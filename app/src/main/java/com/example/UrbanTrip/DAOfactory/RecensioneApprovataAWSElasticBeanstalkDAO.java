package com.example.UrbanTrip.DAOfactory;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.example.UrbanTrip.Utility.JsonClass;

import androidx.appcompat.app.AppCompatActivity;

public class RecensioneApprovataAWSElasticBeanstalkDAO extends AppCompatActivity implements RecensioneApprovataDAO {

    private RequestQueue requestQueue;

    @Override
    public void getRecensioneByCodStruttura(onResultList onResultList, int inputCodStruttura, Context context) {

        String url="http://consigliaviaggi20.us-east-2.elasticbeanstalk.com/recensione_approvata/read_for_cod_struttura.php?inputCodStruttura="+inputCodStruttura;
        JsonClass jsonClass= new JsonClass();
        jsonClass.getJsonRecensioniByCodStruttura(onResultList,context,url);

    }
}
