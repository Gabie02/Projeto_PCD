package gui;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import game.Game;
import game.HumanPlayer;

public class GameServer extends Thread{
	public static final int SOCKET = 8081;
	private ArrayList<GameDealWithClient> clients = new ArrayList<>();
	private Game game;
	
	private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private final Runnable updateGameTask = new Runnable() {
	       public void run() { 
	    	   for (GameDealWithClient client : clients) {
	    		   client.sendGameState(new GameState(game.getBoard()));
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
					HumanPlayer human = new HumanPlayer(90 + count, game);
					
					GameDealWithClient t = new GameDealWithClient(socket, human);
					t.start();
					clients.add(t);
					
					//O humano começa mal se conecta
					human.start();
					
					//Contador de ids
					count++;
				}			
			} finally {
				ss.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//A cada Game.REFRESH_INTERVAL irá enviar o estado do jogo a todos os clientes
	private void setGameStateSender() {
		scheduler.scheduleAtFixedRate(updateGameTask, 0 ,game.REFRESH_INTERVAL, TimeUnit.MILLISECONDS);
	}
}
