package Mazzard.Screens;

import Mazzard.Components.BasicEnums;
import Mazzard.Components.CustomFont;
import java.awt.*;

public class MenuScreen extends GenericMenu {
    private final String[] titles;

    public MenuScreen(String[] titles){
        super();
        this.titles = titles;
        String[] buttons = {
                "CONTINUE GAME",
                "NEW GAME",
                "HELP",
                "SETTINGS",
                "QUIT"
        };
        buttonsText = buttons;
        index = continueValue();
    }

    protected void processInput(){
        switch(index){
            case 0:
                res.getGame().playGame(true);
                break;
            case 1:
                res.getGame().playGame(false);
                break;
            case 2:
                state = BasicEnums.MENUSTATE_HELP;
                break;
            case 3:
                state = BasicEnums.MENUSTATE_SETTINGS;
                break;
            case 4:
                exit();
                break;
        }
    }

    public void setContinueGame(boolean type){
        continueGame = type;
        index = continueValue();
    }

    @Override
    public void drawMain(Graphics g2d){
        int center;
        int percentage = buttonsText.length * (BUTTON_HEIGHT + PADDING_HEIGHT) + TITLE_HEIGHT + SUBTITLE_HEIGHT;
        if(!continueGame) percentage -= PADDING_HEIGHT + BUTTON_HEIGHT;
        drawCurrent = (dim.height - percentage) / 2;

        center = CustomFont.center(g2d, titles[0], TITLE_HEIGHT, 0, dim).width;
        CustomFont.display(g2d, titles[0], TITLE_HEIGHT, center, drawCurrent, color, 0);
        drawCurrent += TITLE_HEIGHT;
        center = CustomFont.center(g2d, titles[1], SUBTITLE_HEIGHT, 0, dim).width;
        CustomFont.display(g2d, titles[1], SUBTITLE_HEIGHT, center, drawCurrent, color, 0);
        drawCurrent += SUBTITLE_HEIGHT + 2 * PADDING_HEIGHT;
    }

    @Override
    public void draw(Graphics g2d) {
        super.draw(g2d);

        switch(state){
            case MENUSTATE_MAIN:
                drawMain(g2d);
                super.drawMain(g2d);
                break;
            case MENUSTATE_HELP:
                drawHelp(g2d);
                break;
            case MENUSTATE_SETTINGS:
                drawSettings(g2d);
                break;
        }
    }
}
