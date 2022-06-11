package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player extends Entity {

    int maxJumps = 1;
    InputController inputController;

    boolean hold = false;
    boolean attacked;

    Attack attack;

    

    public Player(Vector2 pos, boolean multi, InputController input) {
        super(pos, "character/char_small.png");
        this.multi = multi;
        kb = KeyBlock.NO_BLOCK;
        health = 10;
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

        flipPlayer();

    }

    private void attack() {
        attacked = true;
        if (attacks.size() < 2){
            attacks.add(new Attack(getPosition(),"character\\attack.png", this));
            System.out.println(attacks);
            System.out.println("attack");
        }
    }

    public void movePlayer(float delta){
        for (Attack a: attacks){
            a.move(delta);
            if (attacks.isEmpty()) break;
        }
//        if (attack != null){
//            attack.move(delta);
//        }

        move(delta);

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
//        if (attack != null){
//            attack.render(batch, camera);
//        }

        sprite.draw(batch);
    }


    public void update(float delta) {
        input();
        movePlayer(delta);
    }
}