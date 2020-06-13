package Mazzard.Components;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

public class Keyboard implements KeyEventDispatcher {

    public static int KEY_RIGHT = KeyEvent.VK_D;
    public static int KEY_LEFT = KeyEvent.VK_A;
    public static int KEY_UP = KeyEvent.VK_W;
    public static int KEY_DOWN = KeyEvent.VK_S;

    private static final int MAX_KEYS = 256;
    private boolean[] keys;

    public Keyboard(){
        keys = new boolean[MAX_KEYS];
    }

    public boolean getKey(int keycode){
        return keys[keycode];
    }

    public void detachAllKeys(){
        for(int i = 0; i < MAX_KEYS; i++) keys[i] = false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        int id = e.getKeyCode();
        if(id > MAX_KEYS) return false;

        switch(e.getID()){
            case KeyEvent.KEY_PRESSED:
                keys[id] = true;
                break;
            case KeyEvent.KEY_RELEASED:
                keys[id] = false;
                break;
        }
        return true;
    }
}
