package environment;
import java.io.Serializable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import game.Game;
import game.Player;

public class Cell implements Serializable{
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
				createThreadInterrupt();
				notOcupied.await();
			} catch (InterruptedException e) {
				game.addPlayerToGame(player);
				return;
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
		final Player playerToInterrupt = (Player) Thread.currentThread();
		new Thread() {
            @Override
            public void run() {
                try {
                	if(!game.hasStarted) {
                		Thread.sleep(Game.INITIAL_WAITING_TIME);
                		if(playerToInterrupt.getCurrentCell() != null) 
                			return;
                	}
                    Thread.sleep(Game.MAX_WAITING_TIME_FOR_MOVE);
                    if(playerToInterrupt.getState().equals(Thread.State.WAITING)) 
                    	playerToInterrupt.interrupt();

                } catch (InterruptedException e) {
                    return;
                }
            }
        }.start();
	}
	
	
	
	

}
