package gui;
import java.io.Serializable;

import environment.Coordinate;

public class PlayerPosition implements Serializable{

	private Coordinate coord;
	private int playerId;
	private boolean isHuman;
	
	public PlayerPosition(Coordinate coord, int playerId, boolean isHuman) {
		this.coord = coord;
		this.playerId = playerId;
		this.isHuman = isHuman;
	}

	public Coordinate getCoord() {
		return coord;
	}

	public int getPlayerId() {
		return playerId;
	}

	public boolean isHuman() {
		return isHuman;
	}
	
	
}
