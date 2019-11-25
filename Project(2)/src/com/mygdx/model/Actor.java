package com.mygdx.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.util.AnimationSet;

public class Actor{
	
	private TileMap map;
	private int X = 0;
	private int Y = 0;
	private DIRECTION facing;
	
	private float worldX, worldY;
	
	private int srcX, srcY;
	private int destX, destY;
	private float animTimer;
	private float ANIM_TIME = 0.3f;
	
	private float walkTimer;
	private boolean moveRequestThisFrame;
	
	private ACTOR_STATE state;
	
	private AnimationSet animations;
	
	public Actor(TileMap map, int x, int y, AnimationSet animations) {
		this.map = map;
		this.X = x;
		this.Y = y;
		this.worldX = x;
		this.worldY = y;
		this.animations = animations;
		map.getTile(x, y).setActor(this);
		this.state = ACTOR_STATE.STANDING;
		this.facing = DIRECTION.SOUTH;
	}
	
	public enum ACTOR_STATE {
		WALKING,
		STANDING,
		;
	}
	
	public void update(float delta) {
		if (state == ACTOR_STATE.WALKING) {
			animTimer += delta;
			walkTimer += delta;
			
			worldX = Interpolation.linear.apply(srcX, destX, animTimer/ANIM_TIME);
			worldY = Interpolation.linear.apply(srcY, destY, animTimer/ANIM_TIME);
			
			if (animTimer > ANIM_TIME) {
				float leftOverTime = animTimer-ANIM_TIME;
				walkTimer -= leftOverTime;
				finishMove();
				if (moveRequestThisFrame) {
					move(facing);
				} else {
					walkTimer = 0f;
				}
			}
		}
		moveRequestThisFrame = false;
	}
	
	public boolean move(DIRECTION dir) {
		if (state != ACTOR_STATE.STANDING) {
			if (facing == dir) {
				moveRequestThisFrame = true;
			}
			return false;
		}
		
		if (X + dir.getDX() >= map.getWidth() ||
			Y + dir.getDY() >= map.getHeight()||
			X + dir.getDX() < 0 ||
			Y + dir.getDY() < 0)
		{ return false; }
		
		if (map.getTile(X + dir.getDX(), Y + dir.getDY()).getActor() != null)
		{ return false; }
		
		initializeMove(dir);
		map.getTile(X, Y).setActor(null);
		X += dir.getDX();
		Y += dir.getDY();
		map.getTile(X, Y).setActor(this);
		return true;
	}

	private void initializeMove(DIRECTION dir) {
		this.facing = dir;
		this.srcX = X;
		this.srcY = Y;
		this.destX = X + dir.getDX();
		this.destY = Y + dir.getDY();
		this.worldX = X;
		this.worldY = Y;
		animTimer = 0f;
		state = ACTOR_STATE.WALKING;
	}
	
	private void finishMove() {
		state = ACTOR_STATE.STANDING;
		this.worldX = destX;
		this.worldY = destY;
		this.srcX = 0;
		this.srcY = 0;
		this.destX = 0;
		this.destY = 0;
	}
	
	public int getX() {
		return X;
	}
	
	public int getY() {
		return Y;
	}
	
	public float getWorldX() {
		return worldX;
	}

	public float getWorldY() {
		return worldY;
	}
	
	public TextureRegion getSprite() {
		if (state == ACTOR_STATE.WALKING) {
			return (TextureRegion) animations.getWalking(facing).getKeyFrame(walkTimer);
		} else if (state == ACTOR_STATE.STANDING) {
			return animations.getStanding(facing);
		}
		return animations.getStanding(DIRECTION.SOUTH);
	}
	
}
