package com.zsolt.backgammon;

import java.util.ArrayList;

import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;



public class ComputerPlayer extends Player{
	private int moveCount;

	public ComputerPlayer(TableActivity parent,ColumnList columnList, RollDice rollDice, Constants.colorType playerColor) {
		super(parent,columnList, rollDice, playerColor);

	}
	public boolean setToClosestColumn(Stone stone)
	{
		return columnList.setToClosestColumn(stone, rollDice, direction);
	}
	public void onDiceRolled()
	{
		if (rollDice.isDouble() )
			moveCount = 4;
		
		else
			moveCount = 2;
		this.move();
	}
	public void onDicesConsumed()
	{
		rollDice.rollDices();
		//wait player to roll the dice
	}
	public void showPossibleDestinations(Stone stone) {
		//nothing to show
	}
	public void hidePossibleDestinations(Stone stone)
	{
		//nothing to unmark
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
	public void move()
	{
			moveCount--;
			ArrayList<Move> moves =columnList.getAllPossibleMoves(playerColor, rollDice,direction);
			if (moves.size() == 0)
			{
				moveCount =0;
				rollDice.consumeAllDices();
				
			}
			else
			{
			columnList.move(moves.get(0),direction,rollDice);
			if (moveCount !=0)
				parent.waitSeconds(this, 1);
			}
			
	}
	public Stone[] StoneFactory(Scene scene, TiledTextureRegion stoneTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectMAnager, int layer, Constants.colorType color)
	{
		Stone[] returnVal = new Stone[15];
		int i;
		for (i = 0; i < 15; i++) {
			Stone stone = new ComputerStone(this, color, 0,
					0, stoneTextureRegion,
					pVertexBufferObjectMAnager);
			returnVal[i] = stone;
			scene.getChildByIndex(layer).attachChild(stone);
		}
		return returnVal;
	}
	public void onTimePassed()
	{
		move();
	}
}
