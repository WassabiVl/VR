package infovis.paracoords;

import infovis.paracoords.Model;

import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class MouseController implements MouseListener, MouseMotionListener {
	private View view = null;
	private Model model = null;
	Shape currentShape = null;
	
	public void mouseClicked(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
        double x = e.getX();
        double y = e.getY();
	    ArrayList<ArrayList<Line2D>> lines = view.Lines;
		for (ArrayList<Line2D> line : lines){
            for (Line2D lineA : line) {
                System.out.println(Line2D.ptLineDistSq(lineA.getX1(),lineA.getY1(),lineA.getX2(),lineA.getY2(),x,y));
                if (Line2D.ptLineDistSq(lineA.getX1(),lineA.getY1(),lineA.getX2(),lineA.getY2(),x,y) <= 1.0) {
                    view.setMarkLines(line);
                    System.out.println("found");
                }
            }

        }
        view.repaint();

	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseDragged(MouseEvent e) {

	}

	public void mouseMoved(MouseEvent e) {

	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

}
