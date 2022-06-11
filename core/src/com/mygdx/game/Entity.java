package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Entity extends Sprite {
    Texture texture;
    Sprite sprite;
    Rectangle hitRect;
    boolean flipped;

    boolean multi;
    boolean boostRight = false;
    boolean boostLeft = false;

    Vector2 direction = new Vector2();
    float playerScale = 1;

    float gravity;
    double gravSpeed = 42;
    int speed = 300;
    public float midX;
    public float midY;

    int health;
    float color = 1;
    int jumpCount;
    boolean jumped;

    Map map;
    static ArrayList<Rectangle> borderRecs;
    static ArrayList<Rectangle> hiddenRecs;
    static ArrayList<Attack> attacks = new ArrayList<>();
//    static ArrayList<Rectangle> enemyList = new ArrayList<>();
    static ArrayList<Enemy> enemyList = new ArrayList<>();

    Rectangle rec;

    int tilesize = Settings.tilesize;
    int graphicScale = Settings.graphicScale;

    int[][] borders;
    int[][] hidden;
    int[][] enemies;

    float posX;
    float posY;

    int throwback = 10;

    enum KeyBlock{
        NO_BLOCK,
        RIGHT,
        LEFT
    }
    KeyBlock kb;


    public Entity(Vector2 pos, String spriteLink){
        posX = pos.x;
        posY = pos.y;

        sprite = createSprite(spriteLink);

        sprite.setPosition(posX, posY); // random


        hitRect = new Rectangle(posX,posY,sprite.getWidth()*playerScale,sprite.getHeight()*playerScale);

        gravity = 0;

        map = new Map();

        borderRecs = new ArrayList<>();
        //        borders = map.readMap("assets/maps/newLevel_borders.csv");
        Settings.map = Map.mapp;
        borders = Map.borders;
        hidden = Map.hidden;
        enemies = Map.enemies;

        borderRecs = createRectList(borders);
        hiddenRecs = createRectList(hidden);
        enemyList = GameScreen.enemies;
//        enemyList = createRectList(enemies);

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

    Sprite createSprite(String link){
        texture = new Texture(link);
        sprite = new Sprite(texture);
        sprite.setOrigin(0,0);

        return sprite;
//        sprite.setScale(playerScale);
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

    void move(float delta){
        hitRect.x += direction.x*speed*delta;

        collision("hor");

        gravity -= gravSpeed*delta;
        hitRect.y += gravity;
        collision("ver");

        sprite.setPosition(hitRect.x, hitRect.y);
        midX = sprite.getX()+(sprite.getWidth()*playerScale/2);
        midY = sprite.getY()+(sprite.getHeight()*playerScale/2);
    }

    private void collision(String dir){

        if (dir.equals("hor")){
            for (Rectangle border: borderRecs){
                if (hitRect.overlaps(border)){
                    if(direction.x > 0){ //Right
                        if (this.getClass() == Player.class){
                            ((Player) this).wallJump(multi, boostRight);
                        }

                        hitRect.x = border.x-hitRect.width;
                    }

                    if(direction.x < 0){ //Left
                        if (this.getClass() == Player.class){
                            ((Player) this).wallJump(multi, boostLeft);
                        }

                        hitRect.x = border.x+(border.width);
                    }
                    if (this.getClass() == Player.class) {
                        if (!this.jumped){
                            (this).jumped = true;
                        }
                    }
                    if (this.getClass() == Attack.class){
                        attacks.remove(this);
                        break;
                    }
                        //                    direction.x = direction.x*(-1);

                }
            }

            for (Enemy enemy: enemyList){
                if (hitRect.overlaps(enemy.hitRect)){
                    if(direction.x > 0){ //Right
                        kb = KeyBlock.RIGHT;

                        hitRect.x = enemy.hitRect.x-hitRect.width;
                    }

                    if(direction.x < 0){ //Left
                        kb = KeyBlock.LEFT;

                        hitRect.x = enemy.hitRect.x+(enemy.hitRect.width);
                    }

                    if (this.getClass() == Player.class){
                        health -= 1;
                        color = 0;
                    }

                    if (this.getClass() == Attack.class){
                        enemyList.remove(enemy);
                        GameScreen.enemies.remove(enemy);
                        attacks.remove(this);
                        break;
                    }
                }
            }
        }

        if (dir.equals("ver")){
            for (Rectangle border: borderRecs){
                if (border.overlaps(hitRect)){
                    if(gravity > 0){ //Up
                        gravity = 0;
                        hitRect.y = sprite.getY();
                    }
                    else if(gravity <= 0){ //Down
                        gravity = 0;
                        hitRect.y = border.y+(border.height);

                        if (this.getClass() == Player.class){
                            jumpCount = 0;
                            jumped = false;
                            boostRight = true;
                            boostLeft = true;
                        }
                    }
                }
            }

            if (this.getClass() == Player.class){
                for (Enemy enemy: enemyList){
                    if (hitRect.overlaps(enemy.hitRect)){
                        if(gravity > 0){ //Up
                            gravity = 0;
                            hitRect.y = sprite.getY()-throwback;
                        }

                        else if(gravity <= 0){ //Down
                            gravity = -gravity;
                            hitRect.y = enemy.hitRect.y+(enemy.hitRect.height)+throwback;
                        }
                        health -= 1;
                        color = 0;
                    }
                }
            }
        }
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
