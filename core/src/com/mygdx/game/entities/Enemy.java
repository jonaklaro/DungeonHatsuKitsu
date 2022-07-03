package com.mygdx.game.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;

public class Enemy extends Character {
      float width;
      float height;
      int range;

      protected float reactionTimeMax;

      GameScreen gameScreen;

      /**
       * Constructor for all enemies, inherits from Character
       * <p>
       * Every enemy has a position, sprite, and hitbox. They can be moved and get
       * hurt. They also have a range.
       * 
       * @param pos
       */
      public Enemy(Vector2 pos) {
            super(pos, "enemy/enemies.png");
            range = 100;
            gameScreen = GameScreen.getInstance();
      }

      /**
       * Base Method for loot drops
       */
      public void dropLoot() {
            return;
      }

      /**
       * Enemy Update method where they look for a player and set their direction
       * towards them
       * 
       * @param delta
       */
      public void update(float delta) {
            searchPlayer();
            entityUpdate(delta);
            flipCharacter();
      }

      /**
       * A method to search for the player
       * <p>
       * If the player is in range, they will be added to the targetsInRange List.
       * If this list is not empty, the enemy will pick a random target from the list
       * and set their direction towards them.
       * </p>
       */
      void searchPlayer() {
            ArrayList<Player> targetsInRange = new ArrayList<Player>();
            for (Player p : gameScreen.players) {
                  // a function to determine which direction the enemy should head in
                  if (isInRange(p))
                        targetsInRange.add(p);
            }

            if (targetsInRange.size() > 0) {
                  // get random target
                  int random = (int) (Math.random() * targetsInRange.size());
                  Player target = targetsInRange.get(random);
                  determineDirection(target);
            } else {
                  // set enemy direction to 0 if player is too far away
                  direction.x = 0;
            }
      }

      /**
       * A method to determine if the player is in range of the enemy
       * 
       * @param player
       * @return true if player is in range, false if not
       * 
       */
      boolean isInRange(Player player) {
            float dist = GameScreen.getDistance(player.getMidPosition(), this.getMidPosition());
            return dist < range;
      }

      /**
       * A method to determine which direction the enemy should head in
       * <p>
       * If the reaction time is over (gets counted down every frame), the enemy will
       * head in the direction of the player.
       * </p>
       * 
       * @param player
       */
      void determineDirection(Player p) {
            float deltaTime = Gdx.graphics.getDeltaTime();

            // delay direction by reactionTime and delay time
            if (reactionTime > 0)
                  reactionTime -= deltaTime;
            else {
                  // set default reaction time from class
                  reactionTime = reactionTimeMax;

                  // set enemy to 1 if player is to the right
                  if (p.getMidX() > this.getMidX())
                        direction.x = 1;

                  // set enemy to -1 if player is to the left
                  else if (p.getMidX() < this.getMidX())
                        direction.x = -1;
            }
      }

      /**
       * Base method for shooting
       */
      void shoot() {
      }

      /**
       * A method to handle collisions with player bullets
       * <p>
       * If the enemy is hit by a bullet, it will take damage (color set to 0). The
       * bullet Parameter "collided" will be set to true.
       */
      public void onCollide(Entity collidingObject) {
            if (collidingObject instanceof PlayerBullet && !(((PlayerBullet) collidingObject).collided)) {
                  PlayerBullet bullet = (PlayerBullet) collidingObject;

                  bullet.collided = true;
                  health -= bullet.damage;
                  color = 0;
            }
      }

}
