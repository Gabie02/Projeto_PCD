package game;



import environment.Cell;
import environment.Coordinate;

/**
 * Represents a player.
 * @author luismota
 *
 */
public abstract class Player extends Thread {

	protected  Game game;

	private int id;
	
	private byte currentStrength;
	protected byte originalStrength;
	
	
	
	public Cell getCurrentCell() {
		for (int x = 0; x < Game.DIMX; x++) 
			for (int y = 0; y < Game.DIMY; y++) {
				//Isto tudo é para tentar descobrir o erro
				boolean playerInCellNotNull = game.board[x][y].isOcupied();
				try {
					if(playerInCellNotNull && game.board[x][y].getPlayer().equals(this))
								return game.board[x][y];
				} catch (NullPointerException e) {
					System.err.println(game.board[x][y].toString() 
							+ " \nplayerInCellNotNull / cellIsOcupied? " + playerInCellNotNull 
							+ " \nCell.getPlayer(): " + game.board[x][y].getPlayer()
							+ " \nThread: " + getId()
							+ " \nthis: " +  this);
					e.printStackTrace();
				}
			}
		return null;
	}
	
	public Player(int id, Game game, byte strength) {
		super();
		this.id = id;
		this.game=game;
		currentStrength=strength;
		originalStrength=strength;
	}
	
	@Override
	public void run() {
		Coordinate initialPos = game.addPlayerToGame(this);
		System.out.println("Thread nº" + getId() + " player nº" + getIdentification());
		try {
			sleep(game.INITIAL_WAITING_TIME);
		} catch (InterruptedException e1) {}
	}
	
	public boolean hasWon() {
		return currentStrength == game.NUM_POINTS_TO_WIN;
	}
	
	public boolean isObstable() {
		return (currentStrength == 0 || hasWon());
	}
	
	private void setAsObstacle() {
		currentStrength = 0;
	}
	
	public static void settleDisputeBetween(Player p1, Player p2) {
		System.out.println("--- [Disputa] --- \n	Entre: \n	" + p1 + "\n	" + p2);
		Player winner = (p1.currentStrength > p2.currentStrength) ? p1 : p2;
		Player loser = (p1.currentStrength > p2.currentStrength) ? p2 : p1;
		
		winner.currentStrength += loser.currentStrength; 
		
		//Limitar o num de pontos do vencedor a 10
		winner.currentStrength = (winner.currentStrength > 10) ? 
				winner.currentStrength=10 : winner.currentStrength;
		
		loser.setAsObstacle();
		System.out.println("	Vencedor da disputa id=" + winner.id);
	}
	
	public abstract boolean isHumanPlayer();
	
	@Override
	public String toString() {
		return "Player [id=" + id + ", currentStrength=" + currentStrength + ", " + getCurrentCell()
		+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public byte getCurrentStrength() {
		return currentStrength;
	}

	public int getIdentification() {
		return id;
	}
	
	public abstract void move();
	
	public boolean isInsideBoard(Coordinate coords) {
		if(coords.x >= Game.DIMX || coords.x < 0 || coords.y >= Game.DIMY || coords.y < 0)
			return false;
		return true;
	}
	
		
	
}
