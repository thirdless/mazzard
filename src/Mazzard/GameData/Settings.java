package Mazzard.GameData;

import Mazzard.Components.BasicEnums;
import Mazzard.Components.Keyboard;
import Mazzard.Resources;

import java.awt.event.KeyEvent;
import java.sql.ResultSet;

public class Settings {
    public static final int NO_OF_OPTIONS = 4;

    public static final int SOUNDS_DEFAULT = 1;
    public static final int CONTROLS_DEFAULT = 0;
    public static final int FULLSCREEN_DEFAULT = 0;
    public static final int HITBOXES_DEFAULT = 0;

    private int sounds; // 0-OFF 1-ON
    private int controls; // 0-WASD 1-Arrows
    private int fullscreen; // 0-OFF 1-ON
    private int hitboxes; // 0-OFF 1-ON

    public Settings(){

    }

    private void setDefaults(){
        sounds = SOUNDS_DEFAULT;
        controls = CONTROLS_DEFAULT;
        fullscreen = FULLSCREEN_DEFAULT;
        hitboxes = HITBOXES_DEFAULT;
    }

    public void getSettingsFromDB(){
        ResultSet rs = Resources.getInstance().getDB().getSettings();

        if(rs == null){
            setDefaults();
            return;
        }

        try{
            sounds = rs.getInt("Sounds");
            controls = rs.getInt("Controls");
            fullscreen = rs.getInt("FullScreen");
            hitboxes = rs.getInt("Hitboxes");
        }
        catch(Exception e){
            DataBase.ErrorHandler(e);
        }
    }

    private void saveSettingsToDB(){
        Resources.getInstance().getDB().setSettings(sounds, controls, fullscreen, hitboxes);
    }

    private void changeControls(){
        if(controls == 0) controls = 1;
        else controls = 0;

        if(controls == 0){
            Keyboard.KEY_LEFT = KeyEvent.VK_A;
            Keyboard.KEY_RIGHT = KeyEvent.VK_D;
            Keyboard.KEY_UP = KeyEvent.VK_W;
            Keyboard.KEY_DOWN = KeyEvent.VK_S;
        }
        else{
            Keyboard.KEY_LEFT = KeyEvent.VK_LEFT;
            Keyboard.KEY_RIGHT = KeyEvent.VK_RIGHT;
            Keyboard.KEY_UP = KeyEvent.VK_UP;
            Keyboard.KEY_DOWN = KeyEvent.VK_DOWN;
        }
    }

    private void changeFullscreen(){
        if(fullscreen == 0) fullscreen = 1;
        else fullscreen = 0;

        saveSettingsToDB();

        System.exit(0);
    }

    public boolean getMuted(){
        if(sounds == 0) return true;
        return false;
    }

    public boolean getFullScreen(){
        if(fullscreen == 1) return true;
        return false;
    }

    public boolean getHitboxes(){
        if(hitboxes == 0) return false;
        return true;
    }

    public void toggleSetting(int type){
        switch(type){
            case 0:
                if(sounds == 0) sounds = 1;
                else sounds = 0;
                break;
            case 1:
                changeControls();
                break;
            case 2:
                changeFullscreen();
                break;
            case 3:
                if(hitboxes == 0) hitboxes = 1;
                else hitboxes = 0;
                break;
        }

        saveSettingsToDB();
    }

    private String generateString(BasicEnums type){
        String option;
        String name;

        switch(type){
            case SETTINGS_SOUNDS:
                if(sounds == 0) option = "OFF";
                else option = "ON";
                name = "Sounds";
                break;
            case SETTINGS_CONTROLS:
                if(controls == 0) option = "WASD";
                else option = "Arrows";
                name = "Controls";
                break;
            case SETTINGS_FULLSCREEN:
                if(fullscreen == 0) option = "OFF";
                else option = "ON";
                name = "Full-Screen";
                break;
            case SETTINGS_HITBOXES:
                if(hitboxes == 0) option = "OFF";
                else option = "ON";
                name = "Hitboxes";
                break;
            default:
                return "";
        }

        return name + ": " + option;
    }

    @Override
    public String toString() {
        return generateString(BasicEnums.SETTINGS_SOUNDS) +
                ";" + generateString(BasicEnums.SETTINGS_CONTROLS) +
                ";" + generateString(BasicEnums.SETTINGS_FULLSCREEN) +
                ";" + generateString(BasicEnums.SETTINGS_HITBOXES);
    }
}
