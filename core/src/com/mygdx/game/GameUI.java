package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameUI extends Sprite {
    static Texture settings = new Texture("settingsBoard.png");
    static Sprite setSprite = new Sprite(settings);
    static int graphicScale = Settings.graphicScale;

    public static void settings(SpriteBatch batch, Player player){
        setSprite.setScale(4);
        setSprite.setOrigin(0,0);
        setSprite.setPosition(player.midX-setSprite.getWidth()*graphicScale/2, player.midY-setSprite.getHeight()*graphicScale/2);
        setSprite.draw(batch);
    }
}
