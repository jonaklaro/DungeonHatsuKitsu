package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Praktikum extends Game {
	private Game game;

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