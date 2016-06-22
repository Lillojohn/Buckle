package joriregter.me.buckle;

import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationManager;

import java.util.concurrent.ConcurrentLinkedQueue;

public class NavigationActivity extends AppCompatActivity {
    private NavigationManager _nvm = NavigationManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        ConcurrentLinkedQueue<LatLng> path = new ConcurrentLinkedQueue<LatLng>();
//        path.offer(new LatLng());
//        _nvm.setPath(path);
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
}
