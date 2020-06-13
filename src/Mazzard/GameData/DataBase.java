package Mazzard.GameData;

import Mazzard.Components.BasicEnums;

import java.sql.*;

public class DataBase {
    private static final String SETTINGS_NAME = "Settings";
    private static final String SAVES_NAME = "Saves";

    private Connection conn = null;
    private Statement stat = null;

    public static void ErrorHandler(Exception e){
        e.printStackTrace();
    }

    public DataBase(String name){
        try{
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + name);
            conn.setAutoCommit(false);
            stat = conn.createStatement();

            String rs = "CREATE TABLE IF NOT EXISTS " + SETTINGS_NAME + " (Sounds INT NOT NULL, Controls INT NOT NULL, FullScreen INT NOT NULL, Hitboxes INT NOT NULL)";
            stat.executeUpdate(rs);

            rs = "CREATE TABLE IF NOT EXISTS " + SAVES_NAME + " (Level INT NOT NULL, Coins INT NOT NULL, Score INT NOT NULL, Mobs TEXT NOT NULL)";
            stat.execute(rs);
            conn.commit();
        }
        catch(Exception e){
            ErrorHandler(e);
        }
    }

    public ResultSet getSettings(){
        try{
            ResultSet rs = stat.executeQuery("Select * FROM " + SETTINGS_NAME + " ORDER BY ROWID DESC");
            if(rs.next()){
                return rs;
            }
            else return null;
        }
        catch(Exception e){
            ErrorHandler(e);
        }

        return null;
    }

    public void setSettings(int sounds, int controls, int fullscreen, int hitboxes){
        try{
            String rs = "DELETE FROM " + SETTINGS_NAME;
            stat.executeUpdate(rs);

            rs = "INSERT INTO " + SETTINGS_NAME + " (Sounds, Controls, FullScreen, Hitboxes) VALUES (" +
                    sounds + ", " + controls + ", " + fullscreen + ", " + hitboxes
                    + ")";

            stat.executeUpdate(rs);
            conn.commit();
        }
        catch(Exception e){
            ErrorHandler(e);
        }
    }

    public boolean checkLastSave(){
        try{
            ResultSet rs = stat.executeQuery("Select * FROM " + SAVES_NAME + " ORDER BY ROWID DESC");
            if(rs.next()){
                return true;
            }
            else{
                return false;
            }
        }
        catch(Exception e){
            ErrorHandler(e);
        }

        return false;
    }

    public void setSave(Data data){
        try{
            String rs = "INSERT INTO " + SAVES_NAME + " (Level, Coins, Score, Mobs) VALUES (" +
                    data.level + ", " + data.coins + ", " + data.score + ", \"" + data.mobs
                    + "\")";

            stat.executeUpdate(rs);
            conn.commit();
        }
        catch(Exception e){
            ErrorHandler(e);
        }
    }

    public Data getLastSave(){
        Data data = new Data();
        try{
            ResultSet rs = stat.executeQuery("Select * FROM " + SAVES_NAME + " ORDER BY ROWID DESC");
            if(rs.next()){
                data.level = rs.getInt("Level");
                data.score = rs.getInt("Score");
                data.coins = rs.getInt("Coins");
                data.mobs = rs.getString("Mobs");

                return data;
            }
            else return null;
        }
        catch(Exception e){
            ErrorHandler(e);
        }
        return null;
    }

    public void disconnect(){
        try{
            stat.close();
            conn.close();
        }
        catch(Exception e){
            ErrorHandler(e);
        }
    }
}
