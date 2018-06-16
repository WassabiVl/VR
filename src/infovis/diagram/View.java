package infovis.diagram;

import infovis.diagram.elements.Element;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import javax.swing.JPanel;

public class View extends JPanel{
    private Model model = null;
    private Color color = Color.BLUE;
    private double scale = 1;
    private double translateX= 0;
    private double translateY=0;
    private Rectangle2D marker;//small window (red)
    private Rectangle2D overviewRect;//big window (blue)



    public Model getModel() {
        return model;
    }
    public void setModel(Model model) {
        this.model = model;
    }
    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }


    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        Color color1 = Color.BLUE;
        Color color2 = Color.RED;
        Color color3 = new Color(0f,0f,0f,0f);
        
        /*
        * Homework 1.1
        */
        if (getScale()<=0) {
            setScale(1);
           updateTranslation(0,0);
        }


        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.clearRect(0, 0, getWidth(), getHeight());
        marker = new Rectangle2D.Float(0,0,getWidth(),getHeight());
        overviewRect = new Rectangle2D.Float(0,0,getWidth(),getHeight());
        g2D.scale(1*getScale(),1*getScale());
        g2D.translate(getTranslateX(), getTranslateY());
        paintDiagram(g2D);

        /*
         * Homework 1.2
         */
        g2D.translate(-getTranslateX(), -getTranslateY());
        g2D.scale(.1/getScale(),.1/getScale());
        g2D.setColor(color1);
        g2D.draw(overviewRect);
        paintDiagram(g2D);

        g2D.translate(-getTranslateX(),-getTranslateY());
        g2D.scale(1/getScale(),1/getScale());
        g2D.setColor(color2);
        g2D.draw(marker);


        
        // //paintDiagram(g2D);
        
        

    }


    private void paintDiagram(Graphics2D g2D){
        for (Element element: model.getElements()){
            element.paint(g2D);
        }
    }

    public void setScale(double scale) {
        this.scale = scale;
    }
    public double getScale(){
        return scale;
    }
    public double getTranslateX() {
        return translateX;
    }
    public void setTranslateX(double translateX) {
        this.translateX = translateX;
    }
    public double getTranslateY() {
        return translateY;
    }
    public void setTranslateY(double tansslateY) {
        this.translateY = tansslateY;
    }
    public void updateTranslation(double x, double y){
        setTranslateX(x);
        setTranslateY(y);
    }
    public void updateMarker(int x, int y){
        
        marker.setRect(x, y, 1000, 1000);
    }

    public Rectangle2D getMarker(){
        return marker;
    }
    public boolean markerContains(int x, int y){
        
        return marker.contains(x, y);
    }


}
