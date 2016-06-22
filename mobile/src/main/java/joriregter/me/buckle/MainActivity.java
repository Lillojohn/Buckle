package joriregter.me.buckle;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.indooratlas.android.sdk.IALocationManager;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer {
    private VibrationManager _vm;
    private VoiceManager vc;
    private NavigationManager _nvm;
    private TextView tlong;
    private TextView tlat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tlat = (TextView) this.findViewById(R.id.text_Lat);
        tlong = (TextView) this.findViewById(R.id.text_Long);
        vc = new VoiceManager(this, this, getApplicationContext());
        _nvm = NavigationManager.getInstance(
                (SensorManager) getSystemService(SENSOR_SERVICE), IALocationManager.create(this)
        );
        _nvm.addObserver(this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        _nvm.onResume();
    }

    @Override
    protected void onDestroy() {
        _nvm.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        _nvm.onPause();
        super.onPause();
    }

    public void onScreenClick (View v) {
        vc.checkVoice();
    }

    protected void promptSpeechInput(){
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something!");

        try {
            startActivityForResult(i, 100);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(MainActivity.this, "Sorry your device doesn't speech language", Toast.LENGTH_LONG).show();
        }
    }

    public void onActivityResult(int request_code, int result_code, Intent i) {
        super.onActivityResult(request_code, result_code, i);
        vc.onActivityResult(request_code, result_code, i);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data instanceof LatLng) {
            tlat.setText(((LatLng) data).latitude + "");
            tlong.setText(((LatLng) data).longitude + "");
        }
    }

    public void goToNavigation(){
        Intent i = new Intent(this, NavigationActivity.class);
        startActivity(i);
    }
}