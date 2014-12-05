package com.zsolt.backgammon;

import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Player {
	protected ColumnList columnList;
	protected RollDice rollDice;
	protected Constants.colorType playerColor;
	protected int direction;
	protected TableActivity parent;
	protected boolean isMyTurn=false;
	public Player(TableActivity parent, ColumnList columnList, RollDice rollDice, Constants.colorType playerColor) {
		this.parent = parent;
		this.columnList = columnList;
		this.rollDice = rollDice;
		this.playerColor = playerColor;
		if (playerColor == Constants.colorType.COLOR_BLACK)
			direction = 1;
		else direction = -1;
	}
	public boolean setToClosestColumn(Stone stone)
	{
		return columnList.setToClosestColumn(stone, rollDice, direction);
	}
	public void onDiceRolled()
	{

		checkMovePossibility();
	}
	public void onDicesConsumed()
	{
		//wait player to roll the dice
	}
	public void showPossibleDestinations(Stone stone) {
		columnList.setPossibleDestinationIDs(stone,rollDice,parent.getTableState());
	}
	public void hidePossibleDestinations(Stone stone)
	{
		columnList.unMarkPossibleDestinations();
	}
	public void move(int moveNr)
	{

	}
	public void move(Stone stone)
	{
			if (setToClosestColumn(stone))
				checkMovePossibility();	
	}
	public void checkMovePossibility()
	{
		if (!rollDice.noRemainingMoves())
		{
			boolean canMove = columnList.checkMovePossibility(playerColor, rollDice, direction);
			if (!canMove)
			{
				parent.toast("Can't move. Next player turn");
				rollDice.consumeAllDices();

			}
		}
		else
		{

		}
	}
	public void onTimePassed() {
		// TODO Auto-generated method stub
		
	}
	public Stone[] StoneFactory(Scene scene, TiledTextureRegion stoneTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectMAnager, int layer, Constants.colorType color)
	{
		Stone[] returnVal = new Stone[15];
		int i;
		for (i = 0; i < 15; i++) {
			Stone stone = new Stone(this, color, 0,
					0, stoneTextureRegion,
					pVertexBufferObjectMAnager);
			returnVal[i] = stone;
			scene.getChildByIndex(layer).attachChild(stone);
			scene.registerTouchArea(stone);
		}
		return returnVal;
	}
}
