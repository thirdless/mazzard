package Mazzard.Components;

import Mazzard.Entity.Entity;
import Mazzard.Maps.Map;
import Mazzard.Maps.Tile;
import Mazzard.Resources;

import java.awt.*;

public class Camera {
    private int x;
    private int y;
    private Resources res;

    public Camera(){
        res = Resources.getInstance();
    }

    public void center(Entity entity){
        Rectangle bounds = entity.getBounds();
        Dimension screen = res.getScreen();
        Map map = res.getMap();

        x = bounds.x - screen.width / 2 + bounds.width / 2;
        y = bounds.y - screen.height / 2 + bounds.height / 2;

        if(x < 0){
            x = 0;
        }
        else if(x > map.getMapWidth() * Tile.TILE_WIDTH - screen.width){
            x = map.getMapWidth() * Tile.TILE_WIDTH - screen.width;
        }

        if(y < 0){
            y = 0;
        }
        else if(y > map.getMapHeight() * Tile.TILE_HEIGHT - screen.height){
            y = map.getMapHeight() * Tile.TILE_HEIGHT - screen.height;
        }
    }

    public Dimension getOffset(){
        return new Dimension(x, y);
    }
}
