package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player extends Entity {

    int jumpCount;
    boolean jumped = false;
    boolean boostRight = false;
    boolean boostLeft = false;
    int maxJumps = 1;

    boolean hold = false;


    public Player(Vector2 pos) {
        super(pos, "character/char_small.png");
        sprite.setScale( .5f,1);
        sprite.setRegion(10,3,34,61);
    }

    public void input(boolean multi){

        if (!multi){
            direction.set(0,0);
            if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)){
                if (Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.D)) direction.x = 0;
                else if (Gdx.input.isKeyPressed(Input.Keys.A)) direction.x = -1;
                else if (Gdx.input.isKeyPressed(Input.Keys.D)) direction.x = 1;
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

            flipPlayer();
        }
        if (multi){
            direction.set(0,0);
            if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)){
                if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.LEFT)) direction.x = 0;
                else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) direction.x = -1;
                else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) direction.x = 1;
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

            flipPlayer();
        }


    }

    public void move(float delta, boolean multi){
        hitRect.x += direction.x*speed*delta;

        collision("hor", multi);

        gravity -= gravSpeed*delta;
        hitRect.y += gravity;
        collision("ver", multi);

        sprite.setPosition(hitRect.x, hitRect.y);
        midX = sprite.getX()+(sprite.getWidth()*playerScale/2);
        midY = sprite.getY()+(sprite.getHeight()*playerScale/2);

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

    private void collision(String dir, boolean multi){
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

                        hitRect.x = enemy.x-hitRect.width;
                    }

                    if(direction.x < 0){ //Left

                        hitRect.x = enemy.x+(enemy.width);
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
                        jumpCount = 0;
                        jumped = false;
                        boostRight = true;
                        boostLeft = true;
                    }

                }
            }
        }
        for (Rectangle hidden: hiddenRecs){
            if (hitRect.overlaps(hidden)){

                GameScreen.opacity = 0.75f;
                break;
            }
            else {
                GameScreen.opacity = 1;
            }
        }
    }

    public void update(float delta, boolean multi) {
        input(multi);
        move(delta, multi);
    }
}