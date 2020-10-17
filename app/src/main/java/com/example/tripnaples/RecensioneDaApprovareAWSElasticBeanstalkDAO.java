package com.example.tripnaples;

import android.app.Dialog;
import android.content.Context;

public class RecensioneDaApprovareAWSElasticBeanstalkDAO implements RecensioneDaApprovareDAO {

    @Override
    public void putRecensioneByCodStruttura(String data, Context context, Dialog mydialog) {
        String url="http://consigliaviaggi20.us-east-2.elasticbeanstalk.com/recensione_da_approvare/insert_recensione_da_approvare.php";
        JsonClass jsonClass= new JsonClass();
        jsonClass.putJsonRecensioniByCodStruttura(data, context,url,mydialog);
    }
}
