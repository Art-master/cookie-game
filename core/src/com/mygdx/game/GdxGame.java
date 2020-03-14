package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.StartScreen;

public class GdxGame extends Game {

	@Override
	public void create () {
		//setScreen(new GameScreen());
		setScreen(new StartScreen());
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
	}
}
