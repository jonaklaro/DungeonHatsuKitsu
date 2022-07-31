package com.mygdx.game;

public class MapData {
  public int[][] map;

  public MapData(int x, int y) {
    // initialize map
     map = new int[x][y];

     //set all values to -1
      for (int i = 0; i < x; i++) {
        for (int j = 0; j < y; j++) {
          map[i][j] = -1;
        }
      }
  }

  // set map data
  public void setMap(int x, int y, int value) {
    map[y][x] = value;
  }

  // get map data
  public int getMap(int x, int y) {
    return map[x][y];
  }

  // get map width
  public int getWidth() {
    return map.length;
  }

  // get map height
  public int getHeight() {
    return map[0].length;
  }

  // get array of map data
  public int[][] getMapArray() {
    return map;
  }


}
