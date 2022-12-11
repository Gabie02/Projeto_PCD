package gui;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

import javax.swing.JFrame;

import environment.Cell;
import environment.Direction;
import game.Player;

import java.awt.event.KeyEvent;

/**
 * @author Usuario
 *
 */
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
			connectToServer();
			createGameStateReceivingThread();
			sendDirectionToServer();
		} catch (IOException e) {
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}

	
	/**
	 * Cria uma thread que fica à espera de um novo estado de jogo no 
	 * canal de objetos e atualiza o jogo do utilizador.
	 */
	private void createGameStateReceivingThread() {
		new Thread(){
			@Override
			public void run(){
				try { 
					try {
						while(!game.gameOver) {
							Cell[][] gameState = 
									(Cell[][])in.readObject();
							
							game.setBoard(gameState);
							game.notifyChange();
						}
					} finally {
						in.close();
					}
				}catch(Exception e){}
			}
	}.start();

	}

	void connectToServer() throws IOException {
		InetAddress endereco = InetAddress.getByName(address);
		socket = new Socket(endereco, socketNum);
		
		//O in é um canal de objetos
		in = new ObjectInputStream(socket.getInputStream());
		
		//O out é um canal de texto
		out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(socket.getOutputStream())),
				true);
	}
	
	//Apanha as keys do player e manda para o servidor
	private void sendDirectionToServer() {
		while(!game.gameOver) {
			lastDirection = keyListener.getLastPressedDirection();
			if(lastDirection == null) 
				continue;
			// Mandar ao servidor
			out.println(lastDirection.name());
			keyListener.clearLastPressedDirection();
		}
	}
	
	public static void main(String[] args) {
		GameGuiClient client;
		switch (args.length) {
		case 0:
			client = new GameGuiClient(GameServer.SOCKET, "localhost", false);
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
