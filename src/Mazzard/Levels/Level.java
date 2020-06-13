package Mazzard.Levels;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Level {
    public Dimension dim; //no of tiles
    public String mobs;
    public BufferedImage bg;
    public boolean whiteUI;
    public Dimension door;

    public abstract int[][] get();
}
