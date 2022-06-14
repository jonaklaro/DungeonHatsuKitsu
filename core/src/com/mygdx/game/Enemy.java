package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends Character {
    public Enemy(Vector2 pos) {
        super(pos, "enemy/enemy.png");
        sprite.setScale(1,1);
        sprite.setRegion(0,0,64,64);
        hitRect = new Rectangle(pos.x,pos.y,sprite.getWidth()*playerScale,sprite.getHeight()*playerScale);
        setHealth(5);
        damage = 2;
        speed = 200;
    }

    public void update(float delta){
        entityUpdate(delta);
    }


}
