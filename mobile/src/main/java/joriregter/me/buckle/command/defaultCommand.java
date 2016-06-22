package joriregter.me.buckle.command;

import java.util.ArrayList;

import joriregter.me.buckle.GetLocations;
import joriregter.me.buckle.VoiceManager;

/**
 * Created by Jonathan on 21-6-2016.
 */
public class defaultCommand implements CommandBehavior {

    private ArrayList _result;
    private VoiceManager _vc;

    public defaultCommand(VoiceManager vc){
        _vc = vc;
    }

    public void getResult(){
        _result = _vc.get_result();
    }

    public void command(){

        int commandCount = 0;

        for (Object r : _result) {
            String rText = r.toString();
            rText = rText.toLowerCase();

            if (rText.startsWith("bring me to")) {
                new GetLocations(_vc).execute("navigation", rText.split("bring me to the")[1]);
            } else if (rText.startsWith("where am i")) {
                _vc.speakTest();
            }
            else if (rText.startsWith("what is around me")) {
                new GetLocations(_vc).execute("proximity");
            }
            else if (rText.startsWith("save this location")) {
                _vc.changeCommandBehavior(new getLocationCommand(_vc));
                _vc.saveLocation();
            }
            else {
                commandCount++;
            }
        }

        if(commandCount == _result.size()){
            _vc.commandFailed();
        }
    }
}
