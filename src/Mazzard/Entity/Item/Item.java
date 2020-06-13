package Mazzard.Entity.Item;

import Mazzard.Entity.Entity;

import java.awt.*;

public abstract class Item extends Entity {

    private static final int PROTECTED_MAX = 60;

    protected int hearts = 0;
    protected float wings = 0;
    protected int protectedTime = 0;

    public Item(float x, float y, int width, int height, Rectangle bounds, int coins){
        super(
                x,
                y,
                width,
                height,
                bounds,
                new Rectangle(0, 0),
                0,
                0,
                coins
        );

        gravity = -3;
        HEIGHT_FACTOR = 1.2;
    }

    public int getHearts(){
        return hearts;
    }

    public float getWings(){
        return wings;
    }

    public boolean canTake(){
        if(protectedTime == PROTECTED_MAX) return true;
        return false;
    }

    private void updateProtected(){
        protectedTime++;
        if(protectedTime > PROTECTED_MAX) protectedTime = PROTECTED_MAX;
    }

    @Override
    protected void changeState() {

    }

    @Override
    protected void dieFunc() {

    }

    @Override
    public void update() {
        xMove = 0;
        yMove = 0;

        updateGravity();
        checkTileCollisions();

        super.update();
        updateProtected();
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
    }
}
