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
				// Fazer com players com maior energia se movam a cada (intervalo*pontos_de_energia) ciclos.
				sleep(Game.REFRESH_INTERVAL * Byte.toUnsignedInt(originalStrength));
			} catch (InterruptedException e) {}
		}
	}

	@Override
	public void move() {
		int random = ((int) (Math.random()*4));
		Coordinate coordsOld = getCurrentCell().getPosition();
		Direction dir = Direction.UP;
		switch(random) {
		case 0: //LEFT
			dir = Direction.LEFT;
			break;

		case 1: //DOWN
			dir = Direction.DOWN;
			break;

		case 2: //RIGHT
			dir = Direction.RIGHT;
			break;

		case 3: //UP
			dir = Direction.UP;
			break;
		}
		Coordinate coordsNew = coordsOld.translate(dir.getVector());
		Cell cellOld = getCurrentCell();
		if(isInsideBoard(coordsNew)) {
			Cell cellNew = game.getCell(coordsNew);
			cellNew.setPlayer(this);
			cellOld.setPlayer(null);  
		}
//		System.out.println(this.getCurrentStrength() + " moveu-se");
		}

}
