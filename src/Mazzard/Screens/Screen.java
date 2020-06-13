package Mazzard.Screens;

import Mazzard.Resources;

import java.awt.*;

public abstract class Screen {

    private static Screen state = null;

    public static Screen get(){
        return state;
    }

    public static void set(Screen param){
        state = param;
    }

    public abstract void update();
    public abstract void draw(Graphics g);

    protected void exit(){
        System.exit(0);
    }

}
