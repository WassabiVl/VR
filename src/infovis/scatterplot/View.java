package infovis.scatterplot;

import infovis.debug.Debug;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class View extends JPanel {
	     private Model model = null;
	     private Rectangle2D markerRectangle = new Rectangle2D.Double(0,0,0,0); 

		 public Rectangle2D getMarkerRectangle() {
			return markerRectangle;
		}
		 
		@Override
		public void paint(Graphics g) {
            Graphics2D g2D = (Graphics2D) g;
            super.paintComponent(g2D);
            for (String l : model.getLabels()) {
				Debug.print(l);
				Debug.print(",  ");
				Debug.println("");
			}
			for (Range range : model.getRanges()) {
				Debug.print(range.toString());
				Debug.print(",  ");
				Debug.println("");
			}
			for (Data d : model.getList()) {
				Debug.println(d.toString());
				Debug.println("");
                for (double a : d.getValues()){
                    g2D.drawRect((int) a, (int)a, 1, 1);
                    Debug.println(String.valueOf(a));

                }
			}
	        
			
		}
		public void setModel(Model model) {
			this.model = model;
		}
}
