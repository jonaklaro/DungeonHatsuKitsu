package com.mygdx.game;

// import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
// import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Tile extends Sprite {
    Texture texture;
    Sprite sprite;
    Rectangle hitRect;
    ShapeRenderer sr;

    public Tile(float x, float y, int index){
        sr = new ShapeRenderer();

        texture = new Texture("forrest.png");
        sprite = new Sprite(texture);
        sprite.setPosition(x,y);
        sprite.setOrigin(0,0);
        sprite.setRegion((index%12)*16, index / 12*16,16,16);

        hitRect = new Rectangle(x,y,16,16);

    }
}
