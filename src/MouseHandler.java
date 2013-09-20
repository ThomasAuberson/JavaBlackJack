import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseHandler implements MouseListener {
	
	//FIELDS
	Window window;
	
	//CONSTRUCTOR
	public MouseHandler(Window w){
		window = w;
		System.out.println("Mouse Setup");
	}
	
	//ACTION LISTENERS
	@Override
	public void mouseClicked(MouseEvent m) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent m) {		
		window.mouseClicked(m.getLocationOnScreen());
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	// Screen.mouse = new Point((e.getX()) - ((Frame.size.width-
	// Screen.myWidth)/2),(e.getY()) - ((Frame.size.height-
	// Screen.myHeight)/2));

}
