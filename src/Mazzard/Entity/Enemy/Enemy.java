package Mazzard.Entity.Enemy;

import Mazzard.Components.BasicEnums;
import Mazzard.Entity.Entity;
import Mazzard.Entity.Item.Coin;
import Mazzard.Entity.Item.Heart;
import Mazzard.Entity.Item.Wings;
import Mazzard.Maps.Tile;

import java.awt.*;

public abstract class Enemy extends Entity {

    protected String mobName;
    private int alerted;
    private static final int ALERT_TIME = 625; // 625 frames ~= 10 secs

    private int reactionTime;
    private int reactionDelay;

    public Enemy(float x, float y, int width, int height, Rectangle bounds, Rectangle attackBounds, int life, int speed, int coins, int reactionTime){
        super(
                x,
                y,
                width,
                height,
                bounds,
                attackBounds,
                life,
                speed,
                coins
        );

        alerted = 0;
        this.reactionTime = reactionTime;
        reactionDelay = 0;
    }

    public String getName(){
        return mobName;
    }

    public void alertMob(boolean toRight){
        alerted = 1;
        lookingRight = toRight;
    }

    protected void alertedUpdate(){
        if(alerted != 0){
            if(alerted >= ALERT_TIME) alerted = 0;
            else alerted++;

            if(hurtFactor == 0 && reactionDelay == 0){
                if(lookingRight) xMove = speed;
                else xMove = -speed;
            }
        }
    }

    protected void dieFunc() {
        Rectangle bounds = getBounds();
        int rand1 = (int)(Math.random() * 100);
        int rand2 = (int)(Math.random() * 100);

        res.getMap().addItems(new Coin(bounds.x, bounds.y, coins));
        if(rand1 > 70) res.getMap().addItems(new Heart(bounds.x - 20, bounds.y));
        if(rand2 > 85) res.getMap().addItems(new Wings(bounds.x + 30, bounds.y));
    }

    protected abstract void updateState();

    protected void changeState(){
        if(attacking != 0){
            state = BasicEnums.ANIMATION_ATTACK;
        }
        else if(xMove != 0){
            state = BasicEnums.ANIMATION_RUN;
        }
        else{
            state = BasicEnums.ANIMATION_IDLE;
        }

        if(animState != state){
            updateState();
        }

    }

    public void resetReaction(){
        reactionDelay = 0;
    }

    public void increaseReaction(){
        reactionDelay++;

        if(reactionDelay >= reactionTime){
            attack();
            reactionDelay = 0;
        }
    }

    public void changeFacing(boolean facingRight){
        lookingRight = facingRight;
    }

    @Override
    protected void checkTileCollisions() {
        x += xMove;
        y += yMove;

        int temp_width = res.getMap().getMapWidth(),
                temp_height = res.getMap().getMapHeight();

        for(int i = 0; i < temp_height; i++) {
            for (int j = 0; j < temp_width; j++) {
                Tile tile = res.getMap().getTile(i, j);
                Rectangle tileBounds = Tile.getBounds(i, j);

                if (tile == null || !tile.isSolid()){
                    if(hurtFactor != 0) continue;

                    if(getLeftBottomBounds().intersects(tileBounds)){
                        x -= xMove;
                        xMove = 0;
                        lookingRight = true;
                    }
                    else if(getRightBottomBounds().intersects(tileBounds)){
                        x -= xMove;
                        xMove = 0;
                        lookingRight = false;
                    }

                    if(tile != null && tile.isDeadly()) checkDeadlyTile(tileBounds);
                    continue;
                }

                if (getBoundsBottom().intersects(tileBounds)) {
                    y -= yMove;
                    gravity = 0;
                    yMove = 0;
                }
                if (getBoundsLeft().intersects(tileBounds)) {
                    x -= xMove;
                    xMove = 0;
                    lookingRight = true;
                }
                if (getBoundsRight().intersects(tileBounds)) {
                    x -= xMove;
                    xMove = 0;
                    lookingRight = false;
                }
            }
        }

        if(getBounds().x < 0 || getBounds().x > res.getMap().getMapWidth() * (Tile.TILE_WIDTH - 1)){
            x -= xMove;
            xMove = 0;
            lookingRight = !lookingRight;
        }
    }

    @Override
    public void update() {
        if(life == 0){
            super.update();
            return;
        }

        yMove = 0;
        xMove = 0;

        alertedUpdate();
        hurtUpdate();
        updateGravity();
        checkTileCollisions();

        attackUpdate();
        changeState();
        super.update();
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
    }
}
