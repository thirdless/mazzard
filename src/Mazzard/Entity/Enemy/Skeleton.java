package Mazzard.Entity.Enemy;

import Mazzard.Components.Assets;
import Mazzard.Components.BasicEnums;
import Mazzard.Components.Range;
import Mazzard.Entity.Entity;

import java.awt.*;

public class Skeleton extends Enemy{

    public Skeleton(float x, float y, float factor, int life){
        super(
                x,
                y,
                (int)(Entity.DEFAULT_WIDTH * factor * 2.5),
                (int)(Entity.DEFAULT_HEIGHT * factor * 2.5),
                new Rectangle((int)(25 * factor), (int)(30 * factor)),
                new Rectangle((int)(48 * factor), (int)(48 * factor)),
                life == 0 ? 4 : life,
                4,
                6,
                20
        );

        image = Assets.skeleton[0];

        animTime = 5;
        animFrames = Assets.skeleton;
        animRange = new Range(12, 15);;
        animLoop = true;

        mobName = "Skeleton";
    }

    @Override
    protected void dieFunc() {
        super.dieFunc();
        changeAnimation(10, null, new Range(16, 19), false);
    }

    protected void updateState(){
        Range tempRange;

        switch(state){
            case ANIMATION_ATTACK:
                tempRange = new Range(0, 7);
                break;
            case ANIMATION_RUN:
                tempRange = new Range(8, 11);
                break;
            default:
                tempRange = new Range(12, 15);
        }

        changeAnimation(5, null, tempRange, true);
        animState = state;
    }


}
