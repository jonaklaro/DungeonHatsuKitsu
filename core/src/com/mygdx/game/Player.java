package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player extends Entity {

    enum KeyBlock{
        NO_BLOCK,
        RIGHT,
        LEFT
    }

    int jumpCount;
    boolean jumped = false;
    boolean boostRight = false;
    boolean boostLeft = false;
    int maxJumps = 1;
    float color = 1;

    boolean hold = false;
    boolean attacked;
    boolean multi;
    boolean hiding;

    KeyBlock kb;


    public Player(Vector2 pos, boolean multi) {
        super(pos, "character/char_small.png");
        this.multi = multi;
        kb = KeyBlock.NO_BLOCK;
        health = 10;
        sprite.setScale( .5f,1);
        sprite.setRegion(10,3,34,61);
    }

    public void input(){

        if (!multi){
            direction.set(0,0);
            if (!Gdx.input.isKeyPressed(Input.Keys.S)) attacked = false;

            if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)){

                if (Gdx.input.isKeyPressed(Input.Keys.S) && !attacked) {
                    attacked = true;
                    attack();
                }

                if (Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.D)){
                    direction.x = 0;
                }
                else if (Gdx.input.isKeyPressed(Input.Keys.A) && (kb != KeyBlock.LEFT)) direction.x = -1;
                else if (Gdx.input.isKeyPressed(Input.Keys.D) && (kb != KeyBlock.RIGHT)) direction.x = 1;
                else direction.x = 0;

                if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && !hold && (jumpCount < maxJumps)){
                    hold = true;
                    jumpCount++;
                    gravity = 15;

                }
            }
            if (!Gdx.input.isKeyPressed(Input.Keys.SPACE)){
                hold = false;
            }
            if (kb != KeyBlock.NO_BLOCK){
                if ((kb == KeyBlock.LEFT && !Gdx.input.isKeyPressed(Input.Keys.A)) || (kb == KeyBlock.RIGHT && !Gdx.input.isKeyPressed(Input.Keys.D))){
                    kb = KeyBlock.NO_BLOCK;
                }

            }

            flipPlayer();
        }
        if (multi){
            direction.set(0,0);
            if (!Gdx.input.isKeyPressed(Input.Keys.DOWN)) attacked = false;

            if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)){

                if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && !attacked) {
                    attacked = true;
                    attack();
                }

                if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.LEFT)) direction.x = 0;
                else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && (kb != KeyBlock.LEFT)) direction.x = -1;
                else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && (kb != KeyBlock.RIGHT)) direction.x = 1;
                else direction.x = 0;

                if (Gdx.input.isKeyPressed(Input.Keys.UP) && !hold && (jumpCount < maxJumps)){
                    hold = true;
                    jumpCount++;
                    gravity = 15;

                }
            }
            if (!Gdx.input.isKeyPressed(Input.Keys.UP)){
                hold = false;
            }
            if (kb != KeyBlock.NO_BLOCK){
                if ((kb == KeyBlock.LEFT && !Gdx.input.isKeyPressed(Input.Keys.LEFT)) || (kb == KeyBlock.RIGHT && !Gdx.input.isKeyPressed(Input.Keys.RIGHT))){
                    kb = KeyBlock.NO_BLOCK;
                }

            }

            flipPlayer();
        }


    }

    private void attack() {
        System.out.println("attack");
    }

    public void movePlayer(float delta){
        hitRect.x += direction.x*speed*delta;

        collision("hor");

        gravity -= gravSpeed*delta;
        hitRect.y += gravity;
        collision("ver");

        sprite.setPosition(hitRect.x, hitRect.y);
        midX = sprite.getX()+(sprite.getWidth()*playerScale/2);
        midY = sprite.getY()+(sprite.getHeight()*playerScale/2);

//        move(delta);
        drawHurt();
    }

    private void drawHurt(){
        if (color >= 1){
            color = 1;
        } else {
            color += health/200f;
        }
        sprite.setColor(1,color,color,1);

    }

    private void walljump(boolean multi, boolean boost){

        boolean dir;
        dir = direction.x > 0;

        if (!multi){
            if ((Gdx.input.isKeyPressed(Input.Keys.SPACE)) && boost){
                jumpCount--;
                boostRight = !dir;
                boostLeft = dir;
            }
        }
        if (multi){
            if ((Gdx.input.isKeyPressed(Input.Keys.UP)) && boost){
                jumpCount--;
                boostRight = !dir;
                boostLeft = dir;
            }
        }
    }

    private void collision(String dir){
        int throwback = 10;
        if (dir.equals("hor")){
            for (Rectangle border: borderRecs){
                if (hitRect.overlaps(border)){

                    if(direction.x > 0){ //Right
                        walljump(multi, boostRight);

                        hitRect.x = border.x-hitRect.width;
                    }

                    if(direction.x < 0){ //Left
                        walljump(multi, boostLeft);

                        hitRect.x = border.x+(border.width);
                    }
                    if (!jumped){
                        jumped = true;
                    }
                }
            }
            for (Rectangle enemy: enemyList){
                if (hitRect.overlaps(enemy)){
                    if(direction.x > 0){ //Right
                        kb = KeyBlock.RIGHT;
                        hitRect.x = enemy.x-hitRect.width-throwback;
                    }

                    if(direction.x < 0){ //Left
                        kb = KeyBlock.LEFT;
                        hitRect.x = enemy.x+(enemy.width)+throwback;
                    }
                    health -= 1;
                    color = 0;
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
                        jumpCount = 0;
                        jumped = false;
                        boostRight = true;
                        boostLeft = true;
                    }

                }
            }
            for (Rectangle enemy: enemyList){
                if (hitRect.overlaps(enemy)){
                    if(gravity > 0){ //Up
                        gravity = 0;
                        hitRect.y = sprite.getY()-throwback;
                    }

                    else if(gravity <= 0){ //Down
                        gravity = -gravity;
                        hitRect.y = enemy.y+(enemy.height)+throwback;
                    }
                    health -= 1;
                    color = 0;
                }
            }
        }

        for (Rectangle hidden: hiddenRecs){
            if (hitRect.overlaps(hidden)){
                hiding = true;
                GameScreen.opacity = 0.75f;
                break;
            }
            else if (!multi) {
                GameScreen.opacity = 1;
            }
        }

    }

    public void update(float delta) {
        input();
        movePlayer(delta);
    }
}