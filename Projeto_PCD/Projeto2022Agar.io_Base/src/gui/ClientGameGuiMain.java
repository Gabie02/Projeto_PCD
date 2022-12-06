package gui;

public class ClientGameGuiMain extends GameGuiMain {

	public ClientGameGuiMain() {
		super();
	}

	@Override 
	public void init()  {
		frame.setVisible(true);	
	
	}

	public BoardJComponent getBoardJComponent() {
		return boardGui;
	}
	
}
