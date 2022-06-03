package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class Entity extends Sprite {
    Texture texture;
    Sprite sprite;
    Rectangle hitRect;

    float gravity;
    double gravSpeed = 42;

    Map map;
    ArrayList<Rectangle> borderRecs;
    ArrayList<Rectangle> hiddenRecs;


}
