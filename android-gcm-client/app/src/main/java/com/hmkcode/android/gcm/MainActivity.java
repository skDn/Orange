package com.hmkcode.android.gcm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;


public class MainActivity extends android.support.v4.app.FragmentActivity implements OnClickListener {
    //-----------------------------------------
    public static MainActivity MAIN_ACTIVITY;
    public static LatLng current_location;
    public static ArrayList<Marker> markers;
    public static Button response;
    private boolean isRunning;
    //-----------------------------------------
    //Button response;
    Button btnRegId;
    EditText etRegId;
    EditText messageBox;
    public static GoogleMap googleMap;

    GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = "897399289848";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //-----------------------------------------
        MAIN_ACTIVITY = this;
        markers = new ArrayList<Marker>();
        //-----------------------------------------

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRegId = (Button) findViewById(R.id.btnGetRegId);
        response = (Button) findViewById(R.id.button);
        response.setClickable(false);
        response.setVisibility(View.INVISIBLE);

//        response.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                if(response.getText().equals("Accept"))
//                    response.setText("Click to complete");
//                else
//                    response.setText("Completed");
//            }
//        });
        //etRegId = (EditText) findViewById(R.id.etRegId);
        messageBox = (EditText) findViewById(R.id.messageWindow);

        //added by WL
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            googleMap.setMyLocationEnabled(true);

            googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                @Override
                public void onMyLocationChange(Location arg0) {
                    // TODO Auto-generated method stub

                    current_location = new LatLng(arg0.getLatitude(), arg0.getLongitude());
                    Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("It's Me!"));
                    markers.add(marker);

                    //CameraUpdate center=CameraUpdateFactory.newLatLng(new LatLng(arg0.getLatitude(), arg0.getLongitude()));

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(arg0.getLatitude(), arg0.getLongitude()))      // Sets the center of the map to Mountain View
                            .zoom(16)                   // Sets the zoom
                            .build();                   // Creates a CameraPosition from the builder
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    //CameraUpdate zoom=CameraUpdateFactory.zoomTo(11);
                    //googleMap.moveCamera(center);
                    //googleMap.animateCamera(zoom);

                }
            });


        }
        //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current_location, 16));



        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        btnRegId.setOnClickListener(this);
    }
    public void getRegId(){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(PROJECT_NUMBER);
                    msg = "Device registered, registration ID=" + regid;
                    Log.i("GCM", msg);



                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                //etRegId.setText(msg + "\n");
            }
        }.execute(null, null, null);
    }
    @Override
    public void onClick(View v) {
        getRegId();
    }


    //-----------------------------------------
    @Override
    public void onResume() {
        super.onResume();
        isRunning = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isRunning = false;
    }


    public void onPushReceived(String pushText) {
        if (isRunning) { // if activity is in foreground, then show message in a box
            messageBox.setText(pushText);
        }
    }



    //-----------------------------------------
}
