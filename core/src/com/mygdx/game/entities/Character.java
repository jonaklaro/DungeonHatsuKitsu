package com.mygdx.game.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Map;
import com.mygdx.game.Settings;

import java.io.Serializable;
import java.util.ArrayList;

public class Character extends Entity implements Serializable {
    boolean flipped;
    boolean multi;

    boolean boostRight = false;
    boolean boostLeft = false;

    int speed;

    float color = 1;
    int jumpCount;
    boolean jumped;

    Map map;

    int[][] borders;
    int[][] hidden;
    int[][] enemies;

    // constructor for characters
    public Character(Vector2 pos, String spriteLink){
        super(pos, spriteLink);

        direction.y = 0;

        map = new Map();

        borderRecs = new ArrayList<>();
        Settings.map = Map.mapp;
        borders = Map.borders;
        hidden = Map.hidden;
        enemies = Map.enemies;

        borderRecs = createRectList(borders);
        hiddenRecs = createRectList(hidden);
    }

    void flipCharacter() {
        if (direction.x == -1 && !flipped){
            sprite.flip(true,false);
            flipped = true;
        }
        if (direction.x == 1 && flipped){
            sprite.flip(true,false);
            flipped = false;
        }
    }

    // general update method for characters
    public void entityUpdate(float delta, ArrayList characters){
        move(delta);
        drawHurt();
        updateExisting(this, characters);
    }

    //a method to move the different axes of the hit box and do colDet
    void move(float delta){
        float offset = direction.x*speed*delta;
        hitRect.x += offset;

        collision("hor");

        applyGravity(delta);

        updateSprite();

    }

    // if color is lower than 1, slowly count it up and set Sprite color accordingly
    public void drawHurt(){
        if (color >= 1){
            color = 1;
        } else {
            color += 10/200f;
        }
        sprite.setColor(1,color,color,1);
    }
}
