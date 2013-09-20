import java.awt.*;


public class Card {

	//FIELDS
	private String suit;
	private int value;
	private boolean isAce = false;
	private int number;
	private Image img;
	
	public final static int HEIGHT = 100;
	public final static int WIDTH = 75;
	
	//CONSTRUCTOR
	public Card(String s, int n, Image i){
		suit = s;
		number = n;
		img = i;
		if(n==1) isAce = true;
		if(n>10) value = 10;
		else value = n;
	}
	
	//GETTERS
	public String getSuit(){
		return suit;
	}
	
	public String getCardName(){
		String val = number+"";
		if(number==13) val = "King"; 
		if(number==12) val = "Queen";
		if(number==11) val = "Jack"; 
		if(number==1) val = "Ace";
		String name = val+" of "+suit;
		return name;
	}
	
	public int getValue(){
		return value;
	}
	
	public boolean isAce(){
		return isAce;
	}
	
	public Image getImage(){
		return img;
	}
	
	//RENDERING
	public void draw(Graphics g, int x, int y,boolean faceDown){
		Image i = img;
		if(faceDown) i = Window.backImg;
		g.drawImage(i, x, y, WIDTH, HEIGHT, null);
		//System.out.println("Drawing card "+getCardName());
	}
}
