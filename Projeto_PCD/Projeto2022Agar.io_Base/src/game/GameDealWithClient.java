package game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class GameDealWithClient extends Thread {
	private BufferedReader in;
	private PrintWriter out;
	
	public GameDealWithClient(Socket socket) throws IOException {
		doConnections(socket);
	}
	@Override
	public void run() {
		try {
			serve();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void doConnections(Socket socket) throws IOException {
		in = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(socket.getOutputStream())),
				true);
	}
	private void serve() throws IOException {
		while (true) {
			
			//A cada Game.REFRESH_INTERVAL enviar o estado do jogo do cliente e receber a última direção
		}
	}
}
