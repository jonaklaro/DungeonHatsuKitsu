package com.mygdx.game.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entities.loot.LootHealth;

import java.io.Serializable;
import java.util.ArrayList;

public class Pegpeg extends Enemy implements Serializable {

    ArrayList<Attack> attacks;

    public Pegpeg(Vector2 pos) {
        super(pos);
        attacks = new ArrayList<>();
        width = 32;
        height = 32;
        sprite.setScale(.5f,1);
        sprite.setRegion(0,0,width,height);
        hitRect = new Rectangle(pos.x,pos.y,width*playerScale,height*playerScale);
        setHealth(7);
        damage = 2;
        speed = 0;
        sprite.flip(true,false);
        range = 600;
    }

    @Override
    public void dropLoot() {
        super.dropLoot();
        GameScreen.loot.add(new LootHealth(new Vector2(getMidX(),getMidY())));
    }

    //the enemy should shoot at the player when in range
    @Override
    void searchPlayer() {
        for (Player p: GameScreen.players){
            float dist = GameScreen.getDistance(p.getMidPosition(), this.getMidPosition());
            if(dist < range){
                //a function to determine which direction the enemy should head in
                determineDirection(p);
                if (dist < range/2f){
                    shoot(p);
                }
            } else{
                //set enemy direction to 0 if player is too far away
                direction.x = 0;
            }
        }
    }

    //a function to make the enemy shoot
    void shoot(Player player) {
//        attacks.add(new Attack(getPosition(),"enemy/bullet.png", this));
//        System.out.println(attacks);
    }
}
