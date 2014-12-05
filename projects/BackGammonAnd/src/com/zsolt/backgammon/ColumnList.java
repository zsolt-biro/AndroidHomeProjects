package com.zsolt.backgammon;

import java.util.ArrayList;

public class ColumnList {
	public final int BLACK_REMOVE_COLUMN = -1;
	public final int WHITE_REMOVE_COLUMN = 24;
	public final int MAX_LIST_SIZE = 24;
	
	private ArrayList<Column> columnList;
	private ArrayList<Integer> possibleDestinationFromStonePosition;
	private ArrayList<Move> possibleMoves;
	private float columnPosition[][] = { 
			{720,25}, //black removePos

			{ 655, 25 }, { 610, 25 }, { 565, 25 }, { 520, 25 }, 
			{ 475, 25 }, { 430, 25 }, { 330, 25 }, { 285, 25 },
			{ 240, 25 }, { 195, 25 }, { 150, 25 }, { 105, 25 },

			// TODO: abstractize these constants
			{ 105, 413 }, { 150, 413 }, { 195, 413 }, { 240, 413 },
			{ 285, 413 }, { 330, 413 }, { 430, 413 }, { 475, 413 },
			{ 520, 413 }, { 565, 413 }, { 610, 413 }, { 655, 413 },

			{720,413}//white removepos
	};
	/*The first 26 elements of the  array list are the white markerStones the next 26 elements the black markerstones*/
	public ColumnList(ArrayList<Stone> markerStones) {
		possibleDestinationFromStonePosition = new ArrayList<Integer>(5);
		possibleMoves = new ArrayList<Move>(26);
		this.columnList = new ArrayList<Column>(MAX_LIST_SIZE);
		int i;
		for (i = -1; i <= MAX_LIST_SIZE; i++) {
			
			columnList.add(new Column(this.columnPosition[i+1][0],
					this.columnPosition[i+1][1], i, markerStones.get(i+1),markerStones.get(i+27)));
		}

	}
	public final boolean setToClosestColumn(final Stone stone, final RollDice rollDice, final int direction) {
		float stoneX = stone.getX();
		float stoneY = stone.getY();
		int i;
		int newID = 0;
		int oldID = 0;

		double minDiff = Double.MAX_VALUE;
		for (i = -1; i <= MAX_LIST_SIZE; i++) {
			double diff = Math.pow((columnPosition[i+1][0] - stoneX), 2)
					+ Math.pow((columnPosition[i+1][1] - stoneY), 2);
			if (diff < minDiff) {
				newID = i;
				minDiff = diff;
			}
			if (this.get(i).contains(stone)) {
				oldID = i;
			}
		}

		if (possibleDestinationFromStonePosition.contains(Integer.valueOf(newID)) == false) {
			newID = oldID;
		} 
		moveStone(new Move(oldID,newID),direction,stone,rollDice);
		return !(newID==oldID);
	}
	//TODO: change sourceID and destinationID with Move object
	private boolean isPossibleToMoveThere(final int sourceID, final int destinationID,
			final Constants.colorType stoneColor, final RollDice rollDice,final int direction) {
		
		boolean returnVal = true;
		if ( sourceID == destinationID)
		{
			returnVal = false;
		}
		else if ((destinationID<-1) || (destinationID>MAX_LIST_SIZE+1))
			return false;
		if (isRemovedState(stoneColor) && (sourceID != BLACK_REMOVE_COLUMN) && (sourceID != WHITE_REMOVE_COLUMN) )
		{
			returnVal = false;
		}

		else if (((destinationID == BLACK_REMOVE_COLUMN) && (stoneColor !=Constants.colorType.COLOR_WHITE)) || (destinationID == WHITE_REMOVE_COLUMN)&& (stoneColor !=Constants.colorType.COLOR_BLACK))
		{
			returnVal =false;
		}
		else if ((destinationID == BLACK_REMOVE_COLUMN) && (stoneColor == Constants.colorType.COLOR_WHITE) && (!canRemove(stoneColor,direction)))
		{
			returnVal =false;
		}
		else if ((destinationID == WHITE_REMOVE_COLUMN) && (stoneColor == Constants.colorType.COLOR_BLACK) && (!canRemove(stoneColor,direction)))
				{
					returnVal =false;
				}
		else
		{
			Column column = this.get(destinationID);
			if (((stoneColor == Constants.colorType.COLOR_WHITE) && (direction == TableActivity.WHITE_TURN))
					|| ((stoneColor == Constants.colorType.COLOR_BLACK) && (direction == TableActivity.BLACK_TURN))) {
				if ((column.isFull())
						|| ((column.getColumnColor() != stoneColor) && (column
								.getColumnColor() != Constants.colorType.COLOR_NEUTRAL))) {
					returnVal = false;
				} else {

				}

				// rollDice.setDice1ValueToZero();
				//TODO: test the case: when dice1 and dice2 is false, dice1+dice2 shall be false too
				if (((sourceID + direction * rollDice.getDice1Value()) != destinationID)
						&& ((sourceID + direction * rollDice.getDice2Value()) != destinationID)
						&& (((sourceID + direction * rollDice.getDice1Value()+direction * rollDice.getDice2Value()) != destinationID)
						|| (((sourceID + direction * rollDice.getDice1Value()+direction * rollDice.getDice2Value()) == destinationID)
							&& !(isPossibleToMoveThere(sourceID,sourceID + direction * rollDice.getDice1Value(),stoneColor,rollDice,direction))
							&& !(isPossibleToMoveThere(sourceID,sourceID + direction * rollDice.getDice2Value(),stoneColor,rollDice,direction))
								))
						)
				{
					returnVal = false;
				}
			} else {
				returnVal = false;
			}
		}
		return returnVal;

	}
	public final Column get(final int i)
	{
		return columnList.get(i+1);
	}
	public final boolean isRemovedState(final Constants.colorType stoneColor)
	{
		if (stoneColor == Constants.colorType.COLOR_WHITE) {
			if (this.get(WHITE_REMOVE_COLUMN).getSize() != 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return this.get(BLACK_REMOVE_COLUMN).getSize() != 0;
		}

	}
	public final int getSourceID(Stone stone)
	{
		int i;
		int retVal=-1;
		for (i = -1; i <= MAX_LIST_SIZE; i++) {

			if (this.get(i).contains(stone)) {
				retVal = i;
			}
		}
		return retVal;
	}

	public final void setPossibleDestinationIDs(Stone stone,RollDice rollDice, int direction)
	{
		possibleDestinationFromStonePosition.clear();
		int sourceID = getSourceID(stone);
		for (int i = -1; i<=MAX_LIST_SIZE;i++)
		{
			if (isPossibleToMoveThere(sourceID,i,stone.getStoneColor(),rollDice,direction))
			{
				possibleDestinationFromStonePosition.add(Integer.valueOf(i));
			}
		}
		this.markPossibleDestinations(stone.getStoneColor());

	}
	public void markPossibleDestinations(Constants.colorType color)
	{
		int i;
		for (i = 0; i<possibleDestinationFromStonePosition.size() ;i++)
		{
			Column col = this.get(possibleDestinationFromStonePosition.get(i));
			col.markColumn(color);
		}
	}
	public void unMarkPossibleDestinations()
	{
		int i;
		for (i = 0; i<possibleDestinationFromStonePosition.size() ;i++)
		{
			Column col = this.get(possibleDestinationFromStonePosition.get(i));
			col.unMarkColumn();
		}
	}
	
	public ArrayList<Column> getAllColumnsWithColor(Constants.colorType color)
	{
		ArrayList<Column> returnVal = new ArrayList<Column>(26);
		for(int i=0;i<MAX_LIST_SIZE+2;i++)
		if (this.columnList.get(i).getStrictColumnColor() == color)
		{
			returnVal.add(this.columnList.get(i));
		}
		return returnVal;
	}
	public ArrayList<Move> getAllPossibleMoves(Constants.colorType turnColor,RollDice rollDice, int direction)
	{
		setAllPossibleMoves(turnColor, rollDice, direction);
		return possibleMoves;
	}
	public void setAllPossibleMoves(Constants.colorType turnColor,RollDice rollDice, int direction)
	{
		possibleMoves.clear();
		int i;
		ArrayList<Column> columns =  getAllColumnsWithColor(turnColor);
		for (i = 0; i<columns.size() ;i++)
		{
			int sourceId = columns.get(i).getID();
			int destinationId = sourceId+ direction*rollDice.getDice1Value();
			if (isPossibleToMoveThere(sourceId,destinationId,turnColor,rollDice,direction))
			{
				possibleMoves.add(new Move(sourceId,destinationId));
			}
			destinationId = sourceId+ direction*rollDice.getDice2Value();
			if (isPossibleToMoveThere(sourceId,destinationId,turnColor,rollDice,direction))
			{
				possibleMoves.add(new Move(sourceId,destinationId));
			}
		}
	}
	public boolean checkMovePossibility(Constants.colorType turnColor,RollDice rollDice, int direction)
	{
		setAllPossibleMoves(turnColor, rollDice, direction);
		if (possibleMoves.isEmpty())
			return false;
		else return true;
		
	}
	public boolean canRemove(Constants.colorType color, int direction)
	{
		if (direction == 1)
		{
			int lastID = searchLastColumnIDwithStone(color, direction);
			if (lastID > 17) return true;
			else return false;
			
		}
		else if (direction ==-1)
		{
			int lastID = searchLastColumnIDwithStone(color, direction);
			if (lastID < 6) return true;
			else return false;
		}
		else
		{
			return false;
		}
	}
	public int searchLastColumnIDwithStone(Constants.colorType color, int direction)
	{
		int startIndex;
		if (direction ==1 )
			startIndex = -1;
		else
		{
			startIndex= 24;
		}
		for(;(startIndex<26) && (startIndex>0);startIndex+=direction)
		{
			if (this.get(startIndex).getStrictColumnColor() ==color )
				return startIndex;
			
		}
		return startIndex;
	}
	public void move(Move move,int direction, RollDice rollDice) {
		int sourceID = move.getSourceID();
		Column srcColumn = this.get(sourceID);
		Stone stone = srcColumn.getStoneByPosition(0);
		
		moveStone(move,direction,stone,rollDice);
		
		
	}
	public void moveStone(Move move,int direction, Stone stone,RollDice rollDice) {
		int destinationID = move.getDestinationID();
		int sourceID = move.getSourceID();
		
			if (destinationID == sourceID + direction * rollDice.getDice1Value()) {
				rollDice.consumeDice1();
			} else if (destinationID == sourceID + direction
					* rollDice.getDice2Value()) {
				rollDice.consumeDice2();
			} else if (destinationID == sourceID + direction
					* rollDice.getDice2Value()+  direction
					* rollDice.getDice1Value())
			{
				rollDice.consumeDice1();
				rollDice.consumeDice2();
			}
			Column col =this.get(destinationID);
			if ( (col.getSize() ==1))
			{
				
				Stone st = col.getStoneByPosition(0);
				if (st.getStoneColor() != stone.getStoneColor())
				{
					/*Remove stone*/
					col.removeStoneByPosition(0);
					if (st.getStoneColor() == Constants.colorType.COLOR_WHITE)
					{
						this.get(WHITE_REMOVE_COLUMN).addStone(st);

					}
					else
					{
						this.get(BLACK_REMOVE_COLUMN).addStone(st);
					}
				}

			

		}
		
		this.get(sourceID).removeStone(stone);
		if (((destinationID == BLACK_REMOVE_COLUMN) ||(destinationID == BLACK_REMOVE_COLUMN))&&(sourceID!=destinationID))
		{
			stone.setVisible(false);
			stone.setPosition(0, 0);
			
		}
		else
		this.get(destinationID).addStone(stone);
		
		
	}
}
