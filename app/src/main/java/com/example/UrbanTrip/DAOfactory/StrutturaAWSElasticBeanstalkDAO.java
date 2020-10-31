package com.example.UrbanTrip.DAOfactory;

import android.content.Context;

import com.example.UrbanTrip.Utility.JsonClass;

import androidx.appcompat.app.AppCompatActivity;

public class StrutturaAWSElasticBeanstalkDAO extends AppCompatActivity implements StrutturaDAO {

    @Override
    public void getStrutturaByTipo(onResultList onResultList, String inputTipoStruttura, final Context context) {



        String url="http://consigliaviaggi20.us-east-2.elasticbeanstalk.com/struttura/search_strutture.php?inputTipo="+inputTipoStruttura;
        JsonClass jsonClass= new JsonClass();
        jsonClass.getJsonStruttureFromUrl(onResultList,context,url);

    }

    public void getStrutturaByFilter(final onResultList onResultList, String inputTipoStruttura, String inputCittà, int range, final Context context){

        String url = "http://consigliaviaggi20.us-east-2.elasticbeanstalk.com/struttura/search_filter_strutture.php?inputTipo=" + inputTipoStruttura + "&inputCitt%C3%A0=" + inputCittà + "&inputRangePrezzo=" + range;
        JsonClass jsonClass= new JsonClass();
        jsonClass.getJsonStruttureFromUrl(onResultList,context,url);

    }

    public void getAllStrutture (final onResultList onResultList, final Context context) {

        String url = "http://consigliaviaggi20.us-east-2.elasticbeanstalk.com/struttura/read.php";
        JsonClass jsonClass= new JsonClass();
        jsonClass.getJsonStruttureFromUrl(onResultList,context,url);

    }

}