package com.mygdx.game.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entities.loot.LootCredits;

public class RoundStinger extends Enemy {

      GameScreen gameScreen;

      public RoundStinger(Vector2 pos) {
            super(pos);
            sprite = getSpriteByPos(0, 1);

            hitRect = new Rectangle(pos.x, pos.y, 32 * playerScale, 32 * playerScale);
            setHealth(10);
            damage = 2;
            speed = 100;
            maxSpeed = speed;
            range = 400;
            reactionTime = 2;
            reactionTimeMax = reactionTime;
            gameScreen = GameScreen.getInstance();
      }

      // a function to drop loot
      @Override
      public void dropLoot() {
            gameScreen.loot.add(new LootCredits(new Vector2(getMidX(), getMidY())));
            gameScreen.score += 10;
      }

      // onCollide
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
