package Mazzard;

import Mazzard.Components.*;
import Mazzard.Components.Window;
import Mazzard.GameData.Data;
import Mazzard.GameData.DataBase;
import Mazzard.GameData.Settings;
import Mazzard.Screens.MenuScreen;
import Mazzard.Screens.PauseScreen;
import Mazzard.Screens.PlayScreen;
import Mazzard.Screens.Screen;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game implements Runnable{
    private Thread thread;
    private Window win;
    private boolean runState;
    private Keyboard keyboard;
    private Settings settings;
    private AudioHelper audio;
    private DataBase db;
    private Resources res;
    private BufferStrategy bfs;
    private Graphics2D g2d;

    private PlayScreen play;
    private MenuScreen menu;
    private PauseScreen pause;

    public Game(String title, int width, int height){
        win = new Window();
        runState = false;
        keyboard = new Keyboard();
        res = Resources.getInstance();
        db = new DataBase("mazzard.db");
        audio = new AudioHelper();
        settings = new Settings();
        res.init(this, keyboard, settings, audio, db);
        CustomFont.setFont("font.ttf");

        settings.getSettingsFromDB();
        win.build(title, settings.getFullScreen(), width, height);

        Assets.Init();
        menu = new MenuScreen(title.split(": "));
        pause = new PauseScreen();

        if(res.getDB().checkLastSave()) menu.setContinueGame(true);
    }

    public synchronized void start(){
        if(!runState){
            try{
                win.getCanvas().createBufferStrategy(3);
                bfs = win.getCanvas().getBufferStrategy();
            }
            catch(Exception e){
                e.printStackTrace();
            }

            Screen.set(menu);

            runState = true;
            thread = new Thread(this);
            thread.start();
        }
    }

    public synchronized void stop(){
        if(runState){
            try{
                runState = false;
                res.getDB().disconnect();
                thread.join();
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void run(){
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyboard);
        long old = System.nanoTime();
        long current;

        final double fps = 1000000000 / 60;

        while(runState){
            current = System.nanoTime();
            if(current - old > fps){
                update();
                draw(old, current);

                old = current;
            }
        }
    }

    public void playGame(boolean continueFromSave){
        play = new PlayScreen(continueFromSave);
        unpauseGame();
    }

    public void unpauseGame(){
        audio.backgroundStart();
        keyboard.detachAllKeys();
        Screen.set(play);
    }

    public void pauseGame(){
        pause.reset();
        audio.backgroundPause();
        keyboard.detachAllKeys();
        Screen.set(pause);
    }

    public void mainMenu(){
        if(res.getDB().checkLastSave()) menu.setContinueGame(true);
        audio.resetBackground();
        keyboard.detachAllKeys();
        Screen.set(menu);
    }

    private void update(){
        Screen.get().update();
    }

    private void draw(long old, long current)
    {
        Dimension screen = res.getScreen();
        g2d = (Graphics2D)bfs.getDrawGraphics();
        g2d.clearRect(0, 0, screen.width, screen.height);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        // draw

        Screen.get().draw(g2d);


        bfs.show();
        g2d.dispose();
    }

    public void playFailed(){
        play.failed();
    }

    public void playNextLevel(Data data){
        play.nextLevel(data);
    }

    public BasicEnums getPlayState(){
        return play.getState();
    }
}
