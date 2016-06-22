package joriregter.me.buckle.command;

import android.speech.tts.TextToSpeech;

import java.util.ArrayList;

import joriregter.me.buckle.VoiceManager;

/**
 * Created by Jonathan on 21-6-2016.
 */
public class getLocationCommand implements CommandBehavior {

    private ArrayList _result;
    private VoiceManager _vc;

    public getLocationCommand(VoiceManager vc){
        _vc = vc;
    }

    public void getResult(){
        _result = _vc.get_result();
    }

    public void command(){
        String text = _result.get(0).toString();
        _vc.changeCommandBehavior(new confirmCommand(_vc));
        _vc.confirmLocation(text);
    }
}
