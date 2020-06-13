package Mazzard.Entity;

import Mazzard.Components.*;
import Mazzard.Entity.Item.Item;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Hero extends Entity{

    private static final int WINGS_TIME = 625; // ~10 sec

    protected Rectangle eyesightBounds;
    protected int flying;

    public Hero(float x, float y, float factor, int life, int coins){
        super(
                x,
                y,
                (int)(Entity.DEFAULT_WIDTH * factor),
                (int)(Entity.DEFAULT_HEIGHT * factor),
                new Rectangle((int)(20 * factor), (int)(38 * factor)),
                new Rectangle((int)(48 * factor), (int)(48 * factor)),
                life == 0 ? 3 : life,
                5,
                coins
        );

        image = Assets.player[4];
        animTime = 5;
        animFrames = Assets.player;
        animRange = new Range(0, 3);
        animLoop = true;

        eyesightBounds = new Rectangle((int)(200 * factor), (int)(50 * factor));
        flying = 0;
    }

    private void setWings(float factor){
        GRAVITY_REDUCTION = factor;
        flying = 1;
    }

    public boolean getFlying(){
        if(flying != 0) return true;
        return false;
    }

    private void updateWings(){
        if(flying != 0){
            if(flying >= WINGS_TIME){
                flying = 0;
                GRAVITY_REDUCTION = DEFAULT_GRAVITY;
            }
            else flying++;
        }
    }

    private void addHearts(int hearts){
        life += hearts;
        life = Math.min(life, 3);
    }

    public void addItem(Item item){
        if(item.getCoins() != 0) coins += item.getCoins();
        if(item.getHearts() != 0) addHearts(item.getHearts());
        if(item.getWings() != 0) setWings(item.getWings());
    }

    private void jump(){
        if(gravity == 0 && attacking == 0){
            gravity = -8;
            audio.playJump();
        }
    }

    @Override
    protected void dieFunc() {
        changeAnimation(7, null, new Range(32, 36), false);
        res.getGame().playFailed();
    }

    private void inputs(){
        if(life != 0){
            if(res.getKey(Keyboard.KEY_UP)){
                jump();
            }
            if(res.getKey(Keyboard.KEY_LEFT)){
                lookingRight = false;
                xMove = -speed;
            }
            if(res.getKey(Keyboard.KEY_RIGHT)){
                lookingRight = true;
                xMove = speed;
            }
            if(res.getKey(KeyEvent.VK_SPACE)){
                attack();
            }
        }
    }

    protected void changeState(){
        if(attacking != 0){
            state = BasicEnums.ANIMATION_ATTACK;
        }
        else if((int)gravity != 0){
            state = BasicEnums.ANIMATION_JUMP;
        }
        else if(xMove != 0){
            state = BasicEnums.ANIMATION_RUN;
        }
        else{
            state = BasicEnums.ANIMATION_IDLE;
        }

        if(animState != state){
            Range tempRange;

            switch(state){
                case ANIMATION_ATTACK:
                    tempRange = new Range(16, 23);
                    break;
                case ANIMATION_JUMP:
                    tempRange = new Range(35, 35);
                    break;
                case ANIMATION_RUN:
                    tempRange = new Range(8, 15);
                    break;
                default:
                    tempRange = new Range(0, 3);

            }

            changeAnimation(5, null, tempRange, true);
            animState = state;
        }
    }

    public Rectangle getEyesight(){
        return new Rectangle(
                (int)(x + ((width - eyesightBounds.width) / WIDTH_FACTOR)),
                (int)(y + ((height - eyesightBounds.height) / HEIGHT_FACTOR)),
                eyesightBounds.width,
                eyesightBounds.height
        );
    }

    @Override
    public void update() {
        xMove = 0;
        yMove = 0;

        inputs();
        hurtUpdate();
        updateGravity();
        res.getCamera().center(this);
        checkTileCollisions();

        if(life != 0){
            updateWings();
            attackUpdate();
            changeState();
        }

        super.update();
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
    }
}
