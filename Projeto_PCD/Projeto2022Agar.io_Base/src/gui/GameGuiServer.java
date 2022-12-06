package gui;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import game.Game;
import game.HumanPlayer;

public class GameGuiServer {
	public static final int SOCKET = 8080;
	private List<GameDealWithClient> clients = new ArrayList<>();
	private Game game;
	
	private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private final Runnable updateGameTask = new Runnable() {
	       public void run() { 
	    	   for (GameDealWithClient client : clients) 
	    		   client.sendGameState(new GameState(game.getBoard()));
		}};
	
	public GameGuiServer(Game game) {
		this.game = game;
	}

	//Método que irá estar sempre à escuta no canal à espera dos clientes
	public void startServing() throws IOException {
		ServerSocket ss = new ServerSocket(SOCKET);
		int count = 0;
		try {
			while(!game.gameOver){
				
				Socket socket = ss.accept();
				
				HumanPlayer human = new HumanPlayer(90 + count, game);
				
				GameDealWithClient t = new GameDealWithClient(socket, human);
				clients.add(t);
				t.start();
				human.start();
				count++;
			}			
		} finally {
			ss.close();
		}
	}
	
	//A cada Game.REFRESH_INTERVAL irá enviar o estado do jogo a todos os clientes
	private void sendGameState() {
		scheduler.scheduleAtFixedRate(updateGameTask, 0 ,game.REFRESH_INTERVAL, TimeUnit.MILLISECONDS);
	}
}
