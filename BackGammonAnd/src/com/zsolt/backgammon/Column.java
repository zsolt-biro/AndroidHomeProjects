package com.zsolt.backgammon;

import java.util.ArrayList;


public class Column {

	private final int MAX_LIST_SIZE = 10;
	private ArrayList<Stone> stoneList;
	private Stone markerStoneWhite;
	private Stone markerStoneBlack;
	private float xCoord;
	private float yCoord;
	private int columnID;
	private boolean marked = false;
	private Constants.colorType color = Constants.colorType.COLOR_NEUTRAL;
	private int columnDirection = 1;

	public Column(float xCoord, float yCoord, int columnID, Stone markerStoneWhite, Stone markerStoneBlack) {
		stoneList = new ArrayList<Stone>(MAX_LIST_SIZE);
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.columnID = columnID;
		if (columnID < 12)
			columnDirection = 1;
		else
			columnDirection = -1;
		this.markerStoneWhite = markerStoneWhite;
		this.markerStoneBlack = markerStoneBlack;
	}

	public void addStone(Stone stone) {
		float stoneCoordX;
		float stoneCoordY;
		if (stoneList.size() < MAX_LIST_SIZE) {
			if (stoneList.size() < 5) {
				stoneCoordY = yCoord + columnDirection * Stone.STONE_SIZE
						* (stoneList.size());
			} else {
				stoneCoordY = yCoord
						+ columnDirection
						* (Stone.STONE_SIZE / 2 + Stone.STONE_SIZE
								* (stoneList.size() - 5));
			}
			stoneCoordX = xCoord;
			stoneList.add(stone);

			stone.setNewCoordinates(stoneCoordX, stoneCoordY);
		}
		if (stoneList.size() == 2) {
			this.color = stone.getStoneColor();
		}
	}

	public boolean contains(Stone stone) {
		return stoneList.contains(stone);
	}

	public void removeStone(Stone stone) {
		int i = stoneList.indexOf(stone);
		if (i==-1)
			return;
		i++;
		for (; i < stoneList.size(); i++) {
			Stone tmpStone = stoneList.get(i);
			if (i == 5) {
				tmpStone.setNewCoordinates(tmpStone.getX(), yCoord
						+ columnDirection * Stone.STONE_SIZE * 4);
			} else {
				tmpStone.setNewCoordinates(tmpStone.getX(), tmpStone.getY()
						- columnDirection * Stone.STONE_SIZE);
			}
		}
		stoneList.remove(stone);
		if (stoneList.size() < 2) {
			this.color = Constants.colorType.COLOR_NEUTRAL;
		}



	}

	public boolean isFull() {
		return (this.stoneList.size() == MAX_LIST_SIZE);
	}

	public Constants.colorType getColumnColor() {
		return color;
	}
	public Constants.colorType getStrictColumnColor() {
		if (this.stoneList.size() == 1 )
			return this.getStoneByPosition(0).getStoneColor();
		else
			return color;
		
	}
	public int getSize()
	{
		return stoneList.size();
	}
	public Stone getStoneByPosition(int pos)
	{
		Stone st =stoneList.get(pos);

		return st;
	}
	public void removeStoneByPosition(int pos)
	{

		stoneList.remove(pos);

	}
	public int getID()
	{
		return columnID;
	}

	public void markColumn(Constants.colorType color) {
		this.marked = true;
		if (color ==Constants.colorType.COLOR_BLACK)
		{
			this.addStone(this.markerStoneBlack);
			this.markerStoneBlack.setVisible(true);
		}
		else
		{
			this.markerStoneWhite.setVisible(true);
			this.addStone(this.markerStoneWhite);
		}

	}
	public void unMarkColumn() {
		if (marked)
		{
			marked = false;
			this.removeStone(this.markerStoneWhite);
			this.markerStoneWhite.setVisible(false);
			marked = false;
			this.removeStone(this.markerStoneBlack);
			this.markerStoneBlack.setVisible(false);
		}

	}
	public boolean containsStoneWithColor(Constants.colorType color)
	{
		if (this.isEmpty())
			return false;
		else if (this.getStoneByPosition(0).getStoneColor() == color)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public boolean isEmpty()
	{
		if (this.stoneList.size() == 0)
		{
			return true;
		}
		else 
		{
			return false;
		}
		
	}

}
