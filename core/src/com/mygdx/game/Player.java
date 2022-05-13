package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Player extends Sprite {
    Texture texture;
    Sprite sprite;
    Rectangle hitRect;
    Vector2 direction = new Vector2();
    ShapeRenderer sr;

    public float midX;
    public float midY;

    float playerGrav;
    double gravSpeed = 42;
    int jumpCount;
    boolean jumped = false;
    boolean boostRight = false;
    boolean boostLeft = false;
    int maxJumps = 1;

    int speed = 300;

    Vector2 start;
    boolean hold = false;
    int[][] borders;
    int[][] hidden;

    ArrayList<Rectangle> borderRecs;
    ArrayList<Rectangle> hiddenRecs;
    Rectangle rec;

    int tilesize = Settings.tilesize;
    int graphicScale = Settings.graphicScale;
    float playerScale = 0.4f;

    boolean flipped;

    public Player(Vector2 pos) {
        float posX = pos.x;
        float posY = pos.y;
        texture = new Texture("character.png");
        sprite = new Sprite(texture);
        sprite.setOrigin(0,0);
        sprite.setScale(playerScale);

        sprite.setPosition(posX, posY); // random
        hitRect = new Rectangle(posX,posY,sprite.getWidth()*playerScale,sprite.getHeight()*playerScale);

        playerGrav = 0;
        start = new Vector2(posX,posY);

        Map map = new Map();
        borderRecs = new ArrayList<>();
        borders = map.readMap("assets/maps/newLevel_borders.csv");
        hidden = map.readMap("assets/maps/newLevel_hidden_obj.csv");

        sr = new ShapeRenderer();

        borderRecs = createRectList(borders);
        hiddenRecs = createRectList(hidden);

    }

    public void input(){

        direction.set(0,0);
        if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)){
            if (Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.D)) direction.x = 0;
            else if (Gdx.input.isKeyPressed(Input.Keys.A)) direction.x = -1;
            else if (Gdx.input.isKeyPressed(Input.Keys.D)) direction.x = 1;
            else direction.x = 0;

            if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && !hold && (jumpCount < maxJumps)){
                hold = true;
                jumpCount++;
                playerGrav = 15;

            }
        }
        if (!Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            hold = false;
        }

        if (direction.x == -1 && !flipped){
            sprite.flip(true,false);
            flipped = true;
        }
        if (direction.x == 1 && flipped){
            sprite.flip(true,false);
            flipped = false;
        }

    }

    public void move(float delta){
        hitRect.x += direction.x*speed*delta;

        collision("hor");

        playerGrav -= gravSpeed*delta;
        hitRect.y += playerGrav;
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
                        if ((Gdx.input.isKeyPressed(Input.Keys.SPACE)) && boostRight){
                            jumpCount--;
                            System.out.println("right");
                            boostRight = false;
                            boostLeft = true;
                        }
                        hitRect.x = border.x-hitRect.width;
                    }

                    if(direction.x < 0){ //Left
                        if ((Gdx.input.isKeyPressed(Input.Keys.SPACE)) && boostLeft){
                            jumpCount--;
                            System.out.println("left");
                            boostRight = true;
                            boostLeft = false;
                        }
                        hitRect.x = border.x+(border.width);
                    }
                    if (!jumped){
                        jumped = true;
                    }
                }
            }
        }
        if (dir.equals("ver")){
            for (Rectangle border: borderRecs){
                if (border.overlaps(hitRect)){
                    if(playerGrav > 0){ //Up
                        playerGrav = 0;
                        hitRect.y = sprite.getY();
                    }
                    else if(playerGrav <= 0){ //Down
                        playerGrav = 0;
                        hitRect.y = border.y+(border.height);
                        jumpCount = 0;
                        jumped = false;
                        boostRight = true;
                        boostLeft = true;
                    }

                }
            }
        }
        for (Rectangle hidden: hiddenRecs){
            if (hitRect.overlaps(hidden)){
                GameScreen.opacity = 0.75f;
                break;
            }
            else {
                GameScreen.opacity = 1;
            }
        }
    }

    public void update(float delta) {
        input();
        move(delta);
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        batch.setProjectionMatrix(camera.combined);

        sprite.draw(batch);
    }

    public Vector2 getPosition(){
        return new Vector2(sprite.getX(), sprite.getY());
    }

    public float getX(){
        return sprite.getX();
    }

    public float getY(){
        return sprite.getY();
    }

    private ArrayList<Rectangle> createRectList(int[][] map){
        ArrayList<Rectangle> recs = new ArrayList<>();
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++){
                if(map[row][col] != -1){
                    float x = (col*tilesize*graphicScale+20);
                    float y = (float) (-(row)*tilesize*graphicScale);
                    rec = new Rectangle(x, y, (float) (tilesize*graphicScale-40),(float) (tilesize*graphicScale));
//                    rec = new Rectangle((col*tilesize*graphicScale-tilesize*2), (float) (-(row)*tilesize*graphicScale-(tilesize*3)), (float) (tilesize*graphicScale*1.5),(float) (tilesize*graphicScale*1.75));
                    recs.add(rec);
                }
            }
        }
        return recs;
    }
}