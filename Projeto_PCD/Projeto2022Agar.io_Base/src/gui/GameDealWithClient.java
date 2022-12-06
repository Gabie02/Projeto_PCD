package gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;

import environment.Cell;

public class GameDealWithClient extends Thread {
	private BufferedReader in;
	private ObjectInputStream out;
	
	public GameDealWithClient(Socket socket) throws IOException {
		doConnections(socket);
	}
	@Override
	public void run() {
		serve();
	}

	void doConnections(Socket socket) throws IOException {
		//O in é um canal de texto
		in = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		//O out é um canal de objetos (é necessário fazer a limpeza da cache antes de enviar um novo objeto)
		out = new ObjectInputStream(socket.getInputStream());
	}
	private void serve() {
		while (true) {
			//Receber as teclas do cliente e enviar ao jogo
			
			
		}
	}
	
	//Serializa o objeto
	public void sendGameState(GameState gameState) {
		
	}
	
}
