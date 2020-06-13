package Mazzard.Screens;

import Mazzard.Components.Assets;
import Mazzard.Components.BasicEnums;
import Mazzard.Components.CustomFont;
import Mazzard.Components.Keyboard;
import Mazzard.GameData.Settings;
import Mazzard.Resources;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.zip.DeflaterInputStream;

public abstract class GenericMenu extends Screen {
    public final int BUTTON_HEIGHT;
    public final int TITLE_HEIGHT;
    public final int SUBTITLE_HEIGHT;
    public final int PADDING_HEIGHT;
    public final int LINE_HEIGHT;

    protected BasicEnums state;
    protected Resources res;
    protected Dimension dim;
    protected Color color;
    protected Color unselectedColor;
    protected int index;
    private boolean pressed;
    protected String[] buttonsText;
    protected boolean continueGame;
    protected int settingsIndex;

    protected int drawCurrent;

    protected final String[] helpLines = {
            "CONTROLS:",
            "To move use [W][A][S][D] or the arrow keys",
            "depending on your selection from settings.",
            "To shoot use the [SPACE] key",
            "",
            "",
            "STORY:",
            "Kill the monsters and collect the coins",
            "to get back to the land you've been thrown from",
            "by the dark energies and get back the throne",
            "you deserve. Enemies get more powerful as",
            "the level increases",
            "",
            "Realizator: Macovei Ioan-Mihail",
            "Grupa: 1210A"
    };
    public static final String backText = "Press [ESC] to go back";

    public GenericMenu(){
        color = Color.BLACK;
        unselectedColor = Color.DARK_GRAY;
        res = Resources.getInstance();
        dim = res.getScreen();
        index = 0;
        continueGame = false;
        settingsIndex = 0;

        BUTTON_HEIGHT = 5 * dim.height / 100;
        TITLE_HEIGHT = 10 * dim.height / 100;
        SUBTITLE_HEIGHT = 7 * dim.height / 100;
        PADDING_HEIGHT = 5 * dim.height / 100;
        LINE_HEIGHT = 4 * dim.height / 100;
        state = BasicEnums.MENUSTATE_MAIN;
    }

    protected abstract void processInput();

    private void processSettingsInput(){
        res.getSettings().toggleSetting(settingsIndex);
        res.detachAllKeys();
    }

    protected int continueValue(){
        return continueGame ? 0 : 1;
    }

    private void mainInput(){
        if(res.getKey(KeyEvent.VK_DOWN) || res.getKey(KeyEvent.VK_UP)){
            if(!pressed){
                if(res.getKey(KeyEvent.VK_DOWN)) changeIndex(1);
                else if(res.getKey(KeyEvent.VK_UP)) changeIndex(-1);
                pressed = true;
            }
        }
        else pressed = false;

        if(res.getKey(KeyEvent.VK_ENTER)){
            processInput();
            res.detachAllKeys();
        }
    }

    private void settingsInput(){
        if(res.getKey(KeyEvent.VK_DOWN) || res.getKey(KeyEvent.VK_UP)){
            if(!pressed){
                if(res.getKey(KeyEvent.VK_DOWN)) changeSettingsIndex(1);
                else if(res.getKey(KeyEvent.VK_UP)) changeSettingsIndex(-1);
                pressed = true;
            }
        }
        else pressed = false;

        if(res.getKey(KeyEvent.VK_ENTER)){
            processSettingsInput();
        }
    }

    private void backMenu(){
        if(res.getKey(KeyEvent.VK_ESCAPE)){
            state = BasicEnums.MENUSTATE_MAIN;
            res.detachAllKeys();
        }
    }

    @Override
    public void update() {
        switch(state){
            case MENUSTATE_MAIN:
                mainInput();
                break;
            case MENUSTATE_HELP:
                backMenu();
                break;
            case MENUSTATE_SETTINGS:
                settingsInput();
                backMenu();
                break;
        }
    }

    private void changeIndex(int factor){
        int start = continueValue();
        if(index + factor < start){
            index = buttonsText.length - 1;
        }
        else if(index + factor == buttonsText.length){
            index = start;
        }
        else index += factor;
    }

