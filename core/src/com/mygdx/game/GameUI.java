package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class GameUI extends Sprite {
    static Texture settings = new Texture("settingsBoard.png");
    static Sprite setSprite = new Sprite(settings);
    static int graphicScale = Settings.graphicScale;

    public static void settings(SpriteBatch batch, Vector2 cam){
        setSprite.setScale(4);
        setSprite.setOrigin(0,0);
        setSprite.setPosition(cam.x-setSprite.getWidth()*graphicScale/2, cam.y-setSprite.getHeight()*graphicScale/2);
        setSprite.draw(batch);
    }
}
