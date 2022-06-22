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

import com.mygdx.game.entities.Entity;
import com.mygdx.game.entities.Enemy;
import com.mygdx.game.entities.EnemyBullet;
import com.mygdx.game.entities.Player;
import com.mygdx.game.entities.PlayerBullet;
import com.mygdx.game.entities.loot.Loot;

import java.io.*;
import java.util.ArrayList;

public class GameScreen extends ScreenAdapter implements Serializable {

      // static instance of the game screen
      public static GameScreen instance;

      // Imports
      SpriteBatch batch;
      Texture background;
      Sprite backgroundSprite;
      OrthographicCamera camera;
      public static float zoom;
      public static int minDist;
      public static int distFactor;
      ShapeRenderer shapeRenderer;

      State state;

      public enum State {
            READY,
            RUNNING,
            PAUSED,
            OVER
      }

      int[][] hidden;
      int[][] map;

      // Graphics

      float graphicScale = Settings.graphicScale;
      int tilesize = Settings.tilesize;

      boolean multi;
      Map mapp;
      Game game;

      public static float opacity = 1;

      public static ArrayList<Enemy> enemies;
      public static ArrayList<Player> players;
      public ArrayList<Loot> loot;
      public ArrayList<PlayerBullet> playerBullets;
      public ArrayList<EnemyBullet> enemyBullets;

      Texture titleTexture;
      Sprite titleSprite;

      GameData gameData;
      CollisionController collisionController;

      public GameScreen(Game game) {
            this.game = game;

            state = State.READY;
            batch = new SpriteBatch();
            mapp = new Map();
            Map.load();

            shapeRenderer = new ShapeRenderer();
            camera = new OrthographicCamera();

            map = Map.mapp;
            hidden = Map.hidden;

            loot = new ArrayList<>();
            players = new ArrayList<>();
            enemies = new ArrayList<>();
            playerBullets = new ArrayList<>();
            enemyBullets = new ArrayList<>();

            camera.position.set(mapp.getPlayer("assets/maps/level1_entities.csv"), 0);
            minDist = 500;
            distFactor = 2000;

            zoom = (float) minDist / distFactor * 2;
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
            titleSprite.setPosition(camera.position.x + titleSprite.getWidth() / 2,
                        -camera.position.y / 2 - titleSprite.getHeight() / 2);

            gameData = new GameData();

            // set instance of the game screen
            instance = this;
            enemies = mapp.getEnemies("assets/maps/level1_enemies.csv", enemies);

      }

      @Override
      public void render(float delta) {
            update(delta);
            draw();
      }

      public void update(float deltaTime) {
            if (deltaTime > 0.1f)
                  deltaTime = 0.1f;

            switch (state) {
                  case READY:
                        updateReady();
                        break;
                  case RUNNING:
                        updateRunning(deltaTime);
                        break;
                  case PAUSED:
                        updatePaused(deltaTime);
                        break;
                  case OVER:
                        game.setScreen(new MenuScreen(game));
                        // state = State.READY;
            }
      }

      public void draw() {
            switch (state) {
                  case READY:
                        drawReady();
                        break;
                  case RUNNING:
                        drawRunning();
                        break;
                  case PAUSED:
                        drawPaused();
                        break;
                  default:
                        break;
            }
      }

