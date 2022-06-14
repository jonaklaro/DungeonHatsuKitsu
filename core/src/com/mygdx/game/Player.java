package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Player extends Character {

    int maxJumps = 1;
    InputController inputController;

    boolean hold = false;
    boolean attacked;

    Attack attack;
    ArrayList<Attack> attacks;

    

    public Player(Vector2 pos, boolean multi, InputController input) {
        super(pos, "character/char_small.png");
        this.multi = multi;
        this.inputController = input;
        attacks = new ArrayList<>();
        speed = 300;
        kb = KeyBlock.NO_BLOCK;
        setHealth(10);
        sprite.setScale( .5f,1);
        sprite.setRegion(10,3,34,61);
        hitRect = new Rectangle(pos.x,pos.y,34*playerScale,sprite.getHeight()*playerScale);

    }

    public void input(){
        direction.set(0,0);
        if (!this.inputController.isAttacking()) attacked = false;

        if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)){

            if (this.inputController.isAttacking() && !attacked) attack();

            if (this.inputController.isMovingLeft() && this.inputController.isMovingRight()){
                direction.x = 0;
            }
            else if (this.inputController.isMovingLeft() && (kb != KeyBlock.LEFT)) direction.x = -1;
            else if (this.inputController.isMovingRight() && (kb != KeyBlock.RIGHT)) direction.x = 1;
            else direction.x = 0;

            if (this.inputController.isJumping() && !hold && (jumpCount < maxJumps)){
                hold = true;
                jumpCount++;
                gravity = 15;

            }
        }
        if (!this.inputController.isJumping()){
            hold = false;
        }
        if (kb != KeyBlock.NO_BLOCK){
            if ((kb == KeyBlock.LEFT && !this.inputController.isMovingLeft()) || (kb == KeyBlock.RIGHT && !this.inputController.isMovingRight())){
                kb = KeyBlock.NO_BLOCK;
            }

        }

        flipCharacter();

    }

    private void attack() {
        attacked = true;
        if (attacks.size() < 3){
            attacks.add(new Attack(getPosition(),"character\\ball.png", this));
        }
    }

    public void movePlayer(float delta){
        for (Attack a: attacks){
            a.move(delta);
            if (a.colided){
                attacks.remove(a);
                break;
            }
            if (attacks.isEmpty()) break;
        }

    }


    public void wallJump(boolean multi, boolean boost){

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

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        batch.setProjectionMatrix(camera.combined);

        if (attacks != null) {
            for (Attack a : attacks) {
                a.render(batch, camera);
            }
        }

        sprite.draw(batch);
    }


    public void update(float delta) {
        input();
        movePlayer(delta);
        entityUpdate(delta);
    }
}