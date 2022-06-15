package com.mygdx.game.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;

public class RoundStinger extends Enemy implements Serializable {

    public RoundStinger(Vector2 pos) {
        super(pos);
        width = 32;
        height = 32;
        sprite.setScale(.5f,1);
        sprite.setRegion(32,0,width,height);
        hitRect = new Rectangle(pos.x,pos.y,width*playerScale,height*playerScale);
        setHealth(5);
        damage = 2;
        speed = 200;
    }
}
