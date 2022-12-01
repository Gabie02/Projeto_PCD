package game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.event.MenuKeyEvent;

import environment.Direction;
import gui.GameGuiMain;

import java.awt.event.KeyEvent;
import gui.BoardJComponent;

public class GameGuiClient {
	private BufferedReader in;
	private PrintWriter out;
	private Socket socket;
	private int socketNum;
	private String address;
	
	private int LEFT;
	private int RIGHT;
	private int UP;
	private int DOWN;
	
	private HumanPlayer player;
	private Direction lastKeyPressed;
	private GameGuiServer server;
	private BoardJComponent keyListener;


	public GameGuiClient(int socket, String address, int left, int right, int up,
			int down) {
		LEFT = left;
		RIGHT = right;
		UP = up;
		DOWN = down;
//		keyListener = new BoardJComponent(null);
	}

	// Vão ser recebidos 6 argumentos: endereço e porto da aplicação principal, 
	//	teclas para movimentar nasquatro direções 
	public static void main(String[] args) {
		System.out.println("Códigos para as teclas:\n"
				+ "LEFT	_ARROW: 0x25"
				+ "RIGHT_ARROW: 0x27"
				+ "UP_ARROW: 0x26"
				+ "DOWN_ARROW: 0x28"
				+ "A_KEY: 0x41"
				+ "D_KEY: 0x44"
				+ "W_KEY: 0x57"
				+ "S_KEY: 0x53"
				);
		
		if(args.length == 0) {
			GameGuiClient client = new GameGuiClient(GameGuiServer.SOCKET, "localhost",  
					KeyEvent.VK_LEFT,  
					KeyEvent.VK_RIGHT,  
					KeyEvent.VK_UP,  
					KeyEvent.VK_DOWN);
		}
		if(args.length != 6)
			throw new IllegalArgumentException("Número errado de argumentos. Deverá ter a seguinte sintaxe:\n"
					+ "	1. Endereço da aplicação;\n" 
					+ "	2. Porto da aplicação;\n" 
					+ "	3. Tecla da esquerda;\n" 
					+ "	4. Tecla da direita;\n"  
					+ "	5. Tecla de cima;\n"  
					+ "	6. Tecla de baixo;\n");
		
		
	}
	
	public void runClient() {
		try {
			//Apanha as keys do player
			connectToServer();
			
		} catch (IOException e) {// ERRO...
		} finally {//a fechar...
			try {
				socket.close();
			} catch (IOException e) {//... 
			}
		}
	}

	void connectToServer() throws IOException {
		InetAddress endereco = InetAddress.getByName(address);
		socket = new Socket(endereco, socketNum);
		in = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(socket.getOutputStream())),
				true);
	}
	
	private void sendDirection() {
//		lastKeyPressed = gui.BoardJComponent.getLas
	}
	
}
