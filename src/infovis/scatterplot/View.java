package infovis.scatterplot;

import infovis.debug.Debug;

import java.awt.*;
import java.awt.geom.AffineTransform;
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
        clientHeight = clientHeight/num;



        for (String l : model.getLabels()) {
            //horizontal
            int title_y = titleFontMetrics.getAscent();
            int title_x = clientWidth*Index ;
            g2D.setFont(titleFont);
            // Draw the title
            g2D.drawString(l, title_x, title_y);
            g2D.drawLine(title_x,title_y,title_x,d.height);
            Index++;
        }
        //vertical
        Index = 0;
        for (String l : model.getLabels()) {
            //vertical
            int titley = clientHeight * Index ;
            int titlex = titleFontMetrics.getAscent() ;

            g2D.setFont(titleFont);
            g2D.drawLine(titlex,titley,d.width,titley);
            // Draw the title
//            AffineTransform at= AffineTransform.getRotateInstance(Math.toRadians(90));
//            AffineTransform at2= AffineTransform.getRotateInstance(Math.toRadians(-90));
//            g2D.transform(at);
//            g2D.drawString(l, titlex, titley);
//            System.out.println(titlex);
//            System.out.println(titley);
//            g2D.transform(at2); // reset
            Index++;
        }

        for(int x=0; x < model.getList().size(); x++){
            Data data_x = model.getList().get(x);
            int index_x = 0;
            for (double a : data_x.getValues()){
                Range range_x = model.getRanges().get(index_x);
                double max_x = range_x.getMax();
                double min_x = range_x.getMin();
                double value_x = (a - min_x)/(max_x-min_x);
                float valuex = (float) (clientWidth*index_x + clientWidth*value_x);
                index_x++;
                for (int y=0;y<model.getList().size();y++) {
                    Data data_y =model.getList().get(y);
                    int index_y = 0;
                    for ( double b : data_y.getValues()){
                        Range range_y = model.getRanges().get(index_y);
                        double max_y = range_y.getMax();
                        double min_y = range_y.getMin();
                        double value_y = ((max_y-min_y)-(b - min_y))/(max_y-min_y);

                        float valuey = (float) (clientHeight * (index_y + value_y));
                        System.out.println(value_y);
                        Color color1 = Color.BLUE;
                        Rectangle2D mark = new Rectangle2D.Float(valuex,valuey,2,2);//small window (red)
                        g2D.setColor(color1);
                        g2D.draw(mark);
                        index_y++;
                    }
                }
            }
        }

    }
    public void setModel(Model model) {
        this.model = model;
    }
}
