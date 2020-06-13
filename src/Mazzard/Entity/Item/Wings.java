package Mazzard.Entity.Item;

import Mazzard.Components.Assets;
import Mazzard.Components.Range;

import java.awt.*;

public class Wings extends Item {
    public Wings(int x, int y){
        super(x, y, 30, 30, new Rectangle(30, 30), 0);

        wings = 0.15f;

        image = Assets.wings[0];

        animTime = 5;
        animFrames = Assets.wings;
        animRange = new Range(0, 3);
        animLoop = true;
    }
}
