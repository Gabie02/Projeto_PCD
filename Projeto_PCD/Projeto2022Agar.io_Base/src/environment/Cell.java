package environment;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import game.Game;
import game.Player;

public class Cell {
	private Coordinate position;
	private Game game;
	private Player player=null;
	
	private Lock lock = new ReentrantLock();
	private Condition notOcupied = lock.newCondition();
	
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
	
	
	//Usar mecanismo de bloqueio diferente ao do InitialPosition
	public synchronized void setPlayer(Player player) {
		if(player == null) {
			disoccupyCell();
			return;
		}

		if(isOcupied()) {
			System.out.println("--- [Bloqueio por obstáculo] ---\n "
					+ "Posição: " + getPosition() 
					+ "\n Jogador a ocupar: " + getPlayer() 
					+ "\n Jogador a tentar ocupar: " + player);
			createThreadInterrupt();
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println("Fui interrompido " + player);
				return;
			}
		}
		this.player = player;
	}
	
	private void disoccupyCell() {
		this.player = null;
		lock.lock();
		try {
			notOcupied.signalAll();
		} finally {
			lock.unlock();
		}
	}
	
	// Should not be used like this in the initial state: cell might be occupied, must coordinate this operation
	public void setPlayerToInitialPosition(Player player) {
		lock.lock();
		try {
			//A cell está a ser desocupada
			if(player == null) {
				disoccupyCell();
				return;
			}
			
			//A cell está a tentar ser ocupada
			while(isOcupied()) {
				try {
					System.out.println("--- [Bloqueio na colocação inicial] ---\n "
							+ "Posição: " + getPosition() 
							+ "\n Jogador a ocupar: " + getPlayer() 
							+ "\n Jogador a tentar ocupar: " + player);
					notOcupied.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			this.player = player;
		} finally {
			lock.unlock();
		}
		
	}

	@Override
	public String toString() {
		return "Cell(" + position.x + ", " + position.y + ")";
	}
	
	public void createThreadInterrupt() {
		final Thread playerToInterrupt = Thread.currentThread();
		Thread C = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    System.out.println("Passaram 2 seg, a interromper o player");
                    playerToInterrupt.interrupt();

                } catch (InterruptedException e) {
                    System.out.println("C interrupted");
                    return;
                }
            }
        };//C
        C.start();
	}
	
	
	
	

}
