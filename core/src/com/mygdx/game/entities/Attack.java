package com.mygdx.game.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;

public class Attack extends Character implements Serializable {

    public boolean collided;

    public Attack(Vector2 pos, String spriteLink, Character c) {

        super(pos, spriteLink);


        if (c.flipped){
            sprite.flip(true,false);
            sprite.setX(pos.x-sprite.getWidth());
            direction.x = -1;
        } else {
            sprite.setX(pos.x+c.sprite.getWidth()/2);
            direction.x = 1;
        }
        sprite.setRegion(0,0,16,16);
        sprite.setScale(1,1);

        width = sprite.getWidth()*playerScale;
        height = sprite.getHeight()*playerScale;

        hitRect = new Rectangle(c.getMidX()-width/2,c.getMidY()-height/2,width,height);

        gravSpeed = 0;
        damage = 1;
        speed = 500;
    }
}
