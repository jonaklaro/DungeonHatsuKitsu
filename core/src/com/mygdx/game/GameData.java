package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entities.Player;

import java.io.*;
import java.util.ArrayList;

public class GameData implements Serializable {
    public int[] health;
    public Vector2[] pos;


    void loadInfo(ArrayList<Player> players){
        health = new int[players.size()];
        pos = new Vector2[players.size()];

        for(int i=0; i<players.size(); i++){
            health[i] = players.get(i).getHealth();
            pos[i] = players.get(i).getPosition();
        }

    }
    void writeInfo(ArrayList<Player> players){
        for(int i=0; i<players.size(); i++){
            players.get(i).setHealth(health[i]);
            players.get(i).setPosition(pos[i]);
        }
    }
    void saveGameState() throws IOException {
        FileOutputStream fos = new FileOutputStream("game.save");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
        oos.close();
        fos.close();
        System.out.println("Everythings saved!");
    }
}
