package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Entity extends Sprite {
    Texture texture;
    Sprite sprite;
    Rectangle hitRect;

    static ArrayList<Rectangle> borderRecs;
    static ArrayList<Rectangle> hiddenRecs;
    static ArrayList<Enemy> enemyList = new ArrayList<>();

    Rectangle rec;

    int tilesize = Settings.tilesize;
    int graphicScale = Settings.graphicScale;

    private int health;

    Entity(Vector2 pos, String spriteLink){
        sprite = createSprite(spriteLink);
        sprite.setPosition(pos.x, pos.y);
    }

    Sprite createSprite(String link){
        texture = new Texture(link);
        sprite = new Sprite(texture);
        sprite.setOrigin(0,0);

        return sprite;
    }

    ArrayList<Rectangle> createRectList(int[][] map){
        ArrayList<Rectangle> recs = new ArrayList<>();
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++){
                if(map[row][col] != -1){
                    float x = (col*tilesize*graphicScale);
                    float y = (float) (-(row)*tilesize*graphicScale);
                    rec = new Rectangle(x, y, (float) (tilesize*graphicScale),(float) (tilesize*graphicScale));
                    recs.add(rec);
                }
            }
        }
        return recs;
    }

    public void updateExisting(Character e){
        if (e.getHealth() <= 0){
            enemyList.remove(e);
            GameScreen.enemies.remove(e);
        }
    }

    public int getHealth(){
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void recieveDamage(int damage) {
        this.health -= damage;
    }

    public Vector2 getPosition(){
        return new Vector2(sprite.getX(), sprite.getY());
    }



    public float getX(){
        return sprite.getX();
    }
}