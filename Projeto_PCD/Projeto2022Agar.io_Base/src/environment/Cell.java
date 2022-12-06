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
	
	private Lock lock = new ReentrantLock();
	private Condition notOcupied = lock.newCondition();
	
	public Cell(Coordinate position, Game g) {
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
	
	public synchronized void setPlayer(Player player) {
		if(player == null) {
			disoccupyCell();
			notifyAll();
			return;
		}
		
		//Player fica bloqueado se a cell já estiver ocupada
		while(isOcupied()) {
			if(getPlayer().getCurrentStrength() != 0 && getPlayer().getCurrentStrength() != 10) {
				System.err.println("PLAYER NÃO É UM OBSTÁCULO, condições: ");
				System.err.println("newCellPlayer == null? ");
				System.err.println(getPlayer() == null);
				System.err.println("newCellPlayer.isObstable() || newCellPlayer.hasWon()");
				System.err.println((getPlayer().isObstable() || getPlayer().hasWon()));
			}
			
			System.out.println("--- [Bloqueio por obstáculo] --- "
					+ "\n Jogador a ocupar: " + getPlayer() 
					+ "\n Jogador a tentar ocupar: " + player);
			createThreadInterrupt();
			try {
				wait();
			} catch (InterruptedException e) {
				//Quando o player é acordado, deverá tentar ir para outra posição (não é feito o this.player = player)
				return;
			}
			
		}
		//A cell está desocupada
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
		lock.unlock();
	}

	@Override
	public String toString() {
		return "Cell(" + position.x + ", " + position.y + ")";
	}
	
	public void createThreadInterrupt() {
		final Thread playerToInterrupt = Thread.currentThread();
		new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    System.out.println("Passaram 2 seg, a interromper " + playerToInterrupt);
                    playerToInterrupt.interrupt();

                } catch (InterruptedException e) {
                    return;
                }
            }
        }.start();
	}
	
	
	
	

}
