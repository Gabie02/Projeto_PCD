package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import environment.Cell;
import environment.Coordinate;
import gui.GameGuiMain;
import gui.GameState;
import gui.PlayerPosition;

public class Game extends Observable implements Serializable{

	public static final int DIMY = 30;
	public static final int DIMX = 30;

	private static final int NUM_PLAYERS = 90;
	private static final int NUM_FINISHED_PLAYERS_TO_END_GAME=3;

	public static final long REFRESH_INTERVAL = 400;
	public static final double MAX_INITIAL_STRENGTH = 3;
	public static final long MAX_WAITING_TIME_FOR_MOVE = 2000;
	public static final long INITIAL_WAITING_TIME = 10000;
	
	public static final int NUM_POINTS_TO_WIN = 10;
	public CountDownLatch cdl = new CountDownLatch(NUM_FINISHED_PLAYERS_TO_END_GAME);
	
	public boolean gameOver = false;
	public boolean hasStarted = false;

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
		
		return;
	}

	public void init() {
		//Depois trocar por NUM_PLAYERS - NUM_HUMAN_PLAYERS
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
			gameOver = true;
			GameGuiMain.gameOverMessage();
			return;
			
		} catch (InterruptedException e) {
			e.printStackTrace();
			return;
		}
	}

	public Cell[][] getBoard() {
		Cell[][] boardCopy = new Cell[DIMX][];
		 for (int i = 0; i < DIMX; ++i) {
             boardCopy[i] = new Cell[DIMY];
             System.arraycopy(board[i], 0, boardCopy[i], 0, DIMY);
        }
		return board;	
	}
	
	public void setBoard(Cell[][] newBoard) {
		board = newBoard;
	}
	
	//Outra forma de fazer é receber as novas posições e fazer 
	//esses players fazerem setPlayer() na nessa nova position
//	public void updateGame(ArrayList<PlayerPosition> playerPositions) {
//		Cell[][] newBoard = new Cell[DIMX][DIMY];
//		for (PlayerPosition pos : playerPositions) {
//			
//			Coordinate posCoords = pos.getCoord();
//			
//			newBoard[posCoords.x][posCoords.y] 
//					= new Cell(posCoords, this);
//			
//			newBoard[posCoords.x][posCoords.y]
//					.setPlayer(Player.getPlayers().get(pos.getPlayerId()));
//		}
//		
//		for (int i = 0; i < newBoard.length; i++) {
//			for (int j = 0; j < newBoard.length; j++) {
//				
//			}
//		}
//		
//	}
//	
//	public ArrayList<PlayerPosition> getGameState() {
//		ArrayList<PlayerPosition> playerPositions = new ArrayList<>();
//		for (int i = 0; i < board.length; i++) {
//			for (int j = 0; j < board.length; j++) {
//				Cell cell = board[i][j];
//				if(cell.isOcupied()) {
//					Player p = cell.getPlayer();
//					playerPositions.add(
//							new PlayerPosition(cell.getPosition(), 
//									p.getIdentification(), 
//									p.isHumanPlayer()));
//				}
//			}
//		}
//		return playerPositions;
//	}
	
	public Cell getCell(Coordinate at) {
		return board[at.x][at.y];
	}
	
	/**	
	 * Updates GUI. Should be called anytime the game state changes
	 */
	public void notifyChange() {
		setChanged();
		notifyObservers();
	}

	public Cell getRandomCell() {
		Cell newCell=getCell(new Coordinate((int)(Math.random()*Game.DIMX),(int)(Math.random()*Game.DIMY)));
		return newCell; 
	}


}
