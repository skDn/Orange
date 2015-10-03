package com.hmkcode.android.gcm;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.*;
import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class GcmMessageHandler extends IntentService {

    String mes;
    private Handler handler;

    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();


        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        mes = extras.getString("message");
        showToast();
        Log.i("GCM", "Received : (" + messageType + ")  " + extras.getString("title"));


        GcmBroadcastReceiver.completeWakefulIntent(intent);

    }


    public void showToast() {
        handler.post(new Runnable() {
            public void run() {
                //-----------------------------------------
                //Toast.makeText(getApplicationContext(),mes , Toast.LENGTH_LONG).show();
                if (MainActivity.MAIN_ACTIVITY == null) {
                    Log.e("GcmMessageHandler", "MainActivity is not running!");
                    return;
                }
                getCurrentLocation(MainActivity.googleMap);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(mes);
                    jsonObject.getJSONArray("location");


                    MainActivity.MAIN_ACTIVITY.onPushReceived(mes);

                    final LatLng TutorialsPoint = new LatLng((Double) jsonObject.getJSONArray("location").get(0), (Double) jsonObject.getJSONArray("location").get(1));
                    Marker TP = MainActivity.googleMap.addMarker(new MarkerOptions().position(TutorialsPoint).title("TutorialsPoint"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //-----------------------------------------
            }
        });

    }

    void getCurrentLocation(GoogleMap mMap) {
//        Location myLocation  = mMap.getMyLocation();
//        if(myLocation!=null)
//        {
//            double dLatitude = myLocation.getLatitude();
//            double dLongitude = myLocation.getLongitude();
//            Log.i("APPLICATION", " : " + dLatitude);
//            Log.i("APPLICATION", " : " + dLongitude);
//            final LatLng TutorialsPoint = new LatLng(dLatitude, dLongitude);
//            Marker TP = MainActivity.googleMap.addMarker(new MarkerOptions().position(TutorialsPoint).title("TutorialsPoint"));
//
//        }
//        else
//        {
//            Toast.makeText(this, "Unable to fetch the current location", Toast.LENGTH_SHORT).show();
//        }

        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = service.getBestProvider(criteria, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        Location location = service.getLastKnownLocation(provider);
        LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
        //Marker TP = MainActivity.googleMap.addMarker(new MarkerOptions().position(userLocation).title("userLocation"));
    }



}
