package Mazzard.Components;

import Mazzard.Resources;

import java.awt.*;

public class DrawGraphics {
    public static void drawImage(Graphics g, Image img, int x, int y, int width, int height){
        Dimension offset = Resources.getInstance().getCamera().getOffset();
        g.drawImage(img, x - offset.width, y - offset.height, width, height, null);
    }

    public static void drawRect(Graphics g, Rectangle rect){
        Dimension offset = Resources.getInstance().getCamera().getOffset();
        g.drawRect(rect.x - offset.width, rect.y - offset.height, rect.width, rect.height);
    }
}
