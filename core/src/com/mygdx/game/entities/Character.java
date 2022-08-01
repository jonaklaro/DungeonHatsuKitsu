package com.mygdx.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Map;
import com.mygdx.game.Settings;

import java.util.ArrayList;

public class Character extends Entity {
      boolean flipped;
      boolean multi;

      boolean boostRight = false;
      boolean boostLeft = false;

      float reactionTime;
      float shootDelay;
      float shootDelayMax;
      float speed;
      float maxSpeed;

      int jumpCount;
      boolean jumped;

      Map map;

      int[][] borders;
      int[][] hidden;
      int[][] enemies;

      boolean attacked;

      GameScreen gameScreen;

      /**
       * Constructor for Characters
       * <p>
       * Characters inherit from Entity and have a position, sprite, and hitbox. They
       * can be moved and get hurt.
       * </p>
       * 
       * @param pos
       * @param spriteLink
       */
      public Character(Vector2 pos, String spriteLink) {
            super(pos, spriteLink);

            direction.y = 0;

            map = new Map();

            borderRecs = new ArrayList<>();
            Settings.map = Map.mapp;
            borders = Map.borders;
            hidden = Map.hidden;
            enemies = Map.enemies;

            borderRecs = createRectList(borders);
            hiddenRecs = createRectList(hidden);

            gameScreen = GameScreen.getInstance();
      }

      // entity health gets subtracted by damage
      public void receiveDamage(int damage) {
            this.health -= damage;
            this.color = 0;
      }

      /**
       * A function to flip the character's sprite
       * <p>
       * This function is called every frame. It flips the character's sprite
       * depending on the direction it is moving.
       * The parameter flipped is used to determine if the character is flipped
       * already or not (so that no flickering occurs).
       */
      void flipCharacter() {
            if (direction.x == -1 && !flipped) {
                  sprite.flip(true, false);
                  flipped = true;
            }
            if (direction.x == 1 && flipped) {
                  sprite.flip(true, false);
                  flipped = false;
            }
      }

      /**
       * A function to update the character's position and drawn health status
       * 
       * @param delta
       */
      public void entityUpdate(float delta) {
            move(delta);
            drawHurt();
      }

      /**
       * A function to move the character
       * <p>
       * This function is called every frame. First the x offset is calculated and
       * then added to the hitbox.
       * After that the the horizontal border collision is checked.
       * Then, the gravity is applied and the Sprite gets updated.
       * 
       * @param delta
       */
      void move(float delta) {
            float offset = direction.x * speed * delta;
            hitRect.x += offset;

            collision("hor");

            applyGravity(delta);

            updateSprite();

      }

      /**
       * A function to draw the hurt status of a character
       * <p>
       * This function is called every frame. If "color" is lower than 1, slowly count
       * it up and set Sprite color accordingly
       */
      public void drawHurt() {
            if (color >= 1) {
                  color = 1;
            } else {
                  color += 10 / 200f;
            }
            sprite.setColor(1, color, color, 1);
      }
}
