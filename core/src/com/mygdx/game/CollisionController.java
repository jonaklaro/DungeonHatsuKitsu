package com.mygdx.game;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.entities.Player;

public class CollisionController {
      GameScreen gameScreen;

      /**
       * Constructor for the collision controller
       * <p>
       * gameScreen is set to the GameScreen to get an instance
       */
      public CollisionController() {
            gameScreen = GameScreen.getInstance();
      }

      /**
       * If GameState is not RUNNING, the method returns
       * <p>
       * Every Object in the game is checked for collision with every other Object.
       * If two objects are the same type, or both objects are players, the collisions
       * dont get calculated.
       * If any other objects collide, the method calls the onCollide method of the
       * colliding objects.
       */
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

      /**
       * Checks if two entities are colliding with each other
       */
      boolean isCollision(Entity o1, Entity o2) {
            if (o1.getHitRect().overlaps(o2.getHitRect())) {
                  return true;
            }
            return false;
      }

}
