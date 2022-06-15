package com.mygdx.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entities.loot.Loot;

public class Enemy extends Character {
    int width;
    int height;
    public Enemy(Vector2 pos) {
        super(pos, "enemy/enemies.png");
    }

    public void update(float delta){
        entityUpdate(delta, GameScreen.enemies);
    }

    public void dropLoot(){
        System.out.println("loot drop");
    }

}
