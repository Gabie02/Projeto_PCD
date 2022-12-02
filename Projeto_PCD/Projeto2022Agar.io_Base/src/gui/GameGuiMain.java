package gui;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import game.Game;
import game.GameGuiServer;
import game.AutomaticPlayer;

import javax.swing.JFrame;

import environment.Cell;
import environment.Coordinate;

public class GameGuiMain implements Observer {
	private JFrame frame = new JFrame("pcd.io");
	private BoardJComponent boardGui;
	private static Game game;

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
		game.init();		
	
	}

	@Override
	public void update(Observable o, Object arg) {
		boardGui.repaint();
	}
	
	private void SetUpDebugConsole() {
		System.setOut(new java.io.PrintStream(System.out) {

            private StackTraceElement getCallSite() {
                for (StackTraceElement e : Thread.currentThread()
                        .getStackTrace())
                    if (!e.getMethodName().equals("getStackTrace")
                            && !e.getClassName().equals(getClass().getName()))
                        return e;
                return null;
            }

            @Override
            public void println(String s) {
                println((Object) s);
            }

            @Override
            public void println(Object o) {
                StackTraceElement e = getCallSite();
                String callSite = e == null ? "??" :
                    String.format("%s.%s(%s:%d)",
                                  e.getClassName(),
                                  e.getMethodName(),
                                  e.getFileName(),
                                  e.getLineNumber());
                super.println(o + "\t\tat " + callSite);
            }
        });
	}

	public static void main(String[] args) {
		GameGuiMain gameMain = new GameGuiMain();
		//-------------- Serve Para fazer debug --------
		gameMain.SetUpDebugConsole();
		//----------------------------------------------
		gameMain.init();
		try {
			GameGuiServer server = new GameGuiServer(game);
			server.startServing();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
