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
	private Socket socket;
	
	public GameDealWithClient(Socket socket, HumanPlayer player){
		this.player = player;
		this.socket = socket;
	}
	@Override
	public void run() {
		try {
			doConnections(socket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Vou servir: ");
		serve();
	}

	void doConnections(Socket socket) throws IOException {
		
		//O in é um canal de texto
		in = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		//O out é um canal de objetos (é necessário fazer a limpeza da cache antes de enviar um novo objeto)
		out = new ObjectOutputStream(socket.getOutputStream());
		
		System.out.println("Conexões do lado do SERVER feitas para o socket: " + socket.toString());
	}
	
	private void serve() {
		while (true) {
			//Receber as teclas do cliente e enviar ao jogo
			try {
				String lastDirection = in.readLine();
				System.out.println("Direção recebida: " + lastDirection);
				
				//Caso o cliente não tenha precionado nenhuma tecla, mandar direção null
				if(lastDirection==null) 
					continue;
				Direction dir = Direction.valueOf(lastDirection);
				player.setDirection(dir);

			} catch (Exception e) {
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
