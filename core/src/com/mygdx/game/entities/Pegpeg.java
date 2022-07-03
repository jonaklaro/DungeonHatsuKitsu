package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entities.loot.LootHealth;

public class Pegpeg extends Enemy {

      GameScreen gameScreen;

      /**
       * Constructor for the pegpeg enemy
       * <p>
       * damage is set to 1, speed is set to 0, range is set to 600, reaction time is
       * set to 0.2, shootDelay is set to 2, and health is set to 7
       * 
       * @param pos The position of the enemy
       */
      public Pegpeg(Vector2 pos) {
            super(pos);

            sprite = getSpriteByPos(0, 0);

            hitRect = new Rectangle(pos.x, pos.y, 32 * playerScale, 32 * playerScale);
            setHealth(7);
            damage = 1;
            speed = 0;
            maxSpeed = speed;
            sprite.flip(true, false);
            range = 600;
            reactionTime = 0.2f;
            reactionTimeMax = reactionTime;
            shootDelay = 2;
            shootDelayMax = shootDelay;

            gameScreen = GameScreen.getInstance();
      }

      /**
       * A method to drop loot when the enemy is killed
       * <p>
       * Enemy drops two health items. Both get dropped with a little randomness in
       * their x coordinate, so that the player can differentiate between them. Also
       * the player score gets increased by 20
       * 
       */
      @Override
      public void dropLoot() {
            super.dropLoot();
            int random;
            for (int i = 0; i < 2; i++) {
                  random = (int) (Math.random() * 3) - 1;
                  gameScreen.loot.add(new LootHealth(new Vector2(getMidX() + random * 10, getMidY())));
            }
            gameScreen.score += 20;
      }

      /**
       * The search function for the pegpeg enemy ( other search function gets
       * overwritten)
       * <p>
       * if distance to player is less than range, the enemy will look at the player
       * if distance to player is less than half the range, the enemy will shoot at
       * the player after a delay
       * </p>
       */
      @Override
      void searchPlayer() {
            for (Player p : gameScreen.players) {
                  float dist = GameScreen.getDistance(p.getMidPosition(), this.getMidPosition());
                  if (dist < range) {
                        // a function to determine which direction the enemy should head in
                        determineDirection(p);
                        if (dist < range / 2f) {
                              if (shootDelay > 0)
                                    shootDelay -= Gdx.graphics.getDeltaTime();
                              else {
                                    shootDelay = shootDelayMax;
                                    shoot();
                              }
                        }
                  } else {
                        // set enemy direction to 0 if player is too far away
                        direction.x = 0;
                  }
            }
      }

      /**
       * The shoot function for the pegpeg enemy
       * <p>
       * The enemy will shoot a bullet in the direction it is facing
       * </p>
       */
      void shoot() {
            gameScreen = GameScreen.getInstance();
            gameScreen.enemyBullets.add(new EnemyBullet(getPosition(), this));
      }
}
