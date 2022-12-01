package game;

import environment.Direction;

public class HumanPlayer extends Player {
	
	private Direction lastSentDirection;
	
	public HumanPlayer(int id, Game game, byte strength) {
		super(id, game, (byte)5);
	}

	@Override
	public boolean isHumanPlayer() {
		return true;
	}

	@Override
	public void move() {
		
	}

}