      private void updateReady() {
            InputController inputController_p1 = new InputController(
                        Input.Keys.A,
                        Input.Keys.D,
                        Input.Keys.SPACE,
                        Input.Keys.S);

            InputController inputController_p2 = new InputController(
                        Input.Keys.LEFT,
                        Input.Keys.RIGHT,
                        Input.Keys.UP,
                        Input.Keys.DOWN);

            if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
                  multi = false;
                  state = State.RUNNING;
                  players.add(new Player(mapp.getPlayer("assets/maps/level1_entities.csv"), false,
                              inputController_p1));
            }

            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                  multi = true;
                  players.add(new Player(mapp.getPlayer("assets/maps/level1_entities.csv"), false,
                              inputController_p1));
                  players.add(new Player(mapp.getPlayer("assets/maps/level1_entities.csv"), true,
                              inputController_p2));
                  state = State.RUNNING;
            }
            collisionController = new CollisionController();
            loadGameState();

      }

      public void updatePaused(float delta) {
            if (Gdx.input.justTouched()) {
                  state = State.RUNNING;
            }

      }

      public void updateRunning(float delta) {
            if (Gdx.input.justTouched()) {
                  state = State.PAUSED;
                  return;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.F)) {
                  saveGameState();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.G)) {
                  loadGameState();
            }

            collisionController.checkCollisions();

            for (Player p : players) {
                  int size = players.size();
                  p.update(delta);

                  if (p.getHealth() <= 0) {
                        players.remove(p);
                        break;
                  }

                  if (size != players.size())
                        break;
            }

            for (Enemy e : enemies) {
                  int size = enemies.size();
                  e.update(delta);

                  if (e.getHealth() <= 0) {
                        e.dropLoot();
                        enemies.remove(e);
                        break;
                  }

                  if (size != enemies.size())
                        break;
            }

            for (Loot l : loot) {
                  l.update(delta);
            }

            for (EnemyBullet eb : enemyBullets) {
                  int size = enemyBullets.size();
                  eb.update(delta);
                  if (size != enemyBullets.size())
                        break;
            }

            if (players.isEmpty() || enemies.isEmpty())
                  state = State.OVER;

      }

      private void drawPaused() {
            drawRunning();
            Gdx.gl.glEnable(GL30.GL_BLEND);
            Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor((new Color(0, 0, 0, 0.5f)));
            shapeRenderer.rect(0, 0, camera.viewportWidth, camera.viewportHeight);
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

            camera.position.set(getCameraPos(), 0);

            camera.update();

            for (Player p : players) {
                  p.render(batch, camera);

                  if (players.size() > 1) {
                        float dist = getDistance((players.get(0).getMidPosition()),
                                    players.get(1).getMidPosition());
                        if (dist > minDist) {
                              zoom = dist / distFactor * 2;
                              camera.zoom = zoom;
                        }
                  }
            }

            for (Enemy e : enemies) {
                  e.render(batch, camera);
            }

            for (Loot l : loot) {
                  l.render(batch, camera);
            }

            for (PlayerBullet pb : playerBullets) {
                  pb.render(batch, camera);
            }

            for (EnemyBullet eb : enemyBullets) {
                  eb.render(batch, camera);
            }

            Map.drawMap(map, batch, 1);
            Map.drawMap(hidden, batch, opacity);

            for (Player p : players) {
                  GameUI.drawText(batch, "Health: " + p.getHealth(), p.getMidPosition().x, p.getMidPosition().y);
            }

            backgroundSprite.setPosition((float) (getCameraPos().x / 1.5) - 2 * tilesize * graphicScale,
                        (getCameraPos().y / 2) - 5 * tilesize * graphicScale);

            batch.end();
      }

      public static float getDistance(Vector2 pos1, Vector2 pos2) {
            return (float) Math.sqrt(Math.pow((pos1.x - pos2.x), 2) + Math.pow(pos1.y - pos2.y, 2));
      }

      private Vector2 getCameraPos() {
            float x = .0f;
            float y = .0f;

            // get avg position of all players
            for (Player p : players) {
                  x += p.getMidPosition().x;
                  y += p.getMidPosition().y;
            }
            x /= players.size();
            y /= players.size();

            // leap to position
            float leap = 0.1f;
            x = leap * x + (1 - leap) * camera.position.x;
            y = leap * y + (1 - leap) * camera.position.y;

            return new Vector2(x, y);
      }

      private void drawReady() {
            batch.begin();

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
      public void dispose() {
            batch.dispose();
            background.dispose();
      }

      @Override
      public void hide() {
            super.hide();
      }

      public void saveGameState() {
            try {
                  gameData.loadInfo(players);
                  gameData.writeGameState();

            } catch (IOException e) {
                  e.printStackTrace();
            }
      }

      public void loadGameState() {

            try {
                  gameData.loadGameState();
                  gameData.writeInfo(players);
            } catch (IOException | ClassNotFoundException e) {
                  return;
                  // e.printStackTrace();
            }
      }

      public static GameScreen getInstance() {
            return instance;
      }

      public ArrayList<Entity> getAllEntities() {
            ArrayList<Entity> entities = new ArrayList<Entity>();
            // cast players to entityList
            for (Player p : players) {
                  entities.add((Entity) p);
            }
            // cast enemies to entityList
            for (Enemy e : enemies) {
                  entities.add((Entity) e);
            }
            // cast loot to entityList
            for (Loot l : loot) {
                  entities.add((Entity) l);
            }
            // cast PlayerBullet to entityList
            for (PlayerBullet b : playerBullets) {
                  entities.add((Entity) b);
            }
            // cast EnemyBullet to entityList
            for (EnemyBullet b : enemyBullets) {
                  entities.add((Entity) b);
            }
            return entities;
      }

      public State getState() {
            return state;
      }
}