package Mazzard.Components;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class Assets {
    public static BufferedImage spritesheet = null;
    public static BufferedImage menubg;
    public static BufferedImage lvl1bg;
    public static BufferedImage lvl2bg;
    public static BufferedImage[] player;
    public static BufferedImage[] goblin;
    public static BufferedImage[] coin;
    public static BufferedImage[] hearts;
    public static BufferedImage[] wings;
    public static BufferedImage[] skeleton;
    public static BufferedImage[] mushroom;
    public static BufferedImage[] door;
    public static BufferedImage crown;
    public static BufferedImage healthBar;

    public static final int tileSize = 48;
    public static final int goblinSize = 150;
    public static final int coinSize = 32;
    public static final int wingsSize = 100;
    public static final int doorSize = 96;
    public static final Dimension heartSize = new Dimension(20, 18);
    public static final Dimension crownSize = new Dimension(294, 138);

    //width = number of images
    //height = number of images per line
    public static final Dimension PLAYER_COUNT = new Dimension(37, 8);
    public static final Dimension GOBLIN_COUNT = new Dimension(28, 8);
    public static final Dimension SKELETON_COUNT = new Dimension(24, 8);
    public static final Dimension MUSHROOM_COUNT = new Dimension(24, 8);
    public static final Dimension COIN_COUNT = new Dimension(8, 4);
    public static final Dimension HEARTS_COUNT = new Dimension(22, 5);
    public static final Dimension WINGS_COUNT = new Dimension(4, 2);
    public static final Dimension DOOR_COUNT = new Dimension(6, 3);

    public static void Init(){
        if(spritesheet != null) return;

        BufferedImage temp;
        try{
            spritesheet = ImageIO.read(Assets.class.getResource("/textures/spritesheet.png"));
            healthBar = ImageIO.read(Assets.class.getResource("/textures/healthbar.png"));
            crown = ImageIO.read(Assets.class.getResource("/textures/crown.png"));

            menubg = ImageIO.read(Assets.class.getResource("/textures/bgmenu.png"));
            lvl1bg = ImageIO.read(Assets.class.getResource("/textures/bg.png"));
            lvl2bg = ImageIO.read(Assets.class.getResource("/textures/bg2.png"));

            temp = ImageIO.read(Assets.class.getResource("/textures/HeavyBandit.png"));
            player = new BufferedImage[PLAYER_COUNT.width];
            for(int i = 0; i < PLAYER_COUNT.width; i++){
                player[i] = crop(temp, i % PLAYER_COUNT.height, i / PLAYER_COUNT.height, tileSize, tileSize);
            }

            temp = ImageIO.read(Assets.class.getResource("/textures/goblin.png"));
            goblin = new BufferedImage[GOBLIN_COUNT.width];
            for(int i = 0; i < GOBLIN_COUNT.width; i++){
                goblin[i] = crop(temp, i % GOBLIN_COUNT.height, i / GOBLIN_COUNT.height, goblinSize, goblinSize);
            }

            temp = ImageIO.read(Assets.class.getResource("/textures/coin.png"));
            coin = new BufferedImage[COIN_COUNT.width];
            for(int i = 0; i < COIN_COUNT.width; i++){
                coin[i] = crop(temp, i % COIN_COUNT.height, i / COIN_COUNT.height, coinSize, coinSize);
            }

            temp = ImageIO.read(Assets.class.getResource("/textures/hearts.png"));
            hearts = new BufferedImage[HEARTS_COUNT.width];
            for(int i = 0; i < HEARTS_COUNT.width; i++){
                hearts[i] = crop(temp, i % HEARTS_COUNT.height, i / HEARTS_COUNT.height, heartSize.width, heartSize.height);
            }

            temp = ImageIO.read(Assets.class.getResource("/textures/wings.png"));
            wings = new BufferedImage[WINGS_COUNT.width];
            for(int i = 0; i < WINGS_COUNT.width; i++){
                wings[i] = crop(temp, i % WINGS_COUNT.height, i / WINGS_COUNT.height, wingsSize, wingsSize);
            }

            temp = ImageIO.read(Assets.class.getResource("/textures/skeleton.png"));
            skeleton = new BufferedImage[SKELETON_COUNT.width];
            for(int i = 0; i < SKELETON_COUNT.width; i++){
                skeleton[i] = crop(temp, i % SKELETON_COUNT.height, i / SKELETON_COUNT.height, goblinSize, goblinSize);
            }

            temp = ImageIO.read(Assets.class.getResource("/textures/mushroom.png"));
            mushroom = new BufferedImage[MUSHROOM_COUNT.width];
            for(int i = 0; i < MUSHROOM_COUNT.width; i++){
                mushroom[i] = crop(temp, i % MUSHROOM_COUNT.height, i / MUSHROOM_COUNT.height, goblinSize, goblinSize);
            }

            temp = ImageIO.read(Assets.class.getResource("/textures/door.png"));
            door = new BufferedImage[DOOR_COUNT.width];
            for(int i = 0; i < DOOR_COUNT.width; i++){
                door[i] = crop(temp, i % DOOR_COUNT.height, i / DOOR_COUNT.height, doorSize, doorSize);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static BufferedImage crop(BufferedImage image, int x, int y, int width, int height){
        return image.getSubimage(x * width, y * height, width, height);
    }

    public static BufferedImage crop(int x, int y){
        return spritesheet.getSubimage(x * tileSize, y * tileSize, tileSize, tileSize);
    }
}
