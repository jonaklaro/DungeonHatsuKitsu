package com.mygdx.game;

import com.badlogic.gdx.Game;

public class AcrobatDungeon extends Game {
      private final Game game;

      /**
       * Constructor for the AcrobatDungeon Game
       */
      public AcrobatDungeon() {
            game = this;
      }

      /**
       * Create method for the AcrobatDungeon Game where the screen is set to the
       * GameScreen
       *
       */
      @Override
      public void create() {
            setScreen(new GameScreen(game));
      }
}