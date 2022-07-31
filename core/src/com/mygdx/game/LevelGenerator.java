package com.mygdx.game;

import com.mygdx.Level;

public class LevelGenerator {

  // int enemyCount;
  int width;
  int height;
  Level level;

  int startX;
  int startY;
  int exitX;
  int exitY;

  public LevelGenerator(int width, int height) {
    // this.enemyCount = enemyCount;
    this.width = width;
    this.height = height;

    this.level = new Level(width, height);

    generateLevel();
  }

  public Level getLevel() {
    return level;
  }

  public void generateLevel() {
    generateStart();
    generateExit();
    generateBorder();
    generateHidden();
    generateEnemies();
    generateEntities();
    generateTexture();
  }

  private void generateTexture() {
    // use border map to generate texture map
    for (int i = 0; i < level.border.map.length; i++) {
      for (int j = 0; j < level.border.map[i].length; j++) {
        level.texture.map[i][j] = level.border.map[i][j];
      }
    }
  }

  private void generateEntities() {
  }

  private void generateEnemies() {
  }

  private void generateHidden() {
  }

  private void generateBorder() {
    // generate basic border around level
    for (int x = 0; x < width; x++) {
      level.border.setMap(x, 0, 0);
      level.border.setMap(x, height - 1, 0);
    }
    for (int y = 0; y < height; y++) {
      level.border.setMap(0, y, 0);
      level.border.setMap(width - 1, y, 0);
    }

    generatePlatform(startX, startY);
    generatePlatform(exitX, exitY+2);

  }

  private void generatePlatform(int px, int py) {

    for (int x = px - 1; x < px + 2; x++) {
        level.border.setMap(x, py-1, 0);
    }
  }

  private void generateExit() {
    // generate random exit location that is not on player location
    exitX = (int) (Math.random() * (width - 2)) + 1;
    exitY = (int) (Math.random() * (height - 2)) + 1;
    while (exitX == startX && exitY == startY) {
      exitX = (int) (Math.random() * (width - 2)) + 1;
      exitY = (int) (Math.random() * (height - 2)) + 5;
    }
    level.exit.setMap(exitX, exitY, 0);

  }

  private void generateStart() {
    // note: start is represented by a 1 in entities map

    // generate random start position in bounds of level
    startX = (int) (Math.random() * (width - 3)) + 1;
    startY = (int) (Math.random() * (height + 5)) + 5;

    // set start position
    level.entities.setMap(startX, startY, 0);

  }

}
