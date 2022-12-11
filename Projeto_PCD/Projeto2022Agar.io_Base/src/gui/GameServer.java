package gui;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import environment.Cell;
import game.Game;
import game.HumanPlayer;

public class GameServer extends Thread{
	public static final int SOCKET = 8081;
	private ArrayList<GameDealWithClient> clients = new ArrayList<>();
	private Game game;
	
	private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	//Task para enviar a todos os clientes o novo estado de jogo
	private final Runnable updateGameTask = new Runnable() {
	       public void run() { 
	    	   for (GameDealWithClient client : clients) {
	    		   Cell[][] gameState = game.getBoard();
	    		   client.sendGameState(gameState);
	    	   }
		}};
	
	public GameServer(Game game) {
		this.game = game;
		setGameStateSender();
	}
	
	@Override
	public void run() {
		ServerSocket ss;
		int count = 0;
		try {
			ss = new ServerSocket(SOCKET);
			try {
				while(!game.gameOver){
					
					Socket socket = ss.accept();
					System.out.println("O servidor recebeu um cliente");
					HumanPlayer human = new HumanPlayer(100 + count, game);
					
					GameDealWithClient t = new GameDealWithClient(socket, human);
					clients.add(t);
					t.start();
					human.start();
					
					//Contador de ids
					count++;
				}			
			} finally {
				ss.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//A cada Game.REFRESH_INTERVAL irá enviar o estado do jogo a todos os clientes
	private void setGameStateSender() {
		scheduler.scheduleAtFixedRate(updateGameTask, 0, Game.REFRESH_INTERVAL, TimeUnit.MILLISECONDS);
	}
}
