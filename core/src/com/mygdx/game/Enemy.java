package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

// import java.util.ArrayList;

public class Enemy extends Entity{
    public Enemy(Vector2 pos) {
        super(pos, "enemy/enemy.png");
        sprite.setScale(1,1);
        sprite.setRegion(0,0,64,64);
        hitRect = new Rectangle(pos.x,pos.y,sprite.getWidth()*playerScale,sprite.getHeight()*playerScale);

    }

    public void update(float delta){
        move(delta);
    }


}
