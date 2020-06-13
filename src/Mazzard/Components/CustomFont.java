package Mazzard.Components;

import Mazzard.Resources;
import java.awt.*;

public class CustomFont {
    private static Font font = null;

    public static void setFont(String file) {
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, Resources.getInstance().getStream(file));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void display(Graphics g, String text, float size, int posx, int posy, Color color, int style){
        font = font.deriveFont(style, size);
        g.setFont(font);
        g.setColor(color);
        g.drawString(text, posx, posy);
    }

    public static Dimension center(Graphics g, String text, float size, int style, Dimension sizes){
        font = font.deriveFont(style, size);
        FontMetrics metrics = g.getFontMetrics(font);
        int x = (sizes.width - metrics.stringWidth(text)) / 2;
        int y = (sizes.height - metrics.getHeight()) / 2 + metrics.getAscent();
        return new Dimension(x, y);
    }

    public static int width(Graphics g, String text, float size, int style){
        font = font.deriveFont(style, size);
        FontMetrics metrics = g.getFontMetrics(font);
        return metrics.stringWidth(text);
    }
}
