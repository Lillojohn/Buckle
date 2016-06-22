package joriregter.me.buckle;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by joriregter on 21/06/16.
 */
public class PostLocation extends AsyncTask<String,Void,Void> {
    private final String uri = "https://buckle-65946.onmodulus.net/locations";

    @Override
    protected Void doInBackground(String... params) {
        NavigationManager nvm = NavigationManager.getInstance();

        HttpClient client = new DefaultHttpClient();

        JSONObject jsonObject = new JSONObject();
        JSONObject coordinate = new JSONObject();

        try {
            HttpPost post = new HttpPost(uri);
            LatLng latlng = nvm.get_lastLocation();

            coordinate.put("lat", latlng.latitude);
            coordinate.put("lng", latlng.longitude);
//            coordinate.put("lat", 20);
//            coordinate.put("lng", 10);
            jsonObject.put("title", params[0]);
            jsonObject.put("coordinate", coordinate);

            StringEntity se = new StringEntity(jsonObject.toString());
            se.setContentType(new BasicHeader("Content-Type", "application/json"));
            post.setEntity(se);
            client.execute(post);
        } catch (Exception e) {

        }
        return null;
    }


}
