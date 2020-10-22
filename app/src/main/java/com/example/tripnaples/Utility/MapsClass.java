package com.example.tripnaples.Utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;

import com.example.tripnaples.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MapsClass extends AppCompatActivity {

    public static boolean isGPSEnabled(Context context) {
        LocationManager cm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return cm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    //scegli il tipo di marker da inserire su mappa
    public static void chooseTypeMarker(MarkerOptions markerOptionsStrutture, String inputTipoStruttura, Context context) {
        if (inputTipoStruttura.equals("Ristorante")){
            markerOptionsStrutture.icon(bitmapDescriptorFromVector(context, R.drawable.ic_restaurant_marker));
        }
        else if (inputTipoStruttura.equals("Bar")){
            markerOptionsStrutture.icon(bitmapDescriptorFromVector(context, R.drawable.ic_bar_prova_marker));
        }
        else if (inputTipoStruttura.equals("Hotel")){
            markerOptionsStrutture.icon(bitmapDescriptorFromVector(context, R.drawable.ic_hotel_marker));
        }
        else if (inputTipoStruttura.equals("Parco")){
            markerOptionsStrutture.icon(bitmapDescriptorFromVector(context, R.drawable.ic_park_marker));
        }
        else if (inputTipoStruttura.equals("Teatro")){
            markerOptionsStrutture.icon(bitmapDescriptorFromVector(context, R.drawable.ic_theatre_marker));
        }
        else if (inputTipoStruttura.equals("Museo")){
            markerOptionsStrutture.icon(bitmapDescriptorFromVector(context, R.drawable.ic_museo_marker));
        }
    }


    public static BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    //ritorna distanza in km tra due posizioni
    public static double getDistanceKm(LatLng pt1, LatLng pt2){
        if (pt1.latitude>90 || pt2.latitude >90 || pt1.longitude>180 || pt2.longitude >180){
            throw new IllegalArgumentException();
        }
        else if (pt1.latitude<0 || pt2.latitude <0 || pt1.longitude<0 || pt2.longitude <0){
            throw new IllegalArgumentException();
        }
        else {
            double distance = 0d;
            double theta = pt1.longitude - pt2.longitude;
            double dist = Math.sin(Math.toRadians(pt1.latitude)) * Math.sin(Math.toRadians(pt2.latitude))
                    + Math.cos(Math.toRadians(pt1.latitude)) * Math.cos(Math.toRadians(pt2.latitude)) * Math.cos(Math.toRadians(theta));

            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            distance = dist * 60 * 1853.1596;
            distance = distance / 1000;
            distance = Math.round(distance * 100);
            distance = distance / 100;
            return distance;
        }
    }

}
