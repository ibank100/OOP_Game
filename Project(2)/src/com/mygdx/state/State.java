package com.mygdx.state;

import com.badlogic.gdx.Screen;
import com.mygdx.game.Play;

public abstract class State implements Screen {
	
	private Play play;
	
	public State(Play p) {
		this.play = p;
	}

	@Override
	public abstract void show();

	@Override
	public abstract void render(float delta);

	@Override
	public abstract void resize(int width, int height);

	@Override
	public abstract void pause();

	@Override
	public abstract void resume();

	@Override
	public abstract void hide();

	@Override
	public abstract void dispose();

}