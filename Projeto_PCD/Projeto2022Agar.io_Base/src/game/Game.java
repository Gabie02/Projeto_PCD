package game;


import java.util.Observable;
import environment.Cell;
import environment.Coordinate;

public class Game extends Observable {

	public static final int DIMY = 30;
	public static final int DIMX = 30;
	
	private static final int NUM_PLAYERS = 90;
	private static final int NUM_FINISHED_PLAYERS_TO_END_GAME=3;

	public static final long REFRESH_INTERVAL = 400;
	public static final double MAX_INITIAL_STRENGTH = 3;
	public static final long MAX_WAITING_TIME_FOR_MOVE = 2000;
	public static final long INITIAL_WAITING_TIME = 10000;
	
	public static final int NUM_POINTS_TO_WIN = 10;
	//Adicao CountDownLatch
	public CountDownLatch cdl = new CountDownLatch(NUM_FINISHED_PLAYERS_TO_END_GAME);

	protected Cell[][] board;

	public Game() {
		board = new Cell[Game.DIMX][Game.DIMY];
	
		for (int x = 0; x < Game.DIMX; x++) 
			for (int y = 0; y < Game.DIMY; y++) 
				board[x][y] = new Cell(new Coordinate(x, y),this);
	}
	
	/** 
	 * @param player 
	 */
	public void addPlayerToGame(Player player) {
		Cell initialPos=getRandomCell();
		initialPos.setPlayerToInitialPosition(player);
		
		// To update GUI
		notifyChange();
	}

	public void init() {
		//Depois trocar por NUM_PLAYERS
		for (int i = 1; i <= NUM_PLAYERS; i++) {
			int randomEnergy = 1 + (int)(Math.random() * MAX_INITIAL_STRENGTH);
			new AutomaticPlayer(i, this, (byte)randomEnergy).start();
		}
			//Tentativa CountDownLatch
			gameIsOver();
		
	}
	
	private void gameIsOver() {
		try {
			cdl.await();
			System.err.println("ACABOU O JOGO");
			System.exit(0);
			return;
			
		} catch (InterruptedException e) {
			e.printStackTrace();
			return;
		}
//		int count = 0;
//		for (int x = 0; x < Game.DIMX; x++) 
//			for (int y = 0; y < Game.DIMY; y++) 
//				//Adicionar mais tarde nesta condição que o player tem que ser humano
//				if(board[x][y].isOcupied() && board[x][y].getPlayer().getCurrentStrength() == NUM_POINTS_TO_WIN)
//					count++;
//		return count>=NUM_FINISHED_PLAYERS_TO_END_GAME;
	}
	
	public Cell getCell(Coordinate at) {
		return board[at.x][at.y];
	}
	
	/**	
	 * Updates GUI. Should be called anytime the game state changes
	 */
	public void notifyChange() {
//		if(gameIsOver()) {
//			//acabar o jogo
//		}
		setChanged();
		notifyObservers();
	}

	public Cell getRandomCell() {
		Cell newCell=getCell(new Coordinate((int)(Math.random()*Game.DIMX),(int)(Math.random()*Game.DIMY)));
		return newCell; 
	}
}
