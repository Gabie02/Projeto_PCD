package gui;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import environment.Direction;

import java.awt.event.KeyEvent;

public class GameGuiClient extends GameGuiMain {
	private ObjectInputStream in;
	private PrintWriter out;
	private Socket socket;
	private int socketNum;
	private String address;
	
	boolean alternativeKeys;
	
	private Direction lastDirection;
	private BoardJComponent keyListener = boardGui;

	public GameGuiClient(int socket, String address, boolean alternativeKeys) {
		super();
		socketNum = socket;
		this.address = address;
		this.alternativeKeys = alternativeKeys;
	}
	
	@Override 
	public void init()  {
		frame.setVisible(true);	
		frame.setLocation(800, 150);
		runClient();
	}
	
	public void runClient() {
		try {
			//Apanha as keys do player
			connectToServer();
			
			//Fica a receber o gameState a cada intervalo
			//receiveGameState();
			
			sendDirectionToServer();
			
		} catch (IOException e) {
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}

	private void receiveGameState() {
		while(!game.gameOver) {
			try {
				GameState gameState = (GameState)in.readObject();
				game.setBoard(gameState.getBoard());
				game.notifyChange();
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	void connectToServer() throws IOException {
		InetAddress endereco = InetAddress.getByName(address);
		
		socket = new Socket(endereco, socketNum);
		
		//O in é um canal de objetos
		in = new ObjectInputStream (socket.getInputStream());
		
		//O out é um canal de texto
		out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(socket.getOutputStream())),
				true);
		
		System.out.println("Conexões do lado do CLIENT feitas para o socket: " + socket.toString());
	}
	
	private void sendDirectionToServer() {
		while(!game.gameOver) {
			lastDirection = keyListener.getLastPressedDirection();
			if(lastDirection == null) 
				continue;
			// Mandar ao servidor
			out.println(lastDirection.name());
			System.out.println("Direção " +  lastDirection + " enviada");
			keyListener.clearLastPressedDirection();
		}
	}
	
	public static void main(String[] args) {
		GameGuiClient client;
		switch (args.length) {
		case 0:
			client = new GameGuiClient(GameGuiServer.SOCKET, "localhost", false);
			client.init();
			break;
		case 3:
			int socket = Integer.parseInt(args[0]);
			boolean keys = Boolean.getBoolean(args[2]);
			client = new GameGuiClient(socket, args[1], keys);
			client.init();
			break;
		default:
			throw new IllegalArgumentException("Número errado de argumentos. Deverá ter a seguinte sintaxe:\n"
					+ "	1. Endereço da aplicação;\n" 
					+ "	2. Porto da aplicação;\n" 
					+ " 3. Teclas alternativas? 1 (Sim) ou 0 (Não)");
		}
			


	}

}
