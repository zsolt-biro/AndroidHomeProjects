package com.zsolt.backgammon;

import org.andengine.entity.modifier.MoveModifier;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.zsolt.backgammon.Constants.colorType;

public class ComputerStone extends Stone {
	
	public ComputerStone(Player parent, colorType color, float pCellX,
			float pCellY, TiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(parent, color, pCellX, pCellY, pTiledTextureRegion,
				pVertexBufferObjectManager);
		// TODO Auto-generated constructor stub
	}

	public void setNewCoordinates(float x, float y) {
		float duration = 1f;
		this.registerEntityModifier(new MoveModifier(duration, this.getX(), x,
				this.getY(), y, moveFunc));
		
	}


}
