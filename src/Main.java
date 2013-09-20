import javax.swing.*;
import java.awt.*;

/**
 * Java BlackJack Version: 0.2b Author: Thomas Auberson
 * 
 * Lets you play games of BlackJack in Java Swing
 * 
 */

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;

	// FIELDS
	public static final String version = "0.2b";

	private Window window;
	private MenuBar menu;

	private Dimension size = new Dimension(1000, 750);
	private String title = "Java BlackJack";
	private boolean resizable = false;

	// CONSTRUCTOR
	public Main() {

		// Initialise the JFrame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(size);
		setTitle(title);
		setResizable(resizable);
		this.setLocationRelativeTo(null); // Sets window in centre

		setLayout(new GridLayout(1, 1, 0, 0));

		// Generate the main game window
		window = new Window();
		this.add(window);

		// Generate a menu bar
		menu = new MenuBar(window);
		setJMenuBar(menu);

		setVisible(true);
	}

	// MAIN
	public static void main(String[] args) {
		System.out.println("Howdy");
		new Main();
	}

}
