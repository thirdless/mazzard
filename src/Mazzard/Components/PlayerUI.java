package Mazzard.Components;

import Mazzard.Entity.Hero;
import Mazzard.Resources;

import java.awt.*;

public class PlayerUI {
    private static final Dimension heartSize = new Dimension(22, 19);
    private static final Dimension healthBarSize = new Dimension(154, 62);
    private static final int wingSize = 50;
    private static final int coinValueSize = 30;

    private final int coinPos;

    private Dimension screen;
    private Hero hero;
    private Color color;

    public PlayerUI(Hero hero){
        this.hero = hero;
        screen = Resources.getInstance().getScreen();
        color = Color.WHITE;

        coinPos = (int)(screen.width - Assets.coinSize * 2.5);
    }

    public void update(){

    }

    public void setColor(Color color){
        this.color = color;
    }

    public void draw(Graphics g2d){
        int startX = 20;
        int startY = 20;
        g2d.drawImage(Assets.healthBar, startX, startY, healthBarSize.width, healthBarSize.height, null);
        startX += 40;
        startY += 22;

        for(int i = 0; i < hero.getLife(); i++){
            g2d.drawImage(Assets.hearts[0], startX, startY, heartSize.width, heartSize.height, null);
            startX += 25;
        }

        if(hero.getFlying()) g2d.drawImage(Assets.wings[0], 180, 30, wingSize, wingSize, null);

        String coinValue = String.valueOf(hero.getCoins());
        int coinWidth = CustomFont.width(g2d, coinValue, coinValueSize, 0);

        g2d.drawImage(Assets.coin[0], coinPos, 25, Assets.coinSize * 2, Assets.coinSize * 2, null);
        CustomFont.display(g2d, coinValue, coinValueSize, coinPos - coinWidth, 65, color, 0);
    }
}