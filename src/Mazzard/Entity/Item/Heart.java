package Mazzard.Entity.Item;

import Mazzard.Components.Assets;
import Mazzard.Components.Range;

import java.awt.*;

public class Heart extends Item {
    public Heart(int x, int y){
        super(x, y, 30, 30, new Rectangle(30, 30), 0);

        hearts = 1;

        image = Assets.hearts[0];

        animTime = 5;
        animFrames = Assets.hearts;
        animRange = new Range(0, 21);
        animLoop = true;
    }
}
