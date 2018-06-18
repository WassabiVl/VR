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
    private Rectangle2D square = new Rectangle2D.Double(0,0,0,0);
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
        int padding = 12;
        int clientWidth = dim.width - padding;
        int clientHeight = dim.height - padding;
        int num = model.getLabels().size();
        clientWidth = clientWidth/num;
        clientHeight = clientHeight/num;     

        g2D.fill(dataPoint);
        g2D.setColor(black);

        //Generate Content
        double sqHeight = clientHeight;
        double sqWidth = clientWidth;

        //counters for points (generate points), list of points (match points), rows, and columns
        int pointIndex = 0, listIndex = 0, countRow = 0, countCol = 0;

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
                double posX = (currentCol * sqWidth )+ padding;
                double posY = (currentRow * sqHeight) + padding;
                //Box limit for each square
                double leftLimit = ranges.get(currentCol).getMin();
                double rightLimit = ranges.get(currentCol).getMax();
                double topLimit = ranges.get(currentRow).getMin();
                double bottomLimit = ranges.get(currentRow).getMax();
                //X and Y position for the matching data point
                double valMatchX = sqWidth / (rightLimit - leftLimit);
                double valMatchY = sqHeight / (bottomLimit - topLimit);
                //All the points!
                Rectangle2D.Double[] points= new Rectangle2D.Double [models.size()];
        
                //Make the labels!
                //horizontal
                int titlehy = titleFontMetrics.getAscent();
                int titlehx = (int)sqWidth * currentCol;
                g2D.setFont(titleFont);
                // Draw the title
                g2D.drawString(col, titlehx, titlehy);

                //Draw the square
                square.setRect(posX, posY, sqWidth, sqHeight);
                g2D.draw(square);
                
                //Get the new point based on the value/key of both of the titles
                Pair<String,String> newPoint = new Pair(hTitle, vTitle);

                //Make the Data points!
                pointIndex = 0;
                for (Data d : models) {
                    double valOnX = d.getValues()[currentCol] - leftLimit;
                    double valOnY = d.getValues()[currentRow] - topLimit;
                    valOnX *= valMatchX;
                    valOnY *= valMatchY;

                    points[pointIndex] = new Rectangle2D.Double(posX + valOnX + 2, posY + valOnY + 2, 4, 4);
                
                    pointIndex++;
                }

                dataPoints.put(newPoint, points);
            
                listIndex = 0;
                for (Rectangle2D rect : dataPoints.get(newPoint)) {
                    g2D.setColor(white);
                    if (markerRectangle.contains(rect)) {
                        g2D.setColor(red);
                    }else {
                        for(Pair<String, String> match : dataPoints.keySet()){
                            // Debug.println(String.valueOf(match));
                            Rectangle2D.Double matchRect = dataPoints.get(match)[listIndex];
                            if (markerRectangle.contains(matchRect)) {
                                g2D.setColor(red);
                            }
                        }
                    }
                    
                    g2D.fill(rect);
                    g2D.setColor(black);
                    g2D.draw(rect);

                    listIndex++;
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
    
    /**
     * Homework 2.2
     * Brush and linking rectangles
     */
    private void makeBLRectangle(Graphics2D g2D){
        g2D.setColor(red);
		g2D.draw(markerRectangle);
    }
}
     