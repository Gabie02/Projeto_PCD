package environment;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import game.Game;
import game.Player;

public class Cell {
	private Coordinate position;
	private Game game;
	private Player player=null;
	
	//TESTE
	private Lock lock = new ReentrantLock();
	private Condition ocupied = lock.newCondition();
	private Condition notOcupied = lock.newCondition();
	/////////
	
	public Cell(Coordinate position,Game g) {
		super();
		this.position = position;
		this.game=g;
	}

	public Coordinate getPosition() {
		return position;
	}

	public boolean isOcupied() {
		return player!=null;
	}


	public Player getPlayer() {
		return player;
	}

	
	
	// Should not be used like this in the initial state: cell might be occupied, must coordinate this operation
	public void setPlayer(Player player) {
		lock.lock();
		try {
			if(player == null) {
				this.player = null;
				notOcupied.signalAll();
				return;
			}
			while(isOcupied()) {
				try {
					System.out.println("Bloqueio na colocação");
					System.out.println("Posição: " + getPosition() + " Jogador a ocupar: " + getPlayer() + " Jogador a tentar ocupar: " + player);
					notOcupied.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			this.player = player;
			ocupied.signalAll();
		} finally {
			lock.unlock();
		}
		
	}
	
	
	
	

}
