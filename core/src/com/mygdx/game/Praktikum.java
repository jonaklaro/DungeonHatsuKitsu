package com.mygdx.game;

import com.badlogic.gdx.Game;


public class Praktikum extends Game {
	private final Game game;

	public Praktikum(){
		game = this;
	}

	@Override
	public void create() {
		setScreen(new GameScreen(game));

//		setScreen(new MenuScreen(game));
//		setScreen(new OptionScreen());
	}
}