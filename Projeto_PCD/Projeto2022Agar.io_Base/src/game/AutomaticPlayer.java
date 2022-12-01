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
		//	O sleep é feito agora na classe "pai", Player
		super.run();
		while(true) {
			
			//Se já estiver morto, acabar o run
			if(isObstable())
				break;

			//Se o jogador é um dos vencedores, acabar o run e registar no jogo
			if(hasWon()) {
				game.cdl.countDown();
				break;
			}

			//	Apenas players que não estão bloqueados na posição inicial é que fazem move()
			if(getCurrentCell()!=null)
				move();

			game.notifyChange();



			try {
				// Fazer com players com maior energia se movam a cada (intervalo*pontos_de_energia) ciclos.
				sleep(Game.REFRESH_INTERVAL * (int)(originalStrength));
			} catch (InterruptedException e) {}
		}
	}


	@Override
	public void move() {		
		Cell currCell = getCurrentCell();
		Direction dir = generateRandomDirection();

		Coordinate oldCoords = currCell.getPosition();
		Coordinate newCoords = oldCoords.translate(dir.getVector());

		if(!isInsideBoard(newCoords)) 
			return;

		Cell newCell = game.getCell(newCoords);

		Player newCellPlayer = newCell.getPlayer();

		//	Se o movimento for para um posição que estiver vazia.
		if(newCellPlayer == null){
			newCell.setPlayer(this);
			currCell.setPlayer(null);
			return;
		}

		//	Se o movimento for para um posição que estiver ocupada com um jogador morto (obstáculo).
		if(newCellPlayer.isObstable() || newCellPlayer.hasWon()) {
			newCell.setPlayer(this);
			return;
		} 
		
		// Se o movimento for para uma posição com um jogador
		settleDisputeBetween(this, newCellPlayer);
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
