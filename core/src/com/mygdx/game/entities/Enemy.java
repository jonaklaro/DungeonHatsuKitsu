package com.mygdx.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entities.loot.Loot;

public class Enemy extends Character {
    int width;
    int height;
    int range;
    public Enemy(Vector2 pos) {
        super(pos, "enemy/enemies.png");
        range = 100;
    }

    void dropLoot(){
        System.out.println("loot drop");
    }

    public void update(float delta){
        searchPlayer();
        entityUpdate(delta, GameScreen.enemies);
        flipCharacter();
    }

    //a function to let the enemy know when the player is in range
    void searchPlayer() {
        for (Player p: GameScreen.players){
            float dist = GameScreen.getDistance(p.getMidPosition(), this.getMidPosition());
            if(dist < range){
                //a function to determine which direction the enemy should head in
                determineDirection(p);
            } else{
                //set enemy direction to 0 if player is too far away
                direction.x = 0;
            }
        }
    }

    //todo: make this function working
    //a function to determine which direction the enemy should head in
    void determineDirection(Player p) {
        if(p.getMidPosition().x > this.getMidPosition().x){
            direction.x = 1;
        }
        else{
            direction.x = -1;
        }
    }

}
