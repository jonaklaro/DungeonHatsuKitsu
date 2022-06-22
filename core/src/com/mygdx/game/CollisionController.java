package com.mygdx.game;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.entities.Player;

public class CollisionController {
      GameScreen gameScreen;

      public CollisionController() {
            gameScreen = GameScreen.getInstance();
      }

      public void checkCollisions() {
            if (gameScreen.getState() != GameScreen.State.RUNNING)
                  return;
            for (Object o1 : gameScreen.getAllEntities()) {

                  for (Object o2 : gameScreen.getAllEntities()) {
                        if (o1 == o2)
                              continue;
                        if (o1 instanceof Player && o2 instanceof Player)
                              continue;
                        if (isCollision((Entity) o1, (Entity) o2)) {
                              ((Entity) o1).onCollide((Entity) o2);
                              ((Entity) o2).onCollide((Entity) o1);
                              return;
                        }
                  }

            }
      }

      boolean isCollision(Entity o1, Entity o2) {
            if (o1.getHitRect().overlaps(o2.getHitRect())) {
                  return true;
            }
            return false;
      }

}
