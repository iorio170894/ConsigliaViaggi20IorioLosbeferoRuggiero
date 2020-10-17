package com.example.tripnaples;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MapsClass extends AppCompatActivity {

    public static boolean isGPSEnabled(Context context) {
        LocationManager cm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return cm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

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

}
