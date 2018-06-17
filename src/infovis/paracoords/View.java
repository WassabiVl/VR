package infovis.paracoords;

import infovis.scatterplot.Data;
import infovis.scatterplot.Model;
import infovis.scatterplot.Range;

import java.awt.*;
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
		int Index = 0;
		Graphics2D g2D = (Graphics2D) g;
		// Initialize the titleFont
		Font titleFont = new Font("Verdana", Font.BOLD, 10);
		FontMetrics titleFontMetrics = g.getFontMetrics(titleFont);

		// Get the width of the JPanel
		Dimension d = getSize();
		int clientWidth = d.width;
		int clientHeight = d.height;
		int num = model.getLabels().size();
		clientWidth = clientWidth/num;



		for (String l : model.getLabels()) {
			//horizontal
			int title_y = getHeight() - titleFontMetrics.getAscent();
			int title_x = clientWidth*Index + titleFontMetrics.getAscent() ;
			g2D.setFont(titleFont);
			// Draw the title
			g2D.drawString(l, title_x, title_y);
			g2D.drawLine(title_x,title_y - titleFontMetrics.getAscent()*2,title_x,titleFontMetrics.getAscent()	);
			Index++;
		}

		Index = 0;
		for (Range r : model.getRanges()) {
			//horizontal
			int title_y = clientHeight - titleFontMetrics.getAscent();
			int title_x = clientWidth*Index + titleFontMetrics.getAscent() ;
			g2D.setFont(titleFont);
			String r_max = String.valueOf(r.getMax());
			String r_min = String.valueOf(r.getMin());
			// Draw the numbers
			g2D.drawString(r_max, title_x, titleFontMetrics.getAscent());
			g2D.drawString(r_min, title_x, title_y - titleFontMetrics.getAscent());
            // draw vertical rule at left side (bottom to top)
            for (int y=title_y- titleFontMetrics.getAscent()*2; y > titleFontMetrics.getAscent(); y--) {
                int pos = (y-title_y);

                if (pos % 50 == 0) {          // 20 pixel tick every 50 pixels
                    g2D.drawLine(title_x, y, title_x+20, y);
                }
                else if (pos % 10 == 0) {     // 10 pixel tick every 10 pixels
                    g2D.drawLine(title_x, y, title_x+10, y);
                }
                else if (pos % 2 == 0) {      // 5 pixel tick every 2 pixels
                    g2D.drawLine(title_x, y, title_x+5, y);
                }
            }
			Index++;
		}

		for(int x=0; x < model.getList().size(); x++){
			infovis.scatterplot.Data data_x = model.getList().get(x);
            int previous_data = 0;
            int previous_data_y = 0;
			int index_x = 0;
            double hue = Math.random();
            int rgb = Color.HSBtoRGB((float)hue,(float)0.5,(float)0.5);
            Color color = new Color(rgb);
			for (double a : data_x.getValues()){
				infovis.scatterplot.Range range_y = model.getRanges().get(index_x);
				double max_y = range_y.getMax();
				double min_y = range_y.getMin();
				double value_y = ((max_y-min_y)-(a - min_y))/(max_y-min_y);
				int valuey = (int) (clientHeight*value_y);
				if (valuey == 0){
				    valuey = titleFontMetrics.getAscent()+2;
                } else if (valuey >= clientHeight-3*titleFontMetrics.getAscent()) {
				    valuey = clientHeight -3*titleFontMetrics.getAscent();
                }
                g2D.setColor(color);
                g2D.setStroke(new BasicStroke(3));
				if (previous_data_y == 0){
                g2D.drawLine(previous_data+titleFontMetrics.getAscent(), valuey, clientWidth*index_x + titleFontMetrics.getAscent(), valuey);
				}else {
                    g2D.drawLine(previous_data+titleFontMetrics.getAscent(), previous_data_y, clientWidth*index_x + titleFontMetrics.getAscent(), valuey);

                }
                previous_data = clientWidth*index_x;
                previous_data_y = valuey;
				index_x++;

			}
		}

	}
	public void setModel(Model model) {
		this.model = model;
	}
}