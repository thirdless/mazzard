package Mazzard.Screens;

import Mazzard.Components.BasicEnums;
import Mazzard.Components.CustomFont;
import java.awt.*;
import java.awt.event.KeyEvent;

public class PauseScreen extends GenericMenu {

    private final String oldSaved;
    private final String savedText = "SAVED!";
    private int saved;

    public PauseScreen(){
        super();
        String[] buttons = {
                "CONTINUE GAME",
                "SAVE GAME",
                "HELP",
                "EXIT TO MAIN MENU"
        };
        buttonsText = buttons;
        continueGame = true;
        index = continueValue();
        oldSaved = buttons[1];
        saved = 0;
    }

    protected void processInput(){
        switch(index){
            case 0:
                res.getGame().unpauseGame();
                break;
            case 1:
                saveGame();
                break;
            case 2:
                state = BasicEnums.MENUSTATE_HELP;
                break;
            case 3:
                res.getGame().mainMenu();
                break;
        }
    }

    private void saveGame(){
        if(saved != 0) return;

        saved = 1;
        res.getDB().setSave(res.getMap().generateSave());
    }

    public void reset(){
        index = continueValue();
        saved = 0;
    }

    @Override
    public void update() {
        if(saved == 1) buttonsText[1] = savedText;
        else buttonsText[1] = oldSaved;

        super.update();

        if(res.getKey(KeyEvent.VK_ESCAPE) && state == BasicEnums.MENUSTATE_MAIN){
            res.getGame().unpauseGame();
        }
    }

    @Override
    public void draw(Graphics g2d) {
        super.draw(g2d);

        switch(state){
            case MENUSTATE_MAIN:
                int percentage = buttonsText.length * BUTTON_HEIGHT + (buttonsText.length - 1) * PADDING_HEIGHT;
                drawCurrent = (dim.height - percentage) / 2;
                drawMain(g2d);
                break;
            case MENUSTATE_HELP:
                drawHelp(g2d);
                break;
        }

    }
}
