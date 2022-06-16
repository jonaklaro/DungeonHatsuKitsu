package com.mygdx.game.entities.loot;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entities.Entity;

public class Loot extends Entity {
    public int credits;
    public int health;

    public Loot(Vector2 pos, String spriteLink) {
        super(pos, spriteLink);
        width = sprite.getWidth()*playerScale;
        height = sprite.getHeight()*playerScale;
        hitRect = new Rectangle(pos.x-width/2, pos.y-height/2,width,height);

    }

    public void update(float delta){
        applyGravity(delta);
        updateSprite();
    }
}
