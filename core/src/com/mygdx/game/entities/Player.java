package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.InputController;
import com.mygdx.game.entities.loot.Loot;

import java.util.ArrayList;

public class Player extends Character {

      int maxJumps = 1;
      InputController inputController;
      GameScreen gameScreen;

      boolean hold = false;
      boolean invulnerable;
      float invulnerableTime;
      float invulnerableTimeMax;
      ArrayList<PlayerBullet> bullets;

      public Player(Vector2 pos, boolean multi, InputController input) {
            super(pos, "character/char_small.png");
            gameScreen = GameScreen.getInstance();
            this.multi = multi;
            this.inputController = input;
            gameScreen.playerBullets = new ArrayList<>();
            speed = 300;
            kb = KeyBlock.NO_BLOCK;
            health = 10;
            maxHealth = 10;
            invulnerableTime = 1f;
            invulnerableTimeMax = invulnerableTime;
            sprite.setScale(.5f, 1);
            sprite.setRegion(10, 3, 34, 61);
            hitRect = new Rectangle(pos.x, pos.y, 34 * playerScale, sprite.getHeight() * playerScale);
            bullets = new ArrayList<>();
      }

      // a function to handle the player's input
      public void input() {
            direction.set(0, 0);
            if (!this.inputController.isAttacking())
                  attacked = false;

            if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {

                  if (this.inputController.isAttacking() && !attacked)
                        attack();

                  if (this.inputController.isMovingLeft() && this.inputController.isMovingRight()) {
                        direction.x = 0;
                  } else if (this.inputController.isMovingLeft() && (kb != KeyBlock.LEFT))
                        direction.x = -1;
                  else if (this.inputController.isMovingRight() && (kb != KeyBlock.RIGHT))
                        direction.x = 1;
                  else
                        direction.x = 0;

                  if (this.inputController.isJumping() && !hold && (jumpCount < maxJumps)) {
                        hold = true;
                        jumpCount++;
                        gravity = 13;

                  }
            }
            if (!this.inputController.isJumping()) {
                  hold = false;
            }
            if (kb != KeyBlock.NO_BLOCK) {
                  if ((kb == KeyBlock.LEFT && !this.inputController.isMovingLeft())
                              || (kb == KeyBlock.RIGHT && !this.inputController.isMovingRight())) {
                        kb = KeyBlock.NO_BLOCK;
                  }

            }

            flipCharacter();

      }

      // a function to create the attack
      void attack() {
            attacked = true;
            if (bullets.size() < 3) {
                  PlayerBullet bullet = new PlayerBullet(getPosition(), this);
                  bullets.add(bullet);
                  gameScreen.playerBullets.add(bullet);
            }
      }

      // a function to let the player do wall jumps
      public void wallJump(boolean boost) {

            boolean dir;
            dir = direction.x > 0;

            if (inputController.isJumping() && boost) {
                  jumpCount--;
                  boostRight = !dir;
                  boostLeft = dir;
            }
      }

      @Override
      public void onCollide(Entity collidingObject) {
            if (collidingObject instanceof Enemy) {
                  // if coll with enemy set grav to 10 and set jump count to 0
                  // for jumping again
                  if (gravity < 0) { // Down
                        gravity = 10;
                        hitRect.y = collidingObject.hitRect.y + (collidingObject.hitRect.height);
                        jumpCount = 0;
                        updateHealth(collidingObject.getDamage());
                        return;
                  }
                  if (direction.x > 0 && !collided) { // Right
                        hitRect.x = collidingObject.hitRect.x - hitRect.width;
                  }
                  if (direction.x < 0 && !collided) { // Left
                        hitRect.x = collidingObject.hitRect.x + (collidingObject.hitRect.width);
                  }
                  if (gravity > 0) { // Up
                        gravity = 0;
                        hitRect.y = sprite.getY();
                  }

                  updateHealth(collidingObject.getDamage());

            }

            if (collidingObject instanceof EnemyBullet) {
                  EnemyBullet bullet = (EnemyBullet) collidingObject;

                  bullet.collided = true;
                  updateHealth(bullet.getDamage());
            }

      }

      // a function to uptdate the player's health
      public void updateHealth(int damage) {
            if (!invulnerable) {
                  health -= damage;
                  color = 0;
                  invulnerable = true;
            }
      }

      // a function to update the player's invulnerable time
      public void updateinvulnerableTime() {
            if (invulnerable) {
                  invulnerableTime -= Gdx.graphics.getDeltaTime();
                  if (invulnerableTime <= 0) {
                        invulnerable = false;
                        invulnerableTime = invulnerableTimeMax;
                  }
            }
      }

      // a function to update the player
      public void update(float delta) {
            input();

            updateBullets(delta);

            updateinvulnerableTime();

            entityUpdate(delta);
      }

      // a function to update the player's bullets
      private void updateBullets(float delta) {
            for (PlayerBullet pb : bullets) {
                  int size = bullets.size();
                  pb.update(delta, this);
                  if (size != bullets.size())
                        break;
            }
      }

      // Collision detection for the hidden recs
      public void hiddenColDet() {
            for (Rectangle hidden : hiddenRecs) {
                  if (hitRect.overlaps(hidden)) {
                        GameScreen.opacity = 0.75f;
                        break;
                  } else if (!multi) {
                        GameScreen.opacity = 1;
                  }
            }
      }

      // a function to add loot to the player
      public void add(Loot loot) {
            if (loot.credits > 0) {
                  credits += loot.credits;
            }
            if (loot.health > 0 && health < maxHealth) {
                  health += loot.health;
            }
      }

      // a function to reset the player parameters
      void resetPlayerParameters() {
            jumpCount = 0;
            jumped = false;
            boostRight = true;
            boostLeft = true;
      }

      public float getMaxHealth() {
            return maxHealth;
      }

      public boolean isInvulnerable() {
            return invulnerable;
      }

}