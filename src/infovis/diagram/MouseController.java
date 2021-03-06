package infovis.diagram;

import infovis.debug.Debug;
import infovis.diagram.elements.DrawingEdge;
import infovis.diagram.elements.Edge;
import infovis.diagram.elements.Element;
import infovis.diagram.elements.GroupingRectangle;
import infovis.diagram.elements.None;
import infovis.diagram.elements.Vertex;
import infovis.diagram.layout.Fisheye;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MouseController implements MouseListener,MouseMotionListener,MouseWheelListener {
    private Model model;
    private View view;
    private Element selectedElement = new None();
    private double mouseOffsetX;
    private double mouseOffsetY;
    private boolean edgeDrawMode = false;
    private DrawingEdge drawingEdge = null;
    /**
     * Homework 1.2
     * Marker and Overview variables
     */
    private boolean isMarkerMoving = false;
    /**
     * Homework 4.2
     * Fisheye variables
     */
    private boolean fisheyeMode;
    private Fisheye fisheye = new Fisheye();
    private GroupingRectangle groupRectangle;
    /*
     * Getter And Setter
     */
    public Element getSelectedElement(){
        return selectedElement;
    }
    public Model getModel() {
        return model;
    }
    public void setModel(Model diagramModel) {
        this.model = diagramModel;
    }
    public View getView() {
        return view;
    }
    public void setView(View diagramView) {
        this.view = diagramView;
    }
    /*
     * Implements MouseListener
     */
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        double scale = view.getScale();

        if (e.getButton() == MouseEvent.BUTTON3){
            /*
             * add grouped elements to the model
             */
            Vertex groupVertex = (Vertex)getElementContainingPosition(x/scale,y/scale);
            for (Iterator<Vertex> iter = groupVertex.getGroupedElements().iteratorVertices();iter.hasNext();){
                model.addVertex(iter.next());
            }
            for (Iterator<Edge> iter = groupVertex.getGroupedElements().iteratorEdges();iter.hasNext();){
                model.addEdge(iter.next());
            }
            /*
             * remove elements
             */
            List<Edge> edgesToRemove = new ArrayList<>();
            for (Iterator<Edge> iter = model.iteratorEdges(); iter.hasNext();){
                Edge edge = iter.next();
                if (edge.getSource() == groupVertex || edge.getTarget() == groupVertex){
                    edgesToRemove.add(edge);
                }
            }
            model.removeEdges(edgesToRemove);
            model.removeElement(groupVertex);

        }
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        double scale = view.getScale();
        double test_x = view.getWidth() - (view.getWidth()*(1/scale));
        double test_y = view.getHeight() - (view.getHeight()*(1/scale));
        /*
         * Homework 1.2
         */
        // if (scale > 1 && (view.overviewRectContains(x, y))) {
        //     view.setTranslateX((x) * -1);
        //     view.setTranslateY((y) * -1);
        //     // moveSmallMarker(x,y, scale);
        // }
        if(scale > 1 && (view.overviewRectContains(x, y))) {
            isMarkerMoving = true;
        }


        if (edgeDrawMode){
            drawingEdge = new DrawingEdge((Vertex)getElementContainingPosition(x/scale,y/scale));
            model.addElement(drawingEdge);

        } else if (fisheyeMode){
            /*
             * do handle interactions in fisheye mode
             */
            /**
             * Homework 4.2
             * call function fisheyeInteractions
             */
            fisheyeInteractions(x, y);
            view.repaint();
        } else {

            selectedElement = getElementContainingPosition(x/scale, y/scale);
            /*
             * calculate offset
             */
            mouseOffsetX = x - selectedElement.getX() * scale ;
            mouseOffsetY = y - selectedElement.getY() * scale ;
        }

    }
    public void mouseReleased(MouseEvent arg0){
        int x = arg0.getX();
        int y = arg0.getY();
        isMarkerMoving = false;

        if (drawingEdge != null){
            Element to = getElementContainingPosition(x, y);
            model.addEdge(new Edge(drawingEdge.getFrom(),(Vertex)to));
            model.removeElement(drawingEdge);
            drawingEdge = null;
        }
        if (groupRectangle != null){
            Model groupedElements = new Model();
            for (Iterator<Vertex> iter = model.iteratorVertices(); iter.hasNext();) {
                Vertex vertex = iter.next();
                if (groupRectangle.contains(vertex.getShape().getBounds2D())){
                    Debug.p("Vertex found");
                    groupedElements.addVertex(vertex);
                }
            }
            if (!groupedElements.isEmpty()){
                model.removeVertices(groupedElements.getVertices());

                Vertex groupVertex = new Vertex(groupRectangle.getCenterX(),groupRectangle.getCenterX());
                groupVertex.setColor(Color.ORANGE);
                groupVertex.setGroupedElements(groupedElements);
                model.addVertex(groupVertex);

                List<Edge> newEdges = new ArrayList();
                for (Iterator<Edge> iter = model.iteratorEdges(); iter.hasNext();) {
                    Edge edge =  iter.next();
                    if (groupRectangle.contains(edge.getSource().getShape().getBounds2D())
                            && groupRectangle.contains(edge.getTarget().getShape().getBounds2D())){
                        groupVertex.getGroupedElements().addEdge(edge);
                        Debug.p("add Edge to groupedElements");
                        //iter.remove(); // Warum geht das nicht!
                    } else if (groupRectangle.contains(edge.getSource().getShape().getBounds2D())){
                        groupVertex.getGroupedElements().addEdge(edge);
                        newEdges.add(new Edge(groupVertex,edge.getTarget()));
                    } else if (groupRectangle.contains(edge.getTarget().getShape().getBounds2D())){
                        groupVertex.getGroupedElements().addEdge(edge);
                        newEdges.add(new Edge(edge.getSource(),groupVertex));
                    }
                }
                model.addEdges(newEdges);
                model.removeEdges(groupedElements.getEdges());
            }
            model.removeElement(groupRectangle);
            groupRectangle = null;
        }
        view.repaint();
        double scale = view.getScale();
    //    moveSmallMarker(x,y, scale);
        //Reset All
        resetAll();
    }

    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        double scale = view.getScale();
        double test_x = view.getWidth() - (view.getWidth()*(1/scale));
        double test_y = view.getHeight() - (view.getHeight()*(1/scale));
        /*
         * Homework 1.2
         */
        // if (scale > 1 && (view.overviewRectContains(x, y))) {
        //     view.setTranslateX((x) * -1);
        //     view.setTranslateY((y) * -1);
        //     // moveSmallMarker(x,y, scale);
        // }
        if(isMarkerMoving && view.overviewRectContains(x,y)){
            view.setTranslateX(x);
            view.setTranslateY(y);
            moveSmallMarker(x,y, scale);
        }

        if (fisheyeMode){
            /*
             * handle fisheye mode interactions
             */
            /**
             * Homework 4.2
             * Fisheye interactions
             */
            fisheyeInteractions(x, y);
            view.repaint();
        } else if (edgeDrawMode){
            drawingEdge.setX(e.getX());
            drawingEdge.setY(e.getY());
        }else if(selectedElement != null){
            selectedElement.updatePosition((e.getX()-mouseOffsetX)/scale, (e.getY()-mouseOffsetY) /scale);
        }
        view.repaint();
    }
    public void mouseMoved(MouseEvent e) {
    }
    public boolean isDrawingEdges() {
        return edgeDrawMode;
    }
    public void setDrawingEdges(boolean drawingEdges) {
        this.edgeDrawMode = drawingEdges;
    }

    public void setFisheyeMode(boolean b) {
        fisheyeMode = b;
        if (b){
            Debug.p("new Fisheye Layout");
            /*
             * handle fish eye initial call
             */
            view.repaint();
        } else {
            Debug.p("new Normal Layout");
            view.setModel(model);
            view.repaint();
        }
    }

    /*
     * private Methods
     */
    private Element getElementContainingPosition(double x,double y){
        Element currentElement = new None();
        Iterator<Element> iter = getModel().iterator();
        while (iter.hasNext()) {
            Element element =  iter.next();
            if (element.contains(x, y)) currentElement = element;
        }
        return currentElement;
    }

    /*
    * Homework 1.1
    */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int x = e.getScrollAmount();
        int z = e.getWheelRotation();
        double scale = view.getScale();
        view.setScale(scale - (x * z)/3);
        view.repaint();
    }

    /*
    * Homework 1.2
    */
    public void moveSmallMarker(int x, int y, double scale) {
        //Move like the mouse position but inverse to the scale
        int moveX = x * 10;// times 10 because the red marker is only 10% of the actual size
        int moveY = y * 10;

        //Constraints
        double parentsize_x = 1000;//view.getWidth();
        double parentsize_y = 1000; //view.getHeight();
        double getMarkerXLimit = view.getMarker().getX();
        double getMarkerYLimit = view.getMarker().getY();
        //define limits
        double left = 0;
        double top = 0;
        double right = parentsize_x - view.getMarker().getWidth();
        double bottom = parentsize_y - view.getMarker().getHeight();
        boolean limitBox = (moveX >= left && moveX <= right) && (moveY >= top && moveY <= bottom);
        boolean limitRightHeight = moveX >= right && (moveY >= top && moveY <= bottom);
        boolean limitBottomWidth = moveY >= bottom && (moveX >= left && moveX <= right);
        boolean limitLeftHeight = moveX <= left && (moveY >= top && moveY <= bottom);
        boolean limitTopWidth = moveY <= top && (moveX >= left && moveX <= right);

        //Make sure it only moves within limits
        if(limitBox){//move freely if you're within ALL limits
            view.updateMarker(moveX, moveY);
            Debug.print("limitBox: TRUE ");
        }else if(limitRightHeight){//if the limit is beyond the right side, stay on right and move within Y
            view.updateMarker((int) right, moveY);
            Debug.print("limitRightHeight: TRUE ");
        }else if(limitBottomWidth){//if the limit is beyond the bottom, stay on bottom and move within X
            view.updateMarker(moveX, (int) bottom);
            Debug.print("limitBottomWidth: TRUE ");
        }else if(limitLeftHeight){//if the limit is less than left side, stay on left then move within Y
            view.updateMarker((int) left, moveY);
            Debug.print("limitLeftHeight: TRUE ");
        }else if(limitTopWidth){//if the limit is less than top side, stay on top then move within X
            view.updateMarker(moveX, (int) top);
            Debug.print("limitTopWidth: TRUE ");
        }else if(!limitBox){
            view.updateMarker(0,0);
        }

        Debug.println("Marker X = " + String.valueOf(view.getMarker().getX() + "  || Marker Y = " + String.valueOf(view.getMarker().getY())));
        double resetX = view.getTranslateX();
        double resetY = view.getTranslateY();
        view.updateTempTrans(resetX,resetY);
        
        // view.repaint();
    }

    /**
     * Homework 1.2
     * Reset all the position for the marker
     */
    private void resetAll(){
        
        view.updateTranslation(view.getTempTransX(), view.getTempTransY());
    }

    /**
     * Homework 4.2
     * Fisheye interactions
     */
    private void fisheyeInteractions(int x, int y){
        Model tmpModel = new Model();
			tmpModel.generateTestValues();
			int index = 0;
			for (Vertex vert : model.getVertices()) {
                Vertex tempVert = tmpModel.getVertices().get(index);
				vert.setX(tempVert.getX());
				vert.setY(tempVert.getY());
				vert.setHeight(tempVert.getHeight());
				vert.setWidth(tempVert.getWidth());
				index++;
            }
            
			fisheye.setMouseCoords(x, y, view);
            
            model = fisheye.transform(model, view);
    }

}
