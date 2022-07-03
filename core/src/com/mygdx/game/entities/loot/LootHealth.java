package com.mygdx.game.entities.loot;

import com.badlogic.gdx.math.Vector2;

public class LootHealth extends Loot {
      /**
       * Constructor for LootHealth
       * <p>
       * Sets the loot's health to the given value
       * </p>
       * 
       * @param pos
       */
      public LootHealth(Vector2 pos) {
            super(pos, "loot/heart.png");
            credits = 0;
            health = 1;
      }
}
