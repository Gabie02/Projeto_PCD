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

public class GameGuiClient extends GameGuiMain{
	private ObjectInputStream in;
	private PrintWriter out;
	private Socket socket;
	private int socketNum;
	private String address;
	
	private int LEFT;
	private int RIGHT;
	private int UP;
	private int DOWN;
	
	private Direction lastDirection;
	private BoardJComponent keyListener = boardGui;

	public GameGuiClient(int socket, String address, int left, int right, int up, int down) {
		super();
		LEFT = left;
		RIGHT = right;
		UP = up;
		DOWN = down;
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
			receiveGameState();
			
		} catch (IOException e) {// ERRO...
		} finally {//a fechar...
			try {
				socket.close();
			} catch (IOException e) {//... 
			}
		}
	}

	private void receiveGameState() {
		while(!game.gameOver) {
			try {
				GameState gameState = (GameState)in.readObject();
				game.setBoard(gameState.getBoard());
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
	}
	
	private void sendDirectionToServer() {
		lastDirection = keyListener.getLastPressedDirection();
		// Mandar ao servidor
		out.print(lastDirection.name());
		keyListener.clearLastPressedDirection();
	}
	
	// Vão ser recebidos 6 argumentos: endereço e porto da aplicação principal, 
			//	teclas para movimentar nasquatro direções 
	public static void main(String[] args) {
		System.out.println("Códigos para as teclas:\n"
				+ "\nLEFT	_ARROW: 0x25"
				+ "\nRIGHT_ARROW: 0x27"
				+ "\nUP_ARROW: 0x26"
				+ "\nDOWN_ARROW: 0x28"
				+ "\nA_KEY: 0x41"
				+ "\nD_KEY: 0x44"
				+ "\nW_KEY: 0x57"
				+ "\nS_KEY: 0x53"
				);
		switch (args.length) {
		case 0:
			GameGuiClient client = new GameGuiClient(GameGuiServer.SOCKET, "localhost", 
					KeyEvent.VK_LEFT,  
					KeyEvent.VK_RIGHT,  
					KeyEvent.VK_UP,  
					KeyEvent.VK_DOWN);
			client.init();
			break;
//		case 6:
//			GameGuiClient client = new GameGuiClient(GameGuiServer.SOCKET, "localhost", 
//					KeyEvent.VK_LEFT,  
//					KeyEvent.VK_RIGHT,  
//					KeyEvent.VK_UP,  
//					KeyEvent.VK_DOWN);
//			break;
		default:
			throw new IllegalArgumentException("Número errado de argumentos. Deverá ter a seguinte sintaxe:\n"
					+ "	1. Endereço da aplicação;\n" 
					+ "	2. Porto da aplicação;\n" 
					+ "	3. Tecla da esquerda;\n" 
					+ "	4. Tecla da direita;\n"  
					+ "	5. Tecla de cima;\n"  
					+ "	6. Tecla de baixo;\n");
		}
			


	}

}
