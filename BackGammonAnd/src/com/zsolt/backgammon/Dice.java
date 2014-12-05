package com.zsolt.backgammon;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Dice extends AnimatedSprite {

	private int value;
	
	public Dice(float pX, float pY, ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		// TODO Auto-generated constructor stub
	}
	public void onAnimationFinished(int x)
	{
		setValue(x);
	}
	public int getValue() {
		return value;
	}
	
	public void setValue(int x)
	{
		value = x;
	}
	

}
