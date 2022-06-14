package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
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

    // gereral update method for characters
    public void entityUpdate(float delta){
        move(delta);
        drawHurt();
        updateExisting(this);
    }

    //a method to move the different axes of the hitbox and do colDet
    void move(float delta){
        hitRect.x += direction.x*speed*delta;

        collision("hor");

        gravity -= gravSpeed*delta;
        hitRect.y += gravity;
        collision("ver");

        //set Sprite pos to calculated pos and set midX and midY
        sprite.setPosition(hitRect.x, hitRect.y);
        setMidX(sprite.getX()+(sprite.getWidth()*playerScale/2));
        setMidY(sprite.getY()+(sprite.getHeight()*playerScale/2));
    }

    // if color is lower than 1, slowly count it up and set Sprite color accordingly
    public void drawHurt(){
        if (color >= 1){
            color = 1;
        } else {
            color += 10/200f;
        }
        sprite.setColor(1,color,color,1);
    }

    // A method to detect horizontal and vertical colissions
    private void collision(String dir){
        //horizontal detection
        if (dir.equals("hor")){
            //for every border, check if its overlapping with own hitbox
            for (Rectangle border: borderRecs){
                if (hitRect.overlaps(border)){
                    if(direction.x > 0){ //character is moving Right
                        // if character is player, see if they are doing a woll jump
                        if (this.getClass() == Player.class){
                            ((Player) this).wallJump(multi, boostRight);
                        }
                        //set x coordinate of hitbox to x coordinate of the border - hitbox width
                        hitRect.x = border.x-hitRect.width;
                    }

                    if(direction.x < 0){ //Left
                        if (this.getClass() == Player.class){
                            ((Player) this).wallJump(multi, boostLeft);
                        }
                        hitRect.x = border.x+(border.width);
                    }

                    //is character is attack, set attack.collided to true
                    if (this.getClass() == Attack.class){
                        ((Attack) this).colided = true;
                        break;
                    }
                }
            }

            // enemy detection
            for (Enemy enemy: enemyList){
                if (hitRect.overlaps(enemy.hitRect)){
                    // same as border colDec, sets a Keyblock, when Player walks against enemy -> hitbox gets resetet with extra throwback
                    if(direction.x > 0){ //Right
                        kb = KeyBlock.RIGHT;
                        hitRect.x = enemy.hitRect.x-hitRect.width-throwback;
                    }

                    if(direction.x < 0){ //Left
                        kb = KeyBlock.LEFT;
                        hitRect.x = enemy.hitRect.x+(enemy.hitRect.width)+throwback;
                    }

                    //if Player collides with enemy, let them recieve Damage, set red color for damage animation
                    if (this.getClass() == Player.class){
                        this.recieveDamage(enemy.getDamage());
                        color = 0;
                    }

                    //same as Player, but enemy recieves the damage
                    if (this.getClass() == Attack.class){
                        enemy.color = 0;
                        enemy.recieveDamage(this.getDamage());

                        // set Attack.collided to true
                        ((Attack) this).colided = true;
                        break;
                    }
                }
            }
        }

        //Vertical colDetection
        if (dir.equals("ver")){
            //same as horrizontal but the direction is now gravity
            for (Rectangle border: borderRecs){
                if (border.overlaps(hitRect)){
                    //if colliding with topside, set gravity to 0 and reset hitrect to previous Sprite pos
                    if(gravity > 0){ //Up
                        gravity = 0;
                        hitRect.y = sprite.getY();
                    }
                    //if standing or falling set gravity to 0 and set hitrect to border pos + border height
                    else if(gravity <= 0){ //Down
                        gravity = 0;
                        hitRect.y = border.y+(border.height);

                        //if player is Standing on bottom, reset Parameters
                        if (this.getClass() == Player.class){
                            jumpCount = 0;
                            jumped = false;
                            boostRight = true;
                            boostLeft = true;
                        }
                    }
                }
            }

            //if Player is colliding with enemy
            if (this.getClass() == Player.class){
                for (Enemy enemy: enemyList){
                    if (hitRect.overlaps(enemy.hitRect)){
                        // same as earlyer with player and enemy colission
                        if(gravity > 0){ //Up
                            gravity = 0;
                            hitRect.y = sprite.getY()-throwback;
                        }

                        //if coll with bottom, set pos grav (like little jump) and set jumpcount to 0 for jumping again
                        else if(gravity <= 0){ //Down
                            gravity = 10;
                            hitRect.y = enemy.hitRect.y+(enemy.hitRect.height)+throwback;
                            jumpCount = 0;
                        }

                        //subtract enemyDamage from Player health and set color to red for hurt animation
                        recieveDamage(enemy.getDamage());
                        color = 0;
                    }
                }
            }
        }

        //Colission detection for the hidden recs
        if (this.getClass() == Player.class) ((Player) this).hiddenColDet();
    }

    // a render Method
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        batch.setProjectionMatrix(camera.combined);
        sprite.draw(batch);
    }

    public int getDamage(){
        return damage;
    }
}
