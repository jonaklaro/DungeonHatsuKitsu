package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Attack extends Character {

    boolean colided;

    public Attack(Vector2 pos, String spriteLink, Player player) {

        super(pos, spriteLink);

        if (player.flipped){
            sprite.flip(true,false);
            sprite.setX(pos.x-sprite.getWidth());
            direction.x = -1;
        } else {
            sprite.setX(pos.x+player.sprite.getWidth()/2);
            direction.x = 1;
        }
        sprite.setRegion(0,0,25,25);
        sprite.setScale(25/64f);
        hitRect = new Rectangle(sprite.getX(),sprite.getY(),25*playerScale,25*playerScale);

        gravity = 0;
        gravSpeed = 0;
        damage = 1;
        speed = 500;
    }
}
