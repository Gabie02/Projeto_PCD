package game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameGuiServer {
	public static final int SOCKET = 8080;
	private List<Thread> clients = new ArrayList<>();
	private Game game;
	//A ideia é ter uma thradPool que a cada GAMEINTERVAL envia o estado do jogo a todos os clientes
//	private ScheduledThreadPoolExecutor = 
	
	public GameGuiServer(Game game) {
		this.game = game;
	}

	//Método que irá estar sempre à escuta no canal à espera dos clientes
	public void startServing() throws IOException {
		ServerSocket ss = new ServerSocket(SOCKET);
		try {
			while(true){
				Socket socket = ss.accept();
				Thread t = new GameDealWithClient(socket);
				clients.add(t);
				t.start();
			}			
		} finally {
			ss.close();
		}
	}
	
	private void sendGameState() {
		
	}
}
