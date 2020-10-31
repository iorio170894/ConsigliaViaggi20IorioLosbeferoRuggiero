package com.example.UrbanTrip.DAOfactory;

import android.content.Context;

public interface StrutturaDAO {

    public void getStrutturaByTipo(onResultList onResultList,String inputTipoStruttura, Context context);

    public void getStrutturaByFilter(onResultList onResultList,String inputTipoStruttura,String inputCittà,int range, Context context);

    public void getAllStrutture (onResultList onResultList,Context context);

}