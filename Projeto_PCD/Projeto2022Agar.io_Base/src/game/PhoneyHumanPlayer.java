package game;

import environment.Cell;
import environment.Coordinate;
import environment.Direction;

/**
 * Class to demonstrate a player being added to the game.
 * @author luismota
 *
 */
public class PhoneyHumanPlayer extends Player {
	public PhoneyHumanPlayer(int id, Game game, byte strength) {
		super(id, game, strength);
	}

	public boolean isHumanPlayer() {
		return false;
	}
	
	@Override
	public void run() {
		try {
			sleep(10000);
		} catch (InterruptedException e1) {}
		
		while(true) {
			move();
			game.notifyChange();
			try {
				sleep(Game.REFRESH_INTERVAL);
			} catch (InterruptedException e) {}
		}
	}

	@Override
	public void move() {
		int random = ((int) (Math.random()*4));
		System.out.println(random);
		Coordinate coordsOld;
		Coordinate coordsNew;
		Cell cellOld;
		Cell cellNew;
		switch(random) {
		
		case 0: //LEFT
			coordsOld = getCurrentCell().getPosition();
			coordsNew = (coordsOld.translate(Direction.LEFT.getVector()));
			cellOld = getCurrentCell();
			if(isInsideBoard(coordsNew)) {
			cellNew = game.getCell(coordsNew);
			cellOld.setPlayer(null);  
			cellNew.setPlayer(this);
			}
			
			break;
		
		case 1: //DOWN
			coordsOld = getCurrentCell().getPosition();
			coordsNew = (coordsOld.translate(Direction.DOWN.getVector()));
			cellOld = getCurrentCell();
			if(isInsideBoard(coordsNew)) {
			cellNew = game.getCell(coordsNew);
			cellOld.setPlayer(null);  
			cellNew.setPlayer(this);
			}
			
			break;
				
		case 2: //RIGHT
			coordsOld = getCurrentCell().getPosition();
			coordsNew = (coordsOld.translate(Direction.RIGHT.getVector()));
			cellOld = getCurrentCell();
			if(isInsideBoard(coordsNew)) {
			cellNew = game.getCell(coordsNew);
			cellOld.setPlayer(null);  
			cellNew.setPlayer(this);
			}
			
			break;
			
		case 3: //UP
			coordsOld = getCurrentCell().getPosition();
			coordsNew = (coordsOld.translate(Direction.UP.getVector()));
			cellOld = getCurrentCell();
			if(isInsideBoard(coordsNew)) {
			cellNew = game.getCell(coordsNew);
			cellOld.setPlayer(null);  
			cellNew.setPlayer(this);
			}
			
			break;
		
			
		}
		
	}
	
}
