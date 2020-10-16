package com.example.tripnaples;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;

public interface StrutturaDAO {

    public void getStrutturaByTipo(onResultList onResultList,String inputTipoStruttura, Context context);

    public void getStrutturaByFilter(onResultList onResultList,String inputTipoStruttura,String inputCittà,int range, Context context);

    public void getAllStrutture (onResultList onResultList,Context context);

}