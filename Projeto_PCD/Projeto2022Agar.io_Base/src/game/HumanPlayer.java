package game;

import environment.Cell;
import environment.Coordinate;
import environment.Direction;
import gui.GameGuiClient;

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
		//	O sleep é feito na classe "pai", Player
		super.run();
		while(!game.gameOver) {
			
			//Se já estiver morto, acabar o run
			if(isObstable()) {
				GameGuiClient.playerDiedMessage();
				break;
			}

			//Se o jogador é um dos vencedores, acabar o run e registar no jogo
			if(hasWon()) {
				GameGuiClient.playerWonMessage();
				game.cdl.countDown();
				break;
			}
			
			//O player só se move quando recebe uma direção do cliente
			if(lastSentDirection!=null)
				move();
			
			//Fazer reset à última direção
			lastSentDirection = null;
			
			game.notifyChange();
			
			try {
				sleep(Game.REFRESH_INTERVAL);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
