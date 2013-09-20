import java.awt.*;
import java.util.*;

public class Hand {

	// FIELDS
	private ArrayList<Card> cards = new ArrayList<Card>();
	private int x, y;
	private int value;

	private boolean isBust = false;
	private boolean isBlackJack = false;

	// CONSTRUCTOR
	public Hand(Player p, Card c1, Card c2, int x, int y) {
		cards.add(c1);
		cards.add(c2);
		this.x = x;
		this.y = y;
		value = calcValue();
	}

	private int calcValue() {
		int aces = 0;
		int sum = 0;
		for (int i = 0; i < cards.size(); i++) {
			sum += cards.get(i).getValue();
			if (cards.get(i).isAce())
				aces++;

		}
		while ((sum <= 11) && (aces > 0)) {	//Account for Ace = 1 OR 11
			aces--;
			sum += 10;
		}
		if(sum==21) isBlackJack = true;
		else if(sum>21) isBust = true;
		return sum;
	}

	public int addCard(Card c) {
		cards.add(c);
		value = calcValue();
		return value;
	}

	public int getValue() {
		return value;
	}
	
	public boolean getIsBust(){
		return isBust;
	}
	public boolean getIsBlackJack(){
		return isBlackJack;
	}

	//RENDERING
	public void draw(Graphics g, boolean faceDownCondition) {
		boolean faceDown = false;
		for (int i = (cards.size()-1); i >=0 ; i--) {
			if(faceDownCondition && (i == 1)) faceDown = true;
			cards.get(i).draw(g, (int)(x + (i-0.5-(cards.size())*0.5) * (Card.WIDTH/2)), y, faceDown);
			faceDown = false;
		}
	}

}
