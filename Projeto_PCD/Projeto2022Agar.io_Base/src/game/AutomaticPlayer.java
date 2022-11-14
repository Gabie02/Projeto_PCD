package game;

import environment.Cell;
import environment.Coordinate;
import environment.Direction;

/**
 * Class to demonstrate a player being added to the game.
 * @author luismota
 *
 */
public class AutomaticPlayer extends Player {
	public AutomaticPlayer(int id, Game game, byte strength) {
		super(id, game, strength);
	}

	public boolean isHumanPlayer() {
		return false;
	}

	@Override
	public void run() {
		Coordinate coords = new Coordinate(10,10);
		Cell posTesteConflito = game.getCell(coords);
		posTesteConflito.setPlayerToInitialPosition(this);
		
		game.notifyChange();
		//	O sleep é feito agora na classe "pai", Player
		super.run();
		
		while(true) {

			move();
			game.notifyChange();

			//Se já estiver morto, acabar o run
			if(isObstable())
				break;

			//Se o jogador é um dos vencedores, acabar o run e registar no jogo
			if(getCurrentStrength() == game.NUM_POINTS_TO_WIN) {
				//registar no jogo?
				break;
			}

			try {
				// Fazer com players com maior energia se movam a cada (intervalo*pontos_de_energia) ciclos.
				sleep(Game.REFRESH_INTERVAL * (int)(originalStrength));
			} catch (InterruptedException e) {}
		}
	}


	@Override
	public void move() {		
		Direction dir = generateRandomDirection();
		
		Cell cellOld = getCurrentCell();
		
		Coordinate coordsOld = cellOld.getPosition();
		Coordinate coordsNew = coordsOld.translate(dir.getVector());
		
		if(!isInsideBoard(coordsNew)) 
			return;
		
		Cell cellNew = game.getCell(coordsNew);

		Player newCellPlayer = cellNew.getPlayer();
		//Apenas desocupar a posição se n houver conflicto
		if(newCellPlayer == null || !newCellPlayer.isObstable()){
			cellNew.setPlayerToInitialPosition(this);
			cellOld.setPlayerToInitialPosition(null);  
//			try {
//				cellNew.setPlayer(this);
//				cellOld.setPlayer(null);
//			} catch (InterruptedException e) {}
//
//		} else if(newCellPlayer.isObstable()) {
//			//O jogador vai bloquear
//				try {
//					cellNew.setPlayer(this);
//				} catch (InterruptedException e) {}
		} else {
			settleDisputeBetween(this, newCellPlayer);
		}
	}
	
	private Direction generateRandomDirection() {
		int random = ((int) (Math.random()*4));
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
		return dir;
	}
	

}
