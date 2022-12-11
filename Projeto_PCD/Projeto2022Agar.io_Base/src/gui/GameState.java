package gui;
import java.io.Serializable;
import environment.Cell;

public class GameState implements Serializable {
	
	private Cell[][] board;
	
	public GameState(Cell[][] board) {
		this.board = board;
	}

	public Cell[][] getBoard() {
		return board;
	}

}
