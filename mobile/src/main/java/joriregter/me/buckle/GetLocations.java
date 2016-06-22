package joriregter.me.buckle;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by joriregter on 23/05/16.
 */
public class GetLocations extends AsyncTask<String, Void, JSONObject> {

    // Why this?
    private WeakReference<Activity> _caller;
    private final String uri = "https://buckle-65946.onmodulus.net/locations";
    private String caller, _rText;
    public GetLocations(Activity caller) {
        _caller = new WeakReference<Activity>(caller);
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        caller = params[0];
        if (caller == "navigation") {
            _rText = params[1];
        }
        String result = getData ();
        JSONObject jsonObject = JSONparse(result);

        return jsonObject;
    }

    private JSONObject JSONparse (String jsonString) {
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            System.out.println("CANT MAKE JSON");
            e.getStackTrace();
        }
        return jsonObject;
    }

    @Override
    protected void onPostExecute (JSONObject result) {
//        System.out.println(result+ "asdfhauisjklfma");
        if (result != null) {
            Activity activity = _caller.get();

            if (caller == "proximity") {
                ((VoiceManager) activity).handleProximity(result);
            } else if (caller == "navigation") {
                ((VoiceManager) activity).handleNavigation(result, _rText);
            }

        }
    }

    private String getData () {
        InputStream stream = null;
        String result = null;

        // Try to make connection
        try {

            //What exactly happens here?
            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept", "application/json");
            conn.connect();

            stream = conn.getInputStream();

            result = readIt(stream);

        }
        catch (MalformedURLException e) {
            // URL is not right?
            System.out.println(e);
        }
        catch (IOException e) {
            System.out.println(e);
            // Could not connect to internet?

        } finally {
            try {
                stream.close();
            }
            catch (IOException e) {
                e.printStackTrace();

                // Could not connect to internet?
            }
        }
        System.out.println(result);
        return result;
    }

    public String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        StringBuilder responseStrBuilder = new StringBuilder();

        String inputStr;
        while ((inputStr = streamReader.readLine()) != null)
            responseStrBuilder.append(inputStr);

        return responseStrBuilder.toString();
    }
}
