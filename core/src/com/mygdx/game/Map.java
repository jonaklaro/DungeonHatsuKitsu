package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.Level;
import com.mygdx.game.entities.Exit;
import com.mygdx.game.entities.Pegpeg;
import com.mygdx.game.entities.RoundStinger;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Map extends Sprite {
      static BufferedReader br;
      static int numCol;
      static int numRow;
      public float playerX;
      public float playerY;
      Texture tileMap;
      static Sprite spriteMap;
      public static int[][] mapp;
      public static int[][] borders;
      public static int[][] hidden;
      public static int[][] enemies;
      static int tilesize = Settings.tilesize;
      static int graphicScale = Settings.graphicScale;
      static Vector2 playerPos = new Vector2();
      static int[][] exitMap;

      // gameScreen
      public static GameScreen gameScreen;

      /**
       * Constructor for Map
       * <p>
       * Loads SpriteMap and sets up the map parameters
       */
      public Map() {
            tileMap = new Texture("forrest.png");
            spriteMap = new Sprite(tileMap);
            numCol = Settings.numCol;
            numRow = Settings.numRow;
            gameScreen = GameScreen.getInstance();
      }

      /**
       * Loads all Map data from a text files
       * 
       * @param level the level to load
       *
       */
      public void load(String level) {
            Level levelData = new LevelGenerator(16, 16).getLevel();
            borders = levelData.border.map;

            mapp = new int[100][100];
            gameScreen = GameScreen.instance;

            enemies = levelData.enemies.map;
            // enemies = readMap("maps/" + level + "_enemies.csv");

            // borders = readMap("maps/" + level + "_borders.csv");
            hidden = readMap("maps/" + level + "_hidden_obj.csv");
            // exitMap = readMap("maps/" + level + "_exit.csv");
            exitMap = levelData.exit.map;
            readExit();

            // mapp = readMap("maps/" + level + "_map.csv");
            mapp = levelData.texture.map;

            // playerPos = getPlayer("maps/" + level + "_entities.csv");
            // get playerpos from levelData.entities
            playerPos = getPlayer(levelData.entities.map);

            // print playerPos
            System.out.println("playerPos: " + playerPos.x + " " + playerPos.y);
      }

      private void printMap(int[][] mapp2) {
            for (int i = 0; i < mapp2.length; i++) {
                  for (int j = 0; j < mapp2[i].length; j++) {
                        System.out.print(mapp2[i][j] + " ");
                  }
                  System.out.println();
            }

            System.out.println();
      }

      /**
       * Writes the Position of the exit to gameScreen.exit (creates new Sprite there)
       */
      private void readExit() {
            Vector2 pos;
            for (int row = 0; row < exitMap.length; row++) {

                  for (int col = 0; col < exitMap[row].length; col++) {
                        playerX = col * Settings.tilesize * Settings.graphicScale;
                        playerY = -row * Settings.tilesize * Settings.graphicScale;
                        pos = new Vector2(playerX, playerY);
                        if (exitMap[row][col] == 0) {
                              gameScreen.exit = new Exit(pos, "character/exit.png");
                        }
                  }
            }
      }

      public static Vector2 getPlayerPos() {
            return playerPos;
      }

      /**
       * Reads a map from a text file
       * <p>
       * Read csv file into BufferReader and parse it into an int[][] array.
       * After that, the 2D array represents the map and is returned
       * <p>
       * 
       * @param path the name of the file to read
       */
      public static int[][] readMap(String path) {
            mapp = new int[100][100];
            try {
                  br = new BufferedReader(new InputStreamReader(Gdx.files.internal(path).read()));
                  for (int row = 0; row < mapp.length; row++) {
                        String line = br.readLine();
                        String[] tokens = line.split(",");

                        for (int col = 0; col < mapp[row].length; col++) {
                              mapp[row][col] = Integer.parseInt(tokens[col]);
                        }
                  }

            } catch (Exception e) {
                  e.printStackTrace();
            }
            return mapp;
      }

      /**
       * Reads the player from a text file
       * <p>
       * Read csv file into BufferReader and check for a "0" token (the player)
       * If found, return the position of the player as a Vector2 (x =
       * col*tilesize*graphicScale, y = -row*tilesize*graphicScale)
       * <p>
       * 
       * @param path the name of the file to read
       */
      public Vector2 getPlayer(int[][] map) {
            printMap(map);
            Vector2 pos = new Vector2();
            for (int row = 0; row < map.length; row++) {

                  for (int col = 0; col < map[row].length; col++) {
                        if (map[row][col] == 0) {
                              pos.x = col * Settings.tilesize * Settings.graphicScale;
                              pos.y = -row * Settings.tilesize * Settings.graphicScale;
                              return pos;
                        }
                  }
            }
            return pos;
      }

      /**
       * Gets the enemies from the int[][] enemies
       * <p>
       * if the enemies[row][col] is 0, create a new Pegpeg at the position earlier
       * calculated.
       * if the enemies[row][col] is 1, create a new RoundStinger at the position
       * earlier calculated.
       * 
       */
      public void getEnemies() {

            Vector2 pos;

            for (int row = 0; row < enemies.length; row++) {

                  for (int col = 0; col < enemies[row].length; col++) {
                        playerX = col * Settings.tilesize * Settings.graphicScale;
                        playerY = -row * Settings.tilesize * Settings.graphicScale;
                        pos = new Vector2(playerX, playerY);
                        if (enemies[row][col] == 0) {
                              gameScreen.enemies.add(new Pegpeg(pos));
                        }
                        if (enemies[row][col] == 1) {
                              gameScreen.enemies.add(new RoundStinger(pos));
                        }
                  }
            }

      }

      /**
       * Draws the map[][] to the screen
       * <p>
       * Goes throught every element in the map[][] and draws the corresponding tile
       * from the tileMap.
       * Every tile is drawn with an opacity so that the hidden tiles can get see
       * through.
       */
      public static void drawMap(int[][] map, SpriteBatch batch, float opacity) {
            spriteMap.setScale((float) graphicScale / numCol, (float) graphicScale / numRow);
            for (int row = 0; row < map.length; row++) {
                  for (int col = 0; col < map[row].length; col++) {
                        if (map[row][col] != -1) {
                              spriteMap.setPosition(col * tilesize * graphicScale, -row * tilesize * graphicScale);
                              spriteMap.setOrigin(0, 0);
                              // spriteMap.setPosition(0,0);
                              spriteMap.setRegion((map[row][col] % numCol) * tilesize,
                                          map[row][col] / numRow * tilesize,
                                          tilesize,
                                          tilesize);
                              spriteMap.setAlpha(opacity);
                              spriteMap.draw(batch);

                        }
                  }
            }
      }
}
