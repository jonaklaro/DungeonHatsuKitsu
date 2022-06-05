package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Enemy extends Entity{
    public Enemy(Vector2 pos) {
        super(pos, "enemy/enemy.png");
        sprite.setScale(1,1);
        sprite.setRegion(0,0,64,64);

//        direction.x = -1;

    }

    public void update(float delta){
        move(delta);
    }

    private void move(float delta){
        hitRect.x += direction.x*speed*delta;

        collision("hor");

        gravity -= gravSpeed*delta;
        hitRect.y += gravity;
        collision("ver");

        sprite.setPosition(hitRect.x, hitRect.y);
        midX = sprite.getX()+(sprite.getWidth()*playerScale/2);
        midY = sprite.getY()+(sprite.getHeight()*playerScale/2);
    }

    private void collision(String dir){
        if (dir.equals("hor")){
            for (Rectangle border: borderRecs){
                if (hitRect.overlaps(border)){
                    if(direction.x > 0){ //Right

                        hitRect.x = border.x-hitRect.width;
                    }

                    if(direction.x < 0){ //Left

                        hitRect.x = border.x+(border.width);
                    }
//                    direction.x = direction.x*(-1);

                }
            }
        }
        if (dir.equals("ver")){
            for (Rectangle border: borderRecs){
                if (border.overlaps(hitRect)){
                    if(gravity > 0){ //Up
                        gravity = 0;
                        hitRect.y = sprite.getY();
                    }
                    else if(gravity <= 0){ //Down
                        gravity = 0;
                        hitRect.y = border.y+(border.height);
                    }
                }
            }
        }
    }
}
