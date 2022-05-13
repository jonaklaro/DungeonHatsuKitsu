package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen extends ScreenAdapter {

    // Imports
    SpriteBatch batch;
    Texture tileMap;
    Texture background;
    Sprite backgroundSprite;
    OrthographicCamera camera;
    ShapeRenderer shapeRenderer;

    static final int GAME_READY = 0;
    static final int GAME_RUNNING = 1;
    static final int GAME_PAUSED = 2;
    static final int GAME_LEVEL_END = 3;
    static final int GAME_OVER = 4;

    int state;

    int[][] hidden = new int[100][100];

    //Graphics

    float graphicScale = Settings.graphicScale;
    int tilesize = Settings.tilesize;


    Player player;
    Map mapp;
    Game game;

    public static float opacity = 1;


    public GameScreen(Game game) {
        this.game = game;
        state = GAME_READY;
        batch = new SpriteBatch();
        tileMap = new Texture("forrest.png");
        mapp = new Map();

        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();

        Settings.map = mapp.readMap("assets/maps/newLevel_map.csv");
        hidden = mapp.readMap("assets/maps/newLevel_hidden_obj.csv");

        player = new Player(mapp.getPlayer("assets/maps/newLevel_entities.csv"));
        camera.position.set(player.getPosition(),0);
        camera.zoom = 0.5F;

        background = new Texture("background.png");
        backgroundSprite = new Sprite(background);
        backgroundSprite.scale(20);
    }

    @Override
    public void render (float delta) {
        update(delta);
        draw();
    }

    public void update (float deltaTime) {
        if (deltaTime > 0.1f) deltaTime = 0.1f;

        switch (state) {
            case GAME_READY:
                updateReady();
                break;
            case GAME_RUNNING:
                updateRunning(deltaTime);
                break;
            case GAME_PAUSED:
                updatePaused(deltaTime);
                break;
        }
    }

    public void draw(){
        switch (state) {
            case GAME_READY:
                drawReady();
                break;
            case GAME_RUNNING:
                drawRunning();
                break;
            case GAME_PAUSED:
                drawPaused();
                break;
        }
    }

    private void updateReady () {
        if (Gdx.input.justTouched()) {
            state = GAME_RUNNING;
        }
    }

    public void updatePaused(float delta){
        if (Gdx.input.justTouched()) {
            state = GAME_RUNNING;
        }

    }


    public void updateRunning(float delta){
        if (Gdx.input.justTouched()) {
            state = GAME_PAUSED;
            return;
        }

        player.update(delta);

    }
    private void drawPaused(){
        drawRunning();
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor((new Color(0,0,0,0.5f)));
        shapeRenderer.rect(0,0, camera.viewportWidth, camera.viewportHeight);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL30.GL_BLEND);

        batch.begin();
        GameUI.settings(batch, player);
        batch.end();
    }

    private void drawRunning() {
        batch.begin();

        backgroundSprite.draw(batch);

        camera.position.set(player.midX,player.midY,0);

        camera.update();

        player.render(batch,camera);
        Map.drawMap(Settings.map, batch, 1);
        Map.drawMap(hidden,batch, opacity);



        backgroundSprite.setPosition((float) (player.getX()/1.5)-2*tilesize*graphicScale,(player.getY()/2)-5*tilesize*graphicScale);

        batch.end();
    }

    private void drawReady() {
        batch.begin();
//        backgroundSprite.scale(3);
        backgroundSprite.draw(batch);
        batch.end();
    }




    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
    }

    @Override
    public void dispose () {
        batch.dispose();
        tileMap.dispose();
    }

    @Override
    public void hide() {
        super.hide();
    }
}