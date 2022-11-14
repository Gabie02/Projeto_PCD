package gui;

import java.util.Observable;
import java.util.Observer;
import game.Game;
import game.AutomaticPlayer;

import javax.swing.JFrame;

import environment.Cell;
import environment.Coordinate;

public class GameGuiMain implements Observer {
	private JFrame frame = new JFrame("pcd.io");
	private BoardJComponent boardGui;
	private Game game;

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

		// Demo players, should be deleted
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Adiciona todos os players ao jogo
//		game.init();
		
		//Teste 
		AutomaticPlayer p1 = new AutomaticPlayer(3, game, (byte)3);
		AutomaticPlayer p2 = new AutomaticPlayer(2, game, (byte)2);
//		game.addPlayerToGame(p1);
//		game.addPlayerToGame(p2);
		p1.start();
		p2.start();
		
		//Obstáculos para teste
//		Coordinate coordsObs1 = new Coordinate(9,10);
//		Coordinate coordsObs2 = new Coordinate(10,12);
//		Cell posConflitoObs1 = game.getCell(coordsObs1);
//		Cell posConflitoObs2 = game.getCell(coordsObs2);
//		
//		AutomaticPlayer obs1 = new AutomaticPlayer(3, game, (byte)0);
//		AutomaticPlayer obs2 = new AutomaticPlayer(4, game, (byte)0);
//		posConflitoObs1.setPlayer(obs1);
//		posConflitoObs2.setPlayer(obs2);
	}

	@Override
	public void update(Observable o, Object arg) {
		boardGui.repaint();
	}

	public static void main(String[] args) {
		GameGuiMain game = new GameGuiMain();
		game.init();
	}

}
