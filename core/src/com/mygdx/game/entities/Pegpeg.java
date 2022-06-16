package com.mygdx.game.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entities.loot.LootHealth;

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
        speed = 0;
        sprite.flip(true,false);
        range = 600;
    }

    @Override
    public void dropLoot() {
        super.dropLoot();
        GameScreen.loot.add(new LootHealth(new Vector2(getMidX(),getMidY())));
    }
}
