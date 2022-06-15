package com.mygdx.game.entities.loot;

import com.badlogic.gdx.math.Vector2;

public class LootHealth extends Loot{
    public LootHealth(Vector2 pos){
        super(pos, "loot/heart.png");
        credits = 0;
        health = 1;
    }
}
