import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Window extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;

	// FIELDS
	public Thread thread = new Thread(this); // The thread for game loop

	private Color background = new Color(0, 120, 0); // Green colour for
														// background
	public static Image backImg;
	private Image deckImg;

	// The whole deck of 52 Cards. Used as a backup ==> DO NOT MODIFY THIS DECK!
	private ArrayList<Card> deck;
	// The current deck in use. Not always 52 cards. Cards can be drawn and
	// shuffled from this
	private ArrayList<Card> currentDeck;

	private ArrayList<Player> players;
	private Player dealer;
	private Player user;
	private String userName = "PLAYER 0";

	private Player turnPlayer;

	private Image[] buttonImages = new Image[4];
	private Rectangle[] buttons = new Rectangle[4];
	//private String[] BUTTON_NAMES = { "Raise/Lower Bet", "Stand", "Hit","Double Down" };
	private int BUTTON_LOCATION_X = 50;
	private int[] BUTTON_LOCATION_Y = { 50, 150, 250, 350 };
	private Dimension BUTTON_SIZE = new Dimension(60, 60);
	private int SCREEN_WIDTH = 1000, SCREEN_HEIGHT = 750;
	private int DECK_WIDTH = 92, DECK_HEIGHT = 120;
	private int START_CHIPS = 1000;
	private int DEALER_Y = 50, PLAYERS_Y = 500;
	private int NUM_PLAYERS = 3;
	private int nextNUM_PLAYERS = NUM_PLAYERS;

	private Rectangle deckZone;

	private boolean inGame = false;

	// CONSTRUCTOR
	public Window() {
		loadDeck();
		this.addMouseListener(new MouseHandler(this));
		thread.start();

	}

	public void startGame() { // Initialises all the players and the deck
		NUM_PLAYERS = nextNUM_PLAYERS;
		players = new ArrayList<Player>();
		dealer = new Player(0, false, true, SCREEN_WIDTH / 2, DEALER_Y,
				"DEALER", this);
		players.add(new Player(START_CHIPS, true, false,
				(int) ((SCREEN_WIDTH / (NUM_PLAYERS)) * (0.5)), PLAYERS_Y,
				userName, this));
		user = players.get(0);
		for (int i = 1; i < NUM_PLAYERS; i++) {
			players.add(new Player(START_CHIPS, false, false,
					(int) ((SCREEN_WIDTH / (NUM_PLAYERS)) * (i + 0.5)),
					PLAYERS_Y, "PLAYER " + i, this));
		}
		players.add(dealer);
		setupButtons();
		inGame = true;
	}

	public void setupButtons() {
		deckZone = new Rectangle((SCREEN_WIDTH - DECK_WIDTH) / 2,
				(SCREEN_HEIGHT - DECK_HEIGHT) / 2, DECK_WIDTH, DECK_HEIGHT);
		// for(int i = 0; i<buttons)
		System.out.println("Buttons Setup!" + deckZone);
		for (int i = 0; i < BUTTON_LOCATION_Y.length; i++) {
			buttons[i] = new Rectangle(BUTTON_LOCATION_X, BUTTON_LOCATION_Y[i],
					(int) (BUTTON_SIZE.getWidth()),
					(int) (BUTTON_SIZE.getHeight()));
		}
	}

	public void startRound() {
		if (user.getInRound()) {
			JOptionPane.showMessageDialog(null, "You are already in a round",
					"", JOptionPane.PLAIN_MESSAGE);
			return;
		}
		shuffleDeck();
		dealer.startRound(drawCard(), drawCard());
		for (int i = 0; i < NUM_PLAYERS; i++) {
			players.get(i).startRound(drawCard(), drawCard());
			System.out.println("Player #" + i + " - New hand");
		}
		turnPlayer = players.get(0);
		System.out.println("Human player turn starts");
	}

	public void compareHands() {
		int dealerSum = dealer.getHand().getValue();
		boolean dealerBust = false;
		if (dealerSum > 21)
			dealerBust = true;
		for (int i = 0; i < NUM_PLAYERS; i++) {
			Player player = players.get(i);
			int playerSum = player.getHand().getValue();
			if (playerSum > 21)
				player.endRound(-1);
			else if (dealerBust)
				player.endRound(1);
			else {
				if (playerSum > dealerSum)
					player.endRound(1);
				else if (playerSum < dealerSum)
					player.endRound(-1);
				else
					player.endRound(0);
			}
		}
	}

	public void nextPlayer() {
		if (turnPlayer == dealer) {
			compareHands();
			turnPlayer = null;
		} else {
			turnPlayer = players.get((players.indexOf(turnPlayer)) + 1);
			turnPlayer.startTurn();
			System.out.println(turnPlayer + " turn starts");
		}
	}

	public void changeNumPlayers() {
		Object[] options = { 0, 1, 2 };
		Object s = JOptionPane.showInputDialog(null,
				"Select Number of AI Players:", "Number of Players",
				JOptionPane.PLAIN_MESSAGE, null, options, (NUM_PLAYERS - 1));
		if (s == null)
			return;
		int n = (int) s;
		nextNUM_PLAYERS = n + 1;
		JOptionPane.showMessageDialog(null,
				"<HTML>The number of AI players will be set to " + n
						+ "<br> next game.</HTML>", "",
				JOptionPane.PLAIN_MESSAGE);
	}

	public void startingChips() {
		Object[] options = { 1000, 2000, 5000, 10000 };
		Object s = JOptionPane.showInputDialog(null,
				"Select amount of starting chips:", "Starting Chips",
				JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if (s == null)
			return;
		int n = (int) s;
		START_CHIPS = n;
		JOptionPane.showMessageDialog(null,
				"<HTML>The amount of starting chips will be set to "
						+ START_CHIPS + "<br> next game.</HTML>", "",
				JOptionPane.PLAIN_MESSAGE);
	}

	public void loseGame(Player p) {

	}

	// HUMAN PLAYER INTERACTIONS
	public void playerLoseGame() {
		JOptionPane.showMessageDialog(null,
				"You have run out of chips and lost the game!", "",
				JOptionPane.PLAIN_MESSAGE);
		inGame = false;
	}

	public void playerHit() {
		if(!user.getInRound()) JOptionPane.showMessageDialog(null,"You are not currently in a round", "", JOptionPane.PLAIN_MESSAGE);
		if (turnPlayer != user)
			return;
		user.hit();
	}

	public void playerChangeBet() {
		user.changeBet();
	}

	public void playerDoubleDown() {
		if(!user.getInRound()) JOptionPane.showMessageDialog(null,"You are not currently in a round", "", JOptionPane.PLAIN_MESSAGE);
		if (turnPlayer != user)
			return;
		user.doubleDown();
	}

	public void playerSplit() {
		if(!user.getInRound()) JOptionPane.showMessageDialog(null,"You are not currently in a round", "", JOptionPane.PLAIN_MESSAGE);
		if (turnPlayer != user)
			return;
		// Split user hand
	}

	public void playerStand() {
		if(!user.getInRound()) JOptionPane.showMessageDialog(null,"You are not currently in a round", "", JOptionPane.PLAIN_MESSAGE);
		if (turnPlayer != user)
			return;
		user.endTurn();
	}

	public void playerChangeName() {
		String s = JOptionPane.showInputDialog(null, "Type a player name:",
				"Player Name", JOptionPane.PLAIN_MESSAGE);
		if (s == null)
			return;
		userName = s;
		user.setName(userName);
	}

	// DECK CONTROLS
	public void loadDeck() {
		deck = new ArrayList<Card>();
		int deckIndex = 0;

		for (int s = 0; s < 4; s++) {
			String suit = "Spades";
			if (s == 1)
				suit = "Hearts";
			if (s == 2)
				suit = "Diamonds";
			if (s == 3)
				suit = "Clubs";

			for (int i = 1; i <= 13; i++) {
				Image image = new ImageIcon("cards/" + suit + " - " + i
						+ ".png").getImage();
				deck.add(deckIndex, new Card(suit, i, image));
				System.out.println("Player #1 added");
				deckIndex++;
			}
		}
		for(int i=0; i<BUTTON_LOCATION_Y.length; i++){
			buttonImages[i] = new ImageIcon("res/Button"+i+".png").getImage();
		}
		deckImg = new ImageIcon("cards/deck.png").getImage();
		backImg = new ImageIcon("cards/back.png").getImage();
	}

	public Card drawCard() {
		return currentDeck.remove(0);
	}

	public void shuffleDeck() {
		currentDeck = new ArrayList<Card>(deck);
		Collections.shuffle(currentDeck);
	}

	public void drawDeck(Graphics g) {
		g.drawImage(deckImg, (getWidth() - DECK_WIDTH) / 2,
				(getHeight() - DECK_HEIGHT) / 2, DECK_WIDTH, DECK_HEIGHT, null);
	}

	// MOUSE CONTROLS
	public void mouseClicked(Point p) {
		SwingUtilities.convertPointFromScreen(p, this);
		System.out.println("Mouse CLicked" + p);
		if (deckZone.contains(p)) {
			System.out.println("Deck CLicked");
			if (user.getInRound()) {
				playerHit();
			} else {
				startRound();
			}
		}
		if (buttons[0].contains(p)) {
			playerChangeBet();
		}
		if (buttons[1].contains(p)) {
			playerStand();
		}
		if (buttons[2].contains(p)) {
			playerHit();
		}
		if (buttons[3].contains(p)) {
			playerDoubleDown();
		}
	}

	// RENDERING
	public void paintComponent(Graphics g) {
		g.setColor(background);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		if (inGame) {
			drawDeck(g);

			for (int i = 0; i < players.size(); i++) {
				players.get(i).draw(g, (turnPlayer == players.get(i)));
			}
			for (int i = 0; i < BUTTON_LOCATION_Y.length; i++) {
				g.drawImage(buttonImages[i], BUTTON_LOCATION_X, BUTTON_LOCATION_Y[i], (int)(BUTTON_SIZE.getWidth()),
						(int)(BUTTON_SIZE.getHeight()),null);
				if(((user.getInRound()) ^ (!(i==0)))){
					g.setColor(new Color(50, 50, 50, 100));
					g.fillRect(BUTTON_LOCATION_X, BUTTON_LOCATION_Y[i], (int)(BUTTON_SIZE.getWidth()),
						(int)(BUTTON_SIZE.getHeight()));
				}
			}
		}
		else{
			g.setColor(Color.white);
			g.setFont(new Font("Courier New", Font.BOLD, 100));
			g.drawString("GAME OVER!", 200, 380);
		}
	}

	// THREAD / GAME LOOP
	public void run() {
		startGame();
		while (true) {

			if (turnPlayer != null) {
				if (turnPlayer == user) {
				} else {
					turnPlayer.executeTurn();
				}
			}

			repaint();

			try {
				Thread.sleep(10); // 10ms loop cycles ==> Higher loop cycles =
									// lower CPU usage
				
			} catch (Exception e) {
			}
		}
	}
}
