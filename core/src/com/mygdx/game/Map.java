package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entities.Enemy;
import com.mygdx.game.entities.Pegpeg;
import com.mygdx.game.entities.RoundStinger;

// import GameScreen;
import com.mygdx.game.GameScreen;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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

    // gamescreen
    public static GameScreen gameScreen;

    public Map() {
        tileMap = new Texture("forrest.png");
        spriteMap = new Sprite(tileMap);
        numCol = Settings.numCol;
        numRow = Settings.numRow;

    }

    public static void load() {
        gameScreen = GameScreen.instance;
        enemies = readMap("assets/maps/level1_enemies.csv");

        borders = readMap("assets/maps/level1_borders.csv");
        hidden = readMap("assets/maps/level1_hidden_obj.csv");
        mapp = readMap("assets/maps/level1_map.csv");
    }

    public static int[][] readMap(String path) {
        mapp = new int[40][60];
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
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

    public Vector2 getPlayer(String path) {
        mapp = new int[100][100];

        Vector2 pos = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            for (int row = 0; row < mapp.length; row++) {
                String line = br.readLine();
                String[] tokens = line.split(",");

                for (int col = 0; col < mapp[row].length; col++) {
                    if (tokens[col].equals("0")) {
                        playerX = col * Settings.tilesize * Settings.graphicScale;
                        playerY = -row * Settings.tilesize * Settings.graphicScale;
                        pos = new Vector2(playerX, playerY);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return pos;
    }

    public ArrayList<Enemy> getEnemies(String path, ArrayList<Enemy> enemies) {
        mapp = new int[100][100];

        Vector2 pos;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            for (int row = 0; row < mapp.length; row++) {
                String line = br.readLine();
                String[] tokens = line.split(",");

                for (int col = 0; col < mapp[row].length; col++) {
                    playerX = col * Settings.tilesize * Settings.graphicScale;
                    playerY = -row * Settings.tilesize * Settings.graphicScale;
                    pos = new Vector2(playerX, playerY);
                    if (tokens[col].equals("0")) {
                        enemies.add(new RoundStinger(pos));
                    }
                    if (tokens[col].equals("1")) {
                        enemies.add(new Pegpeg(pos));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return enemies;
    }

    public static void drawMap(int[][] map, SpriteBatch batch, float opacity) {
        spriteMap.setScale((float) graphicScale / numCol, (float) graphicScale / numRow);
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                if (map[row][col] != -1) {
                    spriteMap.setPosition(col * tilesize * graphicScale, -row * tilesize * graphicScale);
                    spriteMap.setOrigin(0, 0);
                    // spriteMap.setPosition(0,0);
                    spriteMap.setRegion((map[row][col] % numCol) * tilesize, map[row][col] / numRow * tilesize,
                            tilesize,
                            tilesize);
                    spriteMap.setAlpha(opacity);
                    spriteMap.draw(batch);

                }
            }
        }
    }
}
