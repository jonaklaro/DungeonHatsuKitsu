package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class OptionScreen extends ScreenAdapter {
    SpriteBatch batch;
    Texture img;
    private Game game;

    public OptionScreen(Game game) {
        this.game = game;
        batch = new SpriteBatch();
        img = new Texture("Options.png");
    }

    @Override
    public void render (float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        batch.draw(img, 0, 0, 192, 108);
        batch.end();
    }

    @Override
    public void dispose () {
        batch.dispose();
        img.dispose();
    }

    @Override
    public void hide() {
        super.hide();
    }
}