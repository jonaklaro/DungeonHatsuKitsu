package com.mygdx.game.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;

public class Pegpeg extends Enemy implements Serializable {
    public Pegpeg(Vector2 pos) {
        super(pos);
        width = 32;
        height = 32;
        sprite.setScale(.5f,1);
        sprite.setRegion(0,0,width,height);
        hitRect = new Rectangle(pos.x,pos.y,width*playerScale,height*playerScale);
        setHealth(7);
        damage = 2;
        speed = 300;
    }
}
