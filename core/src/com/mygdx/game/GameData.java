package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entities.Player;

import java.io.*;
import java.util.ArrayList;

public class GameData implements Serializable {
      public GameData gameData = this;

      public int[] health;
      public Vector2[] pos;
      int highscore;

      public void loadInfo() {
            ArrayList<Player> players = GameScreen.players;
            if (players.isEmpty())
                  return;

            health = new int[players.size()];
            pos = new Vector2[players.size()];

            for (int i = 0; i < players.size(); i++) {
                  gameData.health[i] = players.get(i).getHealth();
                  gameData.pos[i] = players.get(i).getPosition();
            }

      }

      void writeInfo(ArrayList<Player> players) {
            if (players.isEmpty())
                  return;

            for (int i = 0; i < players.size(); i++) {
                  players.get(i).setHealth(gameData.health[i]);
                  players.get(i).setPosition(gameData.pos[i]);
            }
      }

      // add highscore to file
      public void addHighscore(int score) {
            highscore = score;
            try {
                  FileOutputStream fileOut = new FileOutputStream("highscore.txt");
                  ObjectOutputStream out = new ObjectOutputStream(fileOut);
                  out.writeObject(highscore);
                  out.close();
                  fileOut.close();
            } catch (IOException i) {
                  i.printStackTrace();
            }
      }

      // read highscore from file
      public int getHighscore() {
            try {
                  FileInputStream fileIn = new FileInputStream("highscore.txt");
                  ObjectInputStream in = new ObjectInputStream(fileIn);
                  highscore = (int) in.readObject();
                  in.close();
                  fileIn.close();
            } catch (IOException i) {
                  i.printStackTrace();
            } catch (ClassNotFoundException c) {
                  System.out.println("Class not found");
            }
            return highscore;
      }

      public void writeGameState() throws IOException {
            FileOutputStream fos;
            if (GameScreen.players.size() == 1) {
                  fos = new FileOutputStream("save.txt");
                  ObjectOutputStream oos = new ObjectOutputStream(fos);
                  oos.writeObject(gameData);
                  oos.close();
                  fos.close();
            }
            if (GameScreen.players.size() == 2) {
                  fos = new FileOutputStream("save2.txt");
                  ObjectOutputStream oos = new ObjectOutputStream(fos);
                  oos.writeObject(gameData);
                  oos.close();
                  fos.close();
            }

            System.out.println("Everything saved!");
      }

      // a function to load the game state
      void loadGameState() throws IOException, ClassNotFoundException {
            if (GameScreen.players.size() == 1) {
                  FileInputStream fis = new FileInputStream("save.txt");
                  ObjectInputStream ois = new ObjectInputStream(fis);
                  gameData = (GameData) ois.readObject();
                  ois.close();
                  fis.close();
            }
            if (GameScreen.players.size() == 2) {
                  FileInputStream fis = new FileInputStream("save2.txt");
                  ObjectInputStream ois = new ObjectInputStream(fis);
                  gameData = (GameData) ois.readObject();
                  ois.close();
                  fis.close();
            }
            System.out.println("Everything loaded!");
      }
}
