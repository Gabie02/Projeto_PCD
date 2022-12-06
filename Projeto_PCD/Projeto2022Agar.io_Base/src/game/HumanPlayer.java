package game;

import environment.Cell;
import environment.Coordinate;
import environment.Direction;

public class HumanPlayer extends Player {
	
	private Direction lastSentDirection;
	
	public HumanPlayer(int id, Game game) {
		super(id, game, (byte)5);
	}

	@Override
	public boolean isHumanPlayer() {
		return true;
	}

	@Override
	public void run() {
		//	O sleep é feito agora na classe "pai", Player
		super.run();
		while(!game.gameOver) {
			
			//Se já estiver morto, acabar o run
			if(isObstable())
				break;

			//Se o jogador é um dos vencedores, acabar o run e registar no jogo
			if(hasWon()) {
//				System.err.println("Jogador " + this + " ganhou");
				game.cdl.countDown();
				break;
			}

			if(lastSentDirection!=null)
				move();
			
			game.notifyChange();
		}
	}
	
	public void setDirection(Direction dir) {
		lastSentDirection = dir;
	}

	
	@Override
	public void move() {
		Cell currCell = getCurrentCell();
		Direction dir = lastSentDirection;

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
			//Ignorar
			return;
		} 
		
		// Se o movimento for para uma posição com um jogador
		settleDisputeBetween(this, newCellPlayer);
	
	}
	
}
