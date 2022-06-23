package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class EndScreen extends ScreenAdapter {

      SpriteBatch batch;
      Texture img;
      private Sprite sprite;
      OrthographicCamera camera;
      Game game;
      GameScreen gameScreen;

      public EndScreen(Game game) {
            this.game = game;
            gameScreen = GameScreen.getInstance();

            batch = new SpriteBatch();
            img = new Texture("Menu.png");
            sprite = new Sprite(img);

            float w = Gdx.graphics.getWidth();
            float h = Gdx.graphics.getHeight();

            sprite.setCenter(w / 2, h / 2);
            sprite.setScale(1);

            camera = new OrthographicCamera(w, h);
            camera.position.set(w / 2, h / 2, 0);
      }

      @Override
      public void render(float delta) {

            if (Gdx.input.isKeyPressed(Input.Keys.ENTER))
                  game.setScreen(new GameScreen(game));

            ScreenUtils.clear(0.5f, 0.5f, 0.5f, 1);
            batch.begin();

            sprite.draw(batch);

            batch.setProjectionMatrix(camera.combined);
            camera.update();

            batch.end();
      }

      @Override
      public void resize(int width, int height) {
            camera.viewportWidth = width;
            camera.viewportHeight = height;
      }

      @Override
      public void dispose() {
            batch.dispose();
            img.dispose();
      }

      @Override
      public void hide() {
            super.hide();
      }
}