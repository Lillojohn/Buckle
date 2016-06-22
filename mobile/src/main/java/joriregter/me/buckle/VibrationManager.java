package joriregter.me.buckle;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.Locale;

public class VibrationManager implements
        DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static VibrationManager _vibrationManager;
    private Vibrator _vibrator;
    private static final String TAG = "VIBRATOR_MANAGER";

    private GoogleApiClient _googleApiClient;
    private DataApi _dataApi;

    public VibrationManager (AppCompatActivity activity) {
        _vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        _googleApiClient = new GoogleApiClient.Builder(activity)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(Wearable.API) // Add Wearable API
            .build();

        _googleApiClient.connect();
    }

    public void updateWear (long[] PATTERN) {
        // Create endpoint for wear to listen to.
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/PATTERN");
        // Put certain vibration pattern.
        putDataMapReq.getDataMap().putLongArray("PATTERN", PATTERN);
        // Make it a request.
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        // Do the request
        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(_googleApiClient, putDataReq);
    }

    public void vibrateMessage(VibrateMessage message) {
        long[] PATTERN;
        switch (message) {
            case Left: {
                PATTERN = new long[]{0, 1000};
                updateWear(PATTERN);
                break;
            }
            case Right: {
                PATTERN = new long[]{0, 1000};
                _vibrator.vibrate(PATTERN, -1);
                break;
            }
            case Arrived: {
                PATTERN = new long[]{200, 1000, 200, 500};
                _vibrator.vibrate(PATTERN, -1);
                updateWear(PATTERN);
                break;
            }
            case Stairs: {
                PATTERN = new long[]{200, 200, 200, 1000};
                _vibrator.vibrate(PATTERN, -1);
                updateWear(PATTERN);
                break;
            }
            case Door: {
                PATTERN = new long[]{200, 500, 200, 500};
                _vibrator.vibrate(PATTERN, -1);
                updateWear(PATTERN);
                break;
            }
            default: {
                PATTERN = new long[]{};
                _vibrator.vibrate(PATTERN, -1);
                updateWear(PATTERN);
                break;
            }
        }

        updateWear(new long[]{});
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "Connected to wear.");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection with wear suspended.");
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        Log.d(TAG, "Syncing data with wear.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Failed to connect to wear.");
    }
}