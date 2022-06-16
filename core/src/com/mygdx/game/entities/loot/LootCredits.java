package com.mygdx.game.entities.loot;

import com.badlogic.gdx.math.Vector2;

public class LootCredits extends Loot{
    public LootCredits(Vector2 pos){
        super(pos, "loot/coinAnim.png");
        sprite.setScale(.5f, 1);
        sprite.setRegion(0,0,16,16);
        credits = 1;
        health = 0;
    }
}
