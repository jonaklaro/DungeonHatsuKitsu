package com.mygdx.game.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entities.loot.LootCredits;

public class RoundStinger extends Enemy {

  public RoundStinger(Vector2 pos) {
    super(pos);
    sprite = getSpriteByPos(0, 1);

    hitRect = new Rectangle(pos.x, pos.y, 32 * playerScale, 32 * playerScale);
    setHealth(5);
    damage = 2;
    speed = 100;
    maxSpeed = speed;
    range = 400;
    reactionTime = 2;
    reactionTimeMax = reactionTime;

  }

  // a function to drop loot
  @Override
  public void dropLoot() {
    GameScreen.loot.add(new LootCredits(new Vector2(getMidX(), getMidY())));
  }
}
