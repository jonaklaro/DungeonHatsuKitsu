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
    Texture background;
    Sprite backgroundSprite;
    OrthographicCamera camera;
    public static float zoom;
    public static int minDist;
    public static int distFactor;
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

    boolean multi;
    Map mapp;
    Game game;

    public static float opacity = 1;

    static ArrayList<Enemy> enemies;
    ArrayList<Player> players;

    Texture titleTexture;
    Sprite titleSprite;

    public GameScreen(Game game) {
        this.game = game;
        state = GAME_READY;
        batch = new SpriteBatch();
        mapp = new Map();
        Map.load();

        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();

        map = Map.mapp;
        hidden = Map.hidden;

        players = new ArrayList<>();
        enemies = new ArrayList<>();
        enemies = mapp.getEnemies("assets/maps/newLevel_enemies.csv", enemies);

        camera.position.set(mapp.getPlayer("assets/maps/newLevel_entities.csv"),0);
        minDist = 500;
        distFactor = 2000;
        zoom = (float) minDist/distFactor*2;
        camera.zoom = zoom;

        background = new Texture("background.png");
        backgroundSprite = new Sprite(background);
        backgroundSprite.scale(20);
        GameUI.setup();
        Settings.width = Gdx.graphics.getWidth();
        Settings.height = Gdx.graphics.getHeight();

        titleTexture = new Texture("title.png");
        titleSprite = new Sprite(titleTexture);
        titleSprite.scale(8);
        titleSprite.setPosition(camera.position.x+titleSprite.getWidth()/2,-camera.position.y/2-titleSprite.getHeight()/2);
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
        InputController inputController_p1 = new InputController(
            Input.Keys.A, 
            Input.Keys.D, 
            Input.Keys.SPACE, 
            Input.Keys.S
        );

        InputController inputController_p2 = new InputController(
            Input.Keys.LEFT,
            Input.Keys.RIGHT,
            Input.Keys.UP,
            Input.Keys.DOWN
        );

        if (Gdx.input.justTouched()) {
            multi = false;
            state = GAME_RUNNING;
            players.add(new Player(mapp.getPlayer("assets/maps/newLevel_entities.csv"), false, inputController_p1));
        }
        
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            multi = true;
            players.add(new Player(mapp.getPlayer("assets/maps/newLevel_entities.csv"), false, inputController_p1));
            players.add(new Player(mapp.getPlayer("assets/maps/newLevel_entities.csv"), true, inputController_p2));
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

        for (Player p: players){
            p.update(delta);
        }

        for (Enemy e: enemies) {
            int size = enemies.size();
            e.update(delta);

            if (size != enemies.size()) break;
        }

        for (Player p: players){
            if (p.getHealth() <= 0) {
                state = GAME_OVER;
                break;
            }
        }
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
        GameUI.settings(batch, camera);
        batch.end();
    }

    private void drawRunning() {
        batch.begin();

        backgroundSprite.draw(batch);
        backgroundSprite.draw(batch);

        camera.position.set(getCameraPos(),0);



        camera.update();

        for (Player p: players){
            p.render(batch, camera);

            if (p.multi){
                if (playerDistance() > minDist){
                    zoom = playerDistance()/distFactor*2;
                    camera.zoom = zoom;

                }
            }
        }


        for (Enemy e: enemies) {
            e.render(batch, camera);
        }


        Map.drawMap(map, batch, 1);
        Map.drawMap(hidden,batch, opacity);

        for (Player p: players){
            GameUI.drawText(batch, "Health: "+ p.getHealth(), getCameraPos().x-Settings.width/4f, getCameraPos().y-Settings.height/5f);
        }



        backgroundSprite.setPosition((float) (getCameraPos().x/1.5)-2*tilesize*graphicScale,(getCameraPos().y/2)-5*tilesize*graphicScale);


        batch.end();
    }

    private float playerDistance(){
        return (float) Math.sqrt(Math.pow((players.get(0).getMidX()-players.get(1).getMidX()),2)+Math.pow(players.get(0).getMidY()-players.get(1).getMidY(),2));
    }

    private Vector2 getCameraPos(){
        float x;
        float y;

        if (players.size() > 1) {
            x = (players.get(0).getMidX() + players.get(1).getMidX()) / 2;
            y = (players.get(0).getMidY() + players.get(1).getMidY()) / 2;
        } else {
            x = players.get(0).getMidX();
            y = players.get(0).getMidY();
        }

        return new Vector2(x,y);
    }

    private void drawReady() {
        batch.begin();
//        backgroundSprite.scale(3);


        backgroundSprite.draw(batch);

        titleSprite.draw(batch);

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
        background.dispose();
    }

    @Override
    public void hide() {
        super.hide();
    }
}