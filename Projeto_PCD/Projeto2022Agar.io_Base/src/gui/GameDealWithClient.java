package gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

import environment.Cell;
import environment.Direction;
import game.HumanPlayer;

public class GameDealWithClient extends Thread {
	
	private HumanPlayer player;
	private GameGuiClient client;
	private BufferedReader in;
	private ObjectOutputStream out;
	
	public GameDealWithClient(Socket socket, HumanPlayer player) throws IOException {
		doConnections(socket);
		this.player = player;
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
		out = new ObjectOutputStream(socket.getOutputStream());
	}
	
	private void serve() {
		while (true) {
			//Receber as teclas do cliente e enviar ao jogo
			try {
				String lastDirection = in.readLine();
				Direction dir = Direction.valueOf(lastDirection);
//				if(dir == null)
//					System.err.println("ERRO AO RECEBER A DIREÇÃO DO CLIENTE");
				player.setDirection(dir);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	//Serializa o objeto
	public void sendGameState(GameState gameState) {
//		try {
//			out.flush();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
			
		try {
			out.writeObject(gameState);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
