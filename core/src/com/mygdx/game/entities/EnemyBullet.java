package com.mygdx.game.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class EnemyBullet extends Character {

      /**
       * Constructor for all enemy bullets, inherits from Character
       * <p>
       * Every enemy bullet has a position, sprite, and hitbox. They can be moved
       * their direction will be set in the direction the enemy is facing.
       * 
       * @param pos
       * @param c
       */
      public EnemyBullet(Vector2 pos, Character c) {

            super(pos, "enemy\\bullet.png");

            if (c.flipped) {
                  sprite.flip(true, false);
                  sprite.setX(pos.x - sprite.getWidth());
                  direction.x = -1;
            } else {
                  sprite.setX(pos.x + c.sprite.getWidth() / 2);
                  direction.x = 1;
            }
            sprite.setRegion(0, 0, 16, 16);
            sprite.setScale(1, 1);

            width = sprite.getWidth() * playerScale;
            height = sprite.getHeight() * playerScale;

            hitRect = new Rectangle(c.getMidX() - width / 2, c.getMidY() - height / 2, width, height);

            gravSpeed = 0;
            damage = 1;
            speed = 500;
      }

      /**
       * A method to move the enemy bullet and check for colissions with the walls
       * <p>
       * if the enemy bullet hits the wall, it will be removed from the game.
       * 
       * @param delta
       */
      public void update(float delta) {
            // if (!gameScreen.playerBullets.isEmpty()) {
            move(delta);
            if (collided) {
                  gameScreen.enemyBullets.remove(this);
                  return;
            }
            if (gameScreen.enemyBullets.isEmpty())
                  return;
      }
}
