package com.hmkcode.android.gcm;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.Toast;

import android.app.FragmentManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

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



                JSONObject jsonObject = null;
                try {
                    // get the message
                    jsonObject = new JSONObject(mes);
                    jsonObject.getJSONArray("location");

                    MainActivity.MAIN_ACTIVITY.onPushReceived(jsonObject.getString("message"));

                    // take the location of the crime
                    final LatLng TutorialsPoint = new LatLng((Double) jsonObject.getJSONArray("location").get(0), (Double) jsonObject.getJSONArray("location").get(1));
                    Marker TP = MainActivity.googleMap.addMarker(new MarkerOptions().position(TutorialsPoint).title("TutorialsPoint"));


                    JSONParser jParser = new JSONParser();

                    // take the current and crime coordinates
                    double cur_lat = MainActivity.current_location.latitude;
                    double cur_long = MainActivity.current_location.longitude;

                    double new_lat = TutorialsPoint.latitude;
                    double new_long = TutorialsPoint.longitude;

                    String url = makeURL(cur_lat, cur_long, new_lat, new_long);
                    String json = jParser.getJSONFromUrl(url);

                    Log.i("test", json);
                    drawPath(json);

                    // zoom
                    MainActivity.markers.add(TP);
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (Marker marker : MainActivity.markers) {
                        builder.include(marker.getPosition());
                    }
                    LatLngBounds bounds = builder.build();

                    // setting the padding
                    int padding = 50; // offset from edges of the map in pixels
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                    // camera animation
                    MainActivity.googleMap.animateCamera(cu);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //-----------------------------------------
            }
        });

    }


    public String makeURL (double sourcelat, double sourcelog, double destlat, double destlog ){
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString
                .append(Double.toString(sourcelog));
        urlString.append("&destination=");// to
        urlString
                .append(Double.toString(destlat));
        urlString.append(",");
        urlString.append(Double.toString( destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        urlString.append("&key=AIzaSyBn-oewJHt5BQsSsjtfy8bvXDpnnWT6LBE");
        Log.i("URL STRING", urlString.toString());
        return urlString.toString();
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }

    public void drawPath(String  result) {

        try {
            //Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);
            Polyline line = MainActivity.googleMap.addPolyline(new PolylineOptions()
                            .addAll(list)
                            .width(12)
                            .color(Color.parseColor("#05b1fb"))//Google maps blue color
                            .geodesic(true)
            );

           for(int z = 0; z<list.size()-1;z++){
                LatLng src= list.get(z);
                LatLng dest= list.get(z+1);
                line = MainActivity.googleMap.addPolyline(new PolylineOptions()
                .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude,   dest.longitude))
                .width(2)
                .color(Color.BLUE).geodesic(true));
            }

        }
        catch (JSONException e) {

        }
    }


}
