package Mazzard.Entity;

import Mazzard.Components.*;
import Mazzard.Maps.Tile;
import Mazzard.Resources;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Entity {
    public static final int DEFAULT_WIDTH = 48;
    public static final int DEFAULT_HEIGHT = 48;
    public static final int HURT_TIME = 60;
    public static final int BOUND_SIZE = 5;
    public static final double DEFAULT_GRAVITY = 0.3;

    protected double GRAVITY_REDUCTION = DEFAULT_GRAVITY;
    protected double HEIGHT_FACTOR = 1.8;
    protected double WIDTH_FACTOR = 2;
    protected float HURT_SPEED;

    protected float x;
    protected float y;
    protected int width;
    protected int height;
    protected Rectangle bounds;
    protected Rectangle attackBounds;

    protected BufferedImage image;
    protected BufferedImage[] animFrames;
    protected int animTime;
    protected int animCounter;
    protected Range animRange;
    protected boolean animLoop;
    protected boolean animFinished;
    protected BasicEnums animState;

    protected int coins;
    protected int life;
    protected float speed;
    protected float xMove;
    protected float yMove;
    protected float gravity;
    protected boolean lookingRight;
    protected int attacking;
    protected int hurtFactor;
    protected BasicEnums state;

    protected Resources res;
    protected AudioHelper audio;

    public Entity(float x, float y, int width, int height, Rectangle bounds, Rectangle attackBounds, int life, int speed, int coins){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bounds = bounds;
        this.attackBounds = attackBounds;

        this.coins = coins;
        this.life = life;
        this.speed = speed;
        xMove = 0;
        yMove = 0;
        gravity = 0;
        attacking = 0;
        hurtFactor = 0;
        lookingRight = true;

        res = Resources.getInstance();
        audio = res.getAudio();
        initAnimation();

        HURT_SPEED = 6 * DEFAULT_HEIGHT / (float)getBounds().height;
    }

    private void initAnimation(){
        animFinished = false;
        animCounter = 0;
    }

    protected abstract void changeState();

    protected void checkDeadly(Tile tile){

    }

    public void update(){
        updateAnimation();
    }

    public void updateAnimation() {
        if(animFinished) return;

        try{
            if(animCounter >= animTime){
                if(animRange.index + 1 > animRange.finish) {
                    if(animLoop) animRange.index = animRange.start;
                    else stopAnimation();
                }
                else animRange.index++;

                image = animFrames[animRange.index];
                animCounter = 0;
            }
            else animCounter++;
        }
        catch(Exception e){
            stopAnimation();
            e.printStackTrace();
        }
    }

    public void changeAnimation(int time, BufferedImage frames[], Range range, boolean loop){
        initAnimation();
        animTime = time;
        if(frames != null) animFrames = frames;
        animRange = range;
        animLoop = loop;
    }

    public void stopAnimation(){
        animFinished = true;
    }

    public void draw(Graphics g){
        int xtemp = (int)x,
            tempwidth = width;

        if(lookingRight){
            xtemp += width;
            tempwidth = -width;
        }

        if(hurtFactor % (HURT_TIME / 5) == 2){
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f);
            ((Graphics2D)g).setComposite(ac);

            DrawGraphics.drawImage(g, image, xtemp, (int)y, tempwidth, height);

            ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1);
            ((Graphics2D)g).setComposite(ac);
        }
        else DrawGraphics.drawImage(g, image, xtemp, (int)y, tempwidth, height);

        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1);
        ((Graphics2D)g).setComposite(ac);

        if(res.getSettings().getHitboxes()) drawHitboxes(g);
    }

    private void drawHitboxes(Graphics g){
        g.setColor(Color.RED);
        Rectangle ceva = getBounds();
        DrawGraphics.drawRect(g, ceva);

        g.setColor(Color.BLUE);
        Rectangle ceva100 = getAttackBounds();
        DrawGraphics.drawRect(g, ceva100);

        Rectangle ceva1 = getBoundsTop();
        g.setColor(Color.MAGENTA);
        DrawGraphics.drawRect(g, ceva1);
        Rectangle ceva2 = getBoundsBottom();
        g.setColor(Color.CYAN);
        DrawGraphics.drawRect(g, ceva2);
        Rectangle ceva3 = getBoundsLeft();
        g.setColor(Color.GREEN);
        DrawGraphics.drawRect(g, ceva3);
        Rectangle ceva4 = getBoundsRight();
        g.setColor(Color.ORANGE);
        DrawGraphics.drawRect(g, ceva4);
        Rectangle ceva5 = getLeftBottomBounds();
        g.setColor(Color.MAGENTA);
        DrawGraphics.drawRect(g, ceva5);
        Rectangle ceva6 = getRightBottomBounds();
        DrawGraphics.drawRect(g, ceva6);
    }

    public int getCoins(){
        return coins;
    }
    public int getLife(){ return life; }
    public float getX(){ return x; }
    public float getY(){ return y; }
    public float getWidth(){ return width; }
    public float getHeight(){ return height; }

    protected abstract void dieFunc();

    private void die(){
        if(life == 0) return;
        life = 0;
        dieFunc();
    }

    public void hurt(boolean toRight){
        if(hurtFactor != 0 || life == 0) return;

        hurtFactor = 1;
        if(life - 1 == 0){
            die();
        }
        else{
            life--;
            gravity = -HURT_SPEED;
            if(toRight) lookingRight = false;
            else lookingRight = true;
        }
    }

    protected void hurtUpdate(){
        if(hurtFactor != 0){
            if(hurtFactor >= HURT_TIME) hurtFactor = 0;
            else hurtFactor++;

            if(hurtFactor <= HURT_TIME / 2 && gravity != 0){
                if(lookingRight) xMove -= HURT_SPEED / 2;
                else xMove = HURT_SPEED / 2;
            }
        }
    }

    protected void attack(){
        if(attacking == 0 && state != BasicEnums.ANIMATION_RUN) attacking = 1;
    }

    protected void attackUpdate(){
        if(attacking != 0){
            if(attacking >= 50) attacking = 0;
            else attacking++;

            if(attacking == 20) audio.playSwing();
            if(gravity == 0) x -= xMove;
        }
    }

    public boolean getAttacking(){
        if(attacking >= 20 && attacking <= 40) return true;
        return false;
    }

    public void updateGravity(){
       if(gravity < 0){
           gravity += GRAVITY_REDUCTION;
           yMove = gravity;
           if(gravity >= 0){
               gravity = 0.1f;
           }
       }
       if(gravity >= 0){
           gravity += GRAVITY_REDUCTION;
           yMove = gravity;
       }
    }

    protected void checkDeadlyTile(Rectangle bound){
        if(getLeftBottomBounds().intersects(bound) && getRightBottomBounds().intersects(bound)){
            die();
        }
    }

    protected void checkTileCollisions(){
        x += xMove;
        y += yMove;

        int temp_width = res.getMap().getMapWidth(),
            temp_height = res.getMap().getMapHeight();

        for(int i = 0; i < temp_height; i++) {
            for (int j = 0; j < temp_width; j++) {
                Tile tile = res.getMap().getTile(i, j);
                Rectangle tileBounds = Tile.getBounds(i, j);

                if (tile == null || !tile.isSolid()){
                    if(tile != null && tile.isDeadly()) checkDeadlyTile(tileBounds);
                    continue;
                }

                if (getBoundsTop().intersects(tileBounds)) {
                    y -= yMove;
                    gravity = 0.1f;
                    yMove = 0;
                }
                if (getBoundsBottom().intersects(tileBounds)) {
                    y -= yMove;
                    gravity = 0;
                    yMove = 0;
                }
                if (getBoundsLeft().intersects(tileBounds)) {
                    x -= xMove;
                    xMove = 0;
                }
                if (getBoundsRight().intersects(tileBounds)) {
                    x -= xMove;
                    xMove = 0;
                }
            }
        }

        Rectangle boundsTemp = getBounds();
        int cameraTemp = res.getCamera().getOffset().width;

        if(boundsTemp.x < cameraTemp || boundsTemp.x > res.getScreen().width + cameraTemp - boundsTemp.width){
            x -= xMove;
            xMove = 0;
        }
    }

    private Rectangle getRectangle(Rectangle bound, int rectwidth, int rectheight, int horizontal, int vertical){
        return new Rectangle(
                (int)(x + ((width - bound.width) / WIDTH_FACTOR)) + horizontal,
                (int)(y + ((height - bound.height) / HEIGHT_FACTOR)) + vertical,
                rectwidth,
                rectheight
        );
    }

    public Rectangle getBounds(){
        return getRectangle(bounds, bounds.width, bounds.height, 0, 0);
    }

    public Rectangle getAttackBounds(){
        return getRectangle(attackBounds, attackBounds.width, attackBounds.height, 0, 0);
    }

    protected Rectangle getBoundsTop(){
        return getRectangle(bounds, bounds.width, BOUND_SIZE, 0, 0);
    }

    protected Rectangle getBoundsBottom(){
        return getRectangle(bounds, bounds.width, BOUND_SIZE, 0, bounds.height - BOUND_SIZE);
    }

    protected Rectangle getBoundsLeft(){
        return getRectangle(bounds, BOUND_SIZE, bounds.height - 2 * BOUND_SIZE, -BOUND_SIZE, BOUND_SIZE);
    }

    protected Rectangle getBoundsRight(){
        return getRectangle(bounds, BOUND_SIZE, bounds.height - 2 * BOUND_SIZE, bounds.width, BOUND_SIZE);
    }

    protected Rectangle getLeftBottomBounds(){
        return getRectangle(bounds, BOUND_SIZE, BOUND_SIZE, -BOUND_SIZE, bounds.height);
    }

    protected Rectangle getRightBottomBounds(){
        return getRectangle(bounds, BOUND_SIZE, BOUND_SIZE, bounds.width, bounds.height);
    }

    public boolean getLookingRight(){
        return lookingRight;
    }

}
