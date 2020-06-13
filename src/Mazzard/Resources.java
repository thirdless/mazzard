package Mazzard;

import Mazzard.Components.AudioHelper;
import Mazzard.Components.Camera;
import Mazzard.Components.Keyboard;
import Mazzard.GameData.Data;
import Mazzard.GameData.DataBase;
import Mazzard.GameData.Settings;
import Mazzard.Maps.Map;

import java.awt.*;
import java.io.InputStream;

public class Resources{
    private static Resources instance = null;
    private int screen_width;
    private int screen_height;
    private Game game;
    private Keyboard keyboard;
    private Map map;
    private Camera camera;
    private Settings settings;
    private AudioHelper audio;
    private DataBase db;

    private Resources(){}

    public static Resources getInstance(){
        if(instance == null) instance = new Resources();
        return instance;
    }

    public void init(Game game, Keyboard keyboard, Settings settings, AudioHelper audio, DataBase db){
        this.game = game;
        this.keyboard = keyboard;
        this.settings = settings;
        this.audio = audio;
        this.db = db;
    }

    public void setScreen(int w, int h){
        screen_height = h;
        screen_width = w;
    }

    public Game getGame(){
        return game;
    }

    public Dimension getScreen(){
        return new Dimension(screen_width, screen_height);
    }

    public void setCamera(Camera camera){
        this.camera = camera;
    }

    public Camera getCamera(){
        return camera;
    }

    public boolean getKey(int key){
        return keyboard.getKey(key);
    }

    public void detachAllKeys(){
        keyboard.detachAllKeys();
    }

    public String getPath(String file){
        return Resources.class.getResource("/").getPath() + file;
    }

    public InputStream getStream(String file){
        return Resources.class.getResourceAsStream("/" + file);
    }

    public void setMap(Map map){
        this.map = map;
    }

    public Map getMap(){
        return map;
    }

    public Settings getSettings(){
        return settings;
    }

    public AudioHelper getAudio(){
        return audio;
    }

    public DataBase getDB(){
        return db;
    }
}