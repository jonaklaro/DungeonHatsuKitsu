package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class GameUI extends Sprite {
      private static Texture settings = new Texture("settingsBoard.png");
      private static Sprite setSprite = new Sprite(settings);
      // private static int graphicScale = Settings.graphicScale;

      private static FreeTypeFontGenerator fontGenerator;
      private static FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
      private static BitmapFont font;
      static Texture gradient;
      static NinePatch healthBar;

      /**
       * Gets called at the initiation of the game to set up the UI (fonts, colors,
       * etc.)
       *
       */
      public static void setup() {
            fontGenerator = new FreeTypeFontGenerator(
                        Gdx.files.internal("fonts\\8-bit-operator\\8bitOperatorPlus-Regular.ttf"));
            fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            fontParameter.size = 30;
            fontParameter.borderWidth = 1;
            fontParameter.borderColor = Color.BLACK;
            fontParameter.color = Color.WHITE;
            font = fontGenerator.generateFont(fontParameter);
            gradient = new Texture("colors.png");
            healthBar = new NinePatch(gradient, 0, 0, 0, 0);
      }

      /**
       * Draws the UI for the Paused Screen
       *
       * @param batch
       * @param camera
       */
      public static void settings(SpriteBatch batch, Camera cam) {
            float zoom = GameScreen.zoom;

            setSprite.setScale(zoom * 8);
            setSprite.setOrigin(0, 0);
            setSprite.setPosition(cam.position.x - setSprite.getWidth() * zoom * 4,
                        cam.position.y - setSprite.getHeight() * zoom * 4);
            setSprite.draw(batch);
      }

      /**
       * Draws Text on the screen
       *
       * @param batch
       * @param text
       * @param x
       * @param y
       */
      public static void drawText(SpriteBatch batch, String text, float x, float y) {
            font.setUseIntegerPositions(false);
            GlyphLayout layout = new GlyphLayout(font, text);
            // draw Text centered on screen
            font.draw(batch, text, x - layout.width / 2, y);

      }
}
