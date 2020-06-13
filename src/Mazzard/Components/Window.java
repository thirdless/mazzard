package Mazzard.Components;

import Mazzard.Resources;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;

public class Window {
    private JFrame      frame;
    private Canvas      canvas;
    private int         width;
    private int         height;

    public Canvas getCanvas(){
        return canvas;
    }

    public void build(String title, boolean fullscreen, int w, int h){
        if(frame != null) return;

        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        canvas = new Canvas();
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        if(fullscreen){
            width = device.getDisplayMode().getWidth();
            height = device.getDisplayMode().getHeight();
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setUndecorated(true);
        }
        else{
            width = w;
            height = h;
        }

        Resources.getInstance().setScreen(width, height);
        frame.setSize(width, height);
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setMaximumSize(new Dimension(width, height));
        canvas.setMinimumSize(new Dimension(width, height));
        canvas.setBackground(Color.BLACK);

        frame.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) { }

            @Override
            public void windowLostFocus(WindowEvent e) {
                Resources.getInstance().detachAllKeys();
            }
        });

        frame.add(canvas);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        try{
            String path = Resources.getInstance().getPath("mazzard.png");
            Toolkit kit = Toolkit.getDefaultToolkit();
            Image img = kit.createImage(path);
            frame.setIconImage(img);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
