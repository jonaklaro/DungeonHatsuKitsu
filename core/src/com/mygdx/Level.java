package com.mygdx;

import com.mygdx.game.MapData;

public class Level {
  
  public MapData border;
  public MapData hidden;
  public MapData enemies;
  public MapData entities;
  public MapData exit;
  public MapData texture;

  public Level(int width, int height) {
    border = new MapData(width, height);
    hidden = new MapData(width, height);
    enemies = new MapData(width, height);
    entities = new MapData(width, height);
    exit = new MapData(width, height);
    texture = new MapData(width, height);
  }
  
}
