package com.zsolt.backgammon;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;

public class Stone extends AnimatedSprite {
	public static final int STONE_SIZE = 40;

	protected final PhysicsHandler mPhysicsHandler;
	protected boolean mSelected = false;
	protected Constants.colorType color;
	protected IEaseFunction moveFunc;
	protected Player parent;
	public Stone(Player parent, Constants.colorType color, final float pCellX,
			final float pCellY, final TiledTextureRegion pTiledTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pCellX, pCellY, pTiledTextureRegion, pVertexBufferObjectManager);
		this.mPhysicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(this.mPhysicsHandler);
		this.mPhysicsHandler.setVelocity(0, 0);
		this.parent = parent;
		this.color = color;
		moveFunc = EaseLinear.getInstance();
		
	}

	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
			final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		switch (pSceneTouchEvent.getAction()) {
		case TouchEvent.ACTION_DOWN:
			this.setScale(1.25f);
			this.mSelected = true;
			parent.showPossibleDestinations(this);
			break;
		case TouchEvent.ACTION_MOVE:
			if (this.mSelected) {
				this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2,
						pSceneTouchEvent.getY() - this.getHeight() / 2);
			}
			break;
		case TouchEvent.ACTION_UP:
			if (this.mSelected) {
				this.mSelected = false;
				this.setScale(1.0f);
				parent.hidePossibleDestinations(this);
				parent.move(this);
				
			}
			break;
		}
		return true;
	}

	public void setNewCoordinates(float x, float y) {
		float duration = 0.2f;
		this.registerEntityModifier(new MoveModifier(duration, this.getX(), x,
				this.getY(), y, moveFunc));

	}

	public Constants.colorType getStoneColor() {
		return this.color;
	}

	

}
