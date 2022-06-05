package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

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
    static final int GAME_OVER = 3;

    int state;


    int[][] hidden;
    int[][] map;

    //Graphics

    float graphicScale = Settings.graphicScale;
    int tilesize = Settings.tilesize;


    Player player;
    Player player2;

    boolean multi;
    Map mapp;
    Game game;

    public static float opacity = 1;

    ArrayList<Enemy> enemies;

    public GameScreen(Game game) {
        this.game = game;
        state = GAME_READY;
        batch = new SpriteBatch();
        tileMap = new Texture("forrest.png");
        mapp = new Map();
        Map.load();

        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();

        map = Map.mapp;
        hidden = Map.hidden;

        player = new Player(mapp.getPlayer("assets/maps/newLevel_entities.csv"));

        enemies = new ArrayList<>();
        enemies = mapp.getEnemies("assets/maps/newLevel_enemies.csv", enemies);

        camera.position.set(player.getPosition(),0);
        camera.zoom = 0.5F;

        background = new Texture("background.png");
        backgroundSprite = new Sprite(background);
        backgroundSprite.scale(20);
        GameUI.setup();
        Settings.width = Gdx.graphics.getWidth();
        Settings.height = Gdx.graphics.getHeight();
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
            case GAME_OVER:
                game.setScreen(new MenuScreen(game));
//                state = GAME_READY;
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
            multi = false;
            state = GAME_RUNNING;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            multi = true;
            player2 = new Player(mapp.getPlayer("assets/maps/newLevel_entities.csv"));
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

        player.update(delta, false);

        for (Enemy e: enemies) {
            e.update(delta);
        }

        if (multi){
            player2.update(delta, true);
        }

        if (player.health <= 0) state = GAME_OVER;
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
        GameUI.settings(batch, getCameraPos());
        batch.end();
    }

    private void drawRunning() {
        batch.begin();

        backgroundSprite.draw(batch);

        camera.position.set(getCameraPos(),0);

        camera.update();

        player.render(batch, camera);


        if (multi){
            player2.render(batch,camera);
        }

        for (Enemy e: enemies) {
            e.render(batch, camera);
        }


        Map.drawMap(map, batch, 1);
        Map.drawMap(hidden,batch, opacity);
        GameUI.drawStats(batch, player, getCameraPos());



        backgroundSprite.setPosition((float) (getCameraPos().x/1.5)-2*tilesize*graphicScale,(getCameraPos().y/2)-5*tilesize*graphicScale);


        batch.end();
    }

    private Vector2 getCameraPos(){
        if (!multi){
            return new Vector2(player.midX,player.midY);
        }
        float x = (player.midX+player2.midX)/2;
        float y = (player.midY+player2.midY)/2;
        return new Vector2(x,y);
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