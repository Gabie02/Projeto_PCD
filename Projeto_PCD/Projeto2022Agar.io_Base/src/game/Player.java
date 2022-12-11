package game;
import java.io.Serializable;
import environment.Cell;
import environment.Coordinate;

/**
 * Represents a player.
 * @author luismota
 *
 */
public abstract class Player extends Thread implements Serializable{

	protected  Game game;

	private int id;
	
	private byte currentStrength;
	protected byte originalStrength;
	
	public Player(int id, Game game, byte strength) {
		super();
		this.id = id;
		this.game=game;
		currentStrength=strength;
		originalStrength=strength;
	}

	public Cell getCurrentCell() {
		for (int i = 0; i < Game.DIMX; i++) {
			for (int j = 0; j < Game.DIMY; j++) {
				
				Cell cell = game.getCell(new Coordinate(i, j));
				
				if(cell.getPlayer()!=null && cell.getPlayer().equals(this))
					return cell;
			}
			
		}
		return null;
	}
	
	@Override
	public void run() {
		game.addPlayerToGame(this);
		if(!game.hasStarted) {
			try {
				sleep(Game.INITIAL_WAITING_TIME);
			} catch (InterruptedException e1) {}
		}
		game.hasStarted = true;
	}
	
	public boolean hasWon() {
		return currentStrength == Game.NUM_POINTS_TO_WIN;
	}
	
	public boolean isObstable() {
		return currentStrength == 0;
	}
	
	public void setAsObstacle() {
		currentStrength = 0;
	}
	
	public static void settleDisputeBetween(Player p1, Player p2) {
		Player winner = (p1.currentStrength > p2.currentStrength) ? p1 : p2;
		Player loser = (p1.currentStrength > p2.currentStrength) ? p2 : p1;
		
		winner.currentStrength += loser.currentStrength; 
		
		//Limitar o num de pontos do vencedor a 10
		winner.currentStrength = (winner.currentStrength > 10) ? 
				winner.currentStrength=10 : winner.currentStrength;
		
		loser.setAsObstacle();
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
