package joriregter.me.buckle.command;

import java.util.ArrayList;

import joriregter.me.buckle.VoiceManager;

/**
 * Created by Jonathan on 21-6-2016.
 */
public class confirmCommand implements CommandBehavior {

    private ArrayList _result;
    private VoiceManager _vc;

    public confirmCommand(VoiceManager vc){
        _vc = vc;
    }

    public void getResult(){
        _result = _vc.get_result();
    }

    public void command(){

        boolean succes = false;
        int commandCount = 0;

        for (Object r : _result) {
            if(succes == false){
                String rText = r.toString();
                rText = rText.toLowerCase();
                if (rText.startsWith("yes")){
                    succes = true;
                    _vc.changeCommandBehavior(new defaultCommand(_vc));
                    _vc.succesfullSaveLocation();
                } else if (rText.startsWith("no")) {
                    _vc.changeCommandBehavior(new getLocationCommand(_vc));
                    _vc.saveLocation();
                } else {
                    commandCount++;
                }
            }
        }

        if(commandCount == _result.size()){
            _vc.commandFailed();
        }
    }
}