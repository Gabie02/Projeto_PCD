package gui;

import java.util.Observable;
import java.util.Observer;
import game.Game;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class GameGuiMain implements Observer {
	protected JFrame frame = new JFrame("pcd.io");
	protected BoardJComponent boardGui;
	protected static Game game;
	private GameServer server;

	public GameGuiMain() {
		super();
		game = new Game();
		game.addObserver(this);
		
		buildGui();
	}

	protected void buildGui() {
		boardGui = new BoardJComponent(game, false);
		frame.add(boardGui);

		frame.setSize(800,800);
		frame.setLocation(0, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void init()  {
		frame.setVisible(true);
		server = new GameServer(game);
		server.start();
		//Adiciona todos os players automáticos ao jogo
		game.init();		
	
	}
	
	public static void gameOverMessage() {
		 JOptionPane.showMessageDialog(null, "Game Over", "The game is over!", JOptionPane.INFORMATION_MESSAGE);
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