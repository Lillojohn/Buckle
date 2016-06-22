package joriregter.me.buckle;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import joriregter.me.buckle.command.CommandBehavior;
import joriregter.me.buckle.command.defaultCommand;
import joriregter.me.buckle.command.getLocationCommand;
import joriregter.me.buckle.command.confirmCommand;


/**
 * Created by Jonathan on 20-6-2016.
 */
public class VoiceManager extends AppCompatActivity {

    private TextToSpeech ttsobject;
    private int result;
    private String savedLocation;
    private Activity _main;
    private Context _applicationContext;
    private MainActivity _mainclass;
    private CommandBehavior _cb;
    private ArrayList _result;

    public VoiceManager(Activity main, MainActivity mainclass, Context applicationContext) {

        _mainclass = mainclass;
        _main = main;
        _applicationContext = applicationContext;

        _cb = new defaultCommand(this);


        ttsobject = new TextToSpeech(_main, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    result = ttsobject.setLanguage(Locale.ENGLISH);
                } else {
                    Toast.makeText(_applicationContext, "Feature not suported", Toast.LENGTH_SHORT).show();
                }
            }
        });


        savedLocation = "";

    }

    protected void checkVoice() {
        _cb = new defaultCommand(this);
        promptSpeechInput();
    }



    public ArrayList get_result() {
        return _result;
    }

    public void changeCommandBehavior(CommandBehavior newcb) {
        _cb = newcb;
    }

    public void promptSpeechInput() {
        _mainclass.promptSpeechInput();
    }

    public void confirmLocation(String resultVoice) {
        if (speech("Your location is " + resultVoice + ". Is this correct?")) {
            savedLocation = resultVoice;
            delayPromptSpeechInput(3000);
        }
    }

    public void confirmNavigation(String resultVoice) {
        if (speech("Do you want to navigate to the " + resultVoice + ".")) {
            // Yes -> start met navigeren
            // No -> Vraag alles opnieuw
            savedLocation = resultVoice;
            delayPromptSpeechInput(3000);
        }
    }

    public void succesfullSaveLocation() {
        if (speech("Your location has been successfully saved. ")) {
            new PostLocation().execute(savedLocation);
        }
    }

    public void onActivityResult(int request_code, int result_code, Intent i) {
        switch (request_code) {
            case 100: {
                // Check if result is okay && not null.
                if (result_code == RESULT_OK && i != null) {
                    // Array of possible strings
                    ArrayList result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    _result = result;
                    _cb.getResult();
                    _cb.command();
                }
                break;
            }
            default: {
                System.out.println("Something went wrong");
                break;
            }
        }

    }

    public void saveLocation() {
        if (speech("What do you want to call this location?")) {
            delayPromptSpeechInput(3000);
        }
    }

    public void commandFailed() {
        if (speech("I didn't get your command. Please try again.")) {
            delayPromptSpeechInput(3000);
        }
    }

    public void handleProximity(JSONObject json) {

        try {
            JSONArray myArray = json.getJSONArray("items");
            String locations = "";


            if (myArray.length() != 0) {
                NavigationManager nvm = NavigationManager.getInstance();
                LatLng lastLocation = nvm.get_lastLocation();
                for (int i = 0; i < myArray.length(); i++) {
                    JSONObject object = myArray.getJSONObject(i);
                    LatLng itemLocation = new LatLng(object.getJSONObject("coordinate").getDouble("lat"), object.getJSONObject("coordinate").getDouble("lng"));

                    if (nvm.atWaypoint(lastLocation, itemLocation, 0.00005)) {
                        final String title = object.getString("title");
                        if (i == myArray.length()) {
                            locations += title + " and ";
                        } else if (i == 0) {
                            locations += title;
                        } else {
                            locations += ", " + title;
                        }
                    }
                }
            }
            if (locations == "") {
                locations = "There are no locations near you.";
            } else {
                speech("Around you is");
            }

            speech(locations);

        } catch (JSONException e) {
            e.getStackTrace();
        }

    }


    public void handleNavigation(JSONObject json, String said) {
        try {
            JSONArray myArray = json.getJSONArray("items");

            if (myArray.length() != 0) {
                for (int i = 0; i < myArray.length(); i++) {
                    JSONObject object = myArray.getJSONObject(i);

                    if (object.getString("title") == said) {
                        confirmNavigation(said);
                    } else {
                        // Zeg dat deze locatie niet bestaat en vraag opniuew
                    }
                }
            }

        } catch (JSONException e) {
            e.getStackTrace();
        }
    }


    public void speakTest() {
        if (savedLocation == "") {
            speech("You are at school");
        } else {
            speech("You are at " + savedLocation);
        }
    }

    public boolean speech(String speechText) {
        if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA) {
            Toast.makeText(_applicationContext, "Feature not supported", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            ttsobject.speak(speechText, TextToSpeech.QUEUE_ADD, null);
            return true;
        }
    }

    public void delayPromptSpeechInput(int delayAmount) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                promptSpeechInput();
            }
        }, delayAmount);
    }
}
