package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Character extends Entity {
    boolean flipped;
    boolean multi;

    boolean boostRight = false;
    boolean boostLeft = false;

    Vector2 direction = new Vector2();
    float playerScale = 1;

    float gravity;
    double gravSpeed = 42;
    int speed;
    public float midX;
    public float midY;

    int damage;
    float color = 1;
    int jumpCount;
    boolean jumped;

    Map map;

    int[][] borders;
    int[][] hidden;
    int[][] enemies;

    int throwback = 10;

    enum KeyBlock{
        NO_BLOCK,
        RIGHT,
        LEFT
    }
    KeyBlock kb;


    public Character(Vector2 pos, String spriteLink){
        super(pos, spriteLink);

        gravity = 0;

        map = new Map();

        borderRecs = new ArrayList<>();
        Settings.map = Map.mapp;
        borders = Map.borders;
        hidden = Map.hidden;
        enemies = Map.enemies;

        borderRecs = createRectList(borders);
        hiddenRecs = createRectList(hidden);
        enemyList = GameScreen.enemies;

    }

    void flipCharacter() {
        if (direction.x == -1 && !flipped){
            sprite.flip(true,false);
            flipped = true;
        }
        if (direction.x == 1 && flipped){
            sprite.flip(true,false);
            flipped = false;
        }
    }

    public void drawHurt(){
        if (color >= 1){
            color = 1;
        } else {
            color += 10/200f;
        }
        sprite.setColor(1,color,color,1);

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
                        ((Attack) this).colided = true;
                        break;
                    }
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
                        this.recieveDamage(enemy.getDamage());
                        color = 0;

                    }

                    if (this.getClass() == Attack.class){
                        enemy.color = 0;
                        enemy.recieveDamage(this.getDamage());

                        ((Attack) this).colided = true;

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
                        recieveDamage(enemy.getDamage());
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

    public void entityUpdate(float delta){
        move(delta);
        drawHurt();
        updateExisting(this);
    }

    public float getY(){
        return sprite.getY();
    }

    public int getDamage(){
        return damage;
    }

}
