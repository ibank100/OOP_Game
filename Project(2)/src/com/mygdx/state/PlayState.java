package com.mygdx.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.mygdx.game.Play;
import com.mygdx.game.Settings;
import com.mygdx.managers.GameKeys;
import com.mygdx.model.Actor;
import com.mygdx.model.Camera;
import com.mygdx.model.TERRAIN;
import com.mygdx.model.TileMap;
import com.mygdx.util.AnimationSet;

public class PlayState extends State{
	
	private GameKeys keys;
	
	private SpriteBatch batch;
	private Texture red;
	private Texture grass1;
	private Texture grass2;
	private Texture house;
	
	private ShapeRenderer shape, shape1;
	
	private Camera camera;
	private Actor player;
	private TileMap map;

	public PlayState(Play p) {
		super(p);
		
		batch = new SpriteBatch();
		red = new Texture("unpacked/brendan_stand_south.png"); // ตัวละคร
		grass1 = new Texture("grass1.png");
		grass2 = new Texture("grass2.png");
		house = new Texture("shop_1.png");
		
		TextureAtlas atlas = p.getAssetManager().get("packed/textures.atlas", TextureAtlas.class);
		
		AnimationSet animations = new AnimationSet(
				new Animation(0.3f/2f, atlas.findRegions("brendan_walk_north"), PlayMode.LOOP_PINGPONG),
				new Animation(0.3f/2f, atlas.findRegions("brendan_walk_south"), PlayMode.LOOP_PINGPONG),
				new Animation(0.3f/2f, atlas.findRegions("brendan_walk_east"), PlayMode.LOOP_PINGPONG),
				new Animation(0.3f/2f, atlas.findRegions("brendan_walk_west"), PlayMode.LOOP_PINGPONG),
				atlas.findRegion("brendan_stand_north"),
				atlas.findRegion("brendan_stand_south"),
				atlas.findRegion("brendan_stand_east"),
				atlas.findRegion("brendan_stand_west")
				);
		
		map = new TileMap(50, 50); // ขนาด ของ MAP
		player = new Actor(map, 10, 0, animations); // จุดที่ตัวละครเกิด
		camera = new Camera();
		
		keys = new GameKeys(player);
		shape = new ShapeRenderer();
		shape1 = new ShapeRenderer();
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(keys);
	}

	@Override
	public void render(float delta) {
		
		keys.update(delta);
		
		player.update(delta);
		
		camera.update(player.getWorldX()+0.5f, player.getWorldY()+0.5f);
		
		batch.begin();
		
		float worldStartX = Gdx.graphics.getWidth()/2 - camera.getCameraX()*Settings.SCALED_TILE_SIZE;
		float worldStartY = Gdx.graphics.getHeight()/2 - camera.getCameraY()*Settings.SCALED_TILE_SIZE;
		
		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y < map.getHeight(); y++) {
				Texture r;
				if (map.getTile(x, y).getTerrain() == TERRAIN.GRASS_1) {
					r = grass1;
				} else {
					r = grass2;
				}
				batch.draw(r,
						worldStartX + x * Settings.SCALED_TILE_SIZE,
						worldStartY + y * Settings.SCALED_TILE_SIZE,
						Settings.SCALED_TILE_SIZE, 
						Settings.SCALED_TILE_SIZE);
			}
		}
		
		batch.end();
		
		shape.begin(ShapeType.Filled);
		shape1.begin(ShapeType.Filled);
		shape1.rect(worldStartX + 2 * Settings.SCALED_TILE_SIZE, 
				worldStartY + 5 * Settings.SCALED_TILE_SIZE, 
				Settings.SCALED_TILE_SIZE * 6, 
				Settings.SCALED_TILE_SIZE * 3);
		shape1.end();
		
		shape.rect(worldStartX + player.getWorldX() * Settings.SCALED_TILE_SIZE, 
				worldStartY + player.getWorldY() * Settings.SCALED_TILE_SIZE,
				Settings.SCALED_TILE_SIZE, 
				Settings.SCALED_TILE_SIZE);
		shape.end();
		
		batch.begin();
		
		
		batch.draw(player.getSprite(), 
				worldStartX + player.getWorldX() * Settings.SCALED_TILE_SIZE, 
				worldStartY + player.getWorldY() * Settings.SCALED_TILE_SIZE, 
				Settings.SCALED_TILE_SIZE, 
				Settings.SCALED_TILE_SIZE);
		
		
		batch.draw(house, 
				worldStartX + 2 * Settings.SCALED_TILE_SIZE,
				worldStartY + 5 * Settings.SCALED_TILE_SIZE,
				Settings.SCALED_TILE_SIZE * 6, 
				Settings.SCALED_TILE_SIZE * 4);
		
		batch.end();
		
		
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
	}
	
}