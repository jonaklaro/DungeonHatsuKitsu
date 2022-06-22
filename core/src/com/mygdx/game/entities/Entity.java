package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Settings;

import java.io.Serializable;
import java.util.ArrayList;

public class Entity implements Serializable {
      Texture texture;
      public Sprite sprite;
      public Rectangle hitRect;

      static ArrayList<Rectangle> borderRecs;
      static ArrayList<Rectangle> hiddenRecs;

      Rectangle rec;
      GameScreen gameScreen;

      int tilesize = Settings.tilesize;
      int graphicScale = Settings.graphicScale;

      int health;
      int maxHealth;
      int credits;

      public float width;
      public float height;
      private float midX;
      public float midY;

      public float playerScale = 1;

      Vector2 direction = new Vector2();
      float gravity;
      double gravSpeed = 35;

      int throwback = 10;
      int damage;
      float color = 1;

      public boolean collided;

      enum KeyBlock {
            NO_BLOCK,
            RIGHT,
            LEFT
      }

      KeyBlock kb;

      // basic Constructor for every entity
      public Entity(Vector2 pos, String spriteLink) {
            sprite = createSprite(spriteLink);
            sprite.setPosition(pos.x, pos.y);
      }

      // a render Method
      public void render(SpriteBatch batch, OrthographicCamera camera) {
            batch.setProjectionMatrix(camera.combined);
            sprite.draw(batch);
      }

      // basic Sprite create function
      Sprite createSprite(String link) {
            texture = new Texture(link);

            sprite = new Sprite(texture);
            sprite.setOrigin(0, 0);
            return sprite;
      }

      Sprite getSpriteByPos(int row, int col) {
            int grid = tilesize * 2;
            return new Sprite(texture, col * grid, row * grid, grid, grid);
      }

      // Add rectangles to list (for borders)
      ArrayList<Rectangle> createRectList(int[][] map) {
            ArrayList<Rectangle> recs = new ArrayList<>();
            for (int row = 0; row < map.length; row++) {
                  for (int col = 0; col < map[row].length; col++) {
                        if (map[row][col] != -1) {
                              float x = (col * tilesize * graphicScale);
                              float y = (float) (-(row) * tilesize * graphicScale);
                              rec = new Rectangle(x, y, (float) (tilesize * graphicScale),
                                          (float) (tilesize * graphicScale));
                              recs.add(rec);
                        }
                  }
            }
            return recs;
      }

      public int getHealth() {
            return health;
      }

      public void setHealth(int health) {
            this.health = health;
      }

      // entity health gets subtracted by damage
      public void receiveDamage(int damage) {
            this.health -= damage;
            this.color = 0;
      }

      // on collide
      public void onCollide(Entity collidingObject) {
      }

      // A method to detect horizontal and vertical collisions
      public boolean collision(String dir) {
            // horizontal detection
            if (dir.equals("hor")) {
                  // for every border, check if it's overlapping with own hit box
                  for (Rectangle border : borderRecs) {
                        if (hitRect.overlaps(border)) {
                              if (direction.x > 0) { // character is moving Right
                                    // if character is player, see if they are doing a wall jump
                                    if (this.getClass() == Player.class) {
                                          ((Player) this).wallJump(((Player) this).boostRight);
                                    }
                                    // set x coordinate of hit box to x coordinate of the border - hit box width
                                    hitRect.x = border.x - hitRect.width;
                              }

                              if (direction.x < 0) { // Left
                                    if (this.getClass() == Player.class) {
                                          ((Player) this).wallJump(((Player) this).boostLeft);
                                    }
                                    hitRect.x = border.x + (border.width);
                              }

                              // if wall is hit by bullet, set bullet.collided to true
                              if (this.getClass() == PlayerBullet.class) {
                                    ((PlayerBullet) this).collided = true;
                                    break;
                              }
                              if (this.getClass() == EnemyBullet.class) {
                                    ((EnemyBullet) this).collided = true;
                                    break;
                              }
                              if (this.getClass() == Player.class)
                                    ((Player) this).collided = true;

                              return true;
                        }
                  }

            }

            // Vertical colDetection
            if (dir.equals("ver")) {
                  // same as horizontal but the direction is now gravity
                  for (Rectangle border : borderRecs) {
                        if (border.overlaps(hitRect)) {
                              // if colliding with topside, set gravity to 0 and reset hit rect to previous
                              // Sprite pos
                              if (gravity > 0) { // Up
                                    gravity = 0;
                                    hitRect.y = sprite.getY();
                              }
                              // if standing or falling set gravity to 0 and set hit rect to border pos +
                              // border height
                              else if (gravity <= 0) { // Down
                                    gravity = 0;
                                    hitRect.y = border.y + (border.height);

                                    // if player is Standing on bottom, reset Parameters
                                    if (this.getClass() == Player.class) {
                                          ((Player) this).resetPlayerParameters();
                                    }
                              }

                              // Collision detection for the hidden recs and Loot
                              if (this.getClass() == Player.class) {
                                    ((Player) this).hiddenColDet();
                                    // ((Player) this).lootColDet();
                              }

                              return true;
                        }
                  }
            }

            if (this.getClass() == Player.class)
                  ((Player) this).collided = false;

            return false;
      }

      public void applyGravity(float delta) {
            gravity -= gravSpeed * delta;
            hitRect.y += gravity;
            collision("ver");
      }

      public void updateSprite() {
            // set Sprite pos to calculated pos and set midX and midY
            sprite.setPosition(hitRect.x, hitRect.y);
            midX = hitRect.x + hitRect.width / 2;
            midY = hitRect.y + hitRect.height / 2;
            // setMidX(sprite.getX()+(sprite.getWidth()*playerScale/2));
            // setMidY(sprite.getY()+(sprite.getHeight()*playerScale/2));
      }

      public Vector2 getPosition() {
            return new Vector2(sprite.getX(), sprite.getY());
      }

      public void setPosition(Vector2 position) {
            hitRect.setX(position.x);
            hitRect.setY(position.y);
      }

      public float getX() {
            return sprite.getX();
      }

      public float getY() {
            return sprite.getY();
      }

      public float getMidX() {
            return midX;
      }

      public float getMidY() {
            return midY;
      }

      public void setMidX(float midX) {
            this.midX = midX;
      }

      public void setMidY(float midY) {
            this.midY = midY;
      }

      public int getDamage() {
            return damage;
      }

      public Vector2 getMidPosition() {
            return new Vector2(getMidX(), getMidY());
      }

      public Rectangle getHitRect() {
            return this.hitRect;
      }
}