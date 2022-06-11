package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Attack extends Entity{
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
        hitRect = new Rectangle(sprite.getX(),sprite.getY(),sprite.getWidth()*playerScale,sprite.getHeight()*playerScale);

        gravity = 0;
        gravSpeed = 0;
    }
}
