package game;

import environment.Cell;
import environment.Coordinate;
import environment.Direction;
import gui.GameDealWithClient;

public class HumanPlayer extends Player {
	
//	private Direction lastSentDirection;
	private GameDealWithClient client;
	
	public HumanPlayer(GameDealWithClient client, int id, Game game) {
		super(id, game, (byte)5);
		this.client = client;
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

			//Apenas players que não estão bloqueados na posição inicial é que fazem move()
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
		Direction dir = client.getLastSentDirection();

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
