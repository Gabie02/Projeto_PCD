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
		try { 
			try {
				while (true) {
					//Receber as teclas do cliente e enviar ao jogo
					String lastDirection = in.readLine();

					//Caso o cliente não tenha pressionado nenhuma tecla, mandar direção null
					if (lastDirection == null)
						continue;
					Direction dir = Direction.valueOf(lastDirection);
					player.setDirection(dir);

				} 
			} finally {
				in.close();
			}
		}catch(Exception e){}
	}

	//Serializa o objeto
	public void sendGameState(Cell[][] gameState) {
		try {
			out.writeObject(gameState);
			out.reset();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
