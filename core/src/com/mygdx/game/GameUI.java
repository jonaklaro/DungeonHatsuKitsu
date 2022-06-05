package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;

public class GameUI extends Sprite {
    private static Texture settings = new Texture("settingsBoard.png");
    private static Sprite setSprite = new Sprite(settings);
    private static int graphicScale = Settings.graphicScale;

    private static FreeTypeFontGenerator fontGenerator;
    private static FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private static BitmapFont font;


    public static void setup(){
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/calibril.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 50;
        fontParameter.borderWidth = 5;
        fontParameter.borderColor = Color.BLACK;
        fontParameter.color = Color.WHITE;
        font = fontGenerator.generateFont(fontParameter);
    }

    public static void settings(SpriteBatch batch, Vector2 cam){
        setSprite.setScale(4);
        setSprite.setOrigin(0,0);
        setSprite.setPosition(cam.x-setSprite.getWidth()*graphicScale/2, cam.y-setSprite.getHeight()*graphicScale/2);
        setSprite.draw(batch);
    }

    static void drawStats(SpriteBatch batch, Player player, Vector2 pos){
        font.setUseIntegerPositions(false);
        font.draw(batch, Integer.toString(player.health), pos.x-Settings.width/4f, pos.y+Settings.height/5f);
    }
}
