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
    private Rectangle2D marker = new Rectangle2D.Double();
    private Rectangle2D overviewRect = new Rectangle2D.Float(0,0,100,100);
    private Graphics2D g2D;
    private Graphics graphics;


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
        setGraphics(g);
        g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.clearRect(0, 0, getWidth(), getHeight());
        paintDiagram(g2D);
        Color color1 = Color.BLUE;
        g2D.setColor(color1);
        g2D.draw(overviewRect); //fills the
        g2D.scale(.1,.1);
        paintDiagram(g2D);
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
        marker.setRect(x, y, 16, 10);
    }
    public Rectangle2D getMarker(){
        return marker;
    }
    public boolean markerContains(int x, int y){
        return marker.contains(x, y);
    }

    public Graphics2D getGraphics2D() {
        return g2D;
    }

    public void setGraphics2D(Graphics2D g2D) {
        this.g2D = g2D;
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
    }

    public void setGraphics(Graphics graphics) {
        this.graphics = graphics;
    }
}
