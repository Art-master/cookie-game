package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.data.AssetLoader;
import com.mygdx.game.screens.GameScreen;

public class GdxGame extends Game {

	@Override
	public void create () {
		AssetLoader.INSTANCE.load();
		setScreen(new GameScreen());
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		AssetLoader.INSTANCE.dispose();
	}
}
