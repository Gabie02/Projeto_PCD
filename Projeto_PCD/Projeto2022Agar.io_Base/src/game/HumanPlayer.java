package game;

public class HumanPlayer extends Player{
	
	
	
	public HumanPlayer(int id, Game game, byte strength) {
		super(id, game, strength);
	}

	@Override
	public boolean isHumanPlayer() {
		return true;
	}

	@Override
	public void move() {
		
	}

}
