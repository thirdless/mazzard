package Mazzard.Maps;

import Mazzard.Components.Assets;
import Mazzard.Components.Camera;
import Mazzard.Components.DrawGraphics;
import Mazzard.Resources;

import javax.naming.TimeLimitExceededException;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {
    public static Tile[] tiles = new Tile[32];
    public static final Tile grass = new Tile(1, true, 0, 0);
    public static final Tile grassUpLeft = new Tile(2, true, 1, 0);
    public static final Tile grassUpRight = new Tile(3, true, 0, 1);
    public static final Tile dirt = new Tile(4, true, 2, 0);
    public static final Tile dirtUpLeft = new Tile(5, true, 3, 0);
    public static final Tile dirtUpRight = new Tile(6, true, 1, 1);
    public static final Tile slimeSurface = new Tile(7, false, 2, 1);
    public static final Tile slime = new Tile(8, false, 3, 1);
    public static final Tile lavaSurface = new Tile(9, false, 0, 2);
    public static final Tile lava = new Tile(10, false, 1, 2);
    public static final Tile grass2 = new Tile(11, true, 2, 2);
    public static final Tile grass2UpLeft = new Tile(12, true, 3, 2);
    public static final Tile grass2UpRight = new Tile(13, true, 2, 3);
    public static final Tile dirt2 = new Tile(14, true, 0, 3);
    public static final Tile dirt2UpLeft = new Tile(15, true, 1, 3);
    public static final Tile dirt2UpRight = new Tile(16, true, 3, 3);

    public static int TILE_WIDTH;
    public static int TILE_HEIGHT;

    public BufferedImage image = null;
    private int id;
    private boolean solid;

    public Tile(int id, boolean solid, int posx, int posy){
        Dimension dim = Resources.getInstance().getScreen();
        TILE_WIDTH = TILE_HEIGHT = (int)Math.ceil(dim.height / 10);

        this.id = id;
        this.solid = solid;
        image = Assets.crop(posx, posy);

        tiles[id] = this;
    }

    public void draw(Graphics g, int x, int y){
        DrawGraphics.drawImage(g, image, x, y, TILE_HEIGHT, TILE_WIDTH);
    }

    public boolean isSolid(){
        return solid;
    }

    public static Rectangle getBounds(float x, float y){
        return new Rectangle((int)(y * TILE_WIDTH), (int)(x * TILE_HEIGHT), TILE_WIDTH, TILE_HEIGHT);
    }

    public int getX(int x){
        return (int)(x * TILE_WIDTH);
    }

    public int getY(int y){
        return (int)(y * TILE_HEIGHT);
    }

    public boolean isDeadly(){
        if(id >= 7 && id <= 10) return true;
        return false;
    }
}
