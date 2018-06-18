package infovis.scatterplot;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;

public class MouseController implements MouseListener, MouseMotionListener {

	private Model model = null;
	private View view = null;

	public void mouseClicked(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
		int x = arg0.getX();
		int y = arg0.getY();
		//Iterator<Data> iter = model.iterator();
		view.getMarkerRectangle().setRect(x, y, 0, 0);
		view.repaint();
	}

	public void mouseReleased(MouseEvent arg0) {
	}

	/**
	 * Homework 2.2
	 * Draw the rectangle to do brush&linking while dragging
	 */
	public void mouseDragged(MouseEvent arg0) {
		int x = arg0.getX();
		int y = arg0.getY();

		//Draw the rectangle highlighter!
		Rectangle2D highlighter =  view.getMarkerRectangle();
		double hlMinX = highlighter.getMinX();
		double hlMinY = highlighter.getMinY();
		double highlighterW=  x - hlMinX;
		double highlighterH = y - hlMinY;

		highlighter.setRect(hlMinX, hlMinY, highlighterW, highlighterH);

		view.repaint();
	}

	public void mouseMoved(MouseEvent arg0) {
	}

	public void setModel(Model model) {
		this.model  = model;	
	}

	public void setView(View view) {
		this.view  = view;
	}

}