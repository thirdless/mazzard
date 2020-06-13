package Mazzard.Entity.Item;

import Mazzard.Components.Assets;
import Mazzard.Components.Range;

import java.awt.*;

public class Coin extends Item{
    public Coin(int x, int y, int coins){
        super(x, y, 60, 60, new Rectangle(30, 30), coins);

        image = Assets.coin[0];

        animTime = 5;
        animFrames = Assets.coin;
        animRange = new Range(0, 7);
        animLoop = true;

        HEIGHT_FACTOR = 2.6f;
    }
}
