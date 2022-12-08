package gui;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import game.Game;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class GameGuiMain implements Observer {
	protected JFrame frame = new JFrame("pcd.io");
	protected BoardJComponent boardGui;
	protected static Game game;

	public GameGuiMain() {
		super();
		game = new Game();
		game.addObserver(this);
		
		buildGui();
	}

	private void buildGui() {
		boardGui = new BoardJComponent(game);
		frame.add(boardGui);

		frame.setSize(800,800);
		frame.setLocation(0, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void init()  {
		frame.setVisible(true);
		try {
			GameGuiServer server = new GameGuiServer(game);
			server.startServing();
		} catch (IOException e) {
			e.printStackTrace();
		}
//
//		// Demo players, should be deleted
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		//Adiciona todos os players ao jogo
		game.init();		
	
	}
	
	public static void gameOverMessage() {
		 JOptionPane.showMessageDialog(null, "The game is over!", "The game is over!", JOptionPane.INFORMATION_MESSAGE);
		 System.exit(0);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		boardGui.repaint();
	}

	public static void main(String[] args) {
		new GameGuiMain().init();
	}

}