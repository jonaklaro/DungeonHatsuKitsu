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

      public Enemy(Vector2 pos) {
            super(pos, "enemy/enemies.png");
            range = 100;
            gameScreen = GameScreen.getInstance();
      }

      public void dropLoot() {
            return;
      }

      public void update(float delta) {
            searchPlayer();
            entityUpdate(delta, GameScreen.enemies);
            flipCharacter();
      }

      // a function to let the enemy know when the player is in range
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
                  // set enemy direction to 0 if player is too far away
            } else {
                  direction.x = 0;
            }
            // if the enemy is in range, it will head towards the player
      }

      boolean isInRange(Player player) {
            float dist = GameScreen.getDistance(player.getMidPosition(), this.getMidPosition());
            return dist < range;
      }

      // a function to determine which direction the enemy should head in
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

      void shoot() {
      }

      // on collide with attack
      public void onCollide(Entity collidingObject) {
            if (collidingObject instanceof PlayerBullet && !(((PlayerBullet) collidingObject).collided)) {
                  PlayerBullet bullet = (PlayerBullet) collidingObject;

                  bullet.collided = true;
                  health -= bullet.damage;
                  color = 0;
            }
      }

}
