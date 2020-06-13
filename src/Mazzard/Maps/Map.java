package Mazzard.Maps;

import Mazzard.Components.PlayerUI;
import Mazzard.Entity.Enemy.Enemy;
import Mazzard.Entity.Enemy.Goblin;
import Mazzard.Entity.Enemy.Mushroom;
import Mazzard.Entity.Enemy.Skeleton;
import Mazzard.Entity.Entity;
import Mazzard.Entity.Hero;
import Mazzard.Entity.Item.Item;
import Mazzard.GameData.Data;
import Mazzard.Levels.Level;
import Mazzard.Levels.LevelFactory;
import Mazzard.Resources;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Map {
    private static final int SCORE_CONSTANT = 3750 * 1000; /// ~60 secs = 1000 points
    private int time;
    private Level level;
    private int lvlNo;
    private int score;
    private int coins;

    private int[][] tiles;
    private int width;
    private int height;

    private Door door;
    private Hero hero;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();
    private PlayerUI ui;

    public Map(int level){
        load(level, null);
    }

    public Map(Data data){
        load(data.level, data);
    }

    private void load(int number, Data data){
        String mobs;
        coins = 0;
        score = 0;
        time = 0;

        level = LevelFactory.get(number);
        if(level == null) System.exit(0);

        tiles = level.get();
        width = level.dim.width;
        height = level.dim.height;
        lvlNo = number;

        if(data == null){
            mobs = level.mobs;
        }
        else{
            if(data.mobs != null) mobs = data.mobs;
            else mobs = level.mobs;
            coins = data.coins;
            score = data.score;
        }

        door = new Door(level.door.width, level.door.height);
        generateMobs(mobs);
        if(hero == null || door == null) System.exit(0);
    }

    private void generateMobs(String mobs){
        String[] split = mobs.split(";");

        for(int i = 0; i < split.length; i++){
            parseMob(split[i]);
        }
    }

    private void parseMob(String mob){
        try{
            String[] split = mob.split(":");
            String[] pos = split[1].split(",");

            float posx = Float.parseFloat(pos[0]),
                    posy = Float.parseFloat(pos[1]);
            int lives = Integer.parseInt(pos[2]);

            switch(split[0]){
                case "Hero":
                    if(hero == null) hero = new Hero(posx, posy, 2, lives, coins);
                    ui = new PlayerUI(hero);
                    break;
                case "Skeleton":
                    enemies.add(new Skeleton(posx, posy, 2, lives));
                    break;
                case "Mushroom":
                    enemies.add(new Mushroom(posx, posy, 2, lives));
                    break;
                case "Goblin":
                    enemies.add(new Goblin(posx, posy, 1.5f, lives));
                    break;
            }
        }
        catch(Exception e){
            System.out.println("corrupt save");
            e.printStackTrace();
        }
    }

    public Data generateSave(){
        Data data = new Data();
        data.coins = hero.getCoins();
        data.score = score + (SCORE_CONSTANT / time) * data.coins;
        data.level = lvlNo;
        data.mobs = generateSaveString();

        return data;
    }

    private String generateSaveString(){
        String string = "";

        string += "Hero:" + hero.getX() + "," + hero.getY() + "," + hero.getLife() + ";";

        for(Enemy enemy : enemies){
            if(enemy.getLife() == 0) continue;
            string += enemy.getName() + ":" + enemy.getX() + "," + enemy.getY() + "," + enemy.getLife() + ";";
        }

        return string;
    }

    private void damageEntity(Entity from, Entity to){
        boolean enemyRight = false;
        if(from.getBounds().x < to.getBounds().x) enemyRight = true;

        if(from.getLookingRight() == enemyRight) to.hurt(enemyRight);
    }

    private void checkEnemy(Enemy enemy){
        if(hero.getEyesight().intersects(enemy.getBounds())){
            if(hero.getBounds().x < enemy.getBounds().x) enemy.alertMob(false);
            else enemy.alertMob(true);
        }

        if(hero.getAttacking() && hero.getAttackBounds().intersects(enemy.getBounds())){
            damageEntity(hero, enemy);
        }

        if(enemy.getAttacking() && enemy.getAttackBounds().intersects(hero.getBounds())){
            damageEntity(enemy, hero);
        }

        if(hero.getBounds().intersects(enemy.getAttackBounds())){
            if(hero.getBounds().x < enemy.getBounds().x) enemy.changeFacing(false);
            else enemy.changeFacing(true);

            enemy.increaseReaction();
        }
        else{
            enemy.resetReaction();
        }
    }

    private void checkItems(Item item, Iterator it){
        if(hero.getBounds().intersects(item.getBounds()) && item.canTake()){
            hero.addItem(item);
            it.remove();
        }
    }

    public void update(){
        time++;
        boolean openDoor = true;

        hero.update();
        ui.update();

        for(Enemy enemy : enemies){
            enemy.update();
            if(enemy.getLife() != 0 && hero.getLife() != 0){
                checkEnemy(enemy);
                openDoor = false;
            }
        }

        for (var it = items.iterator(); it.hasNext();) {
            Item item = it.next();
            item.update();
            if (hero.getLife() != 0) checkItems(item, it);
        }

        if(openDoor) door.open();

        if(door.isOpened() && hero.getBounds().intersects(door.getBounds())){
            Resources.getInstance().getGame().playNextLevel(generateSave());
        }
    }

    public void addItems(Item item){
        items.add(item);
    }

    public void setUIColor(){
        if(level.whiteUI) ui.setColor(Color.WHITE);
        else ui.setColor(Color.BLACK);
    }

    private void drawBg(Graphics g){
        Dimension screen = Resources.getInstance().getScreen();
        g.drawImage(level.bg, 0, 0, screen.width, screen.height, null);
    }

    private void drawTiles(Graphics g){
        Tile tile;
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                tile = getTile(i, j);
                if(tile != null){
                    tile.draw(g, (int)(j * Tile.TILE_WIDTH), (int)(i * Tile.TILE_HEIGHT));
                }
            }
        }
    }

    public void draw(Graphics g){
        drawBg(g);
        drawTiles(g);

        door.draw(g);

        for(Enemy enemy : enemies){
            enemy.draw(g);
        }

        for(Item item : items){
            item.draw(g);
        }

        hero.draw(g);
        ui.draw(g);
    }

    public Tile getTile(int x, int y){
        if(x < 0 || y < 0 || x >= height || y >= width || tiles[x][y] == 0){
            return null;
        }
        return Tile.tiles[tiles[x][y]];
    }

    public int getMapWidth(){
        return width;
    }

    public int getMapHeight(){
        return height;
    }
}
