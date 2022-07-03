package com.mygdx.game.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entities.loot.LootCredits;

public class RoundStinger extends Enemy {

      GameScreen gameScreen;

      /**
       * Constructor for the round stinger enemy
       * <p>
       * damage is set to 2, speed is set to 250, range is set to 400, reaction time
       * is set to 1 and health is set to 10
       */
      public RoundStinger(Vector2 pos) {
            super(pos);
            sprite = getSpriteByPos(0, 1);

            hitRect = new Rectangle(pos.x, pos.y, 32 * playerScale, 32 * playerScale);
            setHealth(10);
            damage = 2;
            speed = 250;
            maxSpeed = speed;
            range = 400;
            reactionTime = 1;
            reactionTimeMax = reactionTime;
            gameScreen = GameScreen.getInstance();
      }

      /**
       * A method to drop loot when the enemy is killed
       * <p>
       * Enemy drops a credit and the player score gets increased by 10
       */
      @Override
      public void dropLoot() {
            // get a random number between -1 and 1
            int random = (int) (Math.random() * 3) - 1;
            gameScreen.loot.add(new LootCredits(new Vector2(getMidX() + random * 10, getMidY())));
            gameScreen.score += 10;
      }

      /**
       * Collision detection method for the round stinger enemy
       * <p>
       * If a playerbullet hits the enemy, the enemy takes damage and the bullet is
       * set to collided. The enemy Color is set to 0.
       * If the enemy collides with the player and its direction is not 0, the enemy
       * gets a knockback and its direction is set to 0.
       */
      @Override
      public void onCollide(Entity collidingObject) {

            // if colliding with bullet, take damage
            if (collidingObject instanceof PlayerBullet) {
                  PlayerBullet bullet = (PlayerBullet) collidingObject;

                  bullet.collided = true;
                  health -= bullet.damage;
                  color = 0;
            }

            // if colliding with player, set direction to 0
            if (collidingObject instanceof Player) {
                  if (direction.x != 0) {
                        hitRect.x -= direction.x * 5;
                        direction.x = 0;
                  }

            }
      }

}
