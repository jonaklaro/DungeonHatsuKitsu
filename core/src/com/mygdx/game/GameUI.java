package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
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
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts\\8-bit-operator\\8bitOperatorPlus-Regular.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 30;
        fontParameter.borderWidth = 1;
        fontParameter.borderColor = Color.BLACK;
        fontParameter.color = Color.WHITE;
        font = fontGenerator.generateFont(fontParameter);
    }

    public static void settings(SpriteBatch batch, Camera cam){
        float zoom = GameScreen.zoom;

        setSprite.setScale(zoom*8);
        setSprite.setOrigin(0,0);
        System.out.println(cam.position+", "+zoom);
        setSprite.setPosition(cam.position.x-setSprite.getWidth()*zoom*4, cam.position.y-setSprite.getHeight()*zoom*4);
        setSprite.draw(batch);
    }

    static void drawText(SpriteBatch batch, String text, float x, float y){
        font.setUseIntegerPositions(false);
        font.draw(batch, text, x, y-font.getCapHeight());
    }
}
