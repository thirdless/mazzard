package Mazzard.Maps;

import Mazzard.Components.Assets;
import Mazzard.Components.DrawGraphics;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Door {
    private boolean opened;
    private int x;
    private int y;
    private int height;
    private int width;
    private BufferedImage[] frames;
    private BufferedImage image;
    private int currentFrame;

    public Door(int x, int y){
        this.x = x;
        this.y = y;
        height = Assets.tileSize * 2;
        width = Assets.tileSize * 2;

        image = Assets.door[0];
        frames = Assets.door;
        currentFrame = 0;
        opened = false;
    }

    public void open(){
        if(currentFrame == 0){
            opened = true;
            currentFrame = 1;
        }
    }

    public boolean isOpened(){
        return opened;
    }

    public Rectangle getBounds(){
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics g){
        image = frames[currentFrame / 5];
        if(currentFrame != 0 && currentFrame < 26) currentFrame++;
        DrawGraphics.drawImage(g, image, x, y, width, height);
    }
}
