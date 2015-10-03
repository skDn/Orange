package com.hmkcode.android.gcm;

import java.io.IOException;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
    //-----------------------------------------
    public static MainActivity MAIN_ACTIVITY;
    private boolean isRunning;
    //-----------------------------------------

    Button btnRegId;
    EditText etRegId;
    EditText messageBox;

    GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = "897399289848";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //-----------------------------------------
        MAIN_ACTIVITY = this;
        //-----------------------------------------

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRegId = (Button) findViewById(R.id.btnGetRegId);
        //etRegId = (EditText) findViewById(R.id.etRegId);
        messageBox = (EditText) findViewById(R.id.messageWindow);


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
                    //msg = "Device registered, registration ID=" + regid;
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
