package com.mygdx.game;

// import isKeyPressed
import com.badlogic.gdx.Gdx;

// A class to define buttons for player inputs
public class InputController {
        int left;
        int right;
        int up;
        int attack;

        public InputController(int left, int right, int up, int attack) {
            this.left = left;
            this.right = right;
            this.up = up;
            this.attack = attack;
        }

        public boolean isMovingLeft() {
            return Gdx.input.isKeyPressed(left);
        }

        public boolean isMovingRight() {
            return Gdx.input.isKeyPressed(right);
        }

        public boolean isJumping() {
            return Gdx.input.isKeyPressed(up);
        }

        public boolean isAttacking() {
            return Gdx.input.isKeyPressed(attack);
        }
}