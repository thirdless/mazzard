package Mazzard.Screens;

import Mazzard.Components.*;
import Mazzard.Entity.Enemy.Goblin;
import Mazzard.Entity.Hero;
import Mazzard.GameData.Data;
import Mazzard.Maps.Map;
import Mazzard.Resources;

import java.awt.*;
import java.awt.event.KeyEvent;

import static Mazzard.Components.BasicEnums.PLAYSTATE_FAILED;
import static Mazzard.Components.BasicEnums.PLAYSTATE_PLAYING;

public class PlayScreen extends Screen {
    private static final int TITLE_SIZE = 60;
    private static final int TEXT_SIZE = 30;
    private static final String FAILED_TEXT = "You FAILED!";
    private static final String WON_TEXT = "You WON!";
    private static final String SCORE_TEXT = "Score: ";
    private static final String BACK_TEXT = "Press [ESC] to go back to the menu";
    private static final String NEXT_TEXT = "Press [ENTER] to get to the next level";

    private Resources res;
    private Dimension screen;
    private Map map;
    private BasicEnums state;
    private Data tempData;

    private String scoreText = "";
    private String coinsText = "";
    private String levelText = "";

    public PlayScreen(boolean continueFromSave){
        res = Resources.getInstance();
        screen = res.getScreen();
        state = BasicEnums.PLAYSTATE_PLAYING;

        if(!continueFromSave) map = new Map(1);
        else{
            map = new Map(res.getDB().getLastSave());
        }

        res.setCamera(new Camera());
        res.setMap(map);
    }

    private void handleKeys(){
        switch(state){
            case PLAYSTATE_PLAYING:
                if(res.getKey(KeyEvent.VK_ESCAPE)){
                    res.getGame().pauseGame();
                }
                break;
            case PLAYSTATE_FAILED:
            case PLAYSTATE_WON:
                if(res.getKey(KeyEvent.VK_ESCAPE)){
                    res.getGame().mainMenu();
                    resetState();
                }
                break;
            case PLAYSTATE_PASSED:
                if(res.getKey(KeyEvent.VK_ENTER)){
                    generateNextLevel(tempData);
                    resetState();
                }
                break;
        }

    }

    @Override
    public void update() {
        handleKeys();
        map.update();
    }

    public void setState(BasicEnums type){
        state = type;
    }

    public BasicEnums getState(){
        return state;
    }

    private void setInfo(int level, int coins, int score){
        coinsText = "   " + String.valueOf(coins);
        scoreText = String.valueOf(score);

        switch(level){
            case 1:
                levelText = level + "st";
                break;
            case 2:
                levelText = level + "nd";
                break;
            case 3:
                levelText = level + "rd";
                break;
            default:
                levelText = level + "th";
        }
    }

    private void resetState(){
        state = BasicEnums.PLAYSTATE_PLAYING;
    }

    public void nextLevel(Data data){
        if(state != PLAYSTATE_PLAYING) return;
        tempData = data;
        if(data.level == 2) state = BasicEnums.PLAYSTATE_WON;
        else state = BasicEnums.PLAYSTATE_PASSED;

        setInfo(data.level, data.coins, data.score);
    }

    private void generateNextLevel(Data data){
        data.level++;
        data.mobs = null;
        map = new Map(data);
        res.setMap(map);
    }

    public void failed(){
        state = PLAYSTATE_FAILED;
    }

    private void drawGeneric(Graphics g2d, String title, String bottom){
        Dimension center = CustomFont.center(g2d, title, TITLE_SIZE, 0, screen);
        CustomFont.display(g2d, title, TITLE_SIZE, center.width, center.height - TITLE_SIZE, Color.WHITE, 0);

        center = CustomFont.center(g2d, SCORE_TEXT + scoreText, TEXT_SIZE, 0, screen);
        CustomFont.display(g2d, SCORE_TEXT + scoreText, TEXT_SIZE, center.width, center.height, Color.WHITE, 0);

        center = CustomFont.center(g2d, coinsText, TEXT_SIZE, 0, screen);
        CustomFont.display(g2d, coinsText, TEXT_SIZE, center.width, center.height + TITLE_SIZE, Color.WHITE, 0);

        g2d.drawImage(Assets.coin[0], center.width - Assets.coinSize / 2, center.height + TEXT_SIZE / 2 + 5, Assets.coinSize * 2, Assets.coinSize * 2, null);

        center = CustomFont.center(g2d, bottom, TEXT_SIZE, 0, screen);
        CustomFont.display(g2d, bottom, TEXT_SIZE, center.width, center.height + 2 * TITLE_SIZE, Color.WHITE, 0);
    }

    public void drawFailed(Graphics g2d){
        drawGeneric(g2d, FAILED_TEXT, BACK_TEXT);

        int size = Assets.tileSize * 3,
            center = CustomFont.center(g2d, "", size, 0, screen).height;

        g2d.drawImage(Assets.player[36], (screen.width - size) / 2, center - TITLE_SIZE * 5, size, size, null);
    }

    public void drawPassed(Graphics g2d){
        drawGeneric(g2d, levelText + " level passed!", NEXT_TEXT);
    }

    public void drawWon(Graphics g2d){
        drawGeneric(g2d, WON_TEXT, BACK_TEXT);

        Dimension size = new Dimension(Assets.crownSize.width / 2, Assets.crownSize.height / 2);
        int center = CustomFont.center(g2d, "", size.height, 0, screen).height;
        g2d.drawImage(Assets.crown, (screen.width - size.width) / 2, center - TITLE_SIZE * 4, size.width, size.height, null);
    }

    @Override
    public void draw(Graphics g2d) {
        switch(state){
            case PLAYSTATE_PLAYING:
                map.draw(g2d);
                break;
            case PLAYSTATE_FAILED:
                drawFailed(g2d);
                break;
            case PLAYSTATE_PASSED:
                drawPassed(g2d);
                break;
            case PLAYSTATE_WON:
                drawWon(g2d);
                break;
        }

    }
}
