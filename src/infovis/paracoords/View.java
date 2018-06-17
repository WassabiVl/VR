package infovis.paracoords;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class View extends JPanel {
    private Model model = null;
    private Rectangle2D markerRectangle = new Rectangle2D.Double(0,0,0,0);

    public Rectangle2D getMarkerRectangle() {
        return markerRectangle;
    }
    ArrayList<ArrayList<Line2D>> Lines;
    private ArrayList<Line2D> markLines = new ArrayList<>();
//    int[] yourvar = new int[model.getLabels().size()];
    int[] yourvar = new int[7];

    public void setYourvar(int[] yourvar){
        this.yourvar = yourvar;
    }
    public int[] getYourvar(){
        return this.yourvar;
    }

    @Override
    public void paint(Graphics g) {
        Lines = new ArrayList<>();
        int Index = 0;
        Graphics2D g2D = (Graphics2D) g;
        // Initialize the titleFont
        Font titleFont = new Font("Verdana", Font.BOLD, 10);
        FontMetrics titleFontMetrics = g.getFontMetrics(titleFont);
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.clearRect(0, 0, getWidth(), getHeight());

        // Get the width of the JPanel
        Dimension d = getSize();
        int clientWidth =d.width;
        int clientHeight = d.height;
        int num = model.getLabels().size();
        clientWidth = clientWidth/num;
        int[] adding = getYourvar();

        for (String l : model.getLabels()) {
            //horizontal
            int title_y = getHeight() - titleFontMetrics.getAscent() ;
            int title_x = clientWidth*Index + titleFontMetrics.getAscent() +adding[Index];
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
            int title_x = clientWidth*Index + titleFontMetrics.getAscent()+adding[Index] ;
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
        // draw the lines
        for(int x=0; x < model.getList().size(); x++){
            //to collect the lines
            ArrayList<Line2D> Line = new ArrayList<>();

            Data data_x = model.getList().get(x);
            int previous_data = 0;
            int previous_data_y = 0;
            int index_x = 0;
            double hue = Math.random();
            int rgb = Color.HSBtoRGB((float)hue,(float)0.5,(float)0.5);
            Color color = new Color(rgb);
            for (double a : data_x.getValues()){
                Range range_y = model.getRanges().get(index_x);
                double max_y = range_y.getMax();
                double min_y = range_y.getMin();
                double value_y = ((max_y-min_y)-(a - min_y))/(max_y-min_y);
                int valuey = (int) (clientHeight*value_y);
                if (valuey == 0){
                    valuey = titleFontMetrics.getAscent()+2 +adding[index_x];
                } else if (valuey >= clientHeight-3*titleFontMetrics.getAscent()) {
                    valuey = clientHeight -3*titleFontMetrics.getAscent()+adding[index_x];
                }
                g2D.setColor(color);
                g2D.setStroke(new BasicStroke(2));
                if (previous_data_y == 0){
                    Line2D lines2d = new Line2D.Double(previous_data+titleFontMetrics.getAscent(), valuey, clientWidth*index_x + titleFontMetrics.getAscent()+adding[index_x], valuey);
                    g2D.drawLine(previous_data+titleFontMetrics.getAscent(), valuey, clientWidth*index_x + titleFontMetrics.getAscent(), valuey);
                    Line.add(lines2d);
                }else {
                    Line2D lines2d = new Line2D.Double(previous_data+titleFontMetrics.getAscent(), previous_data_y, clientWidth*index_x + titleFontMetrics.getAscent()+adding[index_x], valuey);
                    g2D.drawLine(previous_data+titleFontMetrics.getAscent(), previous_data_y, clientWidth*index_x + titleFontMetrics.getAscent(), valuey);
                    Line.add(lines2d);

                }
                previous_data = clientWidth*index_x+adding[index_x];
                previous_data_y = valuey;
                index_x++;

            }
            Lines.add(Line);
        }
        if (!getMarkLines().isEmpty() ){
            Color color = Color.BLACK;
            g2D.setColor(color);
            for (Line2D lines2D: getMarkLines()){
                g2D.setStroke(new BasicStroke(4));
                g2D.draw(lines2D);
            }
        }

    }
    public void setModel(Model model) {
        this.model = model;
    }

    private ArrayList<Line2D> getMarkLines() {
        return markLines;
    }

    public void setMarkLines(ArrayList<Line2D> markLines) {
        this.markLines = markLines;
    }
}