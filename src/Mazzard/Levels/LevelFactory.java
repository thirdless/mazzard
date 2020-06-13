package Mazzard.Levels;

public class LevelFactory{
    public static Level get(int lvl){
        Level level = null;
        switch(lvl){
            case 1:
                level = new Level1();
                break;
            case 2:
                level = new Level2();
                break;
            default:
                return null;
        }
        return level;
    }
}
