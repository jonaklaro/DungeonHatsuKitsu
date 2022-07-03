package com.mygdx.game.entities.loot;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameData;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entities.Player;

public class Loot extends Entity {
      public int credits;
      public int health;
      GameScreen gameScreen;
      GameData gameData;

      /**
       * Constructor for Loot
       * 
       * @param pos        position of loot
       * @param spriteLink link to sprite
       */
      public Loot(Vector2 pos, String spriteLink) {
            super(pos, spriteLink);
            width = sprite.getWidth() * playerScale;
            height = sprite.getHeight() * playerScale;
            hitRect = new Rectangle(pos.x - width / 2, pos.y - height / 2, width, height);
            gameScreen = GameScreen.getInstance();
      }

      /**
       * Update the loot
       * 
       * @param delta time since last update
       */
      public void update(float delta) {
            applyGravity(delta);
            updateSprite();
      }

      /**
       * Detect if Player is colliding with loot, if so, give player loot
       * 
       * @param collidingObject object that is colliding with loot
       */
      public void onCollide(Entity collidingObject) {
            if (collidingObject instanceof Player) {
                  ((Player) collidingObject).add(this);
                  gameScreen.loot.remove(this);
                  gameScreen.saveGameState();

            }
      }
}
