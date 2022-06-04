package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Entity extends Sprite {
    Texture texture;
    Sprite sprite;
    Rectangle hitRect;
    boolean flipped;
    Vector2 direction = new Vector2();
    float playerScale = 1;

    float gravity;
    double gravSpeed = 42;
    int speed = 300;
    public float midX;
    public float midY;

    Map map;
    static ArrayList<Rectangle> borderRecs;
    static ArrayList<Rectangle> hiddenRecs;

    Rectangle rec;

    int tilesize = Settings.tilesize;
    int graphicScale = Settings.graphicScale;

    int[][] borders;
    int[][] hidden;

    float posX;
    float posY;

    public Entity(Vector2 pos, String spriteLink){
        posX = pos.x;
        posY = pos.y;

        createSprite(spriteLink);

        sprite.setPosition(posX, posY); // random
        hitRect = new Rectangle(posX,posY,sprite.getWidth()*playerScale,sprite.getHeight()*playerScale);

        gravity = 0;

        map = new Map();

        borderRecs = new ArrayList<>();
        //        borders = map.readMap("assets/maps/newLevel_borders.csv");
        Settings.map = Map.mapp;
        borders = Map.borders;
        hidden = Map.hidden;

        borderRecs = createRectList(borders);
        hiddenRecs = createRectList(hidden);

    }

    void flipPlayer() {
        if (direction.x == -1 && !flipped){
            sprite.flip(true,false);
            flipped = true;
        }
        if (direction.x == 1 && flipped){
            sprite.flip(true,false);
            flipped = false;
        }
    }

    void createSprite(String link){
        texture = new Texture(link);
        sprite = new Sprite(texture);
        sprite.setOrigin(0,0);
        sprite.setScale(playerScale);
    }

    ArrayList<Rectangle> createRectList(int[][] map){
        ArrayList<Rectangle> recs = new ArrayList<>();
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++){
                if(map[row][col] != -1){
                    float x = (col*tilesize*graphicScale+20);
                    float y = (float) (-(row)*tilesize*graphicScale);
                    rec = new Rectangle(x, y, (float) (tilesize*graphicScale-40),(float) (tilesize*graphicScale));
                    //                    rec = new Rectangle((col*tilesize*graphicScale-tilesize*2), (float) (-(row)*tilesize*graphicScale-(tilesize*3)), (float) (tilesize*graphicScale*1.5),(float) (tilesize*graphicScale*1.75));
                    recs.add(rec);
                }
            }
        }
        return recs;
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        batch.setProjectionMatrix(camera.combined);

        sprite.draw(batch);
    }

    public Vector2 getPosition(){
        return new Vector2(sprite.getX(), sprite.getY());
    }

    public float getX(){
        return sprite.getX();
    }

    public float getY(){
        return sprite.getY();
    }
}
