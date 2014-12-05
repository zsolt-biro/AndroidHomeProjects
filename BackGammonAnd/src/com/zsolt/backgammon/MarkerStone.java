package com.zsolt.backgammon;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.zsolt.backgammon.Constants.colorType;

public class MarkerStone extends Stone {

	public MarkerStone(Player parent, colorType color, float pCellX,
			float pCellY, TiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(parent, color, pCellX, pCellY, pTiledTextureRegion,
				pVertexBufferObjectManager);
		// TODO Auto-generated constructor stub
	}
	
	public void setNewCoordinates(float x, float y) {
		this.setX(x);
		this.setY(y);

	}

}
