package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.managers.GameKeys;
import com.mygdx.model.DIRECTION;
import com.mygdx.state.PlayState;

public class Play extends Game {
	private PlayState play;
	
	private AssetManager assetManager;
		
	
	public void create () {
		assetManager = new AssetManager();
		assetManager.load("packed/textures.atlas", TextureAtlas.class);
		assetManager.finishLoading();
		
		play = new PlayState(this);
		
		this.setScreen(play);
		
		
	}
	
	public void render() {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		super.render();
	}
	
	public AssetManager getAssetManager() {
		return assetManager;
	}

}

