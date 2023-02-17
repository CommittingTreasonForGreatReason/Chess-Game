package BoardStuff;


import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class DrawableObject {
	protected Color baseColor;
	public Point2D centerPoint;
	public DrawableObject(Color baseColor, Point2D centerPoint) {
		this.baseColor = baseColor;
		this.centerPoint = centerPoint;
	}
	
	public void setCenterPoint(Point2D centerPoint){
		this.centerPoint = centerPoint;
	}
	
	// updates the objects data continuously.
	public abstract void update(final double secondsSinceLastFrame);

	// draws the object
	public abstract void draw(final GraphicsContext gc);
	
	// repositions / changes the coordinates and size of the drawable object
	public abstract void repositionGeometryOnResize();
	
}
