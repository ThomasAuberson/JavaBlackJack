import javax.swing.*;
import java.awt.event.*;

public class MenuBar extends JMenuBar implements ActionListener {

	private static final long serialVersionUID = 1L;
	// FIELDS
	private Window window;

	// CONSTRUCTOR
	public MenuBar(Window w) {
		window = w;

		JMenu menu;

		menu = new JMenu("File");
		add(menu);

		menu.add(menuItem("New Game"));
		menu.add(new JSeparator());
		menu.add(menuItem("Player Name"));
		menu.add(menuItem("Number of Players"));
		menu.add(menuItem("Starting Chips"));
		menu.add(new JSeparator());
		menu.add(menuItem("About"));

		menu = new JMenu("Game");
		add(menu);

		menu.add(menuItem("Start Round"));
		menu.add(new JSeparator());
		menu.add(menuItem("Raise/Lower Bet"));
		menu.add(new JSeparator());
		menu.add(menuItem("Hit"));
		menu.add(menuItem("Stand"));
		menu.add(menuItem("Double Down"));
		//menu.add(menuItem("Split"));

	}

	public JMenuItem menuItem(String name) {
		JMenuItem m = new JMenuItem(name);
		m.addActionListener(this);
		m.setActionCommand(name);
		return m;
	}

	// ACTION LISTENER
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("New Game")) {
			// Start a new Game
			window.startGame();
			System.out.println("Game Started");
		} else if (e.getActionCommand().equals("Start Round")) {
			window.startRound();
		} else if (e.getActionCommand().equals("Stand")) {
			window.playerStand();
		} else if (e.getActionCommand().equals("Hit")) {
			window.playerHit();
		} else if (e.getActionCommand().equals("Double Down")) {
			window.playerDoubleDown();
		} else if (e.getActionCommand().equals("Split")) {
			//window.playerSplit();
		} else if (e.getActionCommand().equals("Raise/Lower Bet")) {
			window.playerChangeBet();
		} else if (e.getActionCommand().equals("About")) {
			JOptionPane.showMessageDialog(null, "<HTML>Java BlackJack<br>Version: "+Main.version+"<br>Author: Thomas Auberson<br><br>A basic blackjack game on java swing.</HTML>","Java Black Jack",JOptionPane.PLAIN_MESSAGE);
		} else if(e.getActionCommand().equals("Number of Players")){
			window.changeNumPlayers();
		} else if(e.getActionCommand().equals("Player Name")){
			window.playerChangeName();
		} else if(e.getActionCommand().equals("Starting Chips")){
			window.startingChips();
		}
		
		
	}
}
