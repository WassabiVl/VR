package infovis.diagram.layout;

import infovis.debug.Debug;
import infovis.diagram.Model;
import infovis.diagram.View;
import infovis.diagram.elements.Edge;
import infovis.diagram.elements.Vertex;

import java.util.Iterator;

/*
 * 
 */


public class Fisheye implements Layout{
	private int posX;
	private int posY;

	//Set
	public void setPosX(int x){
		this.posX = x;
	}
	public void setPosY(int y){
		this.posY = y;
	}

	//Get
	public int getPosX(){
		return posX;
	}
	public int getPosY(){
		return posY;
	}

	public void setMouseCoords(int x, int y, View view) {
		// TODO Auto-generated method stub
		this.setPosX(x);
		this.setPosY(y);
	}


	@Override
	public Model transform(Model model, View view) {
		for (Vertex vertex: model.getVertices()){
			
			int tempX = getPosX();
			int tempY = getPosY();
			
			vertex.setX(makeTranslation(vertex.getCenterX() , tempX / view.getScale(), view.getWidth(), view.getScale()));
			vertex.setY(makeTranslation(vertex.getCenterY() , tempY / view.getScale(), view.getHeight(), view.getScale()) );
			vertex.setWidth(makeScale(vertex.getX() , vertex.getCenterX(), tempX/ view.getScale() , view.getWidth(), view.getScale()));
			vertex.setHeight(makeScale(vertex.getY(), vertex.getCenterY(), tempY/ view.getScale() , view.getHeight(), view.getScale()));
		}
		return model;
	}
	
	
	private double makeTranslation(double pNorm, double pFocus, double screenBoundary, double d){

		double dMax = 0;

		//Change the max depending on the focus of the norm
		if (pNorm > pFocus) {
			dMax = screenBoundary - pFocus;
		} else if ( pNorm < pFocus) {
			dMax -= pFocus;
		}  else {
			return pNorm;
		}

		double dNorm = pNorm - pFocus;
		double value = dNorm / dMax;
		double g = ((d + 1) * value) / (d * value + 1);
		double pFish = pFocus + g * dMax;
		
		return pFish;
	}
	
	private double makeScale(double qNorm, double pNorm, double pFocus, double screenBoundary, double d){
		double qFish = makeTranslation(qNorm, pFocus, screenBoundary, d);
		double fishX = Math.abs(qFish - makeTranslation(pNorm, pFocus, screenBoundary, d));
		double fishY = Math.abs(makeTranslation(pNorm, pFocus, screenBoundary, d) - qFish);
		double sGeom = 2 * Math.min(fishX, fishY); 
		
		return sGeom;
	}
	
}
