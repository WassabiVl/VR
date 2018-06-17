package infovis.scatterplot;

import infovis.debug.Debug;
import infovis.diagram.elements.Element;
import javafx.util.Pair;

import java.awt.Color;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

public class View extends JPanel {
    private Model model = null;
    private Rectangle2D cell = new Rectangle2D.Double(0,0,0,0);
    private Rectangle2D dataPoint = new Rectangle2D.Double(0,0,0,0); 
    private Map<Pair<String, String>, Rectangle2D.Double[]> dataPoints = new HashMap<Pair<String, String>, Rectangle2D.Double[]>();
    private Rectangle2D markerRectangle = new Rectangle2D.Double(0,0,0,0); 
    
    private Color orange = Color.ORANGE;
    private Color blue = Color.BLUE;
    private Color black = Color.BLACK;
    private Color red = Color.RED;
    private Color white = Color.WHITE;

    public Rectangle2D getMarkerRectangle() {
        return markerRectangle;
    }

    @Override
    public void paint(Graphics g) {
        /**
         * Homework 2.1
         */
        //Store Label, Range, and Models
        ArrayList<String> labels = model.getLabels();
        ArrayList<Range> ranges = model.getRanges();
        ArrayList<Data> models = model.getList();
            
        // Initialize the titleFont
        Font titleFont = new Font("Verdana", Font.BOLD, 7);
        titleFont.deriveFont(6f);
        FontMetrics titleFontMetrics = g.getFontMetrics(titleFont);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.clearRect(0, 0, getWidth(), getHeight());
        
        // Get the width of the JPanel
        Dimension dim = getSize();
        int clientWidth = dim.width;
        int clientHeight = dim.height;
        int num = model.getLabels().size();
        clientWidth = clientWidth/num;
        clientHeight = clientHeight/num;     
        
        g2D.setColor(orange);
        g2D.fill(cell);
        g2D.setColor(blue);
        g2D.fill(dataPoint);
        g2D.setColor(black);

        //Generate Content
        int totalLabels = model.getLabels().size();
        int Index = 0;
        int paddingLeft = 10;
        int paddingTop = 20;
        double sqHeight = (getHeight())/ totalLabels;
        double sqWidth = (getWidth())/ totalLabels;

        int labelIndex = 0, listIndex = 0, countRow = 0, countCol = 0;

        //Make the table
        for(String row : labels){
            int currentRow = labels.indexOf(row);
            String hTitle = labels.get(currentRow);

            //Make the labels!
            //vertical
            int titlevy = (int) sqHeight * currentRow;
            int titlevx = titleFontMetrics.getAscent();
            g2D.setFont(titleFont);
            // g2D.drawLine(titlevx,titlevy,dim.width,titlevy);
            // g2D.drawString(row, titlevx, titlevy);
            // Draw the title but rotated to the left
            drawRotate(g2D, titlevx, titlevy, -90, row);

            for(String col : labels){
                int currentCol = labels.indexOf(col);
                String vTitle = labels.get(currentCol);
                double posX = (currentCol * sqWidth )+ paddingLeft;
                double posY = (currentRow * sqHeight) + paddingTop;

                //Make the labels!
                //horizontal
                int titlehy = titleFontMetrics.getAscent();
                int titlehx = (int)sqWidth * currentCol;
                g2D.setFont(titleFont);
                // Draw the title
                g2D.drawString(col, titlehx, titlehy);

                //Draw the cell
                cell.setRect(posX, posY, sqWidth, sqHeight);
                g2D.draw(cell);
                
                Pair<String,String> newPoint = new Pair(hTitle, vTitle);
                // private void fillDataPoints(int currentCol, int currentRow, int posX, int posY, double sqHeight, double sqWidth, Pair<String, String> pairKey){
 
                fillDataPoints(currentCol, currentRow, posX, posY, sqHeight, sqWidth, newPoint);
                // Debug.println(String.valueOf(dataPoints));
            
                int i = 0;
                for (Rectangle2D rect : dataPoints.get(newPoint)) {
                    g2D.setColor(white);
                    if (markerRectangle.contains(rect)) {
                        g2D.setColor(red);
                    }else {
                       
                        for(Pair<String, String> match : dataPoints.keySet()){
                            // Debug.println(String.valueOf(match));
                            Rectangle2D.Double matchRect = dataPoints.get(match)[i];
                            if (markerRectangle.contains(matchRect)) {
                                g2D.setColor(red);
                                break;
                            }
                        }
                    }
                    
                    g2D.fill(rect);
                    g2D.setColor(black);
                    g2D.draw(rect);

                    i++;
                }

                countCol++;
            }
            countRow++;
        }

        /**
         * Homework 2.2
         */
        //Make the brush and linking rectangle
        makeBLRectangle(g2D);
        
    }

    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * Homework 2.1
     * Rotate the text
     * Taken from StackOverflow: https://stackoverflow.com/questions/10083913/how-to-rotate-text-with-graphics2d-in-java
     */
    private void drawRotate(Graphics2D g2d, double x, double y, int angle, String text){    
        g2d.translate((float)x,(float)y);
        g2d.rotate(Math.toRadians(angle));
        g2d.drawString(text,(int) (x * -1) -70,0);
        g2d.rotate(-Math.toRadians(angle));
        g2d.translate(-(float)x,-(float)y);
    }

    private void fillDataPoints(int currentCol, int currentRow, double posX, double posY, double sqHeight, double sqWidth, Pair<String, String> pairKey){
 
        //X - Axis
        double xmin = model.getRanges().get(currentCol).getMin();
        double xmax = model.getRanges().get(currentCol).getMax();
        double valMatchX = sqWidth / (xmax - xmin);
        
        //Y - Axis
        double ymin = model.getRanges().get(currentRow).getMin();
        double ymax = model.getRanges().get(currentRow).getMax();
        double valMatchY = sqHeight / (ymax - ymin);

        Rectangle2D.Double[] points= new Rectangle2D.Double [model.getList().size()];
        int index = 0;
        for (Data d : model.getList()) {
            double valOnX = d.getValues()[currentCol] - xmin;
            double valOnY = d.getValues()[currentRow] - ymin;
            valOnX *= valMatchX;
            valOnY *= valMatchY;

            points[index] = new Rectangle2D.Double(posX + valOnX + 4, posY + valOnY + 4, 4, 4);
        
            index++;
        }

        dataPoints.put(pairKey, points);
    }

    /**
     * Homework 2.2
     * Brush and linking rectangles
     */
    private void makeBLRectangle(Graphics2D g2D){
        g2D.setColor(red);
		g2D.draw(markerRectangle);
    }
}
     