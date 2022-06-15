package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Settings;

import java.io.Serializable;
import java.util.ArrayList;

public class Entity extends Sprite implements Serializable {
    Texture texture;
    Sprite sprite;
    Rectangle hitRect;

    static ArrayList<Rectangle> borderRecs;
    static ArrayList<Rectangle> hiddenRecs;

    Rectangle rec;

    int tilesize = Settings.tilesize;
    int graphicScale = Settings.graphicScale;

    private int health;

    private float midX;
    public float midY;

    //basic Constructor for every entity
    Entity(Vector2 pos, String spriteLink){
        sprite = createSprite(spriteLink);
        sprite.setPosition(pos.x, pos.y);
    }

    //basic Sprite create function
    Sprite createSprite(String link){
        texture = new Texture(link);
        sprite = new Sprite(texture);
        sprite.setOrigin(0,0);
        return sprite;
    }

    //Add rectangles to list (for borders)
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

    //remove character from its list when its dead
    public void updateExisting(Character c, ArrayList characters){
        if (c.getHealth() <= 0){

            if (c.getClass().getSuperclass() == Enemy.class) {
                ((Enemy) c).dropLoot();
            }
            characters.remove(c);
        }
    }

    public int getHealth(){
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    // entity health gets subtracted by damage
    public void receiveDamage(int damage) {
        this.health -= damage;
    }

    public Vector2 getPosition(){
        return new Vector2(sprite.getX(), sprite.getY());
    }

    public void setPosition(Vector2 position) {
        hitRect.setX(position.x);
        hitRect.setY(position.y);
    }

    public float getX(){
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
    }

    public float getMidX() {
        return midX;
    }

    public float getMidY() {
        return midY;
    }

    public void setMidX(float midX) {
        this.midX = midX;
    }

    public void setMidY(float midY) {
        this.midY = midY;
    }
}