package com.zsolt.backgammon;

public class Move {
	private int sourceID;
	private int destinationID;
	public Move(int sourceID, int destinationID) {
		super();
		this.sourceID = sourceID;
		this.destinationID = destinationID;
	}
	public int getSourceID() {
		return sourceID;
	}
	public void setSourceID(int sourceID) {
		this.sourceID = sourceID;
	}
	public int getDestinationID() {
		return destinationID;
	}
	public void setDestinationID(int destinationID) {
		this.destinationID = destinationID;
	}
	
	
}
