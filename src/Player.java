import java.awt.*;

import javax.swing.JOptionPane;

public class Player {

	// FIELDS
	private Window window;

	private boolean isDealer;
	private boolean isHuman;
	private boolean inRound = false;
	private boolean doubleDown = false;
	private boolean canDoubleDown = false;
	private boolean faceDownCondition = false;

	private String name;
	private Hand hand;

	private int chips;
	private int bet = 10;
	private int x, y;
	private int aiThinkTime = 0;

	private int DEALER_NAME_LENGTH = 40;
	private int CHAR_LENGTH = 6;
	private int TEXTSIZE = 20;
	private int TOP_GAP= 20;
	private int DEALER_NAME_GAP = 10;
	private int NAME_GAP = 135;
	private int CHIP_GAP = 160;
	// private int CHIP_ZONE = 10;
	private int AI_MAX_THINK_TIME = 250; // In 10ms
	private int AI_MIN_THINK_TIME = 50; // In 10ms
	private int AI_BET_MIN = 1, AI_BET_MAX = 10; // Initial AI bets fall within
													// this range x10

	// CONSTRUCTOR
	public Player(int c, boolean isH, boolean isD, int x, int y, String n,
			Window w) {
		window = w;
		chips = c;
		isHuman = isH;
		isDealer = isD;
		this.x = x;
		this.y = y;
		name = n;
		if (!isHuman && !isDealer)
			initialAIBet();
	}

	// GETTERS AND SETTERS
	public Hand getHand() {
		return hand;
	}

	public void setName(String s) {
		name = s;
	}

	public boolean getInRound() {
		return inRound;
	}

	// GAME ACTIONS
	public void startRound(Card c1, Card c2) {
		hand = new Hand(this, c1, c2, x, y);
		inRound = true;
		doubleDown = false;
		canDoubleDown = true;
		if (chips < (2 * bet))
			canDoubleDown = false;
		if (!isHuman)
			faceDownCondition = true;
	}

	public void endRound(int result) { // result = -1 (loss)/ 0 (draw)/ 1 (win)
		inRound = false;
		int b = bet;
		if (doubleDown)
			b *= 2;
		if (result == -1) {
			chips -= b;
		} else if (result == 1) {
			chips += b;
		}
		if (chips <= 0) {
			loseGame();
		}
		if (isHuman) {
			if (bet > chips)
				bet = chips;
		}
		if (!isHuman && !isDealer) {
			aiBet(result);
		}
	}

	public void loseGame() {
		if (isHuman)
			window.playerLoseGame();
		else
			window.loseGame(this);
	}

	public void changeBet() {
		if (inRound)
			JOptionPane.showMessageDialog(null,
					"You cannot change your bet once a round has started!",
					"Change Bet", JOptionPane.PLAIN_MESSAGE);
		else {
			String s = JOptionPane.showInputDialog(null, "Set bet to:",
					"Change Bet", JOptionPane.PLAIN_MESSAGE);
			if (s == null)
				return;
			int n = Integer.parseInt(s);
			if (n > chips)
				n = chips;
			bet = n;
		}
	}

	public void hit() {
		hand.addCard(window.drawCard());
		if (hand.getValue() >= 21)
			endTurn();
		canDoubleDown = false;
	}

	public void doubleDown() {
		if (!canDoubleDown) {
			if (isHuman) {
				JOptionPane.showMessageDialog(null,
						"<html>You cannot double down once you have already hit<br>and you must have sufficient chips!</html>", "",
						JOptionPane.PLAIN_MESSAGE);
			}
			return;
		}
		hit();
		doubleDown = true; // More to this?
		endTurn();
	}

	public void endTurn() {
		System.out.println(this + "Ends Turn");
		window.nextPlayer();
	}

	// RANDOM NUMBER GENERATOR
	public int generateRandom(int n1, int n2) { // Generate a random int between
												// n1 and n2
		int n = n2 - n1;
		double d = n1 + (n * Math.random());
		return (int) d;
	}

	// AI
	public void startTurn() {
		aiThinkTime = generateRandom(AI_MIN_THINK_TIME, AI_MAX_THINK_TIME);
		faceDownCondition = false;
	}

	public void initialAIBet() {
		int b = generateRandom(AI_BET_MIN, AI_BET_MAX);
		bet = b * 10;
	}

	public void aiBet(int result) {
		int b = (int) (bet * 0.1);
		if (result == -1) {
			b = generateRandom((int) (b / 4), b);
		}
		if (result == 0) {
			b = generateRandom((int) (b / 2), b * 2);
		}
		if (result == 1) {
			b = generateRandom(b, 5 * b);
		}
		if (b < 1)
			b = 1;
		if ((b * 10) > chips)
			bet = chips;
		else
			bet = b * 10;
		if (chips < 5000 && bet > 1000)
			bet = 1000;
		if (chips < 10000 && bet > 2000)
			bet = 2000;
		if (chips < 20000 && bet > 5000)
			bet = 5000;
	}

	public boolean executeTurn() {
		if (aiThinkTime > 0)
			aiThinkTime--;
		else {
			boolean b = executeAIAction();
			if (b)
				endTurn();
			else {
				aiThinkTime = generateRandom(AI_MIN_THINK_TIME,
						AI_MAX_THINK_TIME);
			}
		}
		return false;
	}

	public boolean executeAIAction() { // DOES NOT YET ACCOUNT FOR VARIABLE ACES
		int val = hand.getValue();
		if (isDealer) {
			if (val >= 17)
				return true;
			else
				hit();
			return false;
		}
		int chance = generateRandom(1, 10);
		if (val >= 17) {
			return true;
		} else if (val == 11) {
			if ((chance > 3) && canDoubleDown)
				doubleDown();
			else
				hit();
		} else if ((val == 12) | (val == 10)) {
			if ((chance > 7) && canDoubleDown)
				doubleDown();
			else
				hit();
		} else if (val <= 14) {
			hit();
		} else {
			if (chance > 5)
				hit();
			else
				return true;
		}
		return false;
	}

	// RENDERING
	public void draw(Graphics g, boolean isTurn) {
		g.setColor(Color.white);
		if (isTurn)
			g.setColor(Color.red);
		g.setFont(new Font("Courier New", Font.BOLD, TEXTSIZE));
		if (isDealer)
			g.drawString("DEALER", x - DEALER_NAME_LENGTH, y - DEALER_NAME_GAP);
		else {
			String s = "CHIPS: " + chips + " BET: " + bet;
			int n = s.length();
			g.drawString(s, x - n * CHAR_LENGTH, y + CHIP_GAP);
			n = name.length();
			g.drawString(name, x - n * CHAR_LENGTH, y + NAME_GAP);
			if(hand==null) return;
			if(hand.getIsBust()){
				s = "BUST!";
				n = s.length();
				g.drawString(s, x - n * CHAR_LENGTH, y - TOP_GAP);
			}
			else if(hand.getIsBlackJack()){
				s = "BLACKJACK!";
				n = s.length();
				g.drawString(s, x - n * CHAR_LENGTH, y - TOP_GAP);
			}
		}

		if (hand != null) {
			hand.draw(g, faceDownCondition);
		}
	}
}
