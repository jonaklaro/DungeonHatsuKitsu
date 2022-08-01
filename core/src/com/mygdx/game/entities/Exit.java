package com.mygdx.game.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;

public class Exit extends Entity {
      GameScreen gameScreen;

      /**
       * Constructor for the exit entity
       */
      public Exit(Vector2 pos, String spriteLink) {
            super(pos, spriteLink);
            gameScreen = GameScreen.getInstance();
            sprite.scale(.25f);
            hitRect = new Rectangle(pos.x, pos.y, 32 * playerScale, 64 * playerScale);
      }

      /**
       * If the player collides with the exit and all the enemies are dead, the level
       * is completed
       *
       */
      @Override
      public void onCollide(Entity collidingObject) {
            if (collidingObject instanceof Player) {
                  // if enemy list is empty, set state to won
                  if (gameScreen.enemies.isEmpty()) {
                        gameScreen.setState(GameScreen.State.WON);
                  }
                  collided = true;
            }
      }
}