    private void changeSettingsIndex(int factor){
        int start = 0;
        int length = Settings.NO_OF_OPTIONS;
        if(settingsIndex + factor < start){
            settingsIndex = length - 1;
        }
        else if(settingsIndex + factor == length){
            settingsIndex = start;
        }
        else settingsIndex += factor;
    }

    protected void drawSettings(Graphics g2d){
        int center,
            current = (dim.height - Settings.NO_OF_OPTIONS * BUTTON_HEIGHT - (Settings.NO_OF_OPTIONS - 1) * PADDING_HEIGHT) / 2;

        Color colorize;

        String[] lines = res.getSettings().toString().split(";");
        String tip = "WARNING: FULLSCREEN NOT RECOMMENDED; IF YOU CHANGE IT THE APP";
        String tip1 = "WILL CLOSE AND YOU HAVE TO RESTART TO GET THE EFFECT APPLIED";

        center = CustomFont.center(g2d, "SETTINGS", TITLE_HEIGHT, 0, dim).width;
        CustomFont.display(g2d, "SETTINGS", TITLE_HEIGHT, center, 100, color, 0);
        center = CustomFont.center(g2d, backText, LINE_HEIGHT, 0, dim).width;
        CustomFont.display(g2d, backText, LINE_HEIGHT, center, dim.height - LINE_HEIGHT, color, 0);
        center = CustomFont.center(g2d, tip, 15, 0, dim).width;
        CustomFont.display(g2d, tip, 15, center, dim.height - LINE_HEIGHT * 4, Color.RED, 0);
        center = CustomFont.center(g2d, tip1, 15, 0, dim).width;
        CustomFont.display(g2d, tip1, 15, center, dim.height - LINE_HEIGHT * 3, Color.RED, 0);

        for(int i = 0; i < lines.length; i++){
            colorize = unselectedColor;
            center = CustomFont.center(g2d, lines[i], BUTTON_HEIGHT, 0, dim).width;

            if(settingsIndex == i){
                CustomFont.display(g2d, ">", BUTTON_HEIGHT, center - PADDING_HEIGHT, current, color, 0);
                colorize = color;
            }

            CustomFont.display(g2d, lines[i], BUTTON_HEIGHT, center, current, colorize, 0);
            current += BUTTON_HEIGHT + PADDING_HEIGHT;
        }
    }

    protected void drawHelp(Graphics g2d){
        int current = 200,
            center;

        center = CustomFont.center(g2d, "HELP", TITLE_HEIGHT, 0, dim).width;
        CustomFont.display(g2d, "HELP", TITLE_HEIGHT, center, 100, color, 0);

        for(int i = 0; i < helpLines.length; i++){
            center = CustomFont.center(g2d, helpLines[i], LINE_HEIGHT, 0, dim).width;
            CustomFont.display(g2d, helpLines[i], LINE_HEIGHT, center, current, color, 0);
            current += LINE_HEIGHT;
        }

        center = CustomFont.center(g2d, backText, BUTTON_HEIGHT, 0, dim).width;
        CustomFont.display(g2d, backText, BUTTON_HEIGHT, center, current + PADDING_HEIGHT, color, 0);
    }

    protected void drawMain(Graphics g2d){
        int center;
        Color colorize;

        for(int i = 0; i < buttonsText.length; i++){
            if(!continueGame && i == 0) continue;

            colorize = unselectedColor;

            center = CustomFont.center(g2d, buttonsText[i], BUTTON_HEIGHT, 0, dim).width;

            if(index == i){
                CustomFont.display(g2d, ">", BUTTON_HEIGHT, center - PADDING_HEIGHT, drawCurrent, color, 0);
                colorize = color;
            }

            CustomFont.display(g2d, buttonsText[i], BUTTON_HEIGHT, center, drawCurrent, colorize, 0);
            drawCurrent += BUTTON_HEIGHT + PADDING_HEIGHT;
        }
    }

    @Override
    public void draw(Graphics g2d) {
        Dimension screen = Resources.getInstance().getScreen();
        g2d.drawImage(Assets.menubg, 0, 0, screen.width, screen.height, null);
    }
}
