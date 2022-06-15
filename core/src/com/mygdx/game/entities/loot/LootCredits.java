package com.mygdx.game.entities.loot;

import com.badlogic.gdx.math.Vector2;

public class LootCredits extends Loot{
    public LootCredits(Vector2 pos){
        super(pos, "border.png");
        credits = 1;
        health = 0;
    }
}
